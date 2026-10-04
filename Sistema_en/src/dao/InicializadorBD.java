package dao;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

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
        + "\"id_evento\" TEXT,"
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
        + "\"archivada\" INTEGER DEFAULT 0,"
        + "\"fecha_archivado\" TEXT,"
        + "\"nombre_cliente\" TEXT,"
        + "\"telefono_cliente\" TEXT,"
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

    /**
     * Columnas agregadas a "Cotizacion" despues de que la base ya existiera.
     * Todas son nullable o con valor por defecto, para que ALTER TABLE ADD COLUMN
     * sea valido en SQLite y no requiera rellenar datos previos.
     */
    private static final String[][] COLUMNAS_COTIZACION_NUEVAS = {
        {"archivada", "INTEGER DEFAULT 0"},
        {"fecha_archivado", "TEXT"},
        {"nombre_cliente", "TEXT"},
        {"telefono_cliente", "TEXT"}
    };

    /**
     * Columnas agregadas a "Trabajo" despues de que la base ya existiera.
     * Mismo criterio que COLUMNAS_COTIZACION_NUEVAS: nullable o con valor por
     * defecto, para que ALTER TABLE ADD COLUMN sea valido en SQLite. Los
     * trabajos anteriores quedan con id_evento nulo, que es exactamente el
     * estado de "este trabajo todavia no tiene evento en Google Calendar".
     */
    private static final String[][] COLUMNAS_TRABAJO_NUEVAS = {
        {"id_evento", "TEXT"}
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
        {"automaticamente_ce_excel", "true"},
        {"automaticamente_eventos", "false"},
        {"hora_defecto_calendar", "09:00"},
        {"recordatorio_informe", "false"}
    };

    /**
     * Agrega las columnas faltantes de "Cotizacion" a una base ya existente.
     * El DDL usa CREATE TABLE IF NOT EXISTS, que no agrega columnas a una tabla
     * que ya existe, por eso se inspecciona el esquema real con PRAGMA table_info.
     * Es idempotente: solo altera la tabla en la primera pasada.
     */
    private static void asegurarColumnasCotizacion(Connection con) {
        asegurarColumnas(con, "Cotizacion", COLUMNAS_COTIZACION_NUEVAS);
    }

    /**
     * Agrega las columnas faltantes de "Trabajo" a una base ya existente.
     * Mismo motivo y mismo criterio que Cotizacion.
     */
    private static void asegurarColumnasTrabajo(Connection con) {
        asegurarColumnas(con, "Trabajo", COLUMNAS_TRABAJO_NUEVAS);
    }

    /**
     * Aplica todas las migraciones de columnas faltantes.
     *
     * Hay un unico punto de entrada a proposito: las bases nuevas (crearBaseDatos)
     * y las ya existentes (asegurarEsquema) tienen que recibir exactamente las
     * mismas migraciones. Si cada una las listara por su cuenta, agregar una
     * columna nueva seria facil hacerlo solo en una de las dos, y una base recien
     * creada quedaria sin ella sin ningun error visible.
     */
    private static void asegurarMigraciones(Connection con) {
        asegurarColumnasCotizacion(con);
        asegurarColumnasTrabajo(con);
    }

    /**
     * Agrega a una tabla las columnas de {@code nuevas} que todavia no existan,
     * segun lo que diga PRAGMA table_info. El nombre de la tabla va entre
     * comillas dobles porque asi lo escribe el resto del DDL de este archivo.
     */
    private static void asegurarColumnas(Connection con, String tabla, String[][] nuevas) {
        Set<String> existentes = new HashSet<>();
        try (Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("PRAGMA table_info(\"" + tabla + "\")")) {
            while (rs.next()) {
                existentes.add(rs.getString("name"));
            }
        } catch (Exception e) {
            System.err.println("Error al inspeccionar las columnas de " + tabla + ": " + e.getMessage());
            return;
        }

        for (String[] columna : nuevas) {
            if (existentes.contains(columna[0])) {
                continue;
            }
            try (Statement st = con.createStatement()) {
                st.executeUpdate("ALTER TABLE \"" + tabla + "\" ADD COLUMN \"" + columna[0] + "\" " + columna[1]);
                System.out.println("Columna agregada a " + tabla + ": " + columna[0]);
            } catch (Exception e) {
                System.err.println("No se pudo agregar la columna " + columna[0] + " a " + tabla + ": " + e.getMessage());
            }
        }
    }

    public static boolean asegurarEsquema(String rutaCompleta) {
        if (rutaCompleta == null || rutaCompleta.isBlank()) {
            return false;
        }
        Connection con = DriverManagerSqliteConectar(rutaCompleta);
        if (con == null) {
            return false;
        }
        try {
            try (Statement st = con.createStatement()) {
                for (String ddl : DDL) {
                    st.executeUpdate(ddl);
                }
            }
            asegurarMigraciones(con);
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
            asegurarMigraciones(con);
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
            return java.sql.DriverManager.getConnection("jdbc:sqlite:" + rutaCompleta + "?busy_timeout=5000");
        } catch (Exception e) {
            System.err.println("Error al conectar para crear BD: " + e.getMessage());
            return null;
        }
    }
}
