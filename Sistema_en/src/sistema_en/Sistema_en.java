/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package sistema_en;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import sistema_en.Ventanas.V_login_user;
import com.jthemedetecor.OsThemeDetector;
import dao.ConfiguracionesDAO;
import java.util.List;
import javax.swing.*;



/**
 *
 * @author ares
 */
public class Sistema_en {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {  
    //System.out.println("🚀 Iniciando prueba de respaldo en Google Drive...");
    //sistema_en.Objetos.GoogleDriveProvider.subirArchivo("credentials.json");
    
    //V_login_user login = new V_login_user();

    ConfiguracionesDAO dao = new ConfiguracionesDAO();
    List<String[]> configs = dao.obtenerConfiguraciones();
    
    for (String[] conf : configs) {

    String clave = conf[1];
    String valor = conf[2];
    
    switch (clave) {

        case "tema" -> {
            if ("Claro".equals(valor)){
            
            FlatLightLaf.setup();
            //java.awt.EventQueue.invokeLater(() -> {
            V_login_user login = new V_login_user();
            login.setLocationRelativeTo(null);
            login.setVisible(true);
            }
            
            else if ("Oscuro".equals(valor)){
            
            FlatDarkLaf.setup();
            //java.awt.EventQueue.invokeLater(() -> {
            V_login_user login = new V_login_user();
            login.setLocationRelativeTo(null);
            login.setVisible(true);
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
            }
                                      
                    break;
            }
    }   
    }
    }
}
        

