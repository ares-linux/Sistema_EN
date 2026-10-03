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
    
    /**
     * Convierte el texto de una fecha de la BD a LocalDate. Acepta el formato ISO
     * que escribe la aplicacion y, por tolerancia con registros anteriores, tambien
     * "d 'de' MMMM 'de' yyyy" en espanol. Devuelve null si no se puede interpretar.
     */
    private static LocalDate leerFecha(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(texto.trim());
        } catch (Exception e) {
            try {
                DateTimeFormatter formato = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", new java.util.Locale("es", "ES"));
                return LocalDate.parse(texto.trim(), formato);
            } catch (Exception ex) {
                System.err.println("Error al interpretar la fecha: " + texto);
                return null;
            }
        }
    }

    /**
     * Construye un Cotizacion a partir de una fila de la tabla. id_trabajo es NULL
     * cuando la cotizacion es de prueba, por eso se normaliza a 0, que es el valor
     * que usa la interfaz.
     */
    private static Cotizacion mapearCotizacion(ResultSet rs) throws SQLException {
        Cotizacion c = new Cotizacion();
        c.setId_cotizacion(rs.getInt("id_cotizacion"));
        c.setSubtotal(rs.getFloat("subtotal"));
        c.setIva(rs.getFloat("iva"));
        c.setTotal(rs.getFloat("total"));
        c.setEstado(rs.getString("estado"));
        c.setTipo(rs.getString("tipo"));
        c.setFecha_creacion(leerFecha(rs.getString("fecha_creacion")));
        c.setArchivada(rs.getInt("archivada"));
        c.setFecha_archivado(leerFecha(rs.getString("fecha_archivado")));
        c.setNombre_cliente(rs.getString("nombre_cliente"));
        c.setTelefono_cliente(rs.getString("telefono_cliente"));
        int id_trabajo = rs.getInt("id_trabajo");
        c.setId_trabajo(rs.wasNull() ? 0 : id_trabajo);
        return c;
    }

    public List<Cotizacion> obtenerCotizacionesPrueba() {
        return obtenerCotizacionesPrueba(false);
    }

    /**
     * @param archivadas true devuelve las cotizaciones de prueba archivadas,
     *                   false devuelve las activas
     */
    public List<Cotizacion> obtenerCotizacionesPrueba(boolean archivadas) {
    List<Cotizacion> listaCotizaciones = new ArrayList<>();
    String sql = "SELECT * FROM Cotizacion WHERE tipo = ? AND archivada = ? ORDER BY id_cotizacion DESC";

    Connection con = Conexion.conectar();
    if (con == null) {
        return listaCotizaciones;
    }

    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, "Prueba");
        ps.setInt(2, archivadas ? 1 : 0);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                listaCotizaciones.add(mapearCotizacion(rs));
            }
        }
    } catch (SQLException e) {
        System.out.println("Error SQL: " + e.getMessage());
    } finally {
        cerrarSeguro(con);
    }

    System.out.println("Cotizaciones encontradas: " + listaCotizaciones.size());
    return listaCotizaciones;
}
    
    public List<Cotizacion> obtenerCotizaciones(int id_trabajo) {
    List<Cotizacion> listaCotizaciones = new ArrayList<>();
    // Sin trabajo solo tiene sentido mostrar cotizaciones de prueba activas: las
    // cotizaciones de un trabajo se consultan siempre con su id_trabajo real.
    // Nota: la rama id_trabajo == 0 (lista de prueba) hoy no la usa ninguna
    // ventana: la ventana de cotizaciones de trabajo siempre pasa el id real y
    // la de cotizaciones independientes consulta con sus propios metodos.
    String sql = (id_trabajo == 0) ? "SELECT * FROM Cotizacion WHERE tipo = 'Prueba' AND archivada = 0" : "SELECT * FROM Cotizacion WHERE id_trabajo = ? AND tipo = 'Normal'";

    Connection con = Conexion.conectar();
    if (con == null) {
        return listaCotizaciones;
    }

    try (PreparedStatement ps = con.prepareStatement(sql)) {
        if (id_trabajo != 0) {
            ps.setInt(1, id_trabajo);
        }
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                listaCotizaciones.add(mapearCotizacion(rs));
            }
        }
    } catch (SQLException e) {
        System.out.println("Error SQL: " + e.getMessage());
    } finally {
        cerrarSeguro(con);
    }
    // 4. Devolver la lista completa (estará vacía si no encontró ninguna)
    System.out.println("Cotizaciones encontradas: " + listaCotizaciones.size());
    return listaCotizaciones;
}

    /**
     * Busca cotizaciones de prueba por nombre del prospecto y/o rango de fechas.
     * Los parametros nulos se omiten, asi que cualquier combinacion es valida.
     */
    public List<Cotizacion> buscarCotizacionesPrueba(String nombre, LocalDate desde, LocalDate hasta, boolean archivadas) {
        List<Cotizacion> listaCotizaciones = new ArrayList<>();
        List<Object> parametros = new ArrayList<>();
        StringBuilder where = new StringBuilder(" WHERE tipo = ? AND archivada = ?");

        parametros.add("Prueba");
        parametros.add(archivadas ? 1 : 0);

        if (nombre != null && !nombre.trim().isEmpty()) {
            // Escapamos % y _ para que el usuario los busque como texto literal.
            where.append(" AND nombre_cliente LIKE ? ESCAPE '\\'");
            parametros.add("%" + nombre.trim().replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_") + "%");
        }
        if (desde != null) {
            where.append(" AND fecha_creacion >= ?");
            parametros.add(desde.toString());
        }
        if (hasta != null) {
            where.append(" AND fecha_creacion <= ?");
            parametros.add(hasta.toString());
        }

        String sql = "SELECT * FROM Cotizacion" + where + " ORDER BY id_cotizacion DESC";

        Connection con = Conexion.conectar();
        if (con == null) {
            return listaCotizaciones;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < parametros.size(); i++) {
                ps.setObject(i + 1, parametros.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    listaCotizaciones.add(mapearCotizacion(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar cotizaciones de prueba: " + e.getMessage());
        } finally {
            cerrarSeguro(con);
        }

        return listaCotizaciones;
    }
    
    public Cotizacion obtenerUltimaCotizacion(int id_trabajo) {
        String sql = "SELECT * FROM Cotizacion WHERE id_trabajo = ? ORDER BY id_cotizacion DESC LIMIT 1";

        Connection con = Conexion.conectar();
        if (con == null) {
            return null;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id_trabajo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearCotizacion(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener la ultima cotizacion: " + e.getMessage());
        } finally {
            cerrarSeguro(con);
        }

        return null;
    }

    public Cotizacion obtenerCotizacion(int id_cotizacion) {
    String sql = "SELECT * FROM Cotizacion WHERE id_cotizacion = ?";

    Connection con = Conexion.conectar();
    if (con == null) {
        return null;
    }

    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, id_cotizacion);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return mapearCotizacion(rs);
            }
        }

    } catch (SQLException e) {
        System.err.println("Error SQL: " + e.getMessage());
    } finally {
        cerrarSeguro(con);
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
    
    /**
     * Guarda la cotizacion y sus detalles. Se mantiene igual que siempre para no
     * romper los llamadores que solo necesitan saber si tuvo exito.
     */
    public boolean insertarCotizacionConDetalles(Cotizacion cotizacion, List<Detalles_cotizacion> listaDetalles) {
        return insertarCotizacionConDetallesInterno(cotizacion, listaDetalles) != -1;
    }

    /**
     * Igual que {@link #insertarCotizacionConDetalles} pero devuelve el
     * id_cotizacion generado, o -1 si fallo. Necesario para mostrar el numero de
     * una cotizacion de prueba recien creada y poder editarla, archivarla o
     * convertirla sin volver a buscarla en la tabla.
     */
    public int insertarCotizacionConDetallesDevolviendoId(Cotizacion cotizacion, List<Detalles_cotizacion> listaDetalles) {
        return insertarCotizacionConDetallesInterno(cotizacion, listaDetalles);
    }

    private int insertarCotizacionConDetallesInterno(Cotizacion cotizacion, List<Detalles_cotizacion> listaDetalles) {
        String sqlCotizacion = "INSERT INTO Cotizacion (subtotal, iva, total, estado, tipo, fecha_creacion, id_trabajo, nombre_cliente, telefono_cliente) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO Detalles_cotizacion (concepto, n_material, cantidad, precio_uni, precio, id_cotizacion) VALUES (?, ?, ?, ?, ?, ?)";

        for (int intento = 1; intento <= 3; intento++) {
            Connection con = null;
            try {
                con = Conexion.conectar();
                if (con == null) {
                    System.err.println("No se pudo conectar a la base de datos.");
                    return -1;
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

                    psCotizacion.setString(8, cotizacion.getNombre_cliente());
                    psCotizacion.setString(9, cotizacion.getTelefono_cliente());

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
                return idCotizacionGenerado;

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
                return -1;
            } finally {
                cerrarSeguro(con);
            }
        }
        return -1;
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
                    // executeUpdate devuelve 0 tambien cuando la fila existe pero los
                    // valores son identicos, por eso no basta para considerar un error.
                    if (ps.executeUpdate() == 0 && !existeCotizacion(con, id_cotizacion)) {
                        throw new SQLException("La cotización " + id_cotizacion + " no existe.");
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

    /**
     * Indica si la fila de la cabecera existe, para distinguir "no se modifico nada"
     * de "la cotizacion no existe".
     */
    private static boolean existeCotizacion(Connection con, int id_cotizacion) throws SQLException {
        String sql = "SELECT 1 FROM Cotizacion WHERE id_cotizacion = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id_cotizacion);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Archiva una cotizacion de prueba. El estado comercial no se toca: archivar es
     * unicamente una marca de que el cliente pidio consultarla mas adelante.
     * Solo aplica a cotizaciones de prueba que todavia no estan Convertidas a un
     * trabajo, por eso el guard en el WHERE.
     */
    public boolean archivarCotizacion(int id_cotizacion, LocalDate fecha_archivado) {
        String sql = "UPDATE Cotizacion SET archivada = 1, fecha_archivado = ? "
                + "WHERE id_cotizacion = ? AND tipo = 'Prueba' AND id_trabajo IS NULL";

        Connection con = Conexion.conectar();
        if (con == null) {
            return false;
        }
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, fecha_archivado != null ? fecha_archivado.toString() : null);
            ps.setInt(2, id_cotizacion);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al archivar la cotizacion: " + e.getMessage());
            return false;
        } finally {
            cerrarSeguro(con);
        }
    }

    /**
     * Devuelve una cotizacion de prueba al historial activo. No restaura el estado
     * comercial porque nunca se modifico al archivar.
     */
    public boolean desarchivarCotizacion(int id_cotizacion) {
        String sql = "UPDATE Cotizacion SET archivada = 0, fecha_archivado = NULL WHERE id_cotizacion = ?";

        Connection con = Conexion.conectar();
        if (con == null) {
            return false;
        }
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id_cotizacion);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al desarchivar la cotizacion: " + e.getMessage());
            return false;
        } finally {
            cerrarSeguro(con);
        }
    }

    /**
     * Actualiza los datos del prospecto. Va aparte de
     * actualizarCotizacionConDetalles porque ese metodo ya recibe nueve parametros
     * y estos dos solo aplican a cotizaciones de prueba.
     */
    public boolean actualizarDatosProspecto(int id_cotizacion, String nombre_cliente, String telefono_cliente) {
        String sql = "UPDATE Cotizacion SET nombre_cliente = ?, telefono_cliente = ? WHERE id_cotizacion = ?";

        Connection con = Conexion.conectar();
        if (con == null) {
            return false;
        }
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre_cliente);
            ps.setString(2, telefono_cliente);
            ps.setInt(3, id_cotizacion);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar los datos del prospecto: " + e.getMessage());
            return false;
        } finally {
            cerrarSeguro(con);
        }
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
