/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilidades;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Lectura de archivos de texto empaquetados en el classpath. No maneja el numero de
 * version del programa: ese vive en Conexion.VERSION_APP.
 */
public class UtilidadesTexto {

    /**
     * Devuelve el contenido de un recurso del classpath como texto. Se usa para las
     * notas de version (/archivos/nota.txt).
     *
     * @param ruta ruta del recurso, por ejemplo "/archivos/nota.txt"
     * @return el contenido del recurso, o un mensaje de error si no se pudo leer
     */
    public static String leerTextoRecurso(String ruta) {
        try (InputStream is = UtilidadesTexto.class.getResourceAsStream(ruta)) {
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