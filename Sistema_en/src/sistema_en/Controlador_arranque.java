package sistema_en;

import dao.InicializadorBD;
import dao.UsuariosDAO;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import sistema_en.Ventanas.dialogos.D_registro_usuario;

public class Controlador_arranque {

    public static boolean iniciar() {
        String ruta = Conexion.rutaArchivoBD();
        if (new File(ruta).exists()) {
            InicializadorBD.asegurarTablaUsuarios(ruta);
            if (!new UsuariosDAO().existeAdministrador()) {
                return registrarUsuarioObligatorio();
            }
            return true;
        }

        final boolean[] resultado = new boolean[1];
        try {
            SwingUtilities.invokeAndWait(() -> {
                resultado[0] = recuperarYResolver();
            });
        } catch (Exception e) {
            System.err.println("Error al mostrar el asistente de base de datos: " + e.getMessage());
            resultado[0] = false;
        }
        return resultado[0];
    }

    private static boolean recuperarYResolver() {
        while (true) {
            if (Conexion.existeBaseDatos()) {
                return true;
            }

            boolean conectado = GoogleServicesProvider.estaConectado();

            if (conectado) {
                if (intentarRestaurarDesdeDrive()) {
                    return true;
                }
            }

            List<String> opciones = new ArrayList<>();
            if (!conectado) {
                opciones.add("Vincular cuenta de Google Drive");
            } else {
                opciones.add("Cambiar de cuenta de Google y reintentar");
            }
            opciones.add("Buscar la base de datos manualmente");
            opciones.add("Usar la ubicación por defecto y crear BD");
            opciones.add("Crear una base de datos nueva");
            opciones.add("Salir del programa");

            String mensaje;
            if (conectado) {
                mensaje = "No se encontro la base de datos en la ubicacion esperada y no se pudo "
                        + "restaurar desde Google Drive.\n\nElija una opcion para continuar:";
            } else {
                mensaje = "No se encontro la base de datos en la ubicacion esperada.\n\n"
                        + "Puede vincular una cuenta de Google Drive para intentar restaurar un respaldo,\n"
                        + "buscar la base de datos manualmente o crear una nueva.\n\nElija una opcion:";
            }

            Object eleccion = JOptionPane.showInputDialog(
                    null,
                    mensaje,
                    "Base de datos no encontrada",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones.toArray(),
                    opciones.get(0));

            if (eleccion == null || "Salir del programa".equals(eleccion)) {
                return false;
            }

            if ("Vincular cuenta de Google Drive".equals(eleccion)
                    || "Cambiar de cuenta de Google y reintentar".equals(eleccion)) {
                vincularCuentaGoogle();
                continue;
            }
            if ("Buscar la base de datos manualmente".equals(eleccion)) {
                if (buscarManual()) {
                    return true;
                }
                continue;
            }
            if ("Usar la ubicación por defecto y crear BD".equals(eleccion)) {
                if (crearEnUbicacionPorDefecto()) {
                    return true;
                }
                continue;
            }
            if ("Crear una base de datos nueva".equals(eleccion)) {
                if (crearBaseDatosNueva()) {
                    return true;
                }
                continue;
            }
        }
    }

    private static boolean intentarRestaurarDesdeDrive() {
        String ruta = Conexion.rutaArchivoBD();
        JOptionPane.showMessageDialog(null,
                "Intentando restaurar la base de datos desde Google Drive...",
                "Restaurar base de datos",
                JOptionPane.INFORMATION_MESSAGE);
        boolean ok = GoogleServicesProvider.descargarRespaldoBaseDatos(ruta);
        if (ok && Conexion.existeBaseDatos()) {
            JOptionPane.showMessageDialog(null,
                    "La base de datos fue restaurada desde Google Drive.",
                    "Restauración exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        return false;
    }

    private static void vincularCuentaGoogle() {
        JOptionPane.showMessageDialog(null,
                "Se abrira el navegador para vincular otra cuenta de Google Drive.\n"
                + "Complete la autorizacion en el navegador y vuelva aqui.",
                "Vincular cuenta",
                JOptionPane.INFORMATION_MESSAGE);
        boolean ok = GoogleServicesProvider.vincularNuevaCuenta();
        if (ok) {
            JOptionPane.showMessageDialog(null, "Cuenta de Google vinculada correctamente.",
                    "Cuenta vinculada", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo vincular la cuenta de Google. Puede reintentar.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private static boolean buscarManual() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleccione la base de datos (sistema_EN)");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        int resultado = chooser.showOpenDialog(null);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = chooser.getSelectedFile();
            if (archivo.exists() && archivo.isFile() && archivo.length() > 0) {
                Conexion.guardarRutaBD(archivo.getAbsolutePath());
                return true;
            } else {
                JOptionPane.showMessageDialog(null,
                        "El archivo seleccionado no es una base de datos valida.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return false;
    }

    private static boolean crearBaseDatosNueva() {
        int confirmar = JOptionPane.showConfirmDialog(null,
                "Se creara una base de datos NUEVA y VACIA en la ubicacion prevista.\n\n"
                + "IMPORTANTE: Esta operacion no recupera datos anteriores.\n"
                + "¿Desea continuar?",
                "Crear base de datos nueva",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (confirmar != JOptionPane.YES_OPTION) {
            return false;
        }

        String ruta = Conexion.rutaArchivoBD();
        File archivoBD = new File(ruta);
        File directorio = archivoBD.getParentFile();
        if (directorio != null && !directorio.exists()) {
            directorio.mkdirs();
        }

        if (!InicializadorBD.crearBaseDatos(ruta)) {
            JOptionPane.showMessageDialog(null, "No se pudo crear la base de datos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        Conexion.guardarRutaBD(ruta);

        JOptionPane.showMessageDialog(null,
                "Base de datos nueva creada correctamente en:\n" + ruta,
                "Base de datos creada",
                JOptionPane.INFORMATION_MESSAGE);

        if (GoogleServicesProvider.estaConectado()) {
            int subir = JOptionPane.showConfirmDialog(null,
                    "¿Desea subir esta base de datos nueva a Google Drive como respaldo?\n"
                    + "Esto sobreescribira el respaldo existente en Drive.",
                    "Subir respaldo a Drive",
                    JOptionPane.YES_NO_OPTION);
            if (subir == JOptionPane.YES_OPTION) {
                String idCarpeta = GoogleServicesProvider.obtenerIdCarpetaBaseDatos();
                GoogleServicesProvider.subirArchivo(ruta, idCarpeta);
            }
        }

        return registrarUsuarioObligatorio();
    }

    private static boolean crearEnUbicacionPorDefecto() {
        String ruta = Conexion.rutaDefectoBD();
        File archivoBD = new File(ruta);
        File directorio = archivoBD.getParentFile();
        if (directorio != null && !directorio.exists()) {
            directorio.mkdirs();
        }

        if (archivoBD.exists()) {
            JOptionPane.showMessageDialog(null,
                    "Ya existe una base de datos en la ubicación por defecto:\n" + ruta,
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            Conexion.guardarRutaBD(ruta);
            if (!new UsuariosDAO().existeAdministrador()) {
                return registrarUsuarioObligatorio();
            }
            return true;
        }

        if (!InicializadorBD.crearBaseDatos(ruta)) {
            JOptionPane.showMessageDialog(null, "No se pudo crear la base de datos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        Conexion.guardarRutaBD(ruta);

        JOptionPane.showMessageDialog(null,
                "Base de datos nueva creada correctamente en:\n" + ruta,
                "Base de datos creada",
                JOptionPane.INFORMATION_MESSAGE);

        if (GoogleServicesProvider.estaConectado()) {
            int subir = JOptionPane.showConfirmDialog(null,
                    "¿Desea subir esta base de datos nueva a Google Drive como respaldo?\n"
                    + "Esto sobreescribira el respaldo existente en Drive.",
                    "Subir respaldo a Drive",
                    JOptionPane.YES_NO_OPTION);
            if (subir == JOptionPane.YES_OPTION) {
                String idCarpeta = GoogleServicesProvider.obtenerIdCarpetaBaseDatos();
                GoogleServicesProvider.subirArchivo(ruta, idCarpeta);
            }
        }

        return registrarUsuarioObligatorio();
    }

    private static boolean registrarUsuarioObligatorio() {
        while (true) {
            D_registro_usuario dlg = new D_registro_usuario(null, true);
            dlg.setVisible(true);

            if (dlg.isRegistrado()) {
                return true;
            }

            int respuesta = JOptionPane.showConfirmDialog(null,
                    "Sin un usuario registrado no podrá iniciar sesión.\n"
                    + "¿Desea intentar registrar un usuario nuevamente?",
                    "Registro de usuario",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (respuesta != JOptionPane.YES_OPTION) {
                return false;
            }
        }
    }
}
