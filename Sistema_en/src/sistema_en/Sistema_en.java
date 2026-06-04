/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package sistema_en;

import sistema_en.Ventanas.V_menu;
import java.awt.Color;
import javax.swing.JFrame;

/**
 *
 * @author ares
 */
public class Sistema_en {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        V_menu menu = new V_menu();
        menu.setExtendedState(JFrame.MAXIMIZED_BOTH);
        menu.getContentPane().setBackground(Color.decode("#FFFFFF"));
        menu.setVisible(true);
    }
    
}
