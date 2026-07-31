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
 * @author ares
 */
public class Tipos_herramientaDAO {

    /**
     * Inserta un nuevo tipo de herramienta en la tabla Tipos_herramientas.
     *
     * @param nombre Nombre del tipo de herramienta.
     * @return true si la inserción fue exitosa, false en caso contrario.
     */
    public boolean insertarTipo(String nombre) {
        String sql = "INSERT INTO Tipos_herramienta (nombre) VALUES (?)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al guardar el tipo de herramienta en BD: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene todos los tipos de herramientas como pares [id, nombre].
     *
     * @return Lista de arreglos String[2] con { id, nombre }.
     */
    public List<String[]> obtenerHerramientas() {
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT id_tipo_herramienta, nombre FROM Tipos_herramienta ORDER BY nombre";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (con == null) {
                System.err.println("No se pudo realizar la consulta porque la conexión es null.");
                return lista;
            }

            while (rs.next()) {
                String[] fila = new String[2];
                fila[0] = String.valueOf(rs.getInt("id_tipo_herramienta"));
                fila[1] = rs.getString("nombre");
                lista.add(fila);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener los tipos de herramienta de la BD: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Verifica si ya existe un tipo con el mismo nombre (case-insensitive).
     *
     * @param nombre Nombre a verificar.
     * @return true si ya existe, false si no.
     */
    public boolean existeTipo(String nombre) {
        String sql = "SELECT COUNT(*) FROM Tipos_herramienta WHERE LOWER(nombre) = LOWER(?)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al verificar tipo en BD: " + e.getMessage());
        }
        return false;
    }
}
