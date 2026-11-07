/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package sistema_en;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import sistema_en.Ventanas.V_login_user;
import animaciones.V_splash;
import com.jthemedetecor.OsThemeDetector;
import dao.ConfiguracionesDAO;
import java.util.List;
import javax.swing.*;



/**
 * Punto de entrada de la aplicacion Sistema EN.
 *
 * <p>Muestra la pantalla de bienvenida ({@link animaciones.V_splash}), ejecuta
 * el arranque de la base de datos mediante {@link Controlador_arranque} y, si
 * todo esta listo, abre la ventana de inicio de sesion aplicando el tema
 * configurado por el usuario: claro, oscuro o automatico.</p>
 *
 * @author ares
 */
public class Sistema_en {

    /**
     * Clase de arranque: no se instancia.
     */
    private Sistema_en() {
    }

    /**
     * Inicia la aplicacion: verifica la base de datos y abre la ventana de
     * inicio de sesion con el tema configurado.
     *
     * @param args argumentos de linea de comandos (no se utilizan)
     */
public static void main(String[] args) {      
    //V_login_user login = new V_login_user();

    V_splash splash = new V_splash();
    splash.actualizarEstado("Verificando base de datos…");
    splash.mostrar();

    try {
        if (!Controlador_arranque.iniciar()) {
            splash.cerrar();
            System.exit(0);
            return;
        }

        splash.cerrar();

        ConfiguracionesDAO dao = new ConfiguracionesDAO();
        List<String[]> configs = dao.obtenerConfiguraciones();
        boolean ventanaAbierta = false;

        for (String[] conf : configs) {

        String clave = conf[1];
        String valor = conf[2];

        switch (clave) {

            case "tema" -> {
                if ("Claro".equals(valor)){

                FlatLightLaf.setup();
                V_login_user login = new V_login_user();
                login.setLocationRelativeTo(null);
                login.setVisible(true);
                ventanaAbierta = true;
                }

                else if ("Oscuro".equals(valor)){

                FlatDarkLaf.setup();
                V_login_user login = new V_login_user();
                login.setLocationRelativeTo(null);
                login.setVisible(true);
                ventanaAbierta = true;
                }

                else if ("Automatico".equals(valor)){

                    OsThemeDetector detector = OsThemeDetector.getDetector();
                    Runnable updateTheme = () -> {
                    try {

                        if (detector.isDark()) {
                            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
                        } else {
                            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
                        }

                        com.formdev.flatlaf.FlatLaf.updateUI(); 
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    };

                    updateTheme.run();
                    detector.registerListener(isDark -> {
                        SwingUtilities.invokeLater(updateTheme);
                    });

                    SwingUtilities.invokeLater(() -> {
                        V_login_user login = new V_login_user();
                        login.setLocationRelativeTo(null);
                        login.setVisible(true);
                    });
                    ventanaAbierta = true;
                }

                          break;
            }
        }   
        }

        if (!ventanaAbierta) {
            FlatLightLaf.setup();
            V_login_user login = new V_login_user();
            login.setLocationRelativeTo(null);
            login.setVisible(true);
        }
    } catch (Throwable t) {
        System.out.println("Error al iniciar el programa:");
        t.printStackTrace();
        try {
            JOptionPane.showMessageDialog(null,
                    "No se pudo iniciar el programa:\n" + t.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ignore) {
        }
        System.exit(1);
    }
    }
}
        

