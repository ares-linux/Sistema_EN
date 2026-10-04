package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sistema_en.Conexion;

/**
 * @author aresb
 */
public class HerramientaDAO {

    /**
     * Inserta una nueva herramienta.
     *
     * @param id_tipo  ID del tipo de herramienta (FK a Tipos_herramientas.id).
     * @param n_serie  Número de serie.
     * @param foto     Ruta de la foto.
     * @return true si la inserción fue exitosa.
     */
    public boolean insertarHerramienta(int id_tipo, String n_serie, String foto) {
        String sql = "INSERT INTO Herramienta (t_herramienta, n_serie, foto) VALUES (?, ?, ?)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id_tipo);
            ps.setString(2, n_serie);
            ps.setString(3, foto);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al guardar herramienta en BD: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene todas las herramientas con JOIN a Tipos_herramientas para mostrar el nombre.
     * Devuelve arreglos String[4]: { id_herramienta, nombre_tipo, n_serie, foto }.
     */
    public List<String[]> obtenerHerramienta() {
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT h.id_herramienta, t.nombre AS tipo, h.n_serie, h.foto "
                   + "FROM Herramienta h "
                   + "LEFT JOIN Tipos_herramienta t ON h.t_herramienta = t.id_tipo_herramienta";

        Connection con = Conexion.conectar();

        if (con == null) {
            System.err.println("No se pudo realizar la consulta porque la conexión es null.");
            return lista;
        }

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String[] fila = new String[4];
                fila[0] = String.valueOf(rs.getInt("id_herramienta"));
                fila[1] = rs.getString("tipo");
                fila[2] = rs.getString("n_serie");
                fila[3] = rs.getString("foto");
                lista.add(fila);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener las herramientas de la BD: " + e.getMessage());
        } finally {
            try {
                if (con != null && !con.isClosed()) {
                    con.close();
                }
            } catch (SQLException e) {
                // no se hace nada al cerrar
            }
        }
        return lista;
    }

    /**
     * Busca herramientas por número de serie y/o tipo.
     *
     * Acepta valores nulos o vacíos con seguridad: se interpretan como "sin filtro",
     * igual que el centinela "Todos" para el tipo.
     *
     * @param n_serie       Texto a buscar en n_serie (puede ser vacío o null).
     * @param nombre_tipo   Nombre del tipo, "Todos", vacío o null.
     */
    public List<String[]> buscarHerramienta(String n_serie, String nombre_tipo) {
        List<String[]> lista = new ArrayList<>();

        // Un null en cualquiera de los dos criterios significa "traer todo".
        String serie = (n_serie == null) ? "" : n_serie.trim();
        boolean filtrarPorTipo = nombre_tipo != null
                && !nombre_tipo.trim().isEmpty()
                && !"Todos".equals(nombre_tipo);

        String sql = "SELECT h.id_herramienta, t.nombre AS tipo, h.n_serie, h.foto "
                   + "FROM Herramienta h LEFT JOIN Tipos_herramienta t ON h.t_herramienta = t.id_tipo_herramienta "
                   + "WHERE h.n_serie LIKE ?"
                   + (filtrarPorTipo ? " AND t.nombre = ?" : "");

        Connection con = Conexion.conectar();

        if (con == null) {
            System.err.println("No se pudo realizar la consulta porque la conexión es null.");
            return lista;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + serie + "%");

            if (filtrarPorTipo) {
                ps.setString(2, nombre_tipo.trim());
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String[] herramienta = new String[4];
                    herramienta[0] = String.valueOf(rs.getInt("id_herramienta"));
                    herramienta[1] = rs.getString("tipo");
                    herramienta[2] = rs.getString("n_serie");
                    herramienta[3] = rs.getString("foto");
                    lista.add(herramienta);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en la BD al buscar: " + e.getMessage());
        } finally {
            try {
                if (con != null && !con.isClosed()) {
                    con.close();
                }
            } catch (SQLException e) {
                // no se hace nada al cerrar
            }
        }
        return lista;
    }

    /**
     * Elimina una herramienta por su ID.
     */
    public boolean eliminarHerramienta(int id_herramienta) {
        String sql = "DELETE FROM Herramienta WHERE id_herramienta = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id_herramienta);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar herramienta de la BD: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza una herramienta existente.
     *
     * @param id_herramienta  ID de la herramienta a actualizar.
     * @param id_tipo         Nuevo ID del tipo de herramienta.
     * @param n_serie         Nuevo número de serie.
     * @param foto            Nueva ruta de foto.
     */
    public boolean actualizarHerramienta(int id_herramienta, int id_tipo, String n_serie, String foto) {
        String sql = "UPDATE Herramienta SET t_herramienta = ?, n_serie = ?, foto = ? WHERE id_herramienta = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id_tipo);
            ps.setString(2, n_serie);
            ps.setString(3, foto);
            ps.setInt(4, id_herramienta);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar herramienta en BD: " + e.getMessage());
            return false;
        }
    }
}
