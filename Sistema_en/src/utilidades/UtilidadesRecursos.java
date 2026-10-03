/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilidades;

import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 * Rutas y carga de los recursos empaquetados en el classpath: imagenes, audios y
 * archivos de texto.
 *
 * Centraliza las rutas para que un renombrado de carpeta no tenga que repetirse en
 * cada ventana, y para que ninguna imagen se construya con una URL nula.
 *
 * Las constantes son String y no URL a proposito: valores como FOTO_USUARIO_CLARO se
 * escriben dentro de un campo de texto y se comparan con equals() para detectar que
 * el usuario todavia no eligio una foto.
 */
public final class UtilidadesRecursos {

    private static final String IMAGENES = "/imagenes/";
    private static final String AUDIOS = "/audios/";
    private static final String ARCHIVOS = "/archivos/";

    public static final String LOGO = IMAGENES + "logo.png";
    public static final String LOGO_GRANDE = IMAGENES + "logo 2.0.png";

    public static final String FOTO_CAMARA_CLARO = IMAGENES + "camara_claro.png";
    public static final String FOTO_CAMARA_OSCURO = IMAGENES + "camara_oscuro.png";

    public static final String FOTO_USUARIO_CLARO = IMAGENES + "usuario_claro.png";
    public static final String FOTO_USUARIO_OSCURO = IMAGENES + "usuario_oscuro.png";

    public static final String FOTO_HERRAMIENTA_CLARO = IMAGENES + "herramientas_claro.png";
    public static final String FOTO_HERRAMIENTA_OSCURO = IMAGENES + "herramientas_oscuro.png";

    public static final String AUDIO_ERROR = AUDIOS + "error.wav";

    public static final String NOTA_VERSION = ARCHIVOS + "nota.txt";

    private UtilidadesRecursos() {
    }

    /**
     * Resuelve un recurso del classpath.
     *
     * @param ruta ruta absoluta del recurso, por ejemplo "/imagenes/logo.png"
     * @return la URL del recurso, o null si no esta en el classpath
     */
    public static URL url(String ruta) {
        URL url = UtilidadesRecursos.class.getResource(ruta);

        if (url == null) {
            System.out.println("No se encontró el recurso: " + ruta);
        }

        return url;
    }

    /**
     * Carga un recurso de imagen sin cambiar su tamaño.
     *
     * @param ruta ruta absoluta del recurso
     * @return el icono, o null si el recurso no existe o no se pudo leer
     */
    public static ImageIcon icono(String ruta) {
        URL url = url(ruta);
        return url == null ? null : new ImageIcon(url);
    }

    /**
     * Carga un recurso de imagen y lo estira a la medida indicada. Para imágenes que
     * deben conservar su proporcion usar {@link #icono(String)} y escalarlas en el
     * label.
     *
     * @param ruta  ruta absoluta del recurso
     * @param ancho ancho final en pixeles
     * @param alto  alto final en pixeles
     * @return el icono escalado, o null si el recurso no existe
     */
    public static ImageIcon iconoEscalado(String ruta, int ancho, int alto) {
        ImageIcon original = icono(ruta);

        if (original == null) {
            return null;
        }

        Image imagenEscalada = original.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(imagenEscalada);
    }
}