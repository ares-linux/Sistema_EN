/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistema_en;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 *
 * @author ares
 */
public class Version {
    public static final String Version_programa = Conexion.VERSION_APP;

    public static String leerTextoRecurso(String ruta) {
        try (InputStream is = Version.class.getResourceAsStream(ruta)) {
            if (is == null) {
                return "No se pudieron cargar las notas de versión.";
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (Exception e) {
            return "Error al leer el archivo de notas: " + e.getMessage();
        }
    }

}
