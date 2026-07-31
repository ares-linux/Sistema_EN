package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sistema_en.Conexion;

public class TrabajadorDAO {
        
    public boolean insertarTrabajador(String nombre, String sexo, String puesto,String rfc, String curp, String f_nac, String foto) {
        
        String sql = "INSERT INTO Trabajador (nombre, sexo, puesto, rfc, curp, f_nac, foto) " + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, sexo);
            ps.setString(3, puesto);
            ps.setString(4, rfc);
            ps.setString(5, curp);
            ps.setString(6, f_nac);
            ps.setString(7, foto);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0; 

        } catch (SQLException e) {
            System.err.println("Error al guardar trabajador en BD: " + e.getMessage());
            return false;
        }
    }
    
    public List<String[]> obtenerTrabajadores() {
    List<String[]> lista = new ArrayList<>();
    String sql = "SELECT id_trabajador, nombre, sexo, puesto, rfc, curp, f_nac, foto FROM Trabajador";
    
    Connection con = Conexion.conectar();

    if (con == null) {
        System.err.println("No se pudo realizar la consulta porque la conexión es null.");
        return lista; 
    }

    try (PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            String[] fila = new String[8];
            fila[0] = String.valueOf(rs.getInt("id_trabajador"));
            fila[1] = rs.getString("nombre");
            fila[2] = rs.getString("sexo");
            fila[3] = rs.getString("puesto");
            fila[4] = rs.getString("rfc");
            fila[5] = rs.getString("curp");
            fila[6] = rs.getString("f_nac");
            fila[7] = rs.getString("foto");
            
            lista.add(fila);
        }

    } catch (SQLException e) {
        System.err.println("Error al obtener los trabajadores de la BD: " + e.getMessage());
    } finally {

        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException ex) {
            System.err.println("Error al cerrar conexión: " + ex.getMessage());
        }
    }
    return lista;
}

    public List<String[]> buscarTrabajadores(String nombre, String puesto) {
             
    java.util.List<String[]> lista = new java.util.ArrayList<>();
    
    // Consulta dinámica: Si el puesto es "Todos", ignora ese filtro en el WHERE
    String sql = (puesto.equals("Todos")) 
                 ? "SELECT * FROM Trabajador WHERE nombre LIKE ?" 
                 : "SELECT * FROM Trabajador WHERE nombre LIKE ? AND puesto = ?";
    
    Connection con = Conexion.conectar();
    if (con == null) return lista;

    try (PreparedStatement ps = con.prepareStatement(sql)) {
        // El primer parámetro siempre es el nombre con comodines
        ps.setString(1, "%" + nombre + "%");
        
        // El segundo parámetro solo se añade si filtramos por un puesto específico
        if (!puesto.equals("Todos")) {
            ps.setString(2, puesto);
        }

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                
                String[] trabajador = new String[8]; 
                trabajador[0] = rs.getString("id_trabajador");
                trabajador[1] = rs.getString("nombre");
                trabajador[2] = rs.getString("sexo");
                trabajador[3] = rs.getString("puesto");
                trabajador[4] = rs.getString("rfc");
                trabajador[5] = rs.getString("curp");
                trabajador[6] = rs.getString("f_nac");
                trabajador[7] = rs.getString("foto");
                
                lista.add(trabajador);
            }
        }
    } catch (SQLException e) {
        System.err.println("Error en la BD: " + e.getMessage());
    }
    return lista;
}
    
public boolean eliminarTrabajador(int id_trabajador) {
    String sql = "DELETE FROM Trabajador WHERE id_trabajador = ?";

    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, id_trabajador);

        int filasAfectadas = ps.executeUpdate();
        return filasAfectadas > 0; 

    } catch (SQLException e) {
        System.err.println("Error al eliminar trabajador de la BD: " + e.getMessage());
        return false;
    }
}

public boolean actualizarTrabajador(int id_trabajador, String nombre, String sexo, String puesto, String rfc, String curp, String f_nac, String foto) {
    String sql = "UPDATE Trabajador SET nombre = ?, sexo = ?, puesto = ?, rfc = ?, curp = ?, f_nac = ?, foto = ? WHERE id_trabajador = ?";

    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, nombre);
        ps.setString(2, sexo);
        ps.setString(3, puesto);
        ps.setString(4, rfc);
        ps.setString(5, curp);
        ps.setString(6, f_nac);
        ps.setString(7, foto);
        ps.setInt(8, id_trabajador); 

        int filasAfectadas = ps.executeUpdate();
        return filasAfectadas > 0; 

    } catch (SQLException e) {
        System.err.println("Error al actualizar trabajador en BD: " + e.getMessage());
        return false;
    }
}

}