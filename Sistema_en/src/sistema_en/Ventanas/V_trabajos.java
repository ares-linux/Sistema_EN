/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package sistema_en.Ventanas;

import java.time.LocalDate;
import java.time.LocalTime;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ares
 */
public class V_trabajos extends javax.swing.JFrame {
    
    
    public V_ficha_trabajo ficha;
    V_ficha_trabajo trabajo = new V_ficha_trabajo();
    int fila;

    /**
     * Creates new form V_trabajos
     */
    public V_trabajos() {
        initComponents();
        
        DefaultTableModel modelo = (DefaultTableModel) Tabla_trabajos.getModel();
        
            Object[] fila1 = {"1", "Juan Pérez", "555-1234", "Av. Principal 123", "Reparación de tuberia potable", "2026-06-11", "10:00", "Pendiente"};
            Object[] fila2 = {"2", "María López", "555-5678", "Calle Secundaria 45", "Mantenimiento de tuberia sanitaria", "2026-06-11", "12:30", "En progreso"};
            Object[] fila3 = {"3", "Carlos Gómez", "555-9876", "Ruta Rural Km 4", "Instalación de tuberia para sanitario domestico", "2026-06-10", "16:00", "Completado"};
            Object[] fila4 = {"4", "Ana Martínez", "555-4321", "Calle Los Olivos 789", "Detección de filtraciones", "2026-06-12", "09:00", "Pendiente"};
            Object[] fila5 = {"5", "Luis Rodríguez", "555-8765", "Av. San Martín 456", "Cambio de grifería de cocina", "2026-06-12", "11:15", "En progreso"};
            Object[] fila6 = {"6", "Elena Espinoza", "555-2345", "Pasaje El Sol 12", "Desatasco de desagüe", "2026-06-12", "14:00", "Pendiente"};
            Object[] fila7 = {"7", "Jorge Ramírez", "555-6789", "Blvd. de la Naciones 88", "Instalación de calentador de agua", "2026-06-13", "08:30", "Pendiente"};
            Object[] fila8 = {"8", "Sofía Castro", "555-3456", "Calle Las Flores 234", "Reparación de fuga en baño", "2026-06-13", "10:45", "Pendiente"};
            Object[] fila9 = {"9", "Pedro Morales", "555-7890", "Av. Central 910", "Limpieza de cisterna", "2026-06-13", "15:30", "Pendiente"};
            Object[] fila10 = {"10", "Lucía Fernández", "555-1122", "Ruta Interbalnearia Km 12", "Instalación de bomba de agua", "2026-06-14", "09:15", "Pendiente"};
            Object[] fila11 = {"11", "Miguel Ángel Torres", "555-3344", "Callejón del Beso 5", "Mantenimiento preventivo de tuberías", "2026-06-14", "12:00", "Pendiente"};
            Object[] fila12 = {"12", "Laura Gutiérrez", "555-5566", "Av. de los Incas 742", "Sustitución de bajante pluvial", "2026-06-14", "16:30", "Pendiente"};
            Object[] fila13 = {"13", "Roberto Benítez", "555-7788", "Callejón de la Amargura 44", "Reparación de llave de paso general", "2026-06-15", "08:00", "Pendiente"};
            Object[] fila14 = {"14", "Diana Herrera", "555-9900", "Av. Colón 1050", "Sellado de juntas en lavabo", "2026-06-15", "10:30", "Pendiente"};
            Object[] fila15 = {"15", "Ricardo Silva", "555-1212", "Calle Primaria 89", "Instalación de medidor de agua", "2026-06-15", "13:45", "Pendiente"};
            Object[] fila16 = {"16", "Gabriela Mendoza", "555-3434", "Paseo de la República 321", "Desatasco de tubería principal", "2026-06-16", "09:00", "Pendiente"};
            Object[] fila17 = {"17", "Fernando Ortega", "555-5656", "Av. 18 de Julio 1423", "Reparación de cisterna de inodoro", "2026-06-16", "11:30", "Pendiente"};
            Object[] fila18 = {"18", "Patricia Vargas", "555-7878", "Calle del Pino 67", "Instalación de filtro de agua doméstico", "2026-06-16", "15:00", "Pendiente"};
            Object[] fila19 = {"19", "Alejandro Ruiz", "555-9090", "Camino Viejo a San José 8", "Sustitución de tubería de cobre averiada", "2026-06-17", "08:30", "Pendiente"};
            Object[] fila20 = {"20", "Carmen Delgado", "555-2121", "Av. de la Constitución 55", "Revisión de presión de agua", "2026-06-17", "11:00", "Pendiente"};
            Object[] fila21 = {"21", "Andrés Ibáñez", "555-4343", "Calle Nueva 102", "Instalación de trampa de grasa", "2026-06-17", "14:15", "Pendiente"};
            Object[] fila22 = {"22", "Mónica Suárez", "555-6565", "Plaza Mayor 14", "Reparación de gotera en techo por humedad", "2026-06-18", "10:00", "Pendiente"};
            Object[] fila23 = {"23", "Javier Paredes", "555-8787", "Av. Libertador 2200", "Purgado de radiadores/calefacción", "2026-06-18", "13:00", "Pendiente"};

            modelo.addRow(fila1);
            modelo.addRow(fila2);
            modelo.addRow(fila3);
            modelo.addRow(fila4);
            modelo.addRow(fila5);
            modelo.addRow(fila6);
            modelo.addRow(fila7);
            modelo.addRow(fila8);
            modelo.addRow(fila9);
            modelo.addRow(fila10);
            modelo.addRow(fila11);
            modelo.addRow(fila12);
            modelo.addRow(fila13);
            modelo.addRow(fila14);
            modelo.addRow(fila15);
            modelo.addRow(fila16);
            modelo.addRow(fila17);
            modelo.addRow(fila18);
            modelo.addRow(fila19);
            modelo.addRow(fila20);
            modelo.addRow(fila21);
            modelo.addRow(fila22);
            modelo.addRow(fila23);
}

    public void actualizar_filtro(){
        DefaultTableModel modelo = (DefaultTableModel) Tabla_trabajos.getModel();

        modelo.setRowCount(0);

        String filtro = c_filtro.getSelectedItem().toString();

        Object[][] datos = {
            {"1", "Juan Pérez", "555-1234", "Av. Principal 123", "Reparación de tuberia potable", "2026-06-11", "10:00", "Pendiente"},
            {"2", "María López", "555-5678", "Calle Secundaria 45", "Mantenimiento de tuberia sanitaria", "2026-06-11", "12:30", "En progreso"},
            {"3", "Carlos Gómez", "555-9876", "Ruta Rural Km 4", "Instalación de tuberia para sanitario domestico", "2026-06-10", "16:00", "Completado"},
            {"4", "Ana Martínez", "555-4321", "Calle Los Olivos 789", "Detección de filtraciones", "2026-06-12", "09:00", "Pendiente"},
            {"5", "Luis Rodríguez", "555-8765", "Av. San Martín 456", "Cambio de grifería de cocina", "2026-06-12", "11:15", "En progreso"},
            {"6", "Elena Espinoza", "555-2345", "Pasaje El Sol 12", "Desatasco de desagüe", "2026-06-12", "14:00", "Pendiente"},
            {"7", "Jorge Ramírez", "555-6789", "Blvd. de la Naciones 88", "Instalación de calentador de agua", "2026-06-13", "08:30", "Pendiente"},
            {"8", "Sofía Castro", "555-3456", "Calle Las Flores 234", "Reparación de fuga en baño", "2026-06-13", "10:45", "Pendiente"},
            {"9", "Pedro Morales", "555-7890", "Av. Central 910", "Limpieza de cisterna", "2026-06-13", "15:30", "Pendiente"},
            {"10", "Lucía Fernández", "555-1122", "Ruta Interbalnearia Km 12", "Instalación de bomba de agua", "2026-06-14", "09:15", "Pendiente"},
            {"11", "Miguel Ángel Torres", "555-3344", "Callejón del Beso 5", "Mantenimiento preventivo de tuberías", "2026-06-14", "12:00", "Pendiente"},
            {"12", "Laura Gutiérrez", "555-5566", "Av. de los Incas 742", "Sustitución de bajante pluvial", "2026-06-14", "16:30", "Pendiente"},
            {"13", "Roberto Benítez", "555-7788", "Callejón de la Amargura 44", "Reparación de llave de paso general", "2026-06-15", "08:00", "Pendiente"},
            {"14", "Diana Herrera", "555-9900", "Av. Colón 1050", "Sellado de juntas en lavabo", "2026-06-15", "10:30", "Pendiente"},
            {"15", "Ricardo Silva", "555-1212", "Calle Primaria 89", "Instalación de medidor de agua", "2026-06-15", "13:45", "Pendiente"},
            {"16", "Gabriela Mendoza", "555-3434", "Paseo de la República 321", "Desatasco de tubería principal", "2026-06-16", "09:00", "Pendiente"},
            {"17", "Fernando Ortega", "555-5656", "Av. 18 de Julio 1423", "Reparación de cisterna de inodoro", "2026-06-16", "11:30", "Pendiente"},
            {"18", "Patricia Vargas", "555-7878", "Calle del Pino 67", "Instalación de filtro de agua doméstico", "2026-06-16", "15:00", "Pendiente"},
            {"19", "Alejandro Ruiz", "555-9090", "Camino Viejo a San José 8", "Sustitución de tubería de cobre averiada", "2026-06-17", "08:30", "Pendiente"},
            {"20", "Carmen Delgado", "555-2121", "Av. de la Constitución 55", "Revisión de presión de agua", "2026-06-17", "11:00", "Pendiente"},
            {"21", "Andrés Ibáñez", "555-4343", "Calle Nueva 102", "Instalación de trampa de grasa", "2026-06-17", "14:15", "Pendiente"},
            {"22", "Mónica Suárez", "555-6565", "Plaza Mayor 14", "Reparación de gotera en techo por humedad", "2026-06-18", "10:00", "Pendiente"},
            {"23", "Javier Paredes", "555-8787", "Av. Libertador 2200", "Purgado de radiadores/calefacción", "2026-06-18", "13:00", "Pendiente"}
        };

        for (Object[] fila : datos) {
            if (filtro.equals("Todos") || fila[7].equals(filtro)) {
            modelo.addRow(fila);
            }
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabla_trabajos = new javax.swing.JTable();
        c_filtro = new javax.swing.JComboBox<>();
        b_filtro = new javax.swing.JButton();
        b_cerrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Trabajos registros");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(343, 22, -1, -1));

        Tabla_trabajos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "nombre_cli", "telefono", "direccion", "descripcion", "fecha_r", "hora_r", "estado"
            }
        ));
        Tabla_trabajos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Tabla_trabajosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(Tabla_trabajos);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(27, 80, 1020, -1));

        c_filtro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "Pendiente", "En progreso", "Completado" }));
        getContentPane().add(c_filtro, new org.netbeans.lib.awtextra.AbsoluteConstraints(27, 45, -1, -1));

        b_filtro.setText("Aplicar filtro");
        b_filtro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_filtroActionPerformed(evt);
            }
        });
        getContentPane().add(b_filtro, new org.netbeans.lib.awtextra.AbsoluteConstraints(157, 45, -1, -1));

        b_cerrar.setText("Cerrar");
        b_cerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_cerrarActionPerformed(evt);
            }
        });
        getContentPane().add(b_cerrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 520, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void b_cerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_cerrarActionPerformed
        this.dispose();
    }//GEN-LAST:event_b_cerrarActionPerformed

    private void b_filtroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_filtroActionPerformed
        //c_filtro.setEnabled(true);
        actualizar_filtro();
    }//GEN-LAST:event_b_filtroActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        //c_filtro.setEnabled(false);
    }//GEN-LAST:event_formWindowOpened

    public void cargar_datos_tabla(int indice){
        
        
        ficha = new V_ficha_trabajo();
        ficha.setVisible(true);
    }
    
    private void Tabla_trabajosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Tabla_trabajosMouseClicked
        fila = this.Tabla_trabajos.rowAtPoint(evt.getPoint());
        cargar_datos_tabla(fila);
    }//GEN-LAST:event_Tabla_trabajosMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(V_trabajos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(V_trabajos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(V_trabajos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(V_trabajos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new V_trabajos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabla_trabajos;
    private javax.swing.JButton b_cerrar;
    private javax.swing.JButton b_filtro;
    private javax.swing.JComboBox<String> c_filtro;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
