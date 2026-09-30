package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import sistema_en.Conexion;

/**
 * Convierte una cotizacion de prueba en un trabajo real: registra (o reutiliza) el
 * cliente, crea el trabajo y vincula la cotizacion a ese trabajo.
 *
 * Todo ocurre en una sola conexion y una sola transaccion, a diferencia del flujo
 * de alta de V_R_Trabajo, que reparte Cliente, Trabajo y Drive en conexiones
 * sueltas. Si algo falla, no queda un cliente huerfano ni un trabajo sin cotizacion.
 *
 * La sincronizacion con Google Drive no se hace aqui: ocurre despues del commit
 * desde la interfaz, porque Drive no participa de la transaccion de SQLite.
 */
public class ConversionCotizacionDAO {

    /**
     * @return el id_trabajo generado, o -1 si la conversion no se pudo completar
     */
    public int convertirCotizacionEnTrabajo(int id_cotizacion, String nombre, String telefono,
            String direccion, String descripcion, String fecha_cr, String hora_cr, String estado_cotizacion) {

        String sqlCotizacion = "SELECT * FROM Cotizacion WHERE id_cotizacion = ?";
        String sqlBuscarCliente = "SELECT id_cliente FROM Cliente WHERE nombre_c = ? AND telefono = ?";
        String sqlInsertarCliente = "INSERT INTO Cliente (nombre_c, telefono, fecha_registro) VALUES (?, ?, ?)";
        String sqlInsertarTrabajo = "INSERT INTO Trabajo (direccion, descripcion, fecha_cr, hora_cr, estado, id_cliente) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlVincular = "UPDATE Cotizacion SET id_trabajo = ?, tipo = 'Normal', estado = ?, archivada = 0, fecha_archivado = NULL "
                + "WHERE id_cotizacion = ?";

        for (int intento = 1; intento <= 3; intento++) {
            Connection con = null;
            try {
                con = Conexion.conectar();
                if (con == null) {
                    System.err.println("No se pudo conectar a la base de datos.");
                    return -1;
                }
                con.setAutoCommit(false);

                // 1. La cotizacion debe ser de prueba y todavia no tener trabajo.
                String tipo;
                boolean tieneTrabajo;
                try (PreparedStatement ps = con.prepareStatement(sqlCotizacion)) {
                    ps.setInt(1, id_cotizacion);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("La cotizacion " + id_cotizacion + " no existe.");
                        }
                        tipo = rs.getString("tipo");
                        int id_trabajo = rs.getInt("id_trabajo");
                        tieneTrabajo = !rs.wasNull() && id_trabajo != 0;
                    }
                }

                if (!"Prueba".equals(tipo) || tieneTrabajo) {
                    throw new SQLException("La cotizacion " + id_cotizacion + " ya fue convertida a un trabajo.");
                }

                // 2. Reutilizar el cliente si ya existe con esos datos, si no crearlo.
                int id_cliente = -1;
                try (PreparedStatement ps = con.prepareStatement(sqlBuscarCliente)) {
                    ps.setString(1, nombre);
                    ps.setString(2, telefono);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            id_cliente = rs.getInt("id_cliente");
                        }
                    }
                }

                if (id_cliente == -1) {
                    try (PreparedStatement ps = con.prepareStatement(sqlInsertarCliente, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                        ps.setString(1, nombre);
                        ps.setString(2, telefono);
                        ps.setString(3, fecha_cr);
                        ps.executeUpdate();

                        try (ResultSet rs = ps.getGeneratedKeys()) {
                            if (rs.next()) {
                                id_cliente = rs.getInt(1);
                            }
                        }
                    }
                    if (id_cliente == -1) {
                        throw new SQLException("No se pudo registrar el cliente de la cotizacion " + id_cotizacion + ".");
                    }
                } else {
                    System.out.println("Se reutilizo el cliente existente " + id_cliente + " para la cotizacion " + id_cotizacion + ".");
                }

                // 3. Crear el trabajo.
                int idTrabajoNuevo;
                try (PreparedStatement ps = con.prepareStatement(sqlInsertarTrabajo, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, direccion);
                    ps.setString(2, descripcion);
                    ps.setString(3, fecha_cr);
                    ps.setString(4, hora_cr);
                    ps.setString(5, "Pendiente");
                    ps.setInt(6, id_cliente);
                    ps.executeUpdate();

                    idTrabajoNuevo = -1;
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            idTrabajoNuevo = rs.getInt(1);
                        }
                    }
                }

                if (idTrabajoNuevo == -1) {
                    throw new SQLException("No se pudo crear el trabajo para la cotizacion " + id_cotizacion + ".");
                }

                // 4. Vincular la cotizacion al trabajo: deja de ser de prueba y se desarchiva.
                try (PreparedStatement ps = con.prepareStatement(sqlVincular)) {
                    ps.setInt(1, idTrabajoNuevo);
                    ps.setString(2, estado_cotizacion);
                    ps.setInt(3, id_cotizacion);
                    if (ps.executeUpdate() == 0) {
                        throw new SQLException("No se pudo vincular la cotizacion " + id_cotizacion + " con el trabajo " + idTrabajoNuevo + ".");
                    }
                }

                con.commit();
                System.out.println("Cotizacion " + id_cotizacion + " convertida al trabajo " + idTrabajoNuevo + " del cliente " + id_cliente + ".");
                return idTrabajoNuevo;

            } catch (SQLException e) {
                if (esBloqueoBD(e) && intento < 3) {
                    System.err.println("Base de datos ocupada, reintentando (intento " + intento + " de 3)...");
                    esperarReintento();
                    continue;
                }
                System.err.println("Error al convertir la cotizacion, realizando rollback: " + e.getMessage());
                if (con != null) {
                    try {
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

    /**
     * Informa si ya existe un cliente con ese nombre y telefono, para que la
     * interfaz pueda avisar antes de convertir.
     */
    public boolean clienteExiste(String nombre, String telefono) {
        String sql = "SELECT 1 FROM Cliente WHERE nombre_c = ? AND telefono = ?";

        Connection con = Conexion.conectar();
        if (con == null) {
            return false;
        }
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, telefono);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar si el cliente existe: " + e.getMessage());
            return false;
        } finally {
            cerrarSeguro(con);
        }
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
}
