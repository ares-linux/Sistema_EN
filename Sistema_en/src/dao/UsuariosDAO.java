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
        System.err.println("Error al actualizar contraseña en BD: " + e.getMessage());
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

    public boolean actualizarUsuario(int id ,String usuario) {
    
        String nuevoNombre = usuario;
        
        String sql = "UPDATE Usuarios SET usuario = ? WHERE id_usuarios = ?";

    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, nuevoNombre);
        ps.setInt(2, id);

        int filasAfectadas = ps.executeUpdate();
        return filasAfectadas > 0; 

    } catch (SQLException e) {
        System.err.println("Error al actualizar el nombre de usuario en BD: " + e.getMessage());
        return false;
    }
}

    public boolean insertarUsuario(String usuario, String contraseña) {
        String nuevoHash = BCrypt.hashpw(contraseña, BCrypt.gensalt());
        String sql = "INSERT INTO Usuarios (usuario, contraseña) VALUES (?, ?)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (con == null) return false;

            ps.setString(1, usuario);
            ps.setString(2, nuevoHash);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al crear usuario en BD: " + e.getMessage());
            return false;
        }
    }

    public boolean existeAdministrador() {
        String sql = "SELECT COUNT(*) FROM Usuarios";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (con == null) return false;
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Error al contar usuarios: " + e.getMessage());
            return false;
        }
    }
}
