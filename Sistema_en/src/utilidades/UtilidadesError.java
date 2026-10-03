package utilidades;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 * Utilidades compartidas para informar un error al usuario con un mensaje real
 * en lugar de un {@code printStackTrace} silencioso.
 */
public final class UtilidadesError {

    private UtilidadesError() {
    }

    /**
     * Extrae el mensaje mas util de una cadena de causas. Se queda con el
     * mensaje mas largo porque las excepciones de red suelen envolver a una
     * interna mas corta y generica, por ejemplo {@code EOF reached while
     * reading} debajo de un {@code fixed content-length}.
     *
     * @param error cadena de excepciones
     * @return el mensaje mas descriptivo, o el nombre de la clase si ninguna
     *         causa tiene mensaje
     */
    public static String mensajeDe(Throwable error) {
        if (error == null) {
            return "Ocurrió un error inesperado.";
        }
        String mejor = null;
        for (Throwable causa = error; causa != null; causa = causa.getCause() == causa ? null : causa.getCause()) {
            String mensaje = causa.getMessage();
            if (mensaje != null && !mensaje.isBlank() && (mejor == null || mensaje.length() > mejor.length())) {
                mejor = mensaje;
            }
        }
        return mejor != null ? mejor : error.getClass().getSimpleName();
    }

    public static void error(Component padre, String titulo, Throwable causa) {
        JOptionPane.showMessageDialog(padre, mensajeDe(causa), titulo, JOptionPane.ERROR_MESSAGE);
    }

    public static void error(Component padre, String titulo, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, titulo, JOptionPane.ERROR_MESSAGE);
    }

    public static void aviso(Component padre, String titulo, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, titulo, JOptionPane.WARNING_MESSAGE);
    }

    public static void exito(Component padre, String titulo, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
    }
}
