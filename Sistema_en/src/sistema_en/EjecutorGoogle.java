package sistema_en;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import javax.swing.SwingUtilities;

/**
 * Punto unico de entrada para las operaciones que hablan con la API de Google.
 *
 * Motivo del diseno: las peticiones de red nunca deben ejecutarse en el hilo de
 * eventos de Swing. Cuando eso ocurre la ventana se congela durante los
 * segundos que tardan el sondeo, el refresco del token y la llamada remota.
 * Ademas, el proveedor de servicios serializa el refresco de credenciales con
 * un synchronized, de modo que varias operaciones simultaneas se bloquean entre
 * si y ninguna avanza. Un unico hilo dedicado resuelve ambas cosas: la interfaz
 * queda siempre libre y las operaciones se encadenan en orden.
 *
 * Los resultados y los errores se devuelven siempre en el hilo de eventos, para
 * que el llamador pueda tocar la interfaz sin necesidad de sincronizacion.
 *
 * @author ares
 */
public final class EjecutorGoogle {

    private static final long ESPERA_REINTENTO_MS = 1_500L;

    private static final String SIN_CONEXION
            = "Sin conexion a internet o sin acceso a los servidores de Google. "
            + "Revisa tu conexion e intenta de nuevo.";

    /**
     * Un unico hilo daemon. Es daemon para no impedir que la JVM termine si la
     * aplicacion se cierra con una operacion encolada.
     */
    private static final ExecutorService POZO = Executors.newSingleThreadExecutor(runnable -> {
        Thread hilo = new Thread(runnable, "google");
        hilo.setDaemon(true);
        return hilo;
    });

    /**
     * Clase de utilidad: no se instancia.
     */
    private EjecutorGoogle() {
    }

    /**
     * Se lanza cuando la sonda previa no logra alcanzar los servidores de
     * Google. Se distingue de los demas fallos para poder explicar al usuario
     * que revise su conexion en lugar de mostrarle un error generico.
     */
    public static class SinConexionException extends Exception {

        private static final long serialVersionUID = 1L;

        /**
         * Crea la excepcion con el mensaje que se mostrara al usuario.
         *
         * @param mensaje detalle del fallo de conexion
         */
        public SinConexionException(String mensaje) {
            super(mensaje);
        }
    }

    /**
     * Ejecuta una operacion de solo lectura y reintenta una vez ante un fallo
     * de red transitorio.
     *
     * @param <T>          tipo del resultado
     * @param tarea        operacion a ejecutar en el hilo de Google
     * @param alTerminar   recibe el resultado, en el hilo de eventos
     * @param alFallar     recibe la excepcion, en el hilo de eventos
     */
    public static <T> void ejecutar(Callable<T> tarea, Consumer<T> alTerminar, Consumer<Throwable> alFallar) {
        encolar(tarea, true, alTerminar, alFallar);
    }

    /**
     * Ejecuta una operacion que crea, sube o envia algo, sin reintentar.
     *
     * El reintento se desactiva a proposito: si la peticion llego al servidor
     * y lo que se perdio fue la respuesta, un reintento volveria a ejecutar el
     * efecto y dejaria una carpeta duplicada en Drive, una hoja repetida o un
     * correo de soporte enviado dos veces. Es preferible informar del fallo y
     * que el usuario decida.
     *
     * @param <T>          tipo del resultado
     * @param tarea        operacion a ejecutar en el hilo de Google
     * @param alTerminar   recibe el resultado, en el hilo de eventos
     * @param alFallar     recibe la excepcion, en el hilo de eventos
     */
    public static <T> void ejecutarSinReintento(Callable<T> tarea, Consumer<T> alTerminar, Consumer<Throwable> alFallar) {
        encolar(tarea, false, alTerminar, alFallar);
    }

    /**
     * Ejecuta una operacion sin mostrar ningun dialogo, para tareas automaticas
     * que el usuario no disparo. Los fallos quedan registrados en consola.
     *
     * @param <T>          tipo del resultado
     * @param tarea        operacion a ejecutar en el hilo de Google
     * @param alTerminar   recibe el resultado, en el hilo de eventos
     */
    public static <T> void ejecutarSilencioso(Callable<T> tarea, Consumer<T> alTerminar) {
        encolar(tarea, true, alTerminar, EjecutorGoogle::registrar);
    }

    /**
     * Ejecuta una operacion que no devuelve valor y no reintenta.
     *
     * @param tarea    operacion a ejecutar en el hilo de Google
     * @param alFallar recibe la excepcion, en el hilo de eventos
     */
    public static void ejecutarAccion(Runnable tarea, Consumer<Throwable> alFallar) {
        ejecutarSinReintento(() -> {
            tarea.run();
            return Boolean.TRUE;
        }, resultado -> {
        }, alFallar);
    }

    /**
     * Ejecuta una operacion que no devuelve valor, sin reintentar y sin
     * mostrar dialogos. Para tareas automaticas.
     *
     * @param tarea operacion a ejecutar en el hilo de Google
     */
    public static void ejecutarAccionSilencioso(Runnable tarea) {
        ejecutarAccion(tarea, EjecutorGoogle::registrar);
    }

    /**
     * Encola una operacion y devuelve un futuro para quien necesite esperar el
     * resultado. Reservado para el arranque y el cierre de la aplicacion, donde
     * el programa debe terminar de forma ordenada.
     *
     * @param <T>      tipo del resultado
     * @param tarea    operacion a ejecutar en el hilo de Google
     * @return futuro associated a la operacion
     */
    public static <T> Future<T> enviar(Callable<T> tarea) {
        return POZO.submit(tarea);
    }

    /**
     * Devuelve un mensaje apto para mostrar al usuario. Delega en
     * {@link Internet} para no repetir la distincion entre fallo de red y
     * error de logica en cada punto de llamada.
     *
     * @param error excepcion producida por la operacion
     * @return mensaje para el usuario
     */
    public static String mensajeDeError(Throwable error) {
        if (error instanceof SinConexionException) {
            return SIN_CONEXION;
        }
        return Internet.mensajeDeFallo(error);
    }

    private static <T> void encolar(Callable<T> tarea, boolean permiteReintento,
            Consumer<T> alTerminar, Consumer<Throwable> alFallar) {
        POZO.execute(() -> {
            try {
                final T resultado;
                try {
                    verificarAcceso();
                    resultado = ejecutarConReintento(tarea, permiteReintento);
                } catch (Throwable e) {
                    // Cualquier fallo, incluida la guarda de conexion, tiene que
                    // llegar al callback. Si se perdiera aqui el usuario veria
                    // que no pasa nada: ni un mensaje, ni un error, solo un
                    // boton que quedo deshabilitado para siempre.
                    final Throwable causa = e != null
                            ? e
                            : new IllegalStateException("La operacion de Google finalizo sin resultado.");
                    SwingUtilities.invokeLater(() -> alFallar.accept(causa));
                    return;
                }

                final T valor = resultado;
                SwingUtilities.invokeLater(() -> alTerminar.accept(valor));
            } catch (Throwable fueraDelFlujo) {
                // Nunca dejar morir el hilo del pool: perderlo dejaria toda la
                // aplicacion sin poder encolar operaciones.
                registrar(fueraDelFlujo);
            }
        });
    }

    /**
     * Ejecuta la tarea y, si el fallo es de red y esta permitido reintentar,
     * espera un momento y la repite una unica vez.
     *
     * @throws Throwable el fallo de la tarea, para que lo reciba el llamador
     */
    private static <T> T ejecutarConReintento(Callable<T> tarea, boolean permiteReintento) throws Throwable {
        try {
            return tarea.call();
        } catch (Throwable e) {
            if (!permiteReintento || !Internet.esFalloDeRedTransitorio(e)) {
                throw e;
            }
            esperar(ESPERA_REINTENTO_MS);
            return tarea.call();
        }
    }

    /**
     * Comprueba el acceso a internet antes de tocar la API. Se ejecuta en el
     * hilo de Google, nunca en el de eventos, porque el sondeo puede tardar
     * varios segundos.
     *
     * Ante un fallo se reintenta una vez. Un solo sondeo negativo no es
     * concluyente: el DNS de la red deja de responder de forma puntual, y
     * rechazar la operacion por eso seria un falso negativo que dejaria sin
     * trabajar al usuario sin motivo.
     */
    private static void verificarAcceso() throws SinConexionException {
        if (Internet.hayConexionSinCache()) {
            return;
        }
        esperar(ESPERA_REINTENTO_MS);
        if (!Internet.hayConexionSinCache()) {
            throw new SinConexionException(SIN_CONEXION);
        }
    }

    private static void esperar(long milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void registrar(Throwable error) {
        System.err.println("[EjecutorGoogle] " + error);
        error.printStackTrace();
    }
}
