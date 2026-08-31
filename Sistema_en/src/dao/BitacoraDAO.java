package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import sistema_en.Conexion;
import sistema_en.Objetos.Bitacora;

public class BitacoraDAO {
    
    // Instancia DAO para recuperar las evidencias de cada bitácora
    private final EvidenciaDAO evidenciaDAO = new EvidenciaDAO();

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
                while (rs.next()) {
                    Bitacora b = mapResultSetToBitacora(rs);
                    // Carga las evidencias asociadas a esta bitácora
                    b.setEvidencias(evidenciaDAO.obtenerEvidencia(b.getId_bitacora()));
                    listaBitacoras.add(b); 
                }
            }
        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
        }
        return listaBitacoras; 
    }

    public int insertarBitacora(String fecha, String hora_llegada, String hora_salida, String notas, int id_trabajador, int id_trabajo) {
        String sql = "INSERT INTO Bitacora (fecha, hora_llegada, hora_salida, notas, id_trabajador, id_trabajo) VALUES (?, ?, ?, ?, ?, ?)";
        int idGenerado = -1;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, fecha);
            ps.setString(2, hora_llegada);
            ps.setString(3, hora_salida);
            ps.setString(4, notas);
            ps.setInt(5, id_trabajador);
            ps.setInt(6, id_trabajo);
            
            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        idGenerado = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar la bitacora: " + e.getMessage());
        }
        return idGenerado;
    }

    public boolean actualizarBitacora(String fecha, String hora_llegada, String hora_salida, String notas, int id_trabajador, int id_bitacora) {
        String sql = "UPDATE Bitacora SET fecha = ?, hora_llegada = ?, hora_salida = ?, notas = ?, id_trabajador = ? WHERE id_bitacora = ?";
        
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, fecha);
            ps.setString(2, hora_llegada);
            ps.setString(3, hora_salida);
            ps.setString(4, notas);
            ps.setInt(5, id_trabajador);
            ps.setInt(6, id_bitacora);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar la bitacora: " + e.getMessage());
        }
        return false;
    }

    public List<Bitacora> buscarBitacoras(int id_trabajo, String fecha_buscar) {
        List<Bitacora> listaBitacoras = new ArrayList<>(); 
        String sql = "SELECT * FROM Bitacora WHERE id_trabajo = ? AND fecha = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, id_trabajo);
            ps.setString(2, fecha_buscar); // Se agregó el parámetro que faltaba
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Bitacora b = mapResultSetToBitacora(rs);
                    b.setEvidencias(evidenciaDAO.obtenerEvidencia(b.getId_bitacora()));
                    listaBitacoras.add(b); 
                }
            }
        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
        }
        return listaBitacoras; 
    }

    // Método auxiliar para construir el objeto Bitacora sin duplicar código
    private Bitacora mapResultSetToBitacora(ResultSet rs) throws SQLException {
        Bitacora b = new Bitacora();
        b.setId_bitacora(rs.getInt("id_bitacora"));
        b.setId_trabajo(rs.getInt("id_trabajo"));
        b.setId_trabajador(rs.getInt("id_trabajador"));
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
        return b;
    }
    
    
}