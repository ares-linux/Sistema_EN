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
import sistema_en.Objetos.Detalles_cotizacion;

/**
 *
 * @author ares
 */
public class Detalles_cotizacionDAO {
    
    public List<Detalles_cotizacion> obtenerDetalles_cotizacion(int id_cotizacion) {
    List<Detalles_cotizacion> listaDetalles_cotizacions = new ArrayList<>(); 
    String sql = "SELECT * FROM Detalles_cotizacion WHERE id_cotizacion = ?";

    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, id_cotizacion);
        try (ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Detalles_cotizacion d = new Detalles_cotizacion();

                d.setId_detalle(rs.getInt("id_detalle"));
                d.setId_cotizacion(rs.getInt("id_cotizacion"));
                d.setConcepto(rs.getString("concepto"));
                d.setN_material(rs.getString("n_material"));
                d.setCantidad(rs.getFloat("cantidad"));
                d.setPrecio_uni(rs.getFloat("precio_uni"));
                d.setPrecio(rs.getFloat("precio"));
                
                listaDetalles_cotizacions.add(d); 
            }
        }
    } catch (SQLException e) {
        System.out.println("Error SQL: " + e.getMessage());
    }

    return listaDetalles_cotizacions; 
}
    
    public boolean insertarDetalles_cotizacion(String concepto,String n_material, float cantidad, float precio_uni,float precio, int id_cotizacion) {
    String sql = "INSERT INTO Detalles_cotizacion (concepto,n_material,cantidad,precio_uni,precio,id_cotizacion) VALUES (?,?,?,?,?,?)";
    
    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {
        
        ps.setString(1, concepto);
        ps.setString(2, n_material);
        ps.setFloat(3, cantidad);
        ps.setFloat(4, precio_uni);
        ps.setFloat(5, precio);
        ps.setInt(6, id_cotizacion);
        
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("Error al insertar los detalles cotizacion: " + e.getMessage());
        return false;
    }
}
    
    public boolean actualizarDetalles_cotizacion(String concepto,String n_material, float cantidad, float precio_uni,float precio, int id_cotizacion, int id_detalle) {
        String sql = "UPDATE Detalles_cotizacion SET concepto = ?, n_material = ?, cantidad = ?, precio_uni = ?, precio = ?, id_cotizacion = ? WHERE id_detalle = ?;";
        
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, concepto);
            ps.setString(2, n_material);
            ps.setFloat(3, cantidad);
            ps.setFloat(4, precio_uni);
            ps.setFloat(5, precio);
            ps.setInt(6, id_cotizacion);
            ps.setInt(7, id_detalle);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar los detalles cotizacion: " + e.getMessage());
        }
        return false;
    }

    public boolean eliminarDetalles_cotizacion(int id_detalle) {
    String sql = "DELETE FROM Detalles_cotizacion WHERE id_detalle = ?";

    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, id_detalle);
        return ps.executeUpdate() > 0;

    } catch (SQLException e) {
        System.err.println("Error al eliminar los detalles cotizacion de la BD: " + e.getMessage());
        return false;
    }
}
    
}
