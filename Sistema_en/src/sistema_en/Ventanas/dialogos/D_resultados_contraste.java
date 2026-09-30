/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package sistema_en.Ventanas.dialogos;

import dao.InformesDAO;
import java.awt.GridLayout;
import java.time.LocalDate;
import javax.swing.table.DefaultTableModel;

/**
 * Muestra los resultados de contraste entre dos periodos.
 * El layout base (tabla + área de texto) es editable en el Designer.
 * La visibilidad de cada panel se ajusta en el constructor según los parámetros.
 *
 * @author ares
 */
public class D_resultados_contraste extends javax.swing.JDialog {

    /**
     * @param parent    ventana padre
     * @param modal     true = modal
     * @param p1        periodo 1 (formato "yyyy-MM")
     * @param p2        periodo 2 (formato "yyyy-MM")
     * @param numerico  mostrar tabla de números
     * @param textual   mostrar resumen de texto
     * @param mixto     mostrar ambos paneles
     */
    public D_resultados_contraste(java.awt.Dialog parent, boolean modal,
            String p1, String p2,
            boolean numerico, boolean textual, boolean mixto) {
        super(parent, modal);
        initComponents();

        setTitle("Resultados del Contraste (" + p1 + " vs " + p2 + ")");
        setLocationRelativeTo(parent);

        cargarDatos(p1, p2, numerico, textual, mixto);
    }

    /**
     * Carga los datos desde la BD y ajusta la visibilidad de los paneles
     * según los parámetros recibidos.
     */
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
        scrollTabla.setVisible(mostrarTabla);
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
            tablaResultados.setModel(new DefaultTableModel(datos, columnas) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });
        }

        // --- Texto resumen ---
        boolean mostrarTexto = textual || mixto;
        scrollTexto.setVisible(mostrarTexto);
        if (mostrarTexto) {
            StringBuilder sb = new StringBuilder("Resumen Textual del Contraste:\n\n");
            sb.append(generarTexto("Trabajos registrados",    tRegistrados1, tRegistrados2, p1, p2));
            sb.append(generarTexto("Trabajos iniciados",      tIniciados1,   tIniciados2,   p1, p2));
            sb.append(generarTexto("Trabajos finalizados",    tFinalizados1, tFinalizados2, p1, p2));
            sb.append(generarTexto("Clientes nuevos",         cNuevos1,      cNuevos2,      p1, p2));
            sb.append(generarTexto("Cotizaciones registradas",cotReg1,       cotReg2,       p1, p2));
            sb.append(generarTexto("Cotizaciones convertidas",cotConv1,      cotConv2,      p1, p2));
            areaTexto.setText(sb.toString());
            areaTexto.setCaretPosition(0);
        }

        // Ajustar filas del grid según qué paneles son visibles
        int filas = (mostrarTabla ? 1 : 0) + (mostrarTexto ? 1 : 0);
        panelCentral.setLayout(new GridLayout(Math.max(1, filas), 1));
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

        panelCentral = new javax.swing.JPanel();
        scrollTabla = new javax.swing.JScrollPane();
        tablaResultados = new javax.swing.JTable();
        scrollTexto = new javax.swing.JScrollPane();
        areaTexto = new javax.swing.JTextArea();
        panelSur = new javax.swing.JPanel();
        btnCerrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Resultados del Contraste");
        setMinimumSize(new java.awt.Dimension(800, 600));

        panelCentral.setLayout(new java.awt.GridLayout(2, 1));

        tablaResultados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Estadística", "Periodo 1", "Periodo 2", "Diferencia"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollTabla.setViewportView(tablaResultados);

        panelCentral.add(scrollTabla);

        areaTexto.setEditable(false);
        areaTexto.setColumns(20);
        areaTexto.setLineWrap(true);
        areaTexto.setRows(5);
        areaTexto.setText("Resumen Textual del Contraste:");
        areaTexto.setWrapStyleWord(true);
        scrollTexto.setViewportView(areaTexto);

        panelCentral.add(scrollTexto);

        getContentPane().add(panelCentral, java.awt.BorderLayout.CENTER);

        btnCerrar.setText("Cerrar");
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });
        panelSur.add(btnCerrar);

        getContentPane().add(panelSur, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea areaTexto;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JPanel panelCentral;
    private javax.swing.JPanel panelSur;
    private javax.swing.JScrollPane scrollTabla;
    private javax.swing.JScrollPane scrollTexto;
    private javax.swing.JTable tablaResultados;
    // End of variables declaration//GEN-END:variables
}
