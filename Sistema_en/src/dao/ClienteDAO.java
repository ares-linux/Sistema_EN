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
import sistema_en.Objetos.Cliente;

/**
 *
 * @author aresb
 */
public class ClienteDAO {
    
    public int obtenerOCrearCliente(String nombre, String telefono) {
    // 1. Intentamos buscar si ya existe el cliente por nombre y teléfono
    String sqlBuscar = "SELECT id_cliente FROM Cliente WHERE nombre_c = ? AND telefono = ?";
    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sqlBuscar)) {
        ps.setString(1, nombre);
        ps.setString(2, telefono);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("id_cliente"); // Ya existe, retornamos su ID real
        }
    } catch (SQLException e) {
        System.err.println("Error al buscar cliente: " + e.getMessage());
    }

    // 2. Si no existe, lo insertamos como nuevo cliente
    String sqlInsertar = "INSERT INTO Cliente (nombre_c, telefono) VALUES (?, ?)";
    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sqlInsertar, PreparedStatement.RETURN_GENERATED_KEYS)) {
        ps.setString(1, nombre);
        ps.setString(2, telefono);
        ps.executeUpdate();
        
        ResultSet rsKeys = ps.getGeneratedKeys();
        if (rsKeys.next()) {
            return rsKeys.getInt(1); // Retornamos el ID recién generado
        }
    } catch (SQLException e) {
        System.err.println("Error al insertar cliente nuevo: " + e.getMessage());
    }
    
    return -1;
}
    
    public Cliente obtenerCliente(int id_cliente) {

    String sql = "SELECT * FROM Cliente WHERE id_cliente = ?";

    Connection con = Conexion.conectar();

    try {

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id_cliente);

        ResultSet rs = ps.executeQuery();

        if(rs.next()){

            Cliente c = new Cliente();

            c.setId_cliente(rs.getInt("id_cliente"));
            c.setNombre_c(rs.getString("nombre_c"));
            c.setTelefono(rs.getString("telefono"));
            c.setU_modificacion(rs.getString("u_modificacion"));
            c.setFecha_registro(rs.getString("fecha_registro"));

            return c;
        }

    } catch(SQLException e){
        System.out.println(e.getMessage());
    }

    return null;
}
    
    
    public List<String[]> obtenerClientes() {
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
            String[] fila = new String[4];
            fila[0] = String.valueOf(rs.getInt("id_cliente"));
            fila[1] = rs.getString("nombre_c");
            fila[2] = rs.getString("telefono");
            
            
            lista.add(fila);
        }

    } catch (SQLException e) {
        System.err.println("Error al obtener el cliente de la BD: " + e.getMessage());
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
    
    
    public boolean actualizarCliente(int id_cliente, String nombre_c, String telefono, String u_modificacion) {
    String sql = "UPDATE Cliente SET nombre_c = ?, telefono = ?, u_modificacion = ? WHERE id_cliente = ?";

    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, nombre_c);
        ps.setString(2, telefono);
        ps.setString(3, u_modificacion);
        ps.setInt(4, id_cliente);

        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("Error al actualizar cliente: " + e.getMessage());
        return false;
    }
}
}
