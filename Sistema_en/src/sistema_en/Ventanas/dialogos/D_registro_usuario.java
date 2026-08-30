/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package sistema_en.Ventanas.dialogos;

import dao.UsuariosDAO;
import javax.swing.JOptionPane;

/**
 *
 * @author ares
 */
public class D_registro_usuario extends javax.swing.JDialog {

    private boolean registrado = false;

    /**
     * Creates new form D_registro_usuario
     */
    public D_registro_usuario(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(parent);
    }

    public boolean isRegistrado() {
        return registrado;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        t_usuario = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        t_contraseña = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        t_confirmar = new javax.swing.JPasswordField();
        b_registrar = new javax.swing.JButton();
        b_cancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Registro inicial de usuario");

        jLabel1.setText("Usuario");

        jLabel2.setText("Contraseña");

        jLabel3.setText("Confirmar contraseña");

        b_registrar.setText("Registrar");
        b_registrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_registrarActionPerformed(evt);
            }
        });

        b_cancelar.setText("Salir");
        b_cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_cancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(t_usuario, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                    .addComponent(t_contraseña, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                    .addComponent(t_confirmar, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(b_registrar)
                .addGap(18, 18, 18)
                .addComponent(b_cancelar)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(t_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(t_contraseña, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(t_confirmar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_registrar)
                    .addComponent(b_cancelar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void b_registrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_registrarActionPerformed
        String usuario = t_usuario.getText().trim();
        String contra = new String(t_contraseña.getPassword());
        String confirma = new String(t_confirmar.getPassword());

        if (usuario.isEmpty() || contra.isEmpty() || confirma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Registro", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!contra.equals(confirma)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Registro", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (contra.length() < 4) {
            JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 4 caracteres.", "Registro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        UsuariosDAO dao = new UsuariosDAO();
        if (dao.insertarUsuario(usuario, contra)) {
            JOptionPane.showMessageDialog(this, "Usuario registrado con éxito. Ya puede iniciar sesión.", "Registro", JOptionPane.INFORMATION_MESSAGE);
            registrado = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo registrar el usuario. Verifique que no exista.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_b_registrarActionPerformed

    private void b_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_cancelarActionPerformed
        registrado = false;
        dispose();
    }//GEN-LAST:event_b_cancelarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_cancelar;
    private javax.swing.JButton b_registrar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPasswordField t_confirmar;
    private javax.swing.JPasswordField t_contraseña;
    private javax.swing.JTextField t_usuario;
    // End of variables declaration//GEN-END:variables
}
