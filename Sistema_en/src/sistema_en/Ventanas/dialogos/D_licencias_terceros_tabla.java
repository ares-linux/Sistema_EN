/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package sistema_en.Ventanas.dialogos;

import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ares
 */
public class D_licencias_terceros_tabla extends javax.swing.JDialog {

    /**
     * Creates new form D_licencias_terceros_tabla
     */
    public D_licencias_terceros_tabla(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        Tabla_licencias.setDefaultEditor(Object.class, null);
        Tabla_licencias.setSelectionMode( ListSelectionModel.SINGLE_SELECTION);
        Tabla_licencias.getTableHeader().setReorderingAllowed(false);
        Tabla_licencias.getTableHeader().setResizingAllowed(false);
        
        DefaultTableModel modelo = (DefaultTableModel) Tabla_licencias.getModel();
        
            Object[] fila1 = {"jsvg", "2.1.0", "MIT"};
            modelo.addRow(fila1);

            Object[] fila2 = {"oshi-core", "6.4.0", "MIT"};
            modelo.addRow(fila2);

            Object[] fila3 = {"gson", "2.10.1", "Apache 2.0"};
            modelo.addRow(fila3);

            Object[] fila4 = {"openpdf", "1.3.39", "LGPL / MPL (Dual)"};
            modelo.addRow(fila4);

            Object[] fila5 = {"jbcrypt", "0.4", "ISC / BSD"};
            modelo.addRow(fila5);

            Object[] fila6 = {"jcalendar", "1.4", "LGPL"};
            modelo.addRow(fila6);

            Object[] fila7 = {"flatlaf", "3.7.1", "Apache 2.0"};
            modelo.addRow(fila7);

            Object[] fila8 = {"guava", "32.1.2-jre", "Apache 2.0"};
            modelo.addRow(fila8);

            Object[] fila9 = {"slf4j-api", "2.0.13", "MIT"};
            modelo.addRow(fila9);

            Object[] fila10 = {"jna", "5.19.1", "Apache 2.0 / LGPL (Dual)"};
            modelo.addRow(fila10);

            Object[] fila11 = {"grpc-context", "1.53.0", "Apache 2.0"};
            modelo.addRow(fila11);

            Object[] fila12 = {"slf4j-simple", "2.0.18", "MIT"};
            modelo.addRow(fila12);

            Object[] fila13 = {"sqlite-jdbc", "3.53.2.0", "Apache 2.0"};
            modelo.addRow(fila13);

            Object[] fila14 = {"jna-platform", "5.19.1", "Apache 2.0 / LGPL (Dual)"};
            modelo.addRow(fila14);

            Object[] fila15 = {"jackson-core", "2.15.2", "Apache 2.0"};
            modelo.addRow(fila15);

            Object[] fila16 = {"flatlaf-extras", "3.7.1", "Apache 2.0"};
            modelo.addRow(fila16);

            Object[] fila17 = {"AbsoluteLayout", "Sin versión especificada", "CDDL / GPL (Plataforma NetBeans)"};
            modelo.addRow(fila17);

            Object[] fila18 = {"LGoodDatePicker", "11.2.1", "MIT"};
            modelo.addRow(fila18);

            Object[] fila19 = {"opencensus-api", "0.28.0", "Apache 2.0"};
            modelo.addRow(fila19);

            Object[] fila20  = {"google-api-client", "2.2.0", "Apache 2.0"};           
            modelo.addRow(fila20);

            Object[] fila21  = {"google-http-client", "1.43.3","Apache 2.0"};           
            modelo.addRow(fila21);

            Object[] fila22  = {"google-oauth-client","1.34.1","Apache 2.0"};           
            modelo.addRow(fila22);

            Object[] fila23  = {"flatlaf-javadoc", "3.7.1","Apache 2.0"};           
            modelo.addRow(fila23);

            Object[] fila24  = {"jSystemThemeDetector", "3.9","MIT"};           
            modelo.addRow(fila24);

            Object[] fila25  = {"google-oauth-client-java6","1.34.1","Apache 2.0"};           
            modelo.addRow(fila25);
        
            Object[] fila26  = {"google-oauth-client-jetty","1.34.1","Apache 2.0"};           
            modelo.addRow(fila26);

            Object[] fila27  = {"google-http-client-gson","1.42.3","Apache 2.0"};           
            modelo.addRow(fila27);

            Object[] fila28  = {"google-http-client-jackson2", "1.43.3","Apache 2.0"};           
            modelo.addRow(fila28);

            Object[] fila29  = {"opencensus-contrib-http-util", "0.28.0", "Apache 2.0"};           
            modelo.addRow(fila29);

            Object[] fila30  = {"swing-toast-notifications","1.0.4","Apache 2.0"};           
            modelo.addRow(fila30);

            Object[] fila31  = {"google-api-services-drive-v3-rev20230822","2.0.0","Apache 2.0"};           
            modelo.addRow(fila31); 
            
            Object[] fila32  = {"jgoodies-common", "1.2.0", "BSD 3-Clause"};
            modelo.addRow(fila32);

            Object[] fila33  = {"jgoodies-looks", "2.4.1", "BSD 3-Clause"};
            modelo.addRow(fila33);

            Object[] fila34  = {"google-api-services-sheets-v4-rev20220927", "2.0.0", "Apache 2.0"};
            modelo.addRow(fila34);

            Object[] fila35  = {"junit", "4.6", "EPL 1.0"};
            modelo.addRow(fila35);

            Object[] fila36  = {"org-netbeans-modules-java-j2seproject-copylibstask", "Sin versión especificada", "CDDL / GPL (Plataforma NetBeans)"};
            modelo.addRow(fila36);

            Object[] fila37  = {"google-api-services-calendar-v3-rev20240705", "2.0.0", "Apache 2.0"};
            modelo.addRow(fila37);

            Object[] fila38  = {"google-api-services-gmail-v1-rev20260727", "2.0.0", "Apache 2.0"};
            modelo.addRow(fila38);
            
        Tabla_licencias.setDefaultEditor(Object.class, null);
        Tabla_licencias.setSelectionMode( ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane1 = new javax.swing.JScrollPane();
        T_declaracion = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        Tabla_licencias = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        T_declaracion.setEditable(false);
        T_declaracion.setColumns(20);
        T_declaracion.setRows(5);
        T_declaracion.setText("Sistema EN incorpora bibliotecas y componentes de software desarrollados por terceros. Todos los derechos sobre dichos componentes \npertenecen a sus respectivos autores y se distribuyen bajo sus correspondientes licencias de uso.\n\nSoftium S.A. no reclama propiedad sobre dichos componentes y reconoce expresamente las licencias establecidas por sus autores.");
        jScrollPane1.setViewportView(T_declaracion);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 770;
        gridBagConstraints.ipady = 68;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(jScrollPane1, gridBagConstraints);

        Tabla_licencias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre", "Version", "Licencia"
            }
        ));
        jScrollPane2.setViewportView(Tabla_licencias);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 770;
        gridBagConstraints.ipady = 202;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(jScrollPane2, gridBagConstraints);

        jButton1.setText("Ver licencias completas");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(jButton1, gridBagConstraints);

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel1.setText("Licencias de terceros");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(jLabel1, gridBagConstraints);

        jButton2.setText("Cerrar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(jButton2, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        T_declaracion.setEditable(false);
    }//GEN-LAST:event_formWindowOpened

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        D_licencias_terceros licencias = new D_licencias_terceros((java.awt.Frame) this.getParent(), true);
        licencias.setLocationRelativeTo(D_licencias_terceros_tabla.this);
        java.awt.Rectangle zonaSegura = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        licencias.setBounds(zonaSegura); // Forzamos al diálogo a tomar todo ese tamaño
        licencias.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(D_licencias_terceros_tabla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(D_licencias_terceros_tabla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(D_licencias_terceros_tabla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(D_licencias_terceros_tabla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                D_licencias_terceros_tabla dialog = new D_licencias_terceros_tabla(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea T_declaracion;
    private javax.swing.JTable Tabla_licencias;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
