package sistema_en;

import utilidades.UtilidadesRutas;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Fachada de acceso a la base de datos SQLite del sistema.
 *
 * <p>Resuelve la ubicacion del archivo de base de datos (persistida en
 * {@code base_de_datos.properties}), expone la version de la aplicacion y
 * entrega las conexiones JDBC utilizadas por la capa de persistencia.</p>
 *
 * @author ares
 */
public class Conexion {

    /**
     * Version actual de la aplicacion. Se muestra en la pantalla de
     * bienvenida y se usa para identificar la compilacion publicada.
     */
    public static final String VERSION_APP = "1.2.1";
    private static final String ARCHIVO_CONFIG = "base_de_datos.properties";
    private static final String CLAVE_RUTA = "ruta";
    private static final String NOMBRE_ARCHIVO_BD = "sistema_EN";

    private static String rutaMemoria = null;

    /**
     * Clase de utilidad: no se instancia.
     */
    private Conexion() {
    }

    /**
     * Devuelve la ruta de la base de datos en uso, leyendola la primera vez y
     * conservandola en memoria para las siguientes consultas.
     *
     * @return ruta completa del archivo de base de datos
     */
    public static String getRutaBDActual() {
        if (rutaMemoria == null) {
            rutaMemoria = obtenerRutaBD();
        }
        return rutaMemoria;
    }

    /**
     * Calcula la carpeta de documentos del usuario segun el sistema operativo.
     *
     * @return ruta de la carpeta de documentos por defecto
     */
    public static String rutaCarpetaDocumentosPorDefecto() {
        String home = System.getProperty("user.home");
        boolean windows = System.getProperty("os.name", "").toLowerCase().contains("win");
        String carpetaDocumentos = "Documents";
        if (!windows) {
            java.io.File documentos = new java.io.File(home, "Documentos");
            if (documentos.isDirectory()) {
                carpetaDocumentos = "Documentos";
            }
        }
        return Paths.get(home, carpetaDocumentos).toString();
    }

    /**
     * Devuelve la ubicacion por defecto del archivo de base de datos.
     *
     * @return ruta completa de la base de datos por defecto
     */
    public static String rutaDefectoBD() {
        return Paths.get(rutaCarpetaDocumentosPorDefecto(), "Bases de datos", "sistema_EN").toString();
    }

    /**
     * Obtiene la ruta de la base de datos configurada por el usuario. Si no
     * existe configuracion previa, guarda y devuelve la ruta por defecto.
     *
     * @return ruta completa de la base de datos configurada
     */
    public static String obtenerRutaBD() {
        Properties props = leerArchivoConfig();
        if (props != null) {
            String ruta = props.getProperty(CLAVE_RUTA);
            if (ruta != null && !ruta.isBlank()) {
                return ruta;
            }
        }
        guardarRutaBD(rutaDefectoBD());
        return rutaDefectoBD();
    }

    /**
     * Devuelve la ruta del archivo de base de datos, usando el valor cacheado
     * en memoria cuando ya fue resuelto.
     *
     * @return ruta completa del archivo de base de datos
     */
    public static String rutaArchivoBD() {
        if (rutaMemoria != null) {
            return rutaMemoria;
        }
        String ruta = obtenerRutaBD();
        rutaMemoria = ruta;
        return ruta;
    }

    /**
     * Persiste la ruta de la base de datos en el archivo de configuracion del
     * usuario y actualiza la copia en memoria.
     *
     * @param rutaCompleta ruta completa del archivo de base de datos; si es
     *                     nula o vacia no se realiza ningun cambio
     */
    public static void guardarRutaBD(String rutaCompleta) {
        if (rutaCompleta == null || rutaCompleta.isBlank()) {
            return;
        }
        Properties props = new Properties();
        props.setProperty(CLAVE_RUTA, rutaCompleta);
        try (FileOutputStream out = new FileOutputStream(UtilidadesRutas.archivoConfigUsuario(ARCHIVO_CONFIG))) {
            props.store(out, "Ruta de la base de datos SQLite");
        } catch (IOException e) {
            System.err.println("Error al guardar la ruta de la base de datos: " + e.getMessage());
        }
        rutaMemoria = rutaCompleta;
    }

    /**
     * Actualiza la ruta de la base de datos a partir de la carpeta base
     * elegida por el usuario.
     *
     * @param baseC carpeta base donde se ubica el archivo de base de datos; si
     *              es nula o vacia no se realiza ningun cambio
     */
    public static void actualizarRutaPorBaseC(String baseC) {
        if (baseC == null || baseC.isBlank()) {
            return;
        }
        String rutaCompleta = Paths.get(baseC, NOMBRE_ARCHIVO_BD).toString();
        guardarRutaBD(rutaCompleta);
    }

    /**
     * Comprueba si el archivo de base de datos existe y no esta vacio.
     *
     * @return {@code true} si la base de datos esta disponible
     */
    public static boolean existeBaseDatos() {
        File f = new File(rutaArchivoBD());
        return f.exists() && f.isFile() && f.length() > 0;
    }

    private static Properties leerArchivoConfig() {
        File archivo = UtilidadesRutas.archivoConfigUsuario(ARCHIVO_CONFIG);
        if (!archivo.exists()) {
            return null;
        }
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream(archivo)) {
            props.load(in);
            return props;
        } catch (IOException e) {
            System.err.println("Error al leer la configuracion de la base de datos: " + e.getMessage());
            return null;
        }
    }

    /**
     * Abre una conexion JDBC contra la base de datos SQLite configurada.
     *
     * @return conexion lista para usar, o {@code null} si el archivo no existe
     *         o se produce un error al conectar
     */
    public static Connection conectar() {
        try {
            String ruta = rutaArchivoBD();
            if (ruta == null || !new File(ruta).exists()) {
                System.err.println("No se encontro la base de datos en: " + ruta);
                return null;
            }
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:" + ruta + "?busy_timeout=5000");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error en la conexion: " + e.getMessage());
            return null;
        }
    }

    /**
     * Devuelve el nombre del archivo de base de datos, sin ruta ni extension.
     *
     * @return nombre del archivo de base de datos
     */
    public static String getNombreArchivoBD() {
        return NOMBRE_ARCHIVO_BD;
    }
}
