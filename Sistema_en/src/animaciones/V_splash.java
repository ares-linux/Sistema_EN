package animaciones;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import sistema_en.Conexion;

/**
 * Ventana de bienvenida que se muestra mientras la aplicacion verifica la base
 * de datos y prepara el arranque.
 *
 * @author ares
 */
public class V_splash extends JWindow {

    private static final long MIN_DURACION_MS = 1200;

    /** Etiqueta que muestra el estado del arranque. */
    private final JLabel lb_estado;
    /** Instante (en nanosegundos) en que se mostro la ventana. */
    private long inicio;

    /**
     * Construye la pantalla de bienvenida con el titulo, la version de la
     * aplicacion y una barra de progreso indeterminada.
     */
    public V_splash() {
        JPanel fondo = new JPanel(new BorderLayout(0, 8));
        fondo.setBorder(BorderFactory.createEmptyBorder(24, 28, 22, 28));

        JLabel titulo = new JLabel("Sistema EN", SwingConstants.CENTER);
        titulo.setFont(new Font("Dialog", Font.BOLD, 30));

        JLabel version = new JLabel("Versión " + Conexion.VERSION_APP, SwingConstants.CENTER);
        version.setFont(new Font("Dialog", Font.PLAIN, 13));

        JPanel titulos = new JPanel(new BorderLayout(0, 2));
        titulos.add(titulo, BorderLayout.CENTER);
        titulos.add(version, BorderLayout.SOUTH);

        lb_estado = new JLabel("Iniciando…", SwingConstants.CENTER);
        lb_estado.setFont(new Font("Dialog", Font.PLAIN, 12));

        JProgressBar barra = new JProgressBar();
        barra.setIndeterminate(true);

        JPanel inferior = new JPanel(new BorderLayout(0, 8));
        inferior.add(lb_estado, BorderLayout.NORTH);
        inferior.add(barra, BorderLayout.CENTER);

        fondo.add(titulos, BorderLayout.NORTH);
        fondo.add(inferior, BorderLayout.CENTER);

        setContentPane(fondo);
        setPreferredSize(new Dimension(420, 220));
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Muestra la ventana y registra el instante de inicio para respetar la
     * duracion minima de la pantalla de bienvenida.
     */
    public void mostrar() {
        try {
            SwingUtilities.invokeAndWait(() -> {
                setVisible(true);
                toFront();
            });
            Thread.sleep(120);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("Error al mostrar la splash: " + e.getMessage());
        }
        inicio = System.nanoTime();
    }

    /**
     * Actualiza el texto de estado que se muestra bajo el titulo.
     *
     * @param estado mensaje de estado a mostrar
     */
    public void actualizarEstado(String estado) {
        Runnable cambio = () -> lb_estado.setText(estado);
        if (SwingUtilities.isEventDispatchThread()) {
            cambio.run();
        } else {
            SwingUtilities.invokeLater(cambio);
        }
    }

    /**
     * Cierra la ventana respetando la duracion minima configurada para que la
     * pantalla no desaparezca de forma abrupta.
     */
    public void cerrar() {
        long transcurrido = (System.nanoTime() - inicio) / 1_000_000;
        long resto = MIN_DURACION_MS - transcurrido;
        if (resto > 0) {
            try {
                Thread.sleep(resto);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        try {
            SwingUtilities.invokeAndWait(this::dispose);
        } catch (Exception e) {
            dispose();
        }
    }
}