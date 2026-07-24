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
public class ClienteDAO {
    
    public List<String[]> obtenerCliente() {
    List<String[]> lista = new ArrayList<>();
    String sql = "SELECT id_cliente, nombre_c, telefono FROM Cliente";
    
    Connection con = Conexion.conectar();

    if (con == null) {
        System.err.println("No se pudo realizar la consulta porque la conexión es null.");
        return lista; 
    }

    try (PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            String[] fila = new String[3];
            fila[0] = String.valueOf(rs.getInt("id_cliente"));
            fila[1] = rs.getString("nombre_c");
            fila[2] = rs.getString("telefono");
            
            lista.add(fila);
        }

    } catch (SQLException e) {
        System.err.println("Error al obtener los trabajadores de la BD: " + e.getMessage());
    } finally {

        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException ex) {
            System.err.println("Error al cerrar conexión: " + ex.getMessage());
        }
    }
    return lista;
}
    
    public int insertarClienteYObtenerID(String nombreC, String telefono) {
    // Usamos las columnas del PDF: nombre_c, direccion, telefono
    String sql = "INSERT INTO Cliente (nombre_c,telefono) VALUES ( ?, ?)";
    
    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

        ps.setString(1, nombreC);
        ps.setString(2, telefono);

        int filasAfectadas = ps.executeUpdate();
        
        if (filasAfectadas > 0) {
            // Obtenemos la llave (ID) autoincremental generada
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // Retorna el id_cliente asignado
                }
            }
        }
    } catch (SQLException e) {
        System.err.println("Error al insertar cliente: " + e.getMessage());
    }
    return -1; // Retorna -1 si falló
}
    
    
    public boolean actualizarCliente(int idCliente, String nombreC, String telefono) {
    String sql = "UPDATE Cliente SET nombre_c = ?, telefono = ? WHERE id_cliente = ?";

    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, nombreC);
        ps.setString(2, telefono);
        ps.setInt(3, idCliente);

        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("Error al actualizar cliente: " + e.getMessage());
        return false;
    }
}
}
