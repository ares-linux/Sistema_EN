package sistema_en;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    // Ruta exacta a tu archivo sin extensión
    private static final String URL = "jdbc:sqlite:/home/ares/Documentos/Bases de datos/sistema_EN"; 

    public static Connection conectar() {
        try {
            // Cargamos el Driver de SQLite
            Class.forName("org.sqlite.JDBC");
            
            // Conexión directa y rápida sin contraseñas
            return DriverManager.getConnection(URL);
            
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error en la conexión: " + e.getMessage());
            return null;
        }
    }
}