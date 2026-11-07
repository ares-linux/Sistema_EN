package sistema_en;

import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.NoRouteToHostException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * Utilidad global para comprobar si hay salida a internet.
 *
 * @author ares
 */
public class Internet {

    /**
     * Destino del sondeo. Se consulta por nombre y no por direccion IP a
     * proposito: las operaciones reales de la aplicacion resuelven nombres como
     * oauth2.googleapis.com o gmail.googleapis.com, asi que un sondeo a un
     * literal IP daria "hay internet" en redes donde el DNS de Google no
     * responde, que es exactamente el fallo que terminaba en
     * UnknownHostException. generate_204 pertenece a la misma infraestructura
     * que las APIs, no requiere autenticacion y responde 204.
     *
     * No es final a proposito: las pruebas cambian el destino para simular una
     * caida y comprobar que la guarda corta la operacion.
     */
    static String URL_SONDEO = "https://www.googleapis.com/generate_204";

    /**
     * Tiempo que se reutiliza el resultado de un sondeo. Evita que varios
     * puntos de la aplicacion sumen segundos de espera al abrirse.
     */
    private static final long VALOR_CACHE_MS = 10_000L;

    private static final int TIMEOUT_MS = 3000;

    private static volatile long ultimaConsulta = 0L;
    private static volatile boolean ultimoResultado = false;

    private Internet() {
    }

    /**
     * Realiza un sondeo directo a internet, sin cache.
     *
     * @return {@code true} si el servidor respondio correctamente
     */
    public static boolean Prueba_internet() {
        try {
            URL url = new URL(URL_SONDEO);
            HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();

            // Tiempos de espera cortos para no congelar el flujo
            urlConnect.setConnectTimeout(TIMEOUT_MS);
            urlConnect.setReadTimeout(TIMEOUT_MS);

            int responseCode = urlConnect.getResponseCode();
            urlConnect.disconnect();
            return (responseCode == HttpURLConnection.HTTP_OK
                    || responseCode == HttpURLConnection.HTTP_NO_CONTENT);
        } catch (IOException e) {
            return false; // No hay conexion, no resuelve el nombre o el servidor no responde
        }
    }

    /**
     * Indica si hay salida a internet, reutilizando el resultado del sondeo
     * si se hizo hace menos de {@link #VALOR_CACHE_MS} milisegundos.
     * Debe invocarse fuera del hilo de eventos de Swing.
     *
     * @return true si se pudo alcanzar internet
     */
    public static boolean hayConexion() {
        long ahora = System.currentTimeMillis();
        if (ahora - ultimaConsulta < VALOR_CACHE_MS) {
            return ultimoResultado;
        }
        return hayConexionSinCache();
    }

    /**
     * Igual que {@link #hayConexion()} pero siempre realiza un sondeo nuevo.
     * Usar solo cuando el resultado deba estar fresco, por ejemplo antes de
     * una operacion en la que el usuario lleva tiempo esperando.
     *
     * @return true si se pudo alcanzar internet
     */
    public static boolean hayConexionSinCache() {
        boolean resultado = Prueba_internet();
        ultimoResultado = resultado;
        ultimaConsulta = System.currentTimeMillis();
        return resultado;
    }

    /**
     * Indica si un fallo de red es transitorio, es decir, si vale la pena
     * reintentar la operacion. El DNS de la red puede dejar de responder de
     * forma puntual, y en ese caso la misma peticion funciona al instante
     * siguiente. Se recorre la cadena de causas porque las librerias de Google
     * envuelven la excepcion original.
     *
     * Los errores de logica o de autorizacion no se consideran transitorios:
     * reintentarlos solo duplicaria trabajo y efectos secundarios.
     *
     * @param error excepcion producida por una operacion
     * @return true si conviene reintentar
     */
    public static boolean esFalloDeRedTransitorio(Throwable error) {
        Throwable actual = error;
        int saltos = 0;
        while (actual != null && saltos < 20) {
            if (actual instanceof UnknownHostException
                    || actual instanceof SocketTimeoutException
                    || actual instanceof ConnectException
                    || actual instanceof NoRouteToHostException
                    || actual instanceof SocketException) {
                return true;
            }
            // Evita ciclos en cadenas de causas mal construidas
            if (actual.getCause() == actual) {
                break;
            }
            actual = actual.getCause();
            saltos++;
        }
        return false;
    }

    /**
     * Traduce una excepcion a un mensaje que el usuario pueda entender y
     * actuar, distinguiendo el fallo de red de los demas casos.
     *
     * @param error excepcion producida por una operacion
     * @return mensaje apto para mostrar
     */
    public static String mensajeDeFallo(Throwable error) {
        if (esFalloDeRedTransitorio(error)) {
            return "No se pudo completar la operacion porque la conexion con el servidor de Google "
                    + "se interrumpio o no se pudo resolver su direccion. "
                    + "Revisa tu conexion a internet e intenta de nuevo.";
        }
        String mensaje = error == null ? null : error.getMessage();
        if (mensaje == null || mensaje.isBlank()) {
            return "Ocurrio un error inesperado al completar la operacion.";
        }
        return "Ocurrio un error inesperado al completar la operacion: " + mensaje;
    }
}
