/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistema_en;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

/**
 *
 * @author ares
 */
public class Carpetas {
    
    private static boolean exito_cambiar_carpeta_cotizaciones;
    
    public static void crearCarpetaLocal(String rutaCarpeta) {
        try {
            Path path = Paths.get(rutaCarpeta);
        
            // Verifica si la carpeta ya existe; si no, la crea (incluyendo las carpetas padre si faltan)
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                System.out.println("✅ Carpeta local creada con éxito en: " + path.toAbsolutePath());
            } else {
                System.out.println("ℹ️ La carpeta local ya existe.");
            }
        
        } catch (IOException e) {
            System.err.println("❌ Error al crear la carpeta local: " + e.getMessage());
        }
    }
    
    public static boolean verificarCarpetaExiste(String rutaCarpeta) {
    Path path = Paths.get(rutaCarpeta);
    
    // Retorna true si la ruta existe Y además es una carpeta (no un archivo plano)
    return Files.exists(path) && Files.isDirectory(path);
}
    
    public static void actualizarRutaCotizaciones(String rutaAntigua, String rutaNueva, String nombreCarpetaCotizaciones) {
    java.nio.file.Path pathAntiguo = java.nio.file.Paths.get(rutaAntigua, nombreCarpetaCotizaciones);
    java.nio.file.Path pathNuevo = java.nio.file.Paths.get(rutaNueva, nombreCarpetaCotizaciones);
    
    try {
        // Si la ruta cambió y la carpeta en el directorio antiguo realmente existe
        if (!rutaAntigua.equals(rutaNueva) && java.nio.file.Files.exists(pathAntiguo)) {
            
            // Nos aseguramos de que la ruta nueva exista antes de mover
            java.nio.file.Path directorioNuevoPadre = java.nio.file.Paths.get(rutaNueva);
            if (!java.nio.file.Files.exists(directorioNuevoPadre)) {
                java.nio.file.Files.createDirectories(directorioNuevoPadre);
            }
            
            // Movemos la carpeta con todo su contenido a la nueva ubicación
            java.nio.file.Files.move(pathAntiguo, pathNuevo, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            System.out.println("✅ Las cotizaciones se movieron exitosamente a la nueva ruta configurada.");
            exito_cambiar_carpeta_cotizaciones = true;
            
        } else {
            // Si no existía antes o es la primera vez, simplemente nos aseguramos de crearla en la nueva ruta
            if (!java.nio.file.Files.exists(pathNuevo)) {
                java.nio.file.Files.createDirectories(pathNuevo);
                System.out.println("📁 Carpeta de cotizaciones creada en la nueva ruta.");
                
                exito_cambiar_carpeta_cotizaciones = true;
            }
        }
    } catch (java.io.IOException e) {
        System.err.println("❌ Error al gestionar el cambio de ruta de las cotizaciones: " + e.getMessage());
        exito_cambiar_carpeta_cotizaciones = false;
    }
}
    
    public static boolean Carpetas_cambio_cotizaciones(){
        
        return exito_cambiar_carpeta_cotizaciones;
        
    }
    
}
