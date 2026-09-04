package sistema_en;

import java.io.File;

public class UtilidadesRutas {

    private UtilidadesRutas() {
    }

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

    public static File archivoConfig(String nombre) {
        return new File(directorioBaseConfig(), nombre);
    }

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

    public static File archivoConfigUsuario(String nombre) {
        return new File(directorioConfigUsuario(), nombre);
    }

    public static File directorioTokens() {
        File dir = new File(directorioConfigUsuario(), GoogleServicesProvider.TOKENS_DIRECTORY_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

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