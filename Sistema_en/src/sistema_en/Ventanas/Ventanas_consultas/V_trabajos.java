/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package sistema_en.Ventanas.Ventanas_consultas;

import dao.TrabajoDAO;
import javax.swing.JFrame;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import sistema_en.Ventanas.dialogos.D_ficha_trabajo;

/**
 *
 * @author ares
 */
public class V_trabajos extends javax.swing.JFrame {
    
    private boolean dividida = false;
    
    public D_ficha_trabajo ficha;
    int fila;

    /**
     * Creates new form V_trabajos
     */
    public V_trabajos() {
        initComponents();
        
        Tabla_trabajos.setDefaultEditor(Object.class, null);
        Tabla_trabajos.setSelectionMode( ListSelectionModel.SINGLE_SELECTION);
        
        //DefaultTableModel modelo = (DefaultTableModel) Tabla_trabajos.getModel();
        
            //Object[] fila1 = {"1", "Juan Pérez", "555-1234", "Av. Principal 123", "Reparación de tuberia potable", "2026-06-11", "10:00", "Pendiente"};
            //Object[] fila2 = {"2", "María López", "555-5678", "Calle Secundaria 45", "Mantenimiento de tuberia sanitaria", "2026-06-11", "12:30", "En progreso"};
            //Object[] fila3 = {"3", "Carlos Gómez", "555-9876", "Ruta Rural Km 4", "Instalación de tuberia para sanitario domestico", "2026-06-10", "16:00", "Completado"};
            //Object[] fila4 = {"4", "Ana Martínez", "555-4321", "Calle Los Olivos 789", "Detección de filtraciones", "2026-06-12", "09:00", "Pendiente"};
            //Object[] fila5 = {"5", "Luis Rodríguez", "555-8765", "Av. San Martín 456", "Cambio de grifería de cocina", "2026-06-12", "11:15", "En progreso"};
            //Object[] fila6 = {"6", "Elena Espinoza", "555-2345", "Pasaje El Sol 12", "Desatasco de desagüe", "2026-06-12", "14:00", "Pendiente"};
            //Object[] fila7 = {"7", "Jorge Ramírez", "555-6789", "Blvd. de la Naciones 88", "Instalación de calentador de agua", "2026-06-13", "08:30", "Pendiente"};
            //Object[] fila8 = {"8", "Sofía Castro", "555-3456", "Calle Las Flores 234", "Reparación de fuga en baño", "2026-06-13", "10:45", "Pendiente"};
            //Object[] fila9 = {"9", "Pedro Morales", "555-7890", "Av. Central 910", "Limpieza de cisterna", "2026-06-13", "15:30", "Pendiente"};
            //Object[] fila10 = {"10", "Lucía Fernández", "555-1122", "Ruta Interbalnearia Km 12", "Instalación de bomba de agua", "2026-06-14", "09:15", "Pendiente"};
            //Object[] fila11 = {"11", "Miguel Ángel Torres", "555-3344", "Callejón del Beso 5", "Mantenimiento preventivo de tuberías", "2026-06-14", "12:00", "Pendiente"};
            //Object[] fila12 = {"12", "Laura Gutiérrez", "555-5566", "Av. de los Incas 742", "Sustitución de bajante pluvial", "2026-06-14", "16:30", "Pendiente"};
            //Object[] fila13 = {"13", "Roberto Benítez", "555-7788", "Callejón de la Amargura 44", "Reparación de llave de paso general", "2026-06-15", "08:00", "Pendiente"};
            //Object[] fila14 = {"14", "Diana Herrera", "555-9900", "Av. Colón 1050", "Sellado de juntas en lavabo", "2026-06-15", "10:30", "Pendiente"};
            //Object[] fila15 = {"15", "Ricardo Silva", "555-1212", "Calle Primaria 89", "Instalación de medidor de agua", "2026-06-15", "13:45", "Pendiente"};
            //Object[] fila16 = {"16", "Gabriela Mendoza", "555-3434", "Paseo de la República 321", "Desatasco de tubería principal", "2026-06-16", "09:00", "Pendiente"};
            //Object[] fila17 = {"17", "Fernando Ortega", "555-5656", "Av. 18 de Julio 1423", "Reparación de cisterna de inodoro", "2026-06-16", "11:30", "Pendiente"};
            //Object[] fila18 = {"18", "Patricia Vargas", "555-7878", "Calle del Pino 67", "Instalación de filtro de agua doméstico", "2026-06-16", "15:00", "Pendiente"};
            //Object[] fila19 = {"19", "Alejandro Ruiz", "555-9090", "Camino Viejo a San José 8", "Sustitución de tubería de cobre averiada", "2026-06-17", "08:30", "Pendiente"};
            //Object[] fila20 = {"20", "Carmen Delgado", "555-2121", "Av. de la Constitución 55", "Revisión de presión de agua", "2026-06-17", "11:00", "Pendiente"};
            //Object[] fila21 = {"21", "Andrés Ibáñez", "555-4343", "Calle Nueva 102", "Instalación de trampa de grasa", "2026-06-17", "14:15", "Pendiente"};
            //Object[] fila22 = {"22", "Mónica Suárez", "555-6565", "Plaza Mayor 14", "Reparación de gotera en techo por humedad", "2026-06-18", "10:00", "Pendiente"};
            //Object[] fila23 = {"23", "Javier Paredes", "555-8787", "Av. Libertador 2200", "Purgado de radiadores/calefacción", "2026-06-18", "13:00", "Pendiente"};

            //modelo.addRow(fila1);
            //modelo.addRow(fila2);
            //modelo.addRow(fila3);
            //modelo.addRow(fila4);
            //modelo.addRow(fila5);
            //modelo.addRow(fila6);
            //modelo.addRow(fila7);
            //modelo.addRow(fila8);
            //modelo.addRow(fila9);
            //modelo.addRow(fila10);
            //modelo.addRow(fila11);
            //modelo.addRow(fila12);
            //modelo.addRow(fila13);
            //modelo.addRow(fila14);
            //modelo.addRow(fila15);
            //modelo.addRow(fila16);
            //modelo.addRow(fila17);
            //modelo.addRow(fila18);
            //modelo.addRow(fila19);
            //modelo.addRow(fila20);
            //modelo.addRow(fila21);
            //modelo.addRow(fila22);
            //modelo.addRow(fila23);
}

    //public void actualizar_filtro(){
        //DefaultTableModel modelo = (DefaultTableModel) Tabla_trabajos.getModel();

        //modelo.setRowCount(0);

        //String filtro = c_filtro.getSelectedItem().toString();

        //Object[][] datos = {
            //{"1", "Juan Pérez", "555-1234", "Av. Principal 123", "Reparación de tuberia potable", "2026-06-11", "10:00", "Pendiente"},
            //{"2", "María López", "555-5678", "Calle Secundaria 45", "Mantenimiento de tuberia sanitaria", "2026-06-11", "12:30", "En progreso"},
            //{"3", "Carlos Gómez", "555-9876", "Ruta Rural Km 4", "Instalación de tuberia para sanitario domestico", "2026-06-10", "16:00", "Completado"},
            //{"4", "Ana Martínez", "555-4321", "Calle Los Olivos 789", "Detección de filtraciones", "2026-06-12", "09:00", "Pendiente"},
            //{"5", "Luis Rodríguez", "555-8765", "Av. San Martín 456", "Cambio de grifería de cocina", "2026-06-12", "11:15", "En progreso"},
            //{"6", "Elena Espinoza", "555-2345", "Pasaje El Sol 12", "Desatasco de desagüe", "2026-06-12", "14:00", "Pendiente"},
            //{"7", "Jorge Ramírez", "555-6789", "Blvd. de la Naciones 88", "Instalación de calentador de agua", "2026-06-13", "08:30", "Pendiente"},
            //{"8", "Sofía Castro", "555-3456", "Calle Las Flores 234", "Reparación de fuga en baño", "2026-06-13", "10:45", "Pendiente"},
            //{"9", "Pedro Morales", "555-7890", "Av. Central 910", "Limpieza de cisterna", "2026-06-13", "15:30", "Pendiente"},
            //{"10", "Lucía Fernández", "555-1122", "Ruta Interbalnearia Km 12", "Instalación de bomba de agua", "2026-06-14", "09:15", "Pendiente"},
            //{"11", "Miguel Ángel Torres", "555-3344", "Callejón del Beso 5", "Mantenimiento preventivo de tuberías", "2026-06-14", "12:00", "Pendiente"},
            //{"12", "Laura Gutiérrez", "555-5566", "Av. de los Incas 742", "Sustitución de bajante pluvial", "2026-06-14", "16:30", "Pendiente"},
            //{"13", "Roberto Benítez", "555-7788", "Callejón de la Amargura 44", "Reparación de llave de paso general", "2026-06-15", "08:00", "Pendiente"},
            //{"14", "Diana Herrera", "555-9900", "Av. Colón 1050", "Sellado de juntas en lavabo", "2026-06-15", "10:30", "Pendiente"},
            //{"15", "Ricardo Silva", "555-1212", "Calle Primaria 89", "Instalación de medidor de agua", "2026-06-15", "13:45", "Pendiente"},
            //{"16", "Gabriela Mendoza", "555-3434", "Paseo de la República 321", "Desatasco de tubería principal", "2026-06-16", "09:00", "Pendiente"},
            //{"17", "Fernando Ortega", "555-5656", "Av. 18 de Julio 1423", "Reparación de cisterna de inodoro", "2026-06-16", "11:30", "Pendiente"},
            //{"18", "Patricia Vargas", "555-7878", "Calle del Pino 67", "Instalación de filtro de agua doméstico", "2026-06-16", "15:00", "Pendiente"},
            //{"19", "Alejandro Ruiz", "555-9090", "Camino Viejo a San José 8", "Sustitución de tubería de cobre averiada", "2026-06-17", "08:30", "Pendiente"},
            //{"20", "Carmen Delgado", "555-2121", "Av. de la Constitución 55", "Revisión de presión de agua", "2026-06-17", "11:00", "Pendiente"},
            //{"21", "Andrés Ibáñez", "555-4343", "Calle Nueva 102", "Instalación de trampa de grasa", "2026-06-17", "14:15", "Pendiente"},
            //{"22", "Mónica Suárez", "555-6565", "Plaza Mayor 14", "Reparación de gotera en techo por humedad", "2026-06-18", "10:00", "Pendiente"},
            //{"23", "Javier Paredes", "555-8787", "Av. Libertador 2200", "Purgado de radiadores/calefacción", "2026-06-18", "13:00", "Pendiente"}
        //};

        //for (Object[] fila : datos) {
            //if (filtro.equals("Todos") || fila[7].equals(filtro)) {
            //modelo.addRow(fila);
            //}
        //}
    //}
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        c_filtro = new javax.swing.JComboBox<>();
        b_filtro = new javax.swing.JButton();
        b_dividir_pantalla = new javax.swing.JButton();
        b_actualizar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabla_trabajos = new javax.swing.JTable();
        b_cerrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Historial de Trabajos");
        jLabel1.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(9, 107, 10, 1);
        getContentPane().add(jLabel1, gridBagConstraints);

        jScrollPane2.setAutoscrolls(true);
        jScrollPane2.setMinimumSize(new java.awt.Dimension(20, 60));

        jPanel1.setLayout(new java.awt.GridBagLayout());

        c_filtro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "Pendiente", "En progreso", "Completado" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(c_filtro, gridBagConstraints);

        b_filtro.setText("Aplicar filtro");
        b_filtro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_filtroActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(b_filtro, gridBagConstraints);

        b_dividir_pantalla.setText("Dividir");
        b_dividir_pantalla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_dividir_pantallaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(b_dividir_pantalla, gridBagConstraints);

        b_actualizar.setText("Actualizar");
        b_actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_actualizarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(b_actualizar, gridBagConstraints);

        jScrollPane2.setViewportView(jPanel1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jScrollPane2, gridBagConstraints);

        Tabla_trabajos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Nombre Cliente", "Teléfono", "Dirección", "Descripción", "Fecha Registro", "Hora Registro", "Estado"
            }
        ));
        Tabla_trabajos.setFillsViewportHeight(true);
        Tabla_trabajos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Tabla_trabajosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(Tabla_trabajos);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 1000;
        gridBagConstraints.ipady = 398;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 27, 0, 33);
        getContentPane().add(jScrollPane1, gridBagConstraints);

        b_cerrar.setText("Cerrar");
        b_cerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_cerrarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(18, 531, 15, 21);
        getContentPane().add(b_cerrar, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void b_cerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_cerrarActionPerformed
        this.dispose();
    }//GEN-LAST:event_b_cerrarActionPerformed

    private void b_filtroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_filtroActionPerformed
        //c_filtro.setEnabled(true);
        //actualizar_filtro();
    }//GEN-LAST:event_b_filtroActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        //c_filtro.setEnabled(false);
        llenar_tabla();
    }//GEN-LAST:event_formWindowOpened

    public void cargar_datos_tabla(int indice){
        ficha = new D_ficha_trabajo(this,true);
        ficha.setLocationRelativeTo(V_trabajos.this);
        ficha.setVisible(true);
    }
    
    public void llenar_tabla(){
        DefaultTableModel modelo = (DefaultTableModel) this.Tabla_trabajos.getModel();
        modelo.setRowCount(0); 

        TrabajoDAO dao = new TrabajoDAO();
        java.util.List<String[]> listaTrabajos = dao.obtenerTrabajosConClientes();

        for (String[] fila : listaTrabajos) {
            modelo.addRow(fila);
        }
    }
    
    private void Tabla_trabajosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Tabla_trabajosMouseClicked
        
        fila = Tabla_trabajos.getSelectedRow();
                if (fila == -1) 
                {
                    //javax.swing.JOptionPane.showMessageDialog(this, "Selecciona un trabajo de la tabla para ver su ficha.");
                    return;
                }
                else{
                    fila = this.Tabla_trabajos.rowAtPoint(evt.getPoint());
                    cargar_datos_tabla(fila);
                }
                
    }//GEN-LAST:event_Tabla_trabajosMouseClicked

    private void b_dividir_pantallaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_dividir_pantallaActionPerformed

        if (!dividida) {

            int respuesta = javax.swing.JOptionPane.showConfirmDialog(this, "¿Desea dividir el formulario?", "Confirmar accion", javax.swing.JOptionPane.YES_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE);
            if (respuesta == javax.swing.JOptionPane.YES_OPTION) {
                // 1. Definimos los textos personalizados para los botones
                String[] opciones = {"Izquierda", "Derecha"};

                // 2. Usamos showOptionDialog para personalizar el componente
                int respuesta1 = javax.swing.JOptionPane.showOptionDialog(this, "¿En que lado de la pantalla?", "Seleccione una opción", javax.swing.JOptionPane.DEFAULT_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[1]);

                if (respuesta1 == 0) {
                    System.out.println("Seleccionaste Izquierda");

                    b_dividir_pantalla.setText("Maximizar");
                    dividida = true;

                    try {
                        // 1. SI ESTÁ MAXIMIZADA, LA RESTAURAMOS A SU ESTADO NORMAL Primero
                        if ((this.getExtendedState() & java.awt.Frame.MAXIMIZED_BOTH) != 0) {
                            this.setExtendedState(java.awt.Frame.NORMAL);
                        }

                        java.awt.GraphicsConfiguration config = this.getGraphicsConfiguration();
                        java.awt.Rectangle bounds = config.getBounds();
                        java.awt.Insets insets = java.awt.Toolkit.getDefaultToolkit().getScreenInsets(config);

                        int anchoUtil = bounds.width - insets.left - insets.right;
                        int altoUtil = bounds.height - insets.top - insets.bottom;

                        int nuevaX = insets.left;
                        int nuevaY = insets.top;
                        int nuevoAncho = anchoUtil / 2;
                        int nuevoAlto = altoUtil;

                        this.setBounds(nuevaX, nuevaY, nuevoAncho, nuevoAlto);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (respuesta1 == 1) {
                    System.out.println("Seleccionaste Derecha");

                    b_dividir_pantalla.setText("Maximizar");
                    dividida = true;

                    try {
                        if ((this.getExtendedState() & java.awt.Frame.MAXIMIZED_BOTH) != 0) {
                            this.setExtendedState(java.awt.Frame.NORMAL);
                        }

                        java.awt.GraphicsConfiguration config = this.getGraphicsConfiguration();
                        java.awt.Rectangle bounds = config.getBounds();
                        java.awt.Insets insets = java.awt.Toolkit.getDefaultToolkit().getScreenInsets(config);

                        int anchoUtil = bounds.width - insets.left - insets.right;
                        int altoUtil = bounds.height - insets.top - insets.bottom;

                        int nuevoAncho = anchoUtil / 2;
                        int nuevaX = insets.left + nuevoAncho;
                        int nuevaY = insets.top;
                        int nuevoAlto = altoUtil;

                        this.setBounds(nuevaX, nuevaY, nuevoAncho, nuevoAlto);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        } else {

            this.setExtendedState(JFrame.MAXIMIZED_BOTH);

            b_dividir_pantalla.setText("Dividir");
            dividida = false;
        }
                }//GEN-LAST:event_b_dividir_pantallaActionPerformed

    private void b_actualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_actualizarActionPerformed
        //c_filtro.setSelectedItem(0);
        llenar_tabla();
    }//GEN-LAST:event_b_actualizarActionPerformed

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
    private javax.swing.JButton b_actualizar;
    private javax.swing.JButton b_cerrar;
    private javax.swing.JButton b_dividir_pantalla;
    private javax.swing.JButton b_filtro;
    private javax.swing.JComboBox<String> c_filtro;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
