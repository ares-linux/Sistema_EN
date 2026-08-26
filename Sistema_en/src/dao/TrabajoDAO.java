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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import sistema_en.Conexion;
import sistema_en.Objetos.Trabajo;

/**
 *
 * @author aresb
 */
public class TrabajoDAO {
    
    public Trabajo obtenerTrabajo(int id_trabajo) {
    String sql = "SELECT * FROM Trabajo WHERE id_trabajo = ?";

    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, id_trabajo);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                Trabajo t = new Trabajo();

                t.setId_trabajo(rs.getInt("id_trabajo")); 
                t.setId_cliente(rs.getInt("id_cliente")); 
                t.setDireccion(rs.getString("direccion"));
                t.setDescripcion(rs.getString("descripcion"));

                String fecha_cr = rs.getString("fecha_cr");
                String hora_cr = rs.getString("hora_cr");
                String fecha_inicio = rs.getString("fecha_inicio");
                String fecha_fin = rs.getString("fecha_fin");

                if (fecha_cr != null && !fecha_cr.trim().isEmpty()) {
                    t.setFecha_cr(LocalDate.parse(fecha_cr));
                }
                if (hora_cr != null && !hora_cr.trim().isEmpty()) {
                    t.setHora_cr(LocalTime.parse(hora_cr));
                }
                if (fecha_inicio != null && !fecha_inicio.trim().isEmpty()) {
                    t.setFecha_inicio(LocalDate.parse(fecha_inicio));
                }
                if (fecha_fin != null && !fecha_fin.trim().isEmpty()) {
                    t.setFecha_fin(LocalDate.parse(fecha_fin));
                }

                t.setEstado(rs.getString("estado"));

                return t;
            }
        }
    } catch (SQLException e) {
        System.out.println("Error SQL: " + e.getMessage());
    }

    return null;
}
    
    public int insertarTrabajo(String direccion, String descripcion, String fecha_cr, String hora_cr, String estado, int idCliente) {
    String sql = "INSERT INTO Trabajo (direccion, descripcion, fecha_cr, hora_cr, estado, id_cliente) VALUES (?, ?, ?, ?, ?, ?)";
    
    // Cambiamos el tipo de retorno a int para que devuelva el ID generado
    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
        
        ps.setString(1, direccion);
        ps.setString(2, descripcion);
        ps.setString(3, fecha_cr);
        ps.setString(4, hora_cr);
        ps.setString(5, estado);
        ps.setInt(6, idCliente); 
        
        int filasAfectadas = ps.executeUpdate();
        
        if (filasAfectadas > 0) {
            // Obtenemos la llave (ID) generada por la base de datos
            try (java.sql.ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // ¡Aquí está tu ID exacto de la BD!
                }
            }
        }
    } catch (SQLException e) {
        System.err.println("Error al insertar trabajo: " + e.getMessage());
    }
    return -1; // Retorna -1 si hubo algún error
}
    
    public List<String[]> obtenerTrabajosCliente(int id_cliente) {

    List<String[]> lista = new ArrayList<>();

    String sql = "SELECT t.id_trabajo,t.id_cliente, c.nombre_c, c.telefono, t.direccion, t.descripcion, t.fecha_cr, t.hora_cr, t.estado, t.fecha_inicio, t.fecha_fin " +
                     "FROM Trabajo t " +
                     "INNER JOIN Cliente c ON t.id_cliente = c.id_cliente " +
                     "WHERE t.id_cliente = ?" ;

    Connection con = Conexion.conectar();

    if (con == null) return lista;

    try (PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, id_cliente);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            String[] fila = new String[11];
            fila[0] = String.valueOf(rs.getInt("id_trabajo"));
            fila[1] = String.valueOf(rs.getInt("id_cliente"));
            fila[2] = rs.getString("nombre_c"); 
            fila[3] = rs.getString("telefono");
            fila[4] = rs.getString("direccion");
            fila[5] = rs.getString("descripcion");
            fila[6] = rs.getString("fecha_cr");
            fila[7] = rs.getString("hora_cr");
            fila[8] = rs.getString("estado");
            fila[9] = rs.getString("fecha_inicio");
            fila[10] = rs.getString("fecha_fin");

            lista.add(fila);
        }

    } catch (SQLException e) {
        System.err.println(e.getMessage());
    }

    return lista;
}
    
    public List<String[]> obtenerTrabajosConClientesCompletado() {
    List<String[]> lista = new ArrayList<>();
    String sql = "SELECT t.id_trabajo, t.id_cliente, c.nombre_c, c.telefono, t.direccion, " +
                "t.descripcion, t.fecha_cr, t.hora_cr, t.estado, t.fecha_inicio, t.fecha_fin " +
                "FROM Trabajo t " +
                "INNER JOIN Cliente c ON t.id_cliente = c.id_cliente " +
                "WHERE t.estado LIKE 'Completado' ";

    Connection con = Conexion.conectar();
    
    if (con == null) {
        System.err.println("No se pudo realizar la consulta porque la conexión es null.");
        return lista;
    }

    try (PreparedStatement ps = con.prepareStatement(sql);
            
            
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            
            String[] fila = new String[11];
            fila[0] = String.valueOf(rs.getInt("id_trabajo"));
            fila[1] = rs.getString("id_cliente");
            fila[2] = rs.getString("nombre_c");
            fila[3] = rs.getString("telefono");
            fila[4] = rs.getString("direccion");
            fila[5] = rs.getString("descripcion");
            fila[6] = rs.getString("fecha_cr");
            fila[7] = rs.getString("hora_cr");
            fila[8] = rs.getString("estado");
            fila[9] = rs.getString("fecha_inicio");
            fila[10] = rs.getString("fecha_fin");
            
            lista.add(fila);
        }


    } catch (SQLException e) {
        System.err.println("Error al obtener los trabajos con clientes: " + e.getMessage());
    } finally {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException ex) {
            System.err.println("Error al cerrar la conexión: " + ex.getMessage());
        }
    }
    return lista;
}
    
    public List<String[]> obtenerTrabajosConClientesProgreso() {
    List<String[]> lista = new ArrayList<>();
    String sql = "SELECT t.id_trabajo, t.id_cliente, c.nombre_c, c.telefono, t.direccion, " +
                "t.descripcion, t.fecha_cr, t.hora_cr, t.estado, t.fecha_inicio, t.fecha_fin " +
                "FROM Trabajo t " +
                "INNER JOIN Cliente c ON t.id_cliente = c.id_cliente " +
                "WHERE t.estado LIKE 'En progreso' ";

    Connection con = Conexion.conectar();
    
    if (con == null) {
        System.err.println("No se pudo realizar la consulta porque la conexión es null.");
        return lista;
    }

    try (PreparedStatement ps = con.prepareStatement(sql);
            
            
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            
            String[] fila = new String[11];
            fila[0] = String.valueOf(rs.getInt("id_trabajo"));
            fila[1] = rs.getString("id_cliente");
            fila[2] = rs.getString("nombre_c");
            fila[3] = rs.getString("telefono");
            fila[4] = rs.getString("direccion");
            fila[5] = rs.getString("descripcion");
            fila[6] = rs.getString("fecha_cr");
            fila[7] = rs.getString("hora_cr");
            fila[8] = rs.getString("estado");
            fila[9] = rs.getString("fecha_inicio");
            fila[10] = rs.getString("fecha_fin");
            
            lista.add(fila);
        }


    } catch (SQLException e) {
        System.err.println("Error al obtener los trabajos con clientes: " + e.getMessage());
    } finally {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException ex) {
            System.err.println("Error al cerrar la conexión: " + ex.getMessage());
        }
    }
    return lista;
}
    
    public List<String[]> obtenerTrabajosConClientesPendiente() {
    List<String[]> lista = new ArrayList<>();
    String sql = "SELECT t.id_trabajo, t.id_cliente, c.nombre_c, c.telefono, t.direccion, " +
                "t.descripcion, t.fecha_cr, t.hora_cr, t.estado, t.fecha_inicio, t.fecha_fin " +
                "FROM Trabajo t " +
                "INNER JOIN Cliente c ON t.id_cliente = c.id_cliente " +
                "WHERE t.estado LIKE 'Pendiente' ";

    Connection con = Conexion.conectar();
    
    if (con == null) {
        System.err.println("No se pudo realizar la consulta porque la conexión es null.");
        return lista;
    }

    try (PreparedStatement ps = con.prepareStatement(sql);
            
            
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            
            String[] fila = new String[11];
            fila[0] = String.valueOf(rs.getInt("id_trabajo"));
            fila[1] = rs.getString("id_cliente");
            fila[2] = rs.getString("nombre_c");
            fila[3] = rs.getString("telefono");
            fila[4] = rs.getString("direccion");
            fila[5] = rs.getString("descripcion");
            fila[6] = rs.getString("fecha_cr");
            fila[7] = rs.getString("hora_cr");
            fila[8] = rs.getString("estado");
            fila[9] = rs.getString("fecha_inicio");
            fila[10] = rs.getString("fecha_fin");
            
            lista.add(fila);
        }


    } catch (SQLException e) {
        System.err.println("Error al obtener los trabajos con clientes: " + e.getMessage());
    } finally {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException ex) {
            System.err.println("Error al cerrar la conexión: " + ex.getMessage());
        }
    }
    return lista;
}
    
    public List<String[]> obtenerTrabajosConClientes() {
    List<String[]> lista = new ArrayList<>();
    String sql = "SELECT t.id_trabajo, t.id_cliente, c.nombre_c, c.telefono, t.direccion, t.descripcion, t.fecha_cr, t.hora_cr, t.estado, t.fecha_inicio, t.fecha_fin " +
                 "FROM Trabajo t " +
                 "INNER JOIN Cliente c ON t.id_cliente = c.id_cliente";

    Connection con = Conexion.conectar();
    
    if (con == null) {
        System.err.println("No se pudo realizar la consulta porque la conexión es null.");
        return lista;
    }

    try (PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            String[] fila = new String[11];
            fila[0] = String.valueOf(rs.getInt("id_trabajo"));
            fila[1] = rs.getString("id_cliente");
            fila[2] = rs.getString("nombre_c");
            fila[3] = rs.getString("telefono");
            fila[4] = rs.getString("direccion");
            fila[5] = rs.getString("descripcion");
            fila[6] = rs.getString("fecha_cr");
            fila[7] = rs.getString("hora_cr");
            fila[8] = rs.getString("estado");
            fila[9] = rs.getString("fecha_inicio");
            fila[10] = rs.getString("fecha_fin");
            
            lista.add(fila);
        }

    } catch (SQLException e) {
        System.err.println("Error al obtener los trabajos con clientes: " + e.getMessage());
    } finally {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException ex) {
            System.err.println("Error al cerrar la conexión: " + ex.getMessage());
        }
    }
    return lista;
}

public boolean eliminarTrabajo(int idTrabajo) {
    String sql = "DELETE FROM Trabajo WHERE id_trabajo = ?";

    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, idTrabajo);
        return ps.executeUpdate() > 0;

    } catch (SQLException e) {
        System.err.println("Error al eliminar trabajo de la BD: " + e.getMessage());
        return false;
    }
}

public boolean actualizarTrabajoDetalles(int id_trabajo, String estado, String fecha_inicio, String fecha_fin) {
    String sql = "UPDATE Trabajo SET estado = ?, fecha_inicio = ?, fecha_fin = ? WHERE id_trabajo = ?;";
    try {
        Connection con = Conexion.conectar();
        if (con != null) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, estado);
            ps.setString(2, fecha_inicio);
            ps.setString(3, fecha_fin);
            ps.setInt(4, id_trabajo);
            
            return ps.executeUpdate() > 0;
        }
    } catch (SQLException e) {
        System.err.println("Error al actualizar trabajo: " + e.getMessage());
    }
    return false;
}

public boolean actualizarTrabajoCarpeta(int id_trabajo, String carpeta) {
    String sql = "UPDATE Trabajo SET carpeta = ? WHERE id_trabajo = ?;";
    
    // Usamos try-with-resources para cerrar automáticamente la conexión y el statement
    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con != null ? con.prepareStatement(sql) : null) {
        
        if (ps != null) {
            ps.setString(1, carpeta);
            ps.setInt(2, id_trabajo); // Corregido el índice de 4 a 2
            
            return ps.executeUpdate() > 0;
        }
    } catch (SQLException e) {
        System.err.println("Error al actualizar trabajo: " + e.getMessage());
    }
    return false;
}

public boolean actualizarTrabajo(int id_trabajo, String direccion ,String descripcion) {
    String sql = "UPDATE Trabajo SET direccion = ?, descripcion = ? WHERE id_trabajo = ?;";
    try {
        Connection con = Conexion.conectar();
        if (con != null) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, direccion);
            ps.setString(2, descripcion);
            ps.setInt(3, id_trabajo);
            
            return ps.executeUpdate() > 0;
        }
    } catch (SQLException e) {
        System.err.println("Error al actualizar trabajo: " + e.getMessage());
    }
    return false;
}

public String obtener_fecha_ultimo_trabajo_estado(String estado) {
    String sql = "SELECT fecha_cr FROM Trabajo WHERE estado = ? ORDER BY id_trabajo DESC LIMIT 1";
    
    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {
        
        ps.setString(1, estado);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getString("fecha_cr"); // O "fecha_fin" según la fecha que requieras
            }
        }
    } catch (SQLException e) {
        System.err.println("Error al obtener fecha del último trabajo: " + e.getMessage());
    }
    
    return null; // Retorna null si no hay registros con ese estado
}

public List<Trabajo> obtenerTrabajosPendientes() {
    List<Trabajo> trabajos = new ArrayList<>();
    
    // Validamos la conexión primero de forma segura
    Connection con = Conexion.conectar();
    if (con == null) {
        System.err.println("No se pudo realizar la consulta porque la conexión es null.");
        return trabajos;
    }

    String sql = "SELECT t.id_trabajo, t.id_cliente, t.fecha_cr " +
                 "FROM Trabajo t " +
                 "INNER JOIN Cliente c ON t.id_cliente = c.id_cliente " +
                 "WHERE carpeta = 'Pendiente'"; 

    // Usamos try-with-resources para el PreparedStatement y el ResultSet
    try (PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery();
         con) { // Cerramos la conexión automáticamente aquí también

        while (rs.next()) {
            Trabajo trabajo = new Trabajo();
            trabajo.setId_trabajo(rs.getInt("id_trabajo"));
            trabajo.setFecha_cr(LocalDate.parse(rs.getString("fecha_cr")));
            trabajo.setId_cliente(rs.getInt("id_cliente"));
            
            trabajos.add(trabajo);
        }

    } catch (SQLException e) {
        System.err.println("Error al obtener los trabajos pendientes: " + e.getMessage());
    }
    
    return trabajos;
}


public boolean actualizarTrabajoIdCarpeta(int id_trabajo, String id_carpeta) {
    String sql = "UPDATE Trabajo SET id_carpeta = ? WHERE id_trabajo = ?;";
    try {
        Connection con = Conexion.conectar();
        if (con != null) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, id_carpeta);
            ps.setInt(2, id_trabajo);
            
            return ps.executeUpdate() > 0;
        }
    } catch (SQLException e) {
        System.err.println("Error al actualizar trabajo: " + e.getMessage());
    }
    return false;
}

public Trabajo obtenerTrabajoIdCarpeta(int id_trabajo) {
    String sql = "SELECT id_trabajo, id_carpeta FROM Trabajo WHERE id_trabajo = ?";

    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, id_trabajo);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                Trabajo t = new Trabajo();

                t.setId_trabajo(rs.getInt("id_trabajo")); 
                t.setId_carpeta(rs.getString("id_carpeta"));

                return t;
            }
        }
    } catch (SQLException e) {
        System.out.println("Error SQL: " + e.getMessage());
    }

    return null;
}

}
