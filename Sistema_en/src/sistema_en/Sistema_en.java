/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package sistema_en;

import java.awt.Color;
import javax.swing.JFrame;
import sistema_en.Ventanas.V_login_user;
import sistema_en.Ventanas.V_menu;


/**
 *
 * @author ares
 */
public class Sistema_en {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        V_login_user login = new V_login_user();
        login.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //login.getContentPane().setBackground(Color.decode("#FFFFF"));
        login.setVisible(true);

        //V_menu menu = new V_menu();
        //menu.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //menu.getContentPane().setBackground(Color.decode("#FFFFFF"));
        //menu.setVisible(true);
    }
    
}
