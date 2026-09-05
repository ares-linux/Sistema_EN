package dao;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class InicializadorBD {

    private static final String DDL_CONFIGURACIONES = "CREATE TABLE IF NOT EXISTS \"Configuraciones\" ("
            + "\"id_configuracion\" INTEGER,"
            + "\"clave\" TEXT,"
            + "\"valor\" TEXT,"
            + "PRIMARY KEY(\"id_configuracion\" AUTOINCREMENT)"
            + ");";

    private static final String DDL_USUARIOS = "CREATE TABLE IF NOT EXISTS \"Usuarios\" ("
            + "\"id_usuarios\" INTEGER,"
            + "\"usuario\" TEXT UNIQUE,"
            + "\"contraseña\" TEXT,"
            + "PRIMARY KEY(\"id_usuarios\" AUTOINCREMENT)"
            + ");";

    private static final String[] DDL = {
        DDL_CONFIGURACIONES,
        DDL_USUARIOS,

        "CREATE TABLE IF NOT EXISTS \"Tipos_herramienta\" ("
        + "\"id_tipo_herramienta\" INTEGER,"
        + "\"nombre\" TEXT,"
        + "PRIMARY KEY(\"id_tipo_herramienta\" AUTOINCREMENT)"
        + ");",

        "CREATE TABLE IF NOT EXISTS \"Herramienta\" ("
        + "\"id_herramienta\" INTEGER,"
        + "\"t_herramienta\" INTEGER,"
        + "\"n_serie\" TEXT,"
        + "\"foto\" TEXT,"
        + "PRIMARY KEY(\"id_herramienta\" AUTOINCREMENT),"
        + "CONSTRAINT \"tipo de herramienta\" FOREIGN KEY(\"t_herramienta\") REFERENCES \"Tipos_herramienta\"(\"id_tipo_herramienta\")"
        + ");",

        "CREATE TABLE IF NOT EXISTS \"Cliente\" ("
        + "\"id_cliente\" INTEGER,"
        + "\"nombre_c\" TEXT,"
        + "\"telefono\" TEXT,"
        + "\"u_modificacion\" TEXT,"
        + "\"fecha_registro\" TEXT,"
        + "PRIMARY KEY(\"id_cliente\" AUTOINCREMENT)"
        + ");",

        "CREATE TABLE IF NOT EXISTS \"Trabajo\" ("
        + "\"id_trabajo\" INTEGER,"
        + "\"direccion\" TEXT,"
        + "\"descripcion\" TEXT,"
        + "\"fecha_cr\" TEXT,"
        + "\"hora_cr\" TEXT,"
        + "\"estado\" TEXT,"
        + "\"id_cliente\" INTEGER,"
        + "\"fecha_inicio\" TEXT,"
        + "\"fecha_fin\" TEXT,"
        + "\"carpeta\" TEXT,"
        + "\"id_carpeta\" TEXT,"
        + "PRIMARY KEY(\"id_trabajo\" AUTOINCREMENT),"
        + "CONSTRAINT \"cliente\" FOREIGN KEY(\"id_cliente\") REFERENCES \"Cliente\"(\"id_cliente\")"
        + ");",

        "CREATE TABLE IF NOT EXISTS \"Trabajador\" ("
        + "\"id_trabajador\" INTEGER,"
        + "\"nombre\" TEXT,"
        + "\"sexo\" TEXT,"
        + "\"foto\" TEXT,"
        + "\"puesto\" TEXT,"
        + "\"f_nac\" TEXT,"
        + "\"curp\" TEXT,"
        + "\"rfc\" TEXT,"
        + "PRIMARY KEY(\"id_trabajador\" AUTOINCREMENT)"
        + ");",

        "CREATE TABLE IF NOT EXISTS \"Cotizacion\" ("
        + "\"id_cotizacion\" INTEGER,"
        + "\"subtotal\" REAL,"
        + "\"iva\" REAL,"
        + "\"total\" REAL,"
        + "\"estado\" TEXT,"
        + "\"id_trabajo\" INTEGER,"
        + "\"tipo\" TEXT,"
        + "\"fecha_creacion\" TEXT,"
        + "PRIMARY KEY(\"id_cotizacion\" AUTOINCREMENT),"
        + "CONSTRAINT \"trabajo\" FOREIGN KEY(\"id_trabajo\") REFERENCES \"Trabajo\"(\"id_trabajo\")"
        + ");",

        "CREATE TABLE IF NOT EXISTS \"Detalles_cotizacion\" ("
        + "\"id_detalle\" INTEGER,"
        + "\"id_cotizacion\" INTEGER,"
        + "\"concepto\" TEXT,"
        + "\"n_material\" TEXT,"
        + "\"cantidad\" REAL,"
        + "\"precio_uni\" REAL,"
        + "\"precio\" REAL,"
        + "PRIMARY KEY(\"id_detalle\" AUTOINCREMENT),"
        + "CONSTRAINT \"id_cotizacion\" FOREIGN KEY(\"id_cotizacion\") REFERENCES \"Cotizacion\"(\"id_cotizacion\") ON DELETE CASCADE"
        + ");",

        "CREATE TABLE IF NOT EXISTS \"Pago\" ("
        + "\"id_pago\" INTEGER,"
        + "\"id_trabajo\" INTEGER,"
        + "\"monto\" REAL,"
        + "\"concepto\" TEXT,"
        + "\"fecha_pago\" TEXT,"
        + "PRIMARY KEY(\"id_pago\" AUTOINCREMENT),"
        + "CONSTRAINT \"trabajo\" FOREIGN KEY(\"id_trabajo\") REFERENCES \"Trabajo\"(\"id_trabajo\")"
        + ");",

        "CREATE TABLE IF NOT EXISTS \"Bitacora\" ("
        + "\"id_bitacora\" INTEGER,"
        + "\"fecha\" TEXT,"
        + "\"hora_llegada\" TEXT,"
        + "\"hora_salida\" TEXT,"
        + "\"notas\" TEXT,"
        + "\"id_trabajador\" INTEGER,"
        + "\"id_trabajo\" INTEGER,"
        + "PRIMARY KEY(\"id_bitacora\" AUTOINCREMENT),"
        + "CONSTRAINT \"trabajador\" FOREIGN KEY(\"id_trabajador\") REFERENCES \"Trabajador\"(\"id_trabajador\"),"
        + "CONSTRAINT \"trabajo\" FOREIGN KEY(\"id_trabajo\") REFERENCES \"Trabajo\"(\"id_trabajo\")"
        + ");",

        "CREATE TABLE IF NOT EXISTS \"Evidencia\" ("
        + "\"id_evidencia\" INTEGER,"
        + "\"id_bitacora\" INTEGER,"
        + "\"id_google_drive\" TEXT,"
        + "\"nombre_archivo\" TEXT,"
        + "\"ruta_local\" TEXT,"
        + "PRIMARY KEY(\"id_evidencia\"),"
        + "CONSTRAINT \"bitacora\" FOREIGN KEY(\"id_bitacora\") REFERENCES \"Bitacora\"(\"id_bitacora\")"
        + ");"
    };

    private static final String[][] CONFIGURACIONES_DEFAULT = {
        {"tema", "Claro"},
        {"sonido_error", "false"},
        {"frecuencia", "diaria"},
        {"preguntar", "true"},
        {"manual", "false"},
        {"cotizaciones", "true"},
        {"ultima_copia", ""},
        {"automaticamente_ce", "true"},
        {"automaticamente_ce_excel", "true"}
    };

    public static boolean asegurarEsquema(String rutaCompleta) {
        if (rutaCompleta == null || rutaCompleta.isBlank()) {
            return false;
        }
        Connection con = DriverManagerSqliteConectar(rutaCompleta);
        if (con == null) {
            return false;
        }
        try (Statement st = con.createStatement()) {
            for (String ddl : DDL) {
                st.executeUpdate(ddl);
            }
            return true;
        } catch (Exception e) {
            System.err.println("Error al asegurar el esquema de la base de datos: " + e.getMessage());
            return false;
        } finally {
            try { con.close(); } catch (Exception e) { }
        }
    }

    public static boolean asegurarConfiguraciones(String rutaCompleta) {
        if (rutaCompleta == null || rutaCompleta.isBlank()) {
            return false;
        }
        Connection con = DriverManagerSqliteConectar(rutaCompleta);
        if (con == null) {
            return false;
        }
        try {
            try (Statement st = con.createStatement()) {
                st.executeUpdate(DDL_CONFIGURACIONES);
                st.executeUpdate("DELETE FROM Configuraciones WHERE id_configuracion NOT IN "
                        + "(SELECT MIN(id_configuracion) FROM Configuraciones GROUP BY clave)");
            }

            String directorioBase = Paths.get(rutaCompleta).getParent().toString();
            String carpetaDocumentos = sistema_en.Conexion.rutaCarpetaDocumentosPorDefecto();

            String sqlConfig = "INSERT INTO Configuraciones (clave, valor) "
                    + "SELECT ?, ? WHERE NOT EXISTS (SELECT 1 FROM Configuraciones WHERE clave = ?)";
            try (PreparedStatement ps = con.prepareStatement(sqlConfig)) {
                for (String[] cfg : CONFIGURACIONES_DEFAULT) {
                    ps.setString(1, cfg[0]);
                    ps.setString(2, cfg[1]);
                    ps.setString(3, cfg[0]);
                    ps.addBatch();
                }
                ps.setString(1, "carpeta_ce");
                ps.setString(2, carpetaDocumentos);
                ps.setString(3, "carpeta_ce");
                ps.addBatch();
                ps.setString(1, "base_c");
                ps.setString(2, directorioBase);
                ps.setString(3, "base_c");
                ps.addBatch();
                ps.executeBatch();
            }
            return true;
        } catch (Exception e) {
            System.err.println("Error al asegurar las configuraciones: " + e.getMessage());
            return false;
        } finally {
            if (con != null) {
                try { con.close(); } catch (Exception e) { }
            }
        }
    }

    public static boolean crearBaseDatos(String rutaCompleta) {
        if (rutaCompleta == null || rutaCompleta.isBlank()) {
            return false;
        }
        Connection con = null;
        try {
            con = DriverManagerSqliteConectar(rutaCompleta);
            if (con == null) {
                return false;
            }
            try (Statement st = con.createStatement()) {
                for (String ddl : DDL) {
                    st.executeUpdate(ddl);
                }
            }
            return asegurarConfiguraciones(rutaCompleta);
        } catch (Exception e) {
            System.err.println("Error al crear la base de datos: " + e.getMessage());
            return false;
        } finally {
            if (con != null) {
                try { con.close(); } catch (Exception e) { }
            }
        }
    }

    private static Connection DriverManagerSqliteConectar(String rutaCompleta) {
        try {
            Class.forName("org.sqlite.JDBC");
            return java.sql.DriverManager.getConnection("jdbc:sqlite:" + rutaCompleta);
        } catch (Exception e) {
            System.err.println("Error al conectar para crear BD: " + e.getMessage());
            return null;
        }
    }
}
