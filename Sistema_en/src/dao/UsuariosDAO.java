/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;
import sistema_en.Conexion;
import java.sql.ResultSet;

/**
 *
 * @author ares
 */
public class UsuariosDAO {
    public boolean actualizarContraseña(int id ,String contraseña) {
    
        String nuevoHash = BCrypt.hashpw(contraseña, BCrypt.gensalt());
        
        String sql = "UPDATE Usuarios SET contraseña = ? WHERE id_usuarios = ?";

    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, nuevoHash);
        ps.setInt(2, id);

        int filasAfectadas = ps.executeUpdate();
        return filasAfectadas > 0; 

    } catch (SQLException e) {
        System.err.println("Error al actualizar trabajador en BD: " + e.getMessage());
        return false;
    }
}
    
    public String obtenerHashPorId(int id) {
        String sql = "SELECT contraseña FROM Usuarios WHERE id_usuarios = ?";
        
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
             
            if (con == null) return null;
            
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("contraseña");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el hash de la BD: " + e.getMessage());
        }
        return null;
    }

    
}
