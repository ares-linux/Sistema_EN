/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import sistema_en.Conexion;
import sistema_en.Objetos.Bitacora;

/**
 *
 * @author aresb
 */
public class BitacoraDAO {
    
    
    public int contarBitacoraPorTrabajo(int id_trabajo) {
    String sql = "SELECT COUNT(*) AS total FROM Bitacora WHERE id_trabajo = ?";

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
    
    public List<Bitacora> obtenerBitacoras(int id_trabajo) {
    List<Bitacora> listaBitacoras = new ArrayList<>(); 
    String sql = "SELECT * FROM Bitacora WHERE id_trabajo = ?";

    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, id_trabajo);
        try (ResultSet rs = ps.executeQuery()) {
            
            // 2. Usar 'while' para recorrer todas las filas encontradas
            while (rs.next()) {
                Bitacora b = new Bitacora();

                b.setId_bitacora(rs.getInt("id_bitacora"));
                b.setId_trabajo(rs.getInt("id_trabajo"));
                b.setId_trabajador(rs.getInt("id_trabajador"));
                
                String evidenciaStr = rs.getString("evidencias");
                if (evidenciaStr != null && !evidenciaStr.trim().isEmpty()) {
                    // Convierte el texto separado por comas en un List<String>
                    List<String> listaEvidencias = Arrays.asList(evidenciaStr.split("\\s*,\\s*"));
                    b.setEvidencias(listaEvidencias);
                }
                
                b.setNotas(rs.getString("notas"));

                String fecha = rs.getString("fecha");
                String hora_llegada = rs.getString("hora_llegada");
                String hora_salida = rs.getString("hora_salida");

                if (fecha != null && !fecha.trim().isEmpty()) {
                    b.setFecha(LocalDate.parse(fecha));
                }
                if (hora_llegada != null && !hora_llegada.trim().isEmpty()) {
                    b.setHora_llegada(LocalTime.parse(hora_llegada));
                }
                if (hora_salida != null && !hora_salida.trim().isEmpty()) {
                    b.setHora_salida(LocalTime.parse(hora_salida));
                }

                // 3. Agregar el objeto a la lista en lugar de hacer 'return b;'
                listaBitacoras.add(b); 
            }
        }
    } catch (SQLException e) {
        System.out.println("Error SQL: " + e.getMessage());
    }

    // 4. Devolver la lista completa (estará vacía si no encontró ninguna)
    return listaBitacoras; 
}
    
    public boolean insertarBitacora(String fecha, String hora_llegada,String hora_salida,String evidencia, String notas, int id_trabajo, int id_trabajador) {
    String sql = "INSERT INTO Bitacora (fecha,hora_llegada,hora_salida,evidencias,notas,id_trabajador,id_trabajo) VALUES (?,?,?,?,?,?,?)";
    
    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {
        
        ps.setString(1, fecha);
        ps.setString(2, hora_llegada);
        ps.setString(3, hora_salida);
        ps.setString(4, evidencia);
        ps.setString(5, notas);
        ps.setInt(6, id_trabajador);
        ps.setInt(7, id_trabajo);
        
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("Error al insertar la bitacora: " + e.getMessage());
        return false;
    }
}
    
    public boolean actualizarBitacora(String fecha, String hora_llegada, String hora_salida, String evidencia, String notas, int id_trabajador, int id_bitacora) {
        String sql = "UPDATE Bitacora SET fecha = ?, hora_llegada = ?, hora_salida = ?, evidencias = ?, notas = ?, id_trabajador = ? WHERE id_bitacora = ?;";
        
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, fecha);
            ps.setString(2, hora_llegada);
            ps.setString(3, hora_salida);
            ps.setString(4, evidencia);
            ps.setString(5, notas);
            ps.setInt(6, id_trabajador);
            ps.setInt(7, id_bitacora);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar la bitacora: " + e.getMessage());
        }
        return false;
    }
    
    public List<Bitacora> buscarBitacoras(int id_trabajo,String fecha_buscar) {
    List<Bitacora> listaBitacoras = new ArrayList<>(); 
    String sql = "SELECT * FROM Bitacora WHERE id_trabajo = ? AND fecha = ?";

    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, id_trabajo);
        try (ResultSet rs = ps.executeQuery()) {
            
            // 2. Usar 'while' para recorrer todas las filas encontradas
            while (rs.next()) {
                Bitacora b = new Bitacora();

                b.setId_bitacora(rs.getInt("id_bitacora"));
                b.setId_trabajo(rs.getInt("id_trabajo"));
                b.setId_trabajador(rs.getInt("id_trabajador"));
                
                String evidenciaStr = rs.getString("evidencias");
                if (evidenciaStr != null && !evidenciaStr.trim().isEmpty()) {
                    // Convierte el texto separado por comas en un List<String>
                    List<String> listaEvidencias = Arrays.asList(evidenciaStr.split("\\s*,\\s*"));
                    b.setEvidencias(listaEvidencias);
                }
                
                b.setNotas(rs.getString("notas"));

                String fecha = rs.getString("fecha");
                String hora_llegada = rs.getString("hora_llegada");
                String hora_salida = rs.getString("hora_salida");

                if (fecha != null && !fecha.trim().isEmpty()) {
                    b.setFecha(LocalDate.parse(fecha));
                }
                if (hora_llegada != null && !hora_llegada.trim().isEmpty()) {
                    b.setHora_llegada(LocalTime.parse(hora_llegada));
                }
                if (hora_salida != null && !hora_salida.trim().isEmpty()) {
                    b.setHora_salida(LocalTime.parse(hora_salida));
                }

                // 3. Agregar el objeto a la lista en lugar de hacer 'return b;'
                listaBitacoras.add(b); 
            }
        }
    } catch (SQLException e) {
        System.out.println("Error SQL: " + e.getMessage());
    }

    // 4. Devolver la lista completa (estará vacía si no encontró ninguna)
    return listaBitacoras; 
}
    
}
