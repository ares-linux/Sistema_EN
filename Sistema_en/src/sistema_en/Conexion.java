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

public class Conexion {

    public static final String VERSION_APP = "1.1.0";
    private static final String ARCHIVO_CONFIG = "base_de_datos.properties";
    private static final String CLAVE_RUTA = "ruta";
    private static final String NOMBRE_ARCHIVO_BD = "sistema_EN";

    private static String rutaMemoria = null;

    public static String getRutaBDActual() {
        if (rutaMemoria == null) {
            rutaMemoria = obtenerRutaBD();
        }
        return rutaMemoria;
    }

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

    public static String rutaDefectoBD() {
        return Paths.get(rutaCarpetaDocumentosPorDefecto(), "Bases de datos", "sistema_EN").toString();
    }

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

    public static String rutaArchivoBD() {
        if (rutaMemoria != null) {
            return rutaMemoria;
        }
        String ruta = obtenerRutaBD();
        rutaMemoria = ruta;
        return ruta;
    }

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

    public static void actualizarRutaPorBaseC(String baseC) {
        if (baseC == null || baseC.isBlank()) {
            return;
        }
        String rutaCompleta = Paths.get(baseC, NOMBRE_ARCHIVO_BD).toString();
        guardarRutaBD(rutaCompleta);
    }

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

    public static String getNombreArchivoBD() {
        return NOMBRE_ARCHIVO_BD;
    }
}
