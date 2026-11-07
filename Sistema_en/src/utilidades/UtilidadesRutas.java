package utilidades;

import java.io.File;
import sistema_en.GoogleServicesProvider;

/**
 * Resuelve las rutas de configuracion y credenciales de la aplicacion,
 * distinguiendo entre la carpeta del ejecutable y la carpeta del usuario.
 *
 * @author ares
 */
public class UtilidadesRutas {

    /**
     * Clase de utilidad: no se instancia.
     */
    private UtilidadesRutas() {
    }

    /**
     * Devuelve el directorio donde se encuentra el JAR en ejecucion o, si no
     * se puede determinar, el directorio de trabajo actual.
     *
     * @return directorio base del ejecutable
     */
    public static File directorioBaseConfig() {
        try {
            java.net.URL ubicacion = UtilidadesRutas.class.getProtectionDomain().getCodeSource().getLocation();
            if (ubicacion != null) {
                File f = new File(ubicacion.toURI());
                if (f.isFile() && f.getName().endsWith(".jar")) {
                    File padre = f.getParentFile();
                    if (padre != null) {
                        return padre;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("No se pudo localizar el directorio del JAR: " + e.getMessage());
        }
        return new File(System.getProperty("user.dir"));
    }

    /**
     * Devuelve un archivo de configuracion ubicado junto al ejecutable.
     *
     * @param nombre nombre del archivo
     * @return archivo de configuracion en el directorio base
     */
    public static File archivoConfig(String nombre) {
        return new File(directorioBaseConfig(), nombre);
    }

    /**
     * Devuelve el directorio de configuracion del usuario
     * ({@code ~/.Sistema_EN} o {@code %APPDATA%/.Sistema_EN}), creandolo si no
     * existe.
     *
     * @return directorio de configuracion del usuario
     */
    public static File directorioConfigUsuario() {
        boolean windows = System.getProperty("os.name", "").toLowerCase().contains("win");
        String base = windows
                ? System.getenv("APPDATA")
                : System.getProperty("user.home");
        if (base == null || base.isBlank()) {
            base = System.getProperty("user.home");
        }
        File dir = new File(base, ".Sistema_EN");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * Devuelve un archivo de configuracion ubicado en la carpeta del usuario.
     *
     * @param nombre nombre del archivo
     * @return archivo de configuracion del usuario
     */
    public static File archivoConfigUsuario(String nombre) {
        return new File(directorioConfigUsuario(), nombre);
    }

    /**
     * Devuelve la carpeta donde se guardan los tokens OAuth del usuario,
     * creandola si no existe.
     *
     * @return directorio de tokens
     */
    public static File directorioTokens() {
        File dir = new File(directorioConfigUsuario(), GoogleServicesProvider.TOKENS_DIRECTORY_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * Busca el archivo {@code credentials.json} en las ubicaciones posibles:
     * junto al ejecutable, en la carpeta del usuario y en el directorio de
     * trabajo.
     *
     * @return el primer archivo de credenciales valido, o {@code null} si no
     *         se encuentra
     */
    public static File buscarCredenciales() {
        File[] candidatos = {
            archivoConfig(GoogleServicesProvider.CREDENTIALS_FILE_PATH),
            archivoConfigUsuario(GoogleServicesProvider.CREDENTIALS_FILE_PATH),
            new File(System.getProperty("user.dir"), GoogleServicesProvider.CREDENTIALS_FILE_PATH)
        };
        for (File f : candidatos) {
            if (f.exists() && f.isFile() && f.length() > 0) {
                return f;
            }
        }
        return null;
    }
}
