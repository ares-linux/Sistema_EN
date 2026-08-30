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
import sistema_en.Objetos.Evidencia;

/**
 *
 * @author ares
 */
public class EvidenciaDAO {
    public boolean insertarEvidencia(int id_bitacora, String id_google_drive, String nombre_archivo ,String ruta_local) {
        String sql = "INSERT INTO Evidencia (id_bitacora, id_google_drive, nombre_archivo, ruta_local) VALUES (?, ?, ?, ?)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id_bitacora);
            ps.setString(2, id_google_drive);
            ps.setString(3, nombre_archivo);
            ps.setString(4, ruta_local);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al guardar evidencia en BD: " + e.getMessage());
            return false;
        }
    }
    
    public List<Evidencia> obtenerEvidencia(int id_bitacora) {
    List<Evidencia> lista = new ArrayList<>();

    String sql = "SELECT id_evidencia, id_bitacora, id_google_drive, nombre_archivo, ruta_local FROM Evidencia WHERE id_bitacora = ?";

    try (Connection con = Conexion.conectar()) {

        if (con == null) {
            System.err.println("No se pudo realizar la consulta porque la conexión es null.");
            return lista;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id_bitacora);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Evidencia evidencia = new Evidencia();

                    evidencia.setId_evidencia(
                        rs.getInt("id_evidencia")
                    );
                    evidencia.setId_bitacora(
                        rs.getInt("id_bitacora")
                    );
                    evidencia.setId_google_drive(
                        rs.getString("id_google_drive")
                    );
                    evidencia.setNombre_archivo(
                        rs.getString("nombre_archivo")
                    );
                    evidencia.setRuta_local(
                        rs.getString("ruta_local")
                    );

                    lista.add(evidencia);
                }
            }
        }

    } catch (SQLException e) {
        System.err.println(
            "Error al obtener la evidencia de la BD: " + e.getMessage()
        );
    }

    return lista;
}
            
    public boolean eliminarEvidencia(int id_evidencia) {
        String sql = "DELETE FROM Evidencia WHERE id_evidencia = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id_evidencia);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar evidencia de la BD: " + e.getMessage());
            return false;
        }
    }
    
    public boolean eliminarEvidenciasPorBitacora(int id_bitacora) {
        String sql = "DELETE FROM Evidencia WHERE id_bitacora = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id_bitacora);

            int filasAfectadas = ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al eliminar evidencias de la bitácora en la BD: " + e.getMessage());
            return false;
        }
    }
    
    public boolean actualizarEvidencia(int id_evidencia, int id_bitacora, String id_google_drive, String nombre_archivo ,String ruta_local) {
        String sql = "UPDATE Evidencia SET id_bitacora = ?, id_google_drive = ?, nombre_archivo = ?, ruta_local = ? WHERE id_evidencia = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id_bitacora);
            ps.setString(2, id_google_drive);
            ps.setString(3, nombre_archivo);
            ps.setString(4, ruta_local);
            ps.setInt(5, id_evidencia);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar evidencia en BD: " + e.getMessage());
            return false;
        }
    }
}
