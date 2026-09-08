package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import sistema_en.Conexion;

public class InformesDAO {

    private static final String BETWEEN = " BETWEEN ? AND ?";

    public int contarTrabajosRegistrados(LocalDate inicio, LocalDate fin) {
        String sql = "SELECT COUNT(*) AS total FROM Trabajo WHERE fecha_cr" + BETWEEN;
        return contar(sql, inicio, fin);
    }

    public int contarTrabajosIniciados(LocalDate inicio, LocalDate fin) {
        String sql = "SELECT COUNT(*) AS total FROM Trabajo WHERE fecha_inicio" + BETWEEN;
        return contar(sql, inicio, fin);
    }

    public int contarTrabajosFinalizados(LocalDate inicio, LocalDate fin) {
        String sql = "SELECT COUNT(*) AS total FROM Trabajo WHERE fecha_fin" + BETWEEN;
        return contar(sql, inicio, fin);
    }

    public int contarClientesNuevos(LocalDate inicio, LocalDate fin) {
        String sql = "SELECT COUNT(*) AS total FROM Cliente WHERE fecha_registro" + BETWEEN;
        return contar(sql, inicio, fin);
    }

    public String clienteConMasTrabajos(LocalDate inicio, LocalDate fin) {
        String sql = "SELECT c.nombre_c, COUNT(t.id_trabajo) AS total "
                + "FROM Cliente c "
                + "LEFT JOIN Trabajo t ON t.id_cliente = c.id_cliente "
                + "AND t.fecha_cr BETWEEN ? AND ? "
                + "GROUP BY c.id_cliente "
                + "ORDER BY total DESC LIMIT 1";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, inicio.toString());
            ps.setString(2, fin.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int total = rs.getInt("total");
                    if (total > 0) {
                        return rs.getString("nombre_c") + " (" + total + ")";
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener cliente con mas trabajos: " + e.getMessage());
        }
        return "Sin trabajos en el mes";
    }

    public int contarClientesSoloUnTrabajo(LocalDate inicio, LocalDate fin) {
        String sql = "SELECT COUNT(*) AS total FROM ("
                + "SELECT c.id_cliente "
                + "FROM Cliente c "
                + "JOIN Trabajo t ON t.id_cliente = c.id_cliente "
                + "WHERE t.fecha_cr BETWEEN ? AND ? "
                + "GROUP BY c.id_cliente "
                + "HAVING COUNT(t.id_trabajo) = 1)";
        return contar(sql, inicio, fin);
    }

    public int contarCotizacionesRegistradas(LocalDate inicio, LocalDate fin) {
        String sql = "SELECT COUNT(*) AS total FROM Cotizacion WHERE fecha_creacion" + BETWEEN;
        return contar(sql, inicio, fin);
    }

    public int contarCotizacionesConvertidas(LocalDate inicio, LocalDate fin) {
        String sql = "SELECT COUNT(*) AS total FROM Cotizacion "
                + "WHERE id_trabajo IS NOT NULL AND fecha_creacion" + BETWEEN;
        return contar(sql, inicio, fin);
    }

    public double promedioTrabajosPorCliente(LocalDate inicio, LocalDate fin) {
        String sql = "SELECT COUNT(t.id_trabajo) AS trabajos, COUNT(DISTINCT c.id_cliente) AS clientes "
                + "FROM Cliente c LEFT JOIN Trabajo t ON t.id_cliente = c.id_cliente "
                + "AND t.fecha_cr BETWEEN ? AND ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, inicio.toString());
            ps.setString(2, fin.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int trabajos = rs.getInt("trabajos");
                    int clientes = rs.getInt("clientes");
                    if (clientes > 0) {
                        double promedio = (double) trabajos / clientes;
                        return Math.round(promedio * 100.0) / 100.0;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener promedio de trabajos por cliente: " + e.getMessage());
        }
        return 0.0;
    }

    public double promedioDuracionTrabajo(LocalDate inicio, LocalDate fin) {
        String sql = "SELECT AVG(JULIANDAY(fecha_fin) - JULIANDAY(fecha_inicio)) AS promedio "
                + "FROM Trabajo WHERE fecha_inicio" + BETWEEN
                + " AND fecha_fin IS NOT NULL AND fecha_fin <> ''";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, inicio.toString());
            ps.setString(2, fin.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double promedio = rs.getDouble("promedio");
                    return Math.round(promedio * 100.0) / 100.0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener promedio de duracion de trabajo: " + e.getMessage());
        }
        return 0.0;
    }

    private int contar(String sql, LocalDate inicio, LocalDate fin) {
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, inicio.toString());
            ps.setString(2, fin.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al contar en informe mensual: " + e.getMessage());
        }
        return 0;
    }
}