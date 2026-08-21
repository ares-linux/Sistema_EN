/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package sistema_en.Ventanas;

import dao.ConfiguracionesDAO;
import sistema_en.Ventanas.Ventanas_consultas.V_historial_c;
import sistema_en.Ventanas.Ventanas_consultas.V_clientes;
import sistema_en.Ventanas.Ventanas_consultas.V_trabajos;
import java.awt.Image;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import sistema_en.Ventanas.dialogos.D_configuraciones;
import sistema_en.Ventanas.dialogos.D_informes;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeParseException;
import javax.swing.JOptionPane;
import raven.toast.Notifications;
import sistema_en.GoogleDriveProvider;

/**
 *
 * @author ares
 */
public class V_menu extends javax.swing.JFrame {
    
    // Formularios de consulta
    private boolean sonido;
    private V_clientes ventana_Clientes = null;
    private V_trabajos ventana_Trabajos = null;
    private V_historial_c ventana_h_cotizaciones = null;
    
    //Formularios de registro
    
    private V_R_Trabajo ventana_r_trabajos = null;
    private V_R_Herramienta ventana_r_herramientas = null;
    private V_R_Empleado ventana_r_empleados = null;
    
    private V_R_Cotizacion ventana_r_cotizaciones = null;
    
    LocalDate fecha_hoy = LocalDate.now();
    long dias;
    long diasTranscurridos;
    boolean cierre;
    boolean preguntar;
    
    String frecuencia1;
    String manual1;
    String preguntar1;
    String cotizaciones1;
    String carpeta;
    String base_c1;
                            
     /**
     * Creates new form V_menu
     */
    public V_menu() {
        initComponents();
        
        // --- CONTROL DE FLECHAS PARA EL MENÚ CATÁLOGO ---
        m_catalogo.addMenuListener(new javax.swing.event.MenuListener() {
            @Override
            public void menuSelected(javax.swing.event.MenuEvent e) {
                m_catalogo.setText("Catálogo ▲"); // Sube la flecha al abrirse
            }

            @Override
            public void menuDeselected(javax.swing.event.MenuEvent e) {
                m_catalogo.setText("Catálogo ▼"); // Baja la flecha al cerrarse
            }

            @Override
            public void menuCanceled(javax.swing.event.MenuEvent e) {
                m_catalogo.setText("Catálogo ▼");
            }
        });

        // --- CONTROL DE FLECHAS PARA EL MENÚ REGISTROS ---
        m_registros.addMenuListener(new javax.swing.event.MenuListener() {
            @Override
            public void menuSelected(javax.swing.event.MenuEvent e) {
                m_registros.setText("Registros ▲"); // Sube la flecha al abrirse
            }

            @Override
            public void menuDeselected(javax.swing.event.MenuEvent e) {
                m_registros.setText("Registros ▼"); // Baja la flecha al cerrarse
            }

            @Override
            public void menuCanceled(javax.swing.event.MenuEvent e) {
                m_registros.setText("Registros ▼");
            }
        });
        
        // --- CONTROL DE FLECHAS PARA EL MENÚ REGISTROS ---
        
        m_sistema.addMenuListener(new javax.swing.event.MenuListener() {
            @Override
            public void menuSelected(javax.swing.event.MenuEvent e) {
                m_sistema.setText("Sistema ▲"); // Sube la flecha al abrirse
            }

            @Override
            public void menuDeselected(javax.swing.event.MenuEvent e) {
                m_sistema.setText("Sistema ▼"); // Baja la flecha al cerrarse
            }

            @Override
            public void menuCanceled(javax.swing.event.MenuEvent e) {
                m_sistema.setText("Sistema ▼");
            }
        });
        
        this.addWindowListener(new java.awt.event.WindowAdapter() {
        @Override
        public void windowClosing(java.awt.event.WindowEvent e) {   
        M_cerrar_programa.doClick();
        
    }
    });

    this.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
    
    ConfiguracionesDAO dao = new ConfiguracionesDAO();
    List<String[]> configs = dao.obtenerConfiguraciones();
    
    for (String[] conf : configs) {

    String clave = conf[1];
    String valor = conf[2];

    switch (clave) {

        case "preguntar":
            preguntar1 = valor;
            preguntar = Boolean.parseBoolean(valor);
            break;

        case "manual":
            manual1 = valor;
            break;

        case "cotizaciones":
            cotizaciones1 = valor;
            break;
        
        case "sonido_error":
            if ("true".equals(valor)){
                sonido = true;
            }
            else if ("false".equals(valor)){
                sonido = false;
            }
            break;   
            
        case "base_c":
            base_c1 = valor;
            System.out.println(base_c1);
            break;
                    
        case "ultima_copia":
                if (valor != null && !valor.isEmpty() && !valor.equals("null")) {
                    try { 
                        
                        LocalDate fechaGuardada = LocalDate.parse(valor);
                        
                        // 3. Calculamos la diferencia en días
                        diasTranscurridos = ChronoUnit.DAYS.between(fechaGuardada, fecha_hoy);
                        
                        // 4. Comparamos para tomar decisiones
                        if (diasTranscurridos == 0) {
                            System.out.println("La copia está al día (se hizo hoy).");
                            System.out.println(diasTranscurridos);
                        } else if (diasTranscurridos > 0) {
                            System.out.println("Han pasado " + diasTranscurridos + " días desde la última copia.");
                            
                            // Ejemplo: Si pasaron más de 7 días, podrías mostrar una alerta
                            if (diasTranscurridos >= 7) {
                                // javax.swing.JOptionPane.showMessageDialog(null, "¡Hace más de una semana que no haces copia!");
                            }
                        }
                    } catch (DateTimeParseException e) {
                        // En caso de que la fecha guardada tenga un formato extraño o incorrecto
                        System.out.println("Error al leer la fecha de la última copia: " + valor);
                    }
                }
                break;
                
        case "frecuencia":
            if ("diaria".equals(valor)){
                dias = 1;
                frecuencia1 = valor;
            }
            else if ("semanal".equals(valor)){
                dias = 7;
                frecuencia1 = valor;
            }   
            else if("cada cierre".equals(valor)){
                cierre = true;
                frecuencia1 = valor;
            }
            break;
    }
    }
    
        if (!cierre && diasTranscurridos >= dias) {
            if (preguntar) {
                int respuesta = JOptionPane.showConfirmDialog( this,"¿Desea realizar una copia de seguridad?","Copia de seguridad",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                if (respuesta == JOptionPane.YES_OPTION) {
                    realizar_respaldo();
                }
            } else {
                realizar_respaldo();
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
        java.awt.GridBagConstraints gridBagConstraints;

        jMenuItem1 = new javax.swing.JMenuItem();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        jPopupMenu3 = new javax.swing.JPopupMenu();
        jDialog1 = new javax.swing.JDialog();
        jMenuItem2 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));
        l_foto = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        m_registros = new javax.swing.JMenu();
        m_empleado = new javax.swing.JMenuItem();
        m_herramienta = new javax.swing.JMenuItem();
        m_trabajo = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        m_cotizacion = new javax.swing.JMenuItem();
        m_cotizacion_c = new javax.swing.JMenuItem();
        m_catalogo = new javax.swing.JMenu();
        m_clientes = new javax.swing.JMenuItem();
        m_trabajos = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        m_sistema = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        M_cerrar_programa = new javax.swing.JMenuItem();

        jMenuItem1.setText("jMenuItem1");

        jMenuItem2.setText("jMenuItem2");

        jMenuItem3.setText("jMenuItem3");

        jMenuItem4.setText("jMenuItem4");

        jMenu3.setText("jMenu3");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 44;
        gridBagConstraints.gridheight = 26;
        getContentPane().add(filler1, gridBagConstraints);

        l_foto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        l_foto.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        l_foto.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 601;
        gridBagConstraints.ipady = 366;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(25, 25, 25, 25);
        getContentPane().add(l_foto, gridBagConstraints);

        jMenuBar1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        m_registros.setText("Registros ▼");
        m_registros.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                m_registrosFocusGained(evt);
            }
        });
        m_registros.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                m_registrosKeyPressed(evt);
            }
        });

        m_empleado.setText("Registrar empleado");
        m_empleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_empleadoActionPerformed(evt);
            }
        });
        m_registros.add(m_empleado);

        m_herramienta.setText("Registrar herramienta");
        m_herramienta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_herramientaActionPerformed(evt);
            }
        });
        m_registros.add(m_herramienta);

        m_trabajo.setText("Registrar trabajo");
        m_trabajo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_trabajoActionPerformed(evt);
            }
        });
        m_registros.add(m_trabajo);

        jMenu1.setText("Cotizaciones");

        m_cotizacion.setText("Cotizacion de prueba");
        m_cotizacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_cotizacionActionPerformed(evt);
            }
        });
        jMenu1.add(m_cotizacion);

        m_cotizacion_c.setText("Historial ");
        m_cotizacion_c.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_cotizacion_cActionPerformed(evt);
            }
        });
        jMenu1.add(m_cotizacion_c);

        m_registros.add(jMenu1);

        jMenuBar1.add(m_registros);

        m_catalogo.setText("Catálogo ▼");

        m_clientes.setText("Ver clientes");
        m_clientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_clientesActionPerformed(evt);
            }
        });
        m_catalogo.add(m_clientes);

        m_trabajos.setText("Ver trabajos");
        m_trabajos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_trabajosActionPerformed(evt);
            }
        });
        m_catalogo.add(m_trabajos);

        jMenuItem6.setText("Informes");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        m_catalogo.add(jMenuItem6);

        jMenuBar1.add(m_catalogo);

        m_sistema.setText("Sistema ▼");

        jMenuItem5.setText("Configuraciones");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        m_sistema.add(jMenuItem5);

        M_cerrar_programa.setText("Salir del programa");
        M_cerrar_programa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                M_cerrar_programaActionPerformed(evt);
            }
        });
        m_sistema.add(M_cerrar_programa);

        jMenuBar1.add(m_sistema);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    public void verificar_conexion(){
     if (!GoogleDriveProvider.estaConectado()) {
            String mensaje;
            mensaje = "No se encontró ninguna cuenta de Google vinculada. Puedes conectarla en Configuración > Copias de seguridad. ";
            Notifications.getInstance().show( Notifications.Type.INFO, Notifications.Location.TOP_RIGHT, mensaje);
        }
    }
    
    private void realizar_respaldo(){
        
        String ruta = Paths.get(base_c1, "sistema_EN").toString();
        
        boolean exito = GoogleDriveProvider.subirArchivo(ruta);
        if (exito){
            String ultima_copia = LocalDate.now().toString();
            ConfiguracionesDAO dao = new ConfiguracionesDAO();
            dao.actualizarCopias(frecuencia1, preguntar1, manual1, cotizaciones1, ultima_copia,base_c1);
            System.out.println("la copia de seguridad funciona");
            
            String mensaje;
            mensaje = "La copia de seguridad se creo con exito";
            Notifications.getInstance().show( Notifications.Type.SUCCESS, Notifications.Location.TOP_LEFT, mensaje);
            
            
        }else{
            JOptionPane.showMessageDialog(this, "la copia de seguridad fallo, revise su conexion a internet."); 
            System.out.println("supuestamente fallo");
        }
    }
    
    public void revisar_sonido(){
        ConfiguracionesDAO dao = new ConfiguracionesDAO();
        List<String[]> configs = dao.obtenerConfiguraciones();
    
        for (String[] conf : configs) {

        String clave = conf[1];
        String valor = conf[2];

        switch (clave) {

            case "sonido_error":
                if ("true".equals(valor)){
                    sonido = true;
                }
                else if ("false".equals(valor)){
                    sonido = false;
                }
                break;
            }
        }
    }
    
    public class r_sonido {

    public static void reproducir(String ruta) {
        try {
        
            URL url =
                    r_sonido.class.getResource(ruta);
    
            if (url == null) {
                System.out.println(
                      "No se encontró el audio: " + ruta);
                return;
            }
            AudioInputStream audio =
                    AudioSystem.getAudioInputStream(url);

            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
    
    public void cargar_foto(){
        int ancho = l_foto.getWidth();
        int alto = l_foto.getHeight();
        
        if (ancho <= 0) ancho = 600;
        if (alto <= 0) alto = 400;

        try {
            ImageIcon iconoOriginal = new ImageIcon( getClass().getResource("/Imagenes/logo.png"));

            Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            l_foto.setIcon(new ImageIcon(imagenEscalada));

        if (iconoOriginal.getImageLoadStatus() != java.awt.MediaTracker.COMPLETE) {
            System.out.println("¡Error! No se pudo encontrar o cargar la imagen del menu");
            }
        } catch (Exception e) {
            System.out.println("Error al procesar la imagen: " + e.getMessage());
        }
    
    }
    
    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        cargar_foto();
        verificar_conexion();
        GoogleDriveProvider.sincronizarTrabajosPendientes();
        //r_sonido.reproducir("/audios/inicio.wav");
    }//GEN-LAST:event_formWindowOpened

    private void M_cerrar_programaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_M_cerrar_programaActionPerformed
        revisar_sonido();
        if(sonido == true){
            V_menu.r_sonido.reproducir("/audios/error.wav");
        }
 
        int respuesta = javax.swing.JOptionPane.showConfirmDialog(this, "¿Desea cerrar el programa?", "Confirmar salir", javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE);
        if (respuesta == javax.swing.JOptionPane.YES_OPTION){
            if (cierre == true){
                if (preguntar == true){
                    int respuesta1 = javax.swing.JOptionPane.showConfirmDialog(this, "¿Desea realizar una copia de seguridad antes de salir?", "Copia de seguridad", javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE);
                    if (respuesta1 == javax.swing.JOptionPane.YES_OPTION){
                        realizar_respaldo();
                    }
                }
                else{
                    realizar_respaldo();
                }
            }
            System.exit(0);
        }
        else{
            return;
            //this.toBack();
        }
    }//GEN-LAST:event_M_cerrar_programaActionPerformed

    private void m_herramientaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_herramientaActionPerformed
        if (ventana_r_herramientas == null || !ventana_r_herramientas.isDisplayable()) {
            ventana_r_herramientas = new V_R_Herramienta();
            ventana_r_herramientas.setExtendedState(JFrame.MAXIMIZED_BOTH);
            ventana_r_herramientas.setVisible(true);
            ventana_r_herramientas.toFront();
            ventana_r_herramientas.requestFocus();
        } else {
            if (ventana_r_herramientas.getExtendedState() == JFrame.ICONIFIED) {
                ventana_r_herramientas.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
            ventana_r_herramientas.toFront();
            ventana_r_herramientas.requestFocus();
        }
        
    }//GEN-LAST:event_m_herramientaActionPerformed

    private void m_empleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_empleadoActionPerformed
        if (ventana_r_empleados == null || !ventana_r_empleados.isDisplayable()) {
            ventana_r_empleados = new V_R_Empleado();
            ventana_r_empleados.setExtendedState(JFrame.MAXIMIZED_BOTH);
            ventana_r_empleados.setVisible(true);
            ventana_r_empleados.toFront();
            ventana_r_empleados.requestFocus();
        } else {
            if (ventana_r_empleados.getExtendedState() == JFrame.ICONIFIED) {
                ventana_r_empleados.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
            ventana_r_empleados.toFront();
            ventana_r_empleados.requestFocus();
        }
        
    }//GEN-LAST:event_m_empleadoActionPerformed

    private void m_trabajoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_trabajoActionPerformed
        if (ventana_r_trabajos == null || !ventana_r_trabajos.isDisplayable()) {
            ventana_r_trabajos = new V_R_Trabajo();
            ventana_r_trabajos.setExtendedState(JFrame.MAXIMIZED_BOTH);
            ventana_r_trabajos.setVisible(true);
            ventana_r_trabajos.toFront();
            ventana_r_trabajos.requestFocus();
        } else {
            if (ventana_r_trabajos.getExtendedState() == JFrame.ICONIFIED) {
                ventana_r_trabajos.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
            ventana_r_trabajos.toFront();
            ventana_r_trabajos.requestFocus();
        }
    }//GEN-LAST:event_m_trabajoActionPerformed

    private void m_trabajosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_trabajosActionPerformed
        if (ventana_Trabajos == null || !ventana_Trabajos.isDisplayable()) {
            ventana_Trabajos = new V_trabajos();
            ventana_Trabajos.setExtendedState(JFrame.MAXIMIZED_BOTH);
            ventana_Trabajos.setVisible(true);
            ventana_Trabajos.toFront();
            ventana_Trabajos.requestFocus();
        } else {
            if (ventana_Trabajos.getExtendedState() == JFrame.ICONIFIED) {
                ventana_Trabajos.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
            ventana_Trabajos.toFront();
            ventana_Trabajos.requestFocus();
        }
        
    }//GEN-LAST:event_m_trabajosActionPerformed

    private void m_clientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_clientesActionPerformed
        if (ventana_Clientes == null || !ventana_Clientes.isDisplayable()) {
            ventana_Clientes = new V_clientes();
            ventana_Clientes.setExtendedState(JFrame.MAXIMIZED_BOTH);
            ventana_Clientes.setVisible(true);
            ventana_Clientes.toFront();
            ventana_Clientes.requestFocus();
        } else {
            if (ventana_Clientes.getExtendedState() == JFrame.ICONIFIED) {
                ventana_Clientes.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
            ventana_Clientes.toFront();
            ventana_Clientes.requestFocus();
        }
    }//GEN-LAST:event_m_clientesActionPerformed

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        cargar_foto();
    }//GEN-LAST:event_formComponentResized

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        D_configuraciones configuraciones = new D_configuraciones(this,true);
        configuraciones.setLocationRelativeTo(V_menu.this);
        configuraciones.setVisible(true);  
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void m_registrosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_m_registrosKeyPressed
    
    }//GEN-LAST:event_m_registrosKeyPressed

    private void m_registrosFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_m_registrosFocusGained
    
    }//GEN-LAST:event_m_registrosFocusGained

    private void m_cotizacion_cActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_cotizacion_cActionPerformed
        if (ventana_h_cotizaciones == null || !ventana_h_cotizaciones.isDisplayable()) {
            ventana_h_cotizaciones = new V_historial_c();
            ventana_h_cotizaciones.setExtendedState(JFrame.MAXIMIZED_BOTH);
            ventana_h_cotizaciones.setVisible(true);
            ventana_h_cotizaciones.toFront();
            ventana_h_cotizaciones.requestFocus();
        } else {
            if (ventana_h_cotizaciones.getExtendedState() == JFrame.ICONIFIED) {
                ventana_h_cotizaciones.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
            ventana_h_cotizaciones.toFront();
            ventana_h_cotizaciones.requestFocus();
        }
    }//GEN-LAST:event_m_cotizacion_cActionPerformed

    private void m_cotizacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_cotizacionActionPerformed
        if (ventana_r_cotizaciones == null || !ventana_r_cotizaciones.isDisplayable()) {
            ventana_r_cotizaciones = new V_R_Cotizacion();
            ventana_r_cotizaciones.setExtendedState(JFrame.MAXIMIZED_BOTH);
            ventana_r_cotizaciones.cotizacion_prueba();
            ventana_r_cotizaciones.setVisible(true);
            ventana_r_cotizaciones.toFront();
            ventana_r_cotizaciones.requestFocus();
        } else {
            if (ventana_r_cotizaciones.getExtendedState() == JFrame.ICONIFIED) {
                ventana_r_cotizaciones.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
            ventana_r_cotizaciones.toFront();
            ventana_r_cotizaciones.requestFocus();
        }
        
    }//GEN-LAST:event_m_cotizacionActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        D_informes informes = new D_informes(this,true);
        informes.setLocationRelativeTo(V_menu.this);
        informes.setVisible(true); 
    }//GEN-LAST:event_jMenuItem6ActionPerformed

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
            java.util.logging.Logger.getLogger(V_menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(V_menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(V_menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(V_menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new V_menu().setVisible(true);     }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem M_cerrar_programa;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JPopupMenu jPopupMenu3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel l_foto;
    private javax.swing.JMenu m_catalogo;
    private javax.swing.JMenuItem m_clientes;
    private javax.swing.JMenuItem m_cotizacion;
    private javax.swing.JMenuItem m_cotizacion_c;
    private javax.swing.JMenuItem m_empleado;
    private javax.swing.JMenuItem m_herramienta;
    private javax.swing.JMenu m_registros;
    private javax.swing.JMenu m_sistema;
    private javax.swing.JMenuItem m_trabajo;
    private javax.swing.JMenuItem m_trabajos;
    // End of variables declaration//GEN-END:variables
}

