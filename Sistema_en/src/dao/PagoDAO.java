/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import sistema_en.Objetos.Pago;
import sistema_en.Conexion;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author ares
 */
public class PagoDAO {
    
    public int sumarPagosPorTrabajo(int id_trabajo){
        
        String sql = "SELECT sum(monto) AS total FROM Pago WHERE id_trabajo = ?";

    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, id_trabajo);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return 0;
        
    }
    
    public int contarPagosPorTrabajo(int id_trabajo) {
    String sql = "SELECT COUNT(*) AS total FROM Pago WHERE id_trabajo = ?";

    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, id_trabajo);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return 0;
}
    
    public List<Pago> obtenerPagos(int id_trabajo) {
    List<Pago> listaPagos = new ArrayList<>(); 
    String sql = "SELECT * FROM Pago WHERE id_trabajo = ? ORDER BY fecha_pago DESC";

    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, id_trabajo);
        try (ResultSet rs = ps.executeQuery()) {
            
            // 2. Usar 'while' para recorrer todas las filas encontradas
            while (rs.next()) {
                Pago p = new Pago();

                p.setId_pago(rs.getInt("id_pago"));
                p.setId_trabajo(rs.getInt("id_trabajo"));
                p.setMonto(rs.getFloat("monto"));
                p.setConcepto(rs.getString("concepto"));
                String fecha_pago = rs.getString("fecha_pago");                 

                if (fecha_pago != null && !fecha_pago.trim().isEmpty()) {
                    try {
                        // Opción 1: Si tu base de datos guarda la fecha en formato estándar (YYYY-MM-DD)
                        p.setFecha_pago(LocalDate.parse(fecha_pago));
                    } catch (Exception e) {
                        // Opción 2: Si tu base de datos guarda la fecha con texto en español u otro formato personalizado,
                        // define el formato exacto aquí. (Ejemplo provisional si viene como 'd de MMMM de yyyy'):
                        DateTimeFormatter formato = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", new java.util.Locale("es", "ES"));
                        p.setFecha_pago(LocalDate.parse(fecha_pago, formato));
                    }
                }       
                // 3. Agregar el objeto a la lista en lugar de hacer 'return b;'
                listaPagos.add(p); 
            }
        }
    } catch (SQLException e) {
        System.out.println("Error SQL: " + e.getMessage());
    }
    // 4. Devolver la lista completa (estará vacía si no encontró ninguna)
    return listaPagos; 
}
    
  public boolean insertarPago(float monto, String concepto, String fecha_pago, int id_trabajo) {
    String sql = "INSERT INTO Pago (monto, concepto, fecha_pago, id_trabajo) VALUES (?, ?, ?, ?)";
    
    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {
        
        ps.setFloat(1, monto);
        ps.setString(2, concepto);
        ps.setString(3, fecha_pago);
        ps.setInt(4, id_trabajo);
        
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("Error al insertar el pago: " + e.getMessage());
        return false;
    }
}
    
     public boolean actualizarPago(float monto,String concepto,String fecha_pago , int id_trabajo, int id_pago) {
        String sql = "UPDATE Pago SET monto = ?, concepto = ?, fecha_pago = ?, id_trabajo = ? WHERE id_pago = ?";
        
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setFloat(1, monto);
            ps.setString(2, concepto);
            ps.setString(3, fecha_pago);
            ps.setInt(4, id_trabajo);
            ps.setInt(5, id_pago);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar el pago: " + e.getMessage());
        }
        return false;
    }
    
}
