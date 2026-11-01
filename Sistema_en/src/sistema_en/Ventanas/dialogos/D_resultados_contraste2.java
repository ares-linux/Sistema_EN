/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package sistema_en.Ventanas.dialogos;

import dao.InformesDAO;
import java.time.LocalDate;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * Muestra los resultados de contraste entre dos periodos.
 * Ambos paneles (tabla numérica y resumen textual) están siempre presentes;
 * la visibilidad de cada uno se ajusta según los parámetros recibidos.
 *
 * @author ares
 */
public class D_resultados_contraste2 extends javax.swing.JDialog {

    /**
     * @param parent    ventana padre
     * @param modal     true = modal
     * @param p1        periodo 1 (formato "yyyy-MM")
     * @param p2        periodo 2 (formato "yyyy-MM")
     * @param numerico  mostrar tabla de números
     * @param textual   mostrar resumen de texto
     * @param mixto     mostrar ambos paneles
     */
    public D_resultados_contraste2(java.awt.Dialog parent, boolean modal,
            String p1, String p2,
            boolean numerico, boolean textual, boolean mixto) {
        super(parent, modal);
        initComponents();
        setTitle("Resultados del Contraste (" + p1 + " vs " + p2 + ")");
        configurarTabla();
        cargarDatos(p1, p2, numerico, textual, mixto);
        pack();
        setLocationRelativeTo(parent);
    }

    /**
     * Configura la tabla para que no sea editable y no se reordenen columnas.
     */
    private void configurarTabla() {
        Tabla_resultados.setDefaultEditor(Object.class, null);
        Tabla_resultados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Tabla_resultados.getTableHeader().setReorderingAllowed(false);
        Tabla_resultados.getTableHeader().setResizingAllowed(false);
    }

    private void cargarDatos(String p1, String p2,
            boolean numerico, boolean textual, boolean mixto) {

        LocalDate ini1 = getInicio(p1);
        LocalDate fin1 = getFin(ini1);
        LocalDate ini2 = getInicio(p2);
        LocalDate fin2 = getFin(ini2);

        InformesDAO dao = new InformesDAO();

        int tRegistrados1 = dao.contarTrabajosRegistrados(ini1, fin1);
        int tRegistrados2 = dao.contarTrabajosRegistrados(ini2, fin2);
        int tIniciados1   = dao.contarTrabajosIniciados(ini1, fin1);
        int tIniciados2   = dao.contarTrabajosIniciados(ini2, fin2);
        int tFinalizados1 = dao.contarTrabajosFinalizados(ini1, fin1);
        int tFinalizados2 = dao.contarTrabajosFinalizados(ini2, fin2);
        int cNuevos1      = dao.contarClientesNuevos(ini1, fin1);
        int cNuevos2      = dao.contarClientesNuevos(ini2, fin2);
        int cotReg1       = dao.contarCotizacionesRegistradas(ini1, fin1);
        int cotReg2       = dao.contarCotizacionesRegistradas(ini2, fin2);
        int cotConv1      = dao.contarCotizacionesConvertidas(ini1, fin1);
        int cotConv2      = dao.contarCotizacionesConvertidas(ini2, fin2);

        // --- Tabla numérica ---
        boolean mostrarTabla = numerico || mixto;
        Scroll_tabla.setVisible(mostrarTabla);
        if (mostrarTabla) {
            String[] columnas = {
                "Estadística",
                "Periodo 1 (" + p1 + ")",
                "Periodo 2 (" + p2 + ")",
                "Diferencia"
            };
            Object[][] datos = {
                {"Trabajos registrados",    tRegistrados1, tRegistrados2, tRegistrados1 - tRegistrados2},
                {"Trabajos iniciados",      tIniciados1,   tIniciados2,   tIniciados1   - tIniciados2},
                {"Trabajos finalizados",    tFinalizados1, tFinalizados2, tFinalizados1 - tFinalizados2},
                {"Clientes nuevos",         cNuevos1,      cNuevos2,      cNuevos1      - cNuevos2},
                {"Cotizaciones registradas",cotReg1,       cotReg2,       cotReg1       - cotReg2},
                {"Cotizaciones convertidas",cotConv1,      cotConv2,      cotConv1      - cotConv2}
            };
            Tabla_resultados.setModel(new DefaultTableModel(datos, columnas) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });
        }

        // --- Texto resumen ---
        boolean mostrarTexto = textual || mixto;
        Scroll_texto.setVisible(mostrarTexto);
        if (mostrarTexto) {
            StringBuilder sb = new StringBuilder("Resumen Textual del Contraste:\n\n");
            sb.append(generarTexto("Trabajos registrados",    tRegistrados1, tRegistrados2, p1, p2));
            sb.append(generarTexto("Trabajos iniciados",      tIniciados1,   tIniciados2,   p1, p2));
            sb.append(generarTexto("Trabajos finalizados",    tFinalizados1, tFinalizados2, p1, p2));
            sb.append(generarTexto("Clientes nuevos",         cNuevos1,      cNuevos2,      p1, p2));
            sb.append(generarTexto("Cotizaciones registradas",cotReg1,       cotReg2,       p1, p2));
            sb.append(generarTexto("Cotizaciones convertidas",cotConv1,      cotConv2,      p1, p2));
            Area_texto.setText(sb.toString());
            Area_texto.setCaretPosition(0);
        }

        // Forzar revalidación del layout después de cambiar visibilidad
        Panel_central.revalidate();
        Panel_central.repaint();
    }

    private String generarTexto(String metrica, int val1, int val2, String p1, String p2) {
        int diff = val1 - val2;
        if (diff == 0) {
            return "- " + metrica + " se mantuvo igual en ambos periodos con un total de " + val1 + ".\n";
        } else if (diff > 0) {
            String adverb = (diff > 5) ? "considerablemente" : "ligeramente";
            return "- " + metrica + " aumentó " + adverb + " (+" + diff + ") en " + p1 + " respecto a " + p2 + ".\n";
        } else {
            String adverb = (Math.abs(diff) > 5) ? "considerablemente" : "ligeramente";
            return "- " + metrica + " disminuyó " + adverb + " (" + diff + ") en " + p1 + " respecto a " + p2 + ".\n";
        }
    }

    private LocalDate getInicio(String periodo) {
        String[] p = periodo.split("-");
        return LocalDate.of(Integer.parseInt(p[0]), Integer.parseInt(p[1]), 1);
    }

    private LocalDate getFin(LocalDate inicio) {
        return inicio.plusMonths(1).minusDays(1);
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

        Panel_central = new javax.swing.JPanel();
        Scroll_tabla = new javax.swing.JScrollPane();
        Tabla_resultados = new javax.swing.JTable();
        Scroll_texto = new javax.swing.JScrollPane();
        Area_texto = new javax.swing.JTextArea();
        B_cerrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.BorderLayout());

        Panel_central.setPreferredSize(new java.awt.Dimension(550, 500));
        Panel_central.setLayout(new java.awt.GridBagLayout());

        Scroll_tabla.setMinimumSize(new java.awt.Dimension(200, 150));
        Scroll_tabla.setPreferredSize(new java.awt.Dimension(500, 200));

        Tabla_resultados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Estadística", "Periodo 1", "Periodo 2", "Diferencia"
            }
        ));
        Scroll_tabla.setViewportView(Tabla_resultados);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 5, 10);
        Panel_central.add(Scroll_tabla, gridBagConstraints);

        Scroll_texto.setMinimumSize(new java.awt.Dimension(200, 150));
        Scroll_texto.setPreferredSize(new java.awt.Dimension(500, 200));

        Area_texto.setEditable(false);
        Area_texto.setColumns(20);
        Area_texto.setLineWrap(true);
        Area_texto.setRows(5);
        Area_texto.setWrapStyleWord(true);
        Scroll_texto.setViewportView(Area_texto);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 10);
        Panel_central.add(Scroll_texto, gridBagConstraints);

        B_cerrar.setText("Cerrar");
        B_cerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B_cerrarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 10, 10);
        Panel_central.add(B_cerrar, gridBagConstraints);

        getContentPane().add(Panel_central, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void B_cerrarActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

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
            java.util.logging.Logger.getLogger(D_resultados_contraste2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(D_resultados_contraste2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(D_resultados_contraste2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(D_resultados_contraste2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                D_resultados_contraste2 dialog = new D_resultados_contraste2(new javax.swing.JDialog(), true, "2025-01", "2025-02", true, true, false);
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
    private javax.swing.JTextArea Area_texto;
    private javax.swing.JButton B_cerrar;
    private javax.swing.JPanel Panel_central;
    private javax.swing.JScrollPane Scroll_tabla;
    private javax.swing.JScrollPane Scroll_texto;
    private javax.swing.JTable Tabla_resultados;
    // End of variables declaration//GEN-END:variables
}
