/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sistema_en.Conexion;

/**
 *
 * @author aresb
 */

public class ConfiguracionesDAO {
    public boolean actualizarConfiguraciones(String tema, String sonido_error, String modelo_c,String carpeta_ce, String automaticamente_ce) {
    String sql = "UPDATE Configuraciones SET valor = ? WHERE clave = ?";
        Connection con = Conexion.conectar();
        
        if (con == null) return false;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            // Desactivamos el auto-commit para asegurar consistencia
            con.setAutoCommit(false);

            
            ps.setString(1, tema);
            ps.setString(2, "tema");
            ps.addBatch();

            ps.setString(1, sonido_error);
            ps.setString(2, "sonido_error");
            ps.addBatch();

            ps.setString(1, modelo_c);
            ps.setString(2, "modelo_c");
            ps.addBatch();

            ps.setString(1, carpeta_ce);
            ps.setString(2, "carpeta_ce");
            ps.addBatch();
            
            ps.setString(1, automaticamente_ce);
            ps.setString(2, "automaticamente_ce");
            ps.addBatch();

            System.out.println("Tema: " + tema);
            System.out.println("Sonido: " + sonido_error);
            System.out.println("Modelo: " + modelo_c);
            System.out.println("Carpeta: " + carpeta_ce);
            
            int[] resultados = ps.executeBatch();
            con.commit();

            return resultados.length > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar configuraciones: " + e.getMessage());
            try { con.rollback(); } catch (SQLException ex) { /* Ignorar */ }
        } finally {
            try { con.close(); } catch (SQLException e) { /* Ignorar */ }
        }
        return false;
    }


    public boolean actualizarCopias(String frecuencia, String preguntar, String manual, String cotizaciones, String ultima_copia, String base_c) {
    String sql = "UPDATE Configuraciones SET valor = ? WHERE clave = ?";
        Connection con = Conexion.conectar();
        
        if (con == null) return false;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            // Desactivamos el auto-commit para asegurar consistencia
            con.setAutoCommit(false);

            
            ps.setString(1, frecuencia);
            ps.setString(2, "frecuencia");
            ps.addBatch();

            ps.setString(1, preguntar);
            ps.setString(2, "preguntar");
            ps.addBatch();

            ps.setString(1, manual);
            ps.setString(2, "manual");
            ps.addBatch();

            ps.setString(1, cotizaciones);
            ps.setString(2, "cotizaciones");
            ps.addBatch();

            ps.setString(1, ultima_copia);
            ps.setString(2, "ultima_copia");
            ps.addBatch();
            
            ps.setString(1, base_c);
            ps.setString(2, "base_c");
            ps.addBatch();
            
            int[] resultados = ps.executeBatch();
            con.commit();

            return resultados.length > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar copias: " + e.getMessage());
            try { con.rollback(); } catch (SQLException ex) { /* Ignorar */ }
        } finally {
            try { con.close(); } catch (SQLException e) { /* Ignorar */ }
        }
        return false;
    }
    
    
    public List<String[]> obtenerConfiguraciones() {
    List<String[]> lista = new ArrayList<>();
    
    String sql = "SELECT id_configuracion,clave,valor FROM Configuraciones";

    Connection con = Conexion.conectar();
    
    if (con == null) {
        System.err.println("No se pudo realizar la consulta porque la conexión es null.");
        return lista;
    }

    try (PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            
            String[] fila = new String[3];
            fila[0] = String.valueOf(rs.getInt("id_configuracion"));
            fila[1] = rs.getString("clave");
            fila[2] = rs.getString("valor");
            
            lista.add(fila);
        }

    } catch (SQLException e) {
        System.err.println("Error al obtener los trabajos con clientes: " + e.getMessage());
    } finally {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException ex) {
            System.err.println("Error al cerrar la conexión: " + ex.getMessage());
        }
    }
    return lista;
}
       
}

    
