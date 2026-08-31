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

    public static File directorioTokens() {
        return new File(directorioBaseConfig(), GoogleServicesProvider.TOKENS_DIRECTORY_PATH);
    }
}
