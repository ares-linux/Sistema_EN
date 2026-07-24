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
public class TrabajoDAO {
    public boolean insertarTrabajo(String direccion, String descripcion, String fecha, String hora, String estado, int idCliente) {
    
    String sql = "INSERT INTO Trabajo (direccion,descripcion, fecha, hora, estado, cliente) VALUES (?, ?, ?, ?, ?, ?)";
    
    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {
        
        ps.setString(1, direccion);
        ps.setString(2, descripcion);
        ps.setString(3, fecha);
        ps.setString(4, hora);
        ps.setString(5, estado);
        ps.setInt(6, idCliente); 
        
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("Error al insertar trabajo: " + e.getMessage());
        return false;
    }
}
    
    public List<String[]> obtenerTrabajosConClientes() {
    List<String[]> lista = new ArrayList<>();
    
    String sql = "SELECT t.id_trabajo, c.nombre_c, c.telefono, t.direccion, t.descripcion, t.fecha, t.hora " +
                 "FROM Trabajo t " +
                 "INNER JOIN Cliente c ON t.cliente = c.id_cliente";

    Connection con = Conexion.conectar();
    
    if (con == null) {
        System.err.println("No se pudo realizar la consulta porque la conexión es null.");
        return lista;
    }

    try (PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            
            String[] fila = new String[7];
            fila[0] = String.valueOf(rs.getInt("id_trabajo"));
            fila[1] = rs.getString("nombre_c");
            fila[2] = rs.getString("telefono");
            fila[3] = rs.getString("direccion");
            fila[4] = rs.getString("descripcion");
            fila[5] = rs.getString("fecha");
            fila[6] = rs.getString("hora");
            
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

public boolean eliminarTrabajo(int idTrabajo) {
    String sql = "DELETE FROM Trabajo WHERE id_trabajo = ?";

    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, idTrabajo);
        return ps.executeUpdate() > 0;

    } catch (SQLException e) {
        System.err.println("Error al eliminar trabajo de la BD: " + e.getMessage());
        return false;
    }
}

public boolean actualizarTrabajo(int idTrabajo, String direccion ,String descripcion, String fecha, String hora, String estado) {
    String sql = "UPDATE Trabajo SET direccion = ?, descripcion = ?, fecha = ?, hora = ?, estado = ? WHERE id_trabajo = ?;";
    try {
        Connection con = Conexion.conectar();
        if (con != null) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, direccion);
            ps.setString(2, descripcion);
            ps.setString(3, fecha);
            ps.setString(4, hora);
            ps.setString(5, estado);
            ps.setInt(6, idTrabajo);
            
            return ps.executeUpdate() > 0;
        }
    } catch (SQLException e) {
        System.err.println("Error al actualizar trabajo: " + e.getMessage());
    }
    return false;
}

}
