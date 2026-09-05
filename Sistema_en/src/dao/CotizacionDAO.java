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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import sistema_en.Conexion;
import sistema_en.Objetos.Cotizacion;
import sistema_en.Objetos.Detalles_cotizacion;

/**
 *
 * @author aresb
 */
public class CotizacionDAO {
    
    public List<Cotizacion> obtenerCotizacionesPrueba() {
    List<Cotizacion> listaCotizaciones = new ArrayList<>(); 
    String sql = "SELECT * FROM Cotizacion WHERE tipo = ?";

    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {
        
        // Asignamos el valor al parámetro '?' para mayor seguridad
        ps.setString(1, "Prueba");

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Cotizacion c = new Cotizacion();

                c.setId_cotizacion(rs.getInt("id_cotizacion"));
                c.setTipo(rs.getString("tipo"));
                c.setSubtotal(rs.getFloat("subtotal"));
                c.setIva(rs.getFloat("iva"));
                c.setTotal(rs.getFloat("total"));
                c.setEstado(rs.getString("estado"));
                String fecha_creacion = rs.getString("fecha_creacion");                

                if (fecha_creacion != null && !fecha_creacion.trim().isEmpty()) {
                    try {
                        try {
                            c.setFecha_creacion(LocalDate.parse(fecha_creacion));
                        } catch (Exception e) {
                            DateTimeFormatter formato = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", new java.util.Locale("es", "ES"));
                            c.setFecha_creacion(LocalDate.parse(fecha_creacion, formato));
                        }
                    } catch (Exception ex) {
                        System.err.println("Error parsing date: " + fecha_creacion);
                    }
                }       
                listaCotizaciones.add(c); 
            }
        }
    } catch (SQLException e) {
        System.out.println("Error SQL: " + e.getMessage());
    }
    
    System.out.println("Cotizaciones encontradas: " + listaCotizaciones.size());
    return listaCotizaciones; 
}
    
    public List<Cotizacion> obtenerCotizaciones(int id_trabajo) {
        System.out.println("ID trabajo recibido: " + id_trabajo);
    List<Cotizacion> listaCotizaciones = new ArrayList<>(); 
    String sql = (id_trabajo == 0) ? "SELECT * FROM Cotizacion" : "SELECT * FROM Cotizacion WHERE id_trabajo = ? AND tipo = 'Normal'";

    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        System.out.println("Ejecutando consulta para id_trabajo = " + id_trabajo);
        if (id_trabajo != 0) {
            ps.setInt(1, id_trabajo);
        }
        try (ResultSet rs = ps.executeQuery()) {
            
            // 2. Usar 'while' para recorrer todas las filas encontradas
            while (rs.next()) {
                Cotizacion c = new Cotizacion();

                c.setId_cotizacion(rs.getInt("id_cotizacion"));
                c.setTipo(rs.getString("tipo"));
                c.setSubtotal(rs.getFloat("subtotal"));
                c.setIva(rs.getFloat("iva"));
                c.setTotal(rs.getFloat("total"));
                c.setEstado(rs.getString("estado"));
                String fecha_creacion = rs.getString("fecha_creacion");                 

                if (fecha_creacion != null && !fecha_creacion.trim().isEmpty()) {
                    try {
                        try {
                            // Opción 1: Si tu base de datos guarda la fecha en formato estándar (YYYY-MM-DD)
                            c.setFecha_creacion(LocalDate.parse(fecha_creacion));
                        } catch (Exception e) {
                            // Opción 2: Si tu base de datos guarda la fecha con texto en español u otro formato personalizado,
                            // define el formato exacto aquí. (Ejemplo provisional si viene como 'd de MMMM de yyyy'):
                            DateTimeFormatter formato = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", new java.util.Locale("es", "ES"));
                            c.setFecha_creacion(LocalDate.parse(fecha_creacion, formato));
                        }
                    } catch (Exception ex) {
                        System.err.println("Error parsing date: " + fecha_creacion);
                    }
                }       
                // 3. Agregar el objeto a la lista en lugar de hacer 'return b;'
                listaCotizaciones.add(c); 
            }
        }
    } catch (SQLException e) {
        System.out.println("Error SQL: " + e.getMessage());
    }
    // 4. Devolver la lista completa (estará vacía si no encontró ninguna)
    System.out.println("Cotizaciones encontradas: " + listaCotizaciones.size());
    return listaCotizaciones; 
}
    
    public Cotizacion obtenerUltimaCotizacion(int id_trabajo) {
        String sql = "SELECT * FROM Cotizacion WHERE id_trabajo = ? ORDER BY id_cotizacion DESC LIMIT 1";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (con == null) return null;

            ps.setInt(1, id_trabajo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cotizacion c = new Cotizacion();

                    c.setId_cotizacion(rs.getInt("id_cotizacion"));
                    c.setId_trabajo(rs.getInt("id_trabajo"));
                    c.setSubtotal(rs.getFloat("subtotal"));
                    c.setIva(rs.getFloat("iva"));
                    c.setTotal(rs.getFloat("total"));
                    c.setEstado(rs.getString("estado"));
                    c.setTipo(rs.getString("tipo"));

                    String fecha_creacion = rs.getString("fecha_creacion");
                    if (fecha_creacion != null && !fecha_creacion.trim().isEmpty()) {
                        c.setFecha_creacion(LocalDate.parse(fecha_creacion));
                    }

                    return c;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener la ultima cotizacion: " + e.getMessage());
        }

        return null;
    }

    public Cotizacion obtenerCotizacion(int id_cotizacion) {
    String sql = "SELECT * FROM Cotizacion WHERE id_cotizacion = ?";

    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, id_cotizacion);

        try (ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                Cotizacion c = new Cotizacion();

                c.setId_cotizacion(rs.getInt("id_cotizacion"));
                c.setId_trabajo(rs.getInt("id_trabajo"));
                c.setSubtotal(rs.getFloat("subtotal"));
                c.setIva(rs.getFloat("iva"));
                c.setTotal(rs.getFloat("total"));
                c.setEstado(rs.getString("estado"));
                c.setTipo(rs.getString("tipo"));

                String fecha_creacion = rs.getString("fecha_creacion");

                if (fecha_creacion != null && !fecha_creacion.trim().isEmpty()) {
                    c.setFecha_creacion(LocalDate.parse(fecha_creacion));
                }

                return c;
            }
        }

    } catch (SQLException e) {
        System.err.println("Error SQL: " + e.getMessage());
    }

    return null;
}
    
    public int insertarCotizacion(float subtotal, float iva, float total,String estado, String tipo, String fecha_creacion, Integer id_trabajo) {
    String sql = "INSERT INTO Cotizacion (subtotal, iva, total, estado, tipo, fecha_creacion, id_trabajo) VALUES (?, ?, ?, ?, ?, ?, ?)";

    try (Connection con = Conexion.conectar();
            PreparedStatement ps = con.prepareStatement(
                 sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            ps.setFloat(1, subtotal);
            ps.setFloat(2, iva);
            ps.setFloat(3, total);
            ps.setString(4, estado);
            ps.setString(5, tipo);
            ps.setString(6, fecha_creacion);

        if (id_trabajo != null) {
            ps.setInt(7, id_trabajo);
        } else {
            ps.setNull(7, java.sql.Types.INTEGER);
        }

        ps.executeUpdate();

        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }

    } catch (SQLException e) {
        System.err.println("Error al insertar la cotizacion: " + e.getMessage());
    }

    return -1;
}
    
    public boolean actualizarCotizacion(float subtotal,float iva,float total, String estado, String tipo, String fecha_creacion,Integer id_trabajo,int id_cotizacion) {
        String sql = "UPDATE Cotizacion SET subtotal = ?, iva = ?, total = ?, estado = ?, tipo = ?, fecha_creacion = ?, id_trabajo = ? WHERE id_cotizacion = ?;";
        
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setFloat(1, subtotal);
            ps.setFloat(2, iva);
            ps.setFloat(3, total);
            ps.setString(4, estado);
            ps.setString(5, tipo);
            ps.setString(6, fecha_creacion);
            if (id_trabajo != null) {
                ps.setInt(7, id_trabajo);
            } else {
                ps.setNull(7, java.sql.Types.INTEGER);
            }
            ps.setInt(8, id_cotizacion);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar la cotizacion: " + e.getMessage());
        }
        return false;
    }

    public boolean eliminarCotizacion(int id_cotizacion) {
    String sql = "DELETE FROM Cotizacion WHERE id_cotizacion = ?";

    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, id_cotizacion);
        return ps.executeUpdate() > 0;

    } catch (SQLException e) {
        System.err.println("Error al eliminar la cotizacion de la BD: " + e.getMessage());
        return false;
    }
}
    
    public boolean insertarCotizacionConDetalles(Cotizacion cotizacion, List<Detalles_cotizacion> listaDetalles) {
        String sqlCotizacion = "INSERT INTO Cotizacion (subtotal, iva, total, estado, tipo, fecha_creacion, id_trabajo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO Detalles_cotizacion (concepto, n_material, cantidad, precio_uni, precio, id_cotizacion) VALUES (?, ?, ?, ?, ?, ?)";

        for (int intento = 1; intento <= 3; intento++) {
            Connection con = null;
            try {
                con = Conexion.conectar();
                if (con == null) {
                    System.err.println("No se pudo conectar a la base de datos.");
                    return false;
                }
                // 1. Desactivamos el autoCommit para iniciar manualmente la transacción
                con.setAutoCommit(false);

                // 2. Insertar la Cotización principal
                int idCotizacionGenerado = -1;
                try (PreparedStatement psCotizacion = con.prepareStatement(sqlCotizacion, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                    psCotizacion.setFloat(1, cotizacion.getSubtotal());
                    psCotizacion.setFloat(2, cotizacion.getIva());
                    psCotizacion.setFloat(3, cotizacion.getTotal());
                    psCotizacion.setString(4, cotizacion.getEstado());
                    psCotizacion.setString(5, cotizacion.getTipo());
                    // Asegúrate de enviar la fecha adecuadamente según tu BD (String o LocalDate)
                    psCotizacion.setString(6, cotizacion.getFecha_creacion() != null ? cotizacion.getFecha_creacion().toString() : null);

                    if (cotizacion.getId_trabajo() != 0) {
                        psCotizacion.setInt(7, cotizacion.getId_trabajo());
                    } else {
                        psCotizacion.setNull(7, java.sql.Types.INTEGER);
                    }

                    psCotizacion.executeUpdate();

                    try (ResultSet rs = psCotizacion.getGeneratedKeys()) {
                        if (rs.next()) {
                            idCotizacionGenerado = rs.getInt(1);
                        }
                    }
                }

                if (idCotizacionGenerado == -1) {
                    throw new SQLException("No se pudo obtener el ID de la cotización generada.");
                }

                // 3. Insertar todos los detalles asociados
                try (PreparedStatement psDetalle = con.prepareStatement(sqlDetalle)) {
                    for (Detalles_cotizacion det : listaDetalles) {
                        psDetalle.setString(1, det.getConcepto());
                        psDetalle.setString(2, det.getN_material());
                        psDetalle.setFloat(3, det.getCantidad());
                        psDetalle.setFloat(4, det.getPrecio_uni());
                        psDetalle.setFloat(5, det.getPrecio());
                        psDetalle.setInt(6, idCotizacionGenerado);

                        // Añadimos la consulta al lote (batch) para mejor rendimiento
                        psDetalle.addBatch();
                    }
                    // Ejecutamos la inserción en lote
                    psDetalle.executeBatch();
                }

                // 4. Si todo salió bien, confirmamos todos los cambios en la BD
                con.commit();
                return true;

            } catch (SQLException e) {
                if (esBloqueoBD(e) && intento < 3) {
                    System.err.println("Base de datos ocupada, reintentando (intento " + intento + " de 3)...");
                    esperarReintento();
                    continue;
                }
                System.err.println("Error en la transacción, realizando rollback: " + e.getMessage());
                if (con != null) {
                    try {
                        // Deshacemos cualquier cambio en caso de error
                        con.rollback();
                    } catch (SQLException ex) {
                        System.err.println("Error al intentar realizar el rollback: " + ex.getMessage());
                    }
                }
                return false;
            } finally {
                cerrarSeguro(con);
            }
        }
        return false;
    }
    
    public boolean actualizarCotizacionConDetalles(float subtotal, float iva, float total, String estado,
            String tipo, String fecha_creacion, int id_trabajo, int id_cotizacion,
            List<Detalles_cotizacion> listaDetalles) {
        String sqlCotizacion = "UPDATE Cotizacion SET subtotal = ?, iva = ?, total = ?, estado = ?, tipo = ?, "
                + "fecha_creacion = ?, id_trabajo = ? WHERE id_cotizacion = ?";
        String sqlDetalleNuevo = "INSERT INTO Detalles_cotizacion (concepto, n_material, cantidad, precio_uni, precio, id_cotizacion) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        String sqlDetalleUpdate = "UPDATE Detalles_cotizacion SET concepto = ?, n_material = ?, cantidad = ?, "
                + "precio_uni = ?, precio = ? WHERE id_detalle = ? AND id_cotizacion = ?";

        for (int intento = 1; intento <= 3; intento++) {
            Connection con = null;
            try {
                con = Conexion.conectar();
                if (con == null) {
                    System.err.println("No se pudo conectar a la base de datos.");
                    return false;
                }
                con.setAutoCommit(false);

                try (PreparedStatement ps = con.prepareStatement(sqlCotizacion)) {
                    ps.setFloat(1, subtotal);
                    ps.setFloat(2, iva);
                    ps.setFloat(3, total);
                    ps.setString(4, estado);
                    ps.setString(5, tipo);
                    ps.setString(6, fecha_creacion);
                    if (id_trabajo != 0) {
                        ps.setInt(7, id_trabajo);
                    } else {
                        ps.setNull(7, java.sql.Types.INTEGER);
                    }
                    ps.setInt(8, id_cotizacion);
                    if (ps.executeUpdate() == 0) {
                        throw new SQLException("La cotización " + id_cotizacion + " no existe o no fue modificada.");
                    }
                }

                try (PreparedStatement psNuevo = con.prepareStatement(sqlDetalleNuevo);
                     PreparedStatement psUpdate = con.prepareStatement(sqlDetalleUpdate)) {
                    for (Detalles_cotizacion det : listaDetalles) {
                        if (det.getId_detalle() > 0) {
                            psUpdate.setString(1, det.getConcepto());
                            psUpdate.setString(2, det.getN_material());
                            psUpdate.setFloat(3, det.getCantidad());
                            psUpdate.setFloat(4, det.getPrecio_uni());
                            psUpdate.setFloat(5, det.getPrecio());
                            psUpdate.setInt(6, det.getId_detalle());
                            psUpdate.setInt(7, id_cotizacion);
                            psUpdate.addBatch();
                        } else {
                            psNuevo.setString(1, det.getConcepto());
                            psNuevo.setString(2, det.getN_material());
                            psNuevo.setFloat(3, det.getCantidad());
                            psNuevo.setFloat(4, det.getPrecio_uni());
                            psNuevo.setFloat(5, det.getPrecio());
                            psNuevo.setInt(6, id_cotizacion);
                            psNuevo.addBatch();
                        }
                    }
                    psUpdate.executeBatch();
                    psNuevo.executeBatch();
                }

                con.commit();
                return true;

            } catch (SQLException e) {
                if (esBloqueoBD(e) && intento < 3) {
                    System.err.println("Base de datos ocupada, reintentando (intento " + intento + " de 3)...");
                    esperarReintento();
                    continue;
                }
                System.err.println("Error en la transaccion de actualizacion, realizando rollback: " + e.getMessage());
                if (con != null) {
                    try {
                        con.rollback();
                    } catch (SQLException ex) {
                        System.err.println("Error al intentar realizar el rollback: " + ex.getMessage());
                    }
                }
                return false;
            } finally {
                cerrarSeguro(con);
            }
        }
        return false;
    }

    private static boolean esBloqueoBD(SQLException e) {
        String msg = e.getMessage();
        return msg != null && (msg.contains("locked") || e.getErrorCode() == 5);
    }

    private static void esperarReintento() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    private static void cerrarSeguro(Connection con) {
        if (con != null) {
            try {
                con.setAutoCommit(true);
                con.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexion: " + e.getMessage());
            }
        }
    }

    public String obtenerTotal(int id_trabajo) {
        String sql = "SELECT COALESCE(total, 0) AS total FROM Cotizacion WHERE id_trabajo = ? AND estado = 'Aprobada'";
        
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
             
            if (con == null) return "0";
            
            ps.setInt(1, id_trabajo);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("total");
                }
                return "0";
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el total de la cotizacion aprobada de la BD: " + e.getMessage());
        }
        return "0";
    }
    
}
