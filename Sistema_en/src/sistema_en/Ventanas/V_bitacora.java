/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package sistema_en.Ventanas;

import java.awt.Image;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import sistema_en.Objetos.Bitacora;
import java.time.LocalDate;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

/**
 *
 * @author zahid
 */
public class V_bitacora extends javax.swing.JFrame {
    
    Bitacora bitacora = new Bitacora();
    int fila;
    
    // =========================================================================
    // [CONFIGURACIÓN JTREE] - Propiedades y Listas para la gestión de la Bitácora
    // =========================================================================
    // listaCompleta: Fuente de verdad que almacena todos los registros de la bitácora ingresados.
    private java.util.List<Bitacora> listaCompleta = new java.util.ArrayList<>();
    // listaFiltrada: Subconjunto de registros actualmente mostrados en la tabla según el filtro del JTree.
    private java.util.List<Bitacora> listaFiltrada = new java.util.ArrayList<>();

    // =========================================================================
    // [CONFIGURACIÓN JTREE] - Wrapper para personalizar los nodos hoja del JTree
    // =========================================================================
    public static class BitacoraNodeWrapper {
        private final Bitacora bitacoraWrapper;
        public BitacoraNodeWrapper(Bitacora b) {
            this.bitacoraWrapper = b;
        }
        public Bitacora getBitacora() {
            return bitacoraWrapper;
        }
        // toString(): Define la etiqueta visible del registro en el árbol (Plomero y horas)
        @Override
        public String toString() {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("hh:mm a");
            String entrada = bitacoraWrapper.getHora_llegada() != null ? bitacoraWrapper.getHora_llegada().format(format) : "";
            String salida = bitacoraWrapper.getHora_salida() != null ? bitacoraWrapper.getHora_salida().format(format) : "";
            return bitacoraWrapper.getTrabajador() + " (" + entrada + " - " + salida + ")";
        }
    }

    // =========================================================================
    // [CONFIGURACIÓN JTREE] - Métodos de Inicialización, Actualización y Filtrado
    // =========================================================================
    
    // inicializarArbol(): Configura el nodo raíz del JTree y registra el listener de eventos de selección
    public void inicializarArbol() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Bitacora");
        a_bitacora.setModel(new DefaultTreeModel(root));
        
        a_bitacora.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                refrescarTablaSegunSeleccionArbol();
            }
        });
    }
    
    // actualizarArbol(): Agrupa los registros de listaCompleta por fecha y reconstruye el JTree
    public void actualizarArbol() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Bitacora");
        
        // Agrupar los registros usando un TreeMap para ordenar las fechas cronológicamente
        java.util.Map<LocalDate, java.util.List<Bitacora>> agrupados = new java.util.TreeMap<>();
        for (Bitacora b : listaCompleta) {
            if (b.getFecha() != null) {
                agrupados.computeIfAbsent(b.getFecha(), k -> new java.util.ArrayList<>()).add(b);
            }
        }
        
        // Construir nodos intermedios (Fechas) y nodos hoja (Registros individuales)
        for (java.util.Map.Entry<LocalDate, java.util.List<Bitacora>> entrada : agrupados.entrySet()) {
            DefaultMutableTreeNode dateNode = new DefaultMutableTreeNode(entrada.getKey());
            for (Bitacora b : entrada.getValue()) {
                DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(new BitacoraNodeWrapper(b));
                dateNode.add(leaf);
            }
            root.add(dateNode);
        }
        
        a_bitacora.setModel(new DefaultTreeModel(root));
        
        // Expandir automáticamente todas las ramas del JTree
        for (int i = 0; i < a_bitacora.getRowCount(); i++) {
            a_bitacora.expandRow(i);
        }
    }
    
    // filtrarTablaPorFecha(): Llena la tabla y listaFiltrada solo con los registros de la fecha especificada
    private void filtrarTablaPorFecha(LocalDate fecha) {
        DefaultTableModel modelo = (DefaultTableModel) Tabla_bitacora.getModel();
        modelo.setRowCount(0);
        listaFiltrada.clear();
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("hh:mm a");
        for (Bitacora b : listaCompleta) {
            if (b.getFecha() != null && b.getFecha().equals(fecha)) {
                listaFiltrada.add(b);
                Object[] info = new Object[5];
                info[0] = b.getTrabajador();
                info[1] = b.getFecha().toString();
                info[2] = b.getHora_llegada() != null ? b.getHora_llegada().format(formateador) : "";
                info[3] = b.getHora_salida() != null ? b.getHora_salida().format(formateador) : "";
                info[4] = b.getEvidencia();
                modelo.addRow(info);
            }
        }
    }
    
    // mostrarTodosEnTabla(): Llena la tabla con la totalidad de registros almacenados en listaCompleta
    private void mostrarTodosEnTabla() {
        DefaultTableModel modelo = (DefaultTableModel) Tabla_bitacora.getModel();
        modelo.setRowCount(0);
        listaFiltrada.clear();
        listaFiltrada.addAll(listaCompleta);
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("hh:mm a");
        for (Bitacora b : listaFiltrada) {
            Object[] info = new Object[5];
            info[0] = b.getTrabajador();
            info[1] = b.getFecha().toString();
            info[2] = b.getHora_llegada() != null ? b.getHora_llegada().format(formateador) : "";
            info[3] = b.getHora_salida() != null ? b.getHora_salida().format(formateador) : "";
            info[4] = b.getEvidencia();
            modelo.addRow(info);
        }
    }
    
    // refrescarTablaSegunSeleccionArbol(): Reacciona a la selección de nodos en el JTree y actualiza la tabla/formulario
    private void refrescarTablaSegunSeleccionArbol() {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) a_bitacora.getLastSelectedPathComponent();
        if (selectedNode == null || selectedNode.isRoot()) {
            mostrarTodosEnTabla();
        } else if (selectedNode.getUserObject() instanceof LocalDate) {
            filtrarTablaPorFecha((LocalDate) selectedNode.getUserObject());
        } else if (selectedNode.getUserObject() instanceof BitacoraNodeWrapper) {
            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();
            LocalDate fecha = (LocalDate) parentNode.getUserObject();
            filtrarTablaPorFecha(fecha);
            
            Bitacora target = ((BitacoraNodeWrapper) selectedNode.getUserObject()).getBitacora();
            for (int i = 0; i < listaFiltrada.size(); i++) {
                if (listaFiltrada.get(i) == target) {
                    Tabla_bitacora.setRowSelectionInterval(i, i);
                    fila = i;
                    cargar_datos_tabla(i);
                    break;
                }
            }
        }
    }

    /**
     * Creates new form V_bitacora
     */
    public V_bitacora() {
        initComponents();
        inicializarArbol();
        actualizarArbol();
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

        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        t_id = new javax.swing.JTextField();
        t_fecha = new javax.swing.JTextField();
        c_plomero = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        t_evidencia = new javax.swing.JTextField();
        b_foto = new javax.swing.JButton();
        b_nuevo = new javax.swing.JButton();
        b_eliminar = new javax.swing.JButton();
        b_editar = new javax.swing.JButton();
        l_evidencia = new javax.swing.JLabel();
        b_buscar = new javax.swing.JButton();
        time_hora_llegada = new com.github.lgooddatepicker.components.TimePicker();
        time_hora_salida = new com.github.lgooddatepicker.components.TimePicker();
        jButton1 = new javax.swing.JButton();
        b_cancelar = new javax.swing.JButton();
        jScrollPaneArbol = new javax.swing.JScrollPane();
        a_bitacora = new javax.swing.JTree();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabla_bitacora = new javax.swing.JTable();

        jLabel5.setText("Fecha");

        jLabel4.setText("Registrar pago");

        jLabel6.setText("Hora");

        jLabel7.setText("Nombre del cliente");

        jLabel8.setText("Monto");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Bitacora de asistencia");
        jLabel1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        jLabel2.setText("Trabajo");

        jLabel3.setText("Fecha");

        t_id.setEditable(false);

        t_fecha.setEditable(false);

        c_plomero.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "PLO_00", "PLO_01", "PLO_02", "PLO_03", "PLO_04", "PLO_05", "PLO_06", "PLO_07", "PLO_08", "PLO_09", "Administrador" }));
        c_plomero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c_plomeroActionPerformed(evt);
            }
        });

        jLabel9.setText("Seleccionar plomero:");

        jLabel10.setText("Hora de entrada");

        jLabel11.setText("Hora de salida");

        jLabel12.setText("Ruta de evidencia");

        t_evidencia.setEditable(false);

        b_foto.setText("...");
        b_foto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_fotoActionPerformed(evt);
            }
        });

        b_nuevo.setText("Nuevo");
        b_nuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_nuevoActionPerformed(evt);
            }
        });

        b_eliminar.setText("Eliminar");
        b_eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_eliminarActionPerformed(evt);
            }
        });

        b_editar.setText("Editar");
        b_editar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_editarActionPerformed(evt);
            }
        });

        l_evidencia.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        b_buscar.setText("Buscar");

        jButton1.setText("Cerrar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        b_cancelar.setText("Cancelar");
        b_cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_cancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel9))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel10)
                        .addGap(68, 68, 68)
                        .addComponent(jLabel11))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(120, 120, 120)
                        .addComponent(jLabel12))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(t_evidencia)
                        .addGap(10, 10, 10)
                        .addComponent(b_foto))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(b_nuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(c_plomero, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(time_hora_llegada, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                                .addGap(14, 14, 14)
                                .addComponent(time_hora_salida, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)))
                        .addGap(1, 1, 1)))
                .addGap(39, 39, 39)
                .addComponent(l_evidencia, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(t_id, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
                    .addComponent(t_fecha)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(70, 70, 70)
                                .addComponent(jLabel2))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(80, 80, 80)
                                .addComponent(jLabel3))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(b_buscar)
                                    .addComponent(jButton1))
                                .addGap(19, 19, 19)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(b_cancelar)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(b_eliminar)
                                        .addGap(22, 22, 22)
                                        .addComponent(b_editar)))))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGap(40, 40, 40)
                .addComponent(jLabel9)
                .addGap(13, 13, 13)
                .addComponent(c_plomero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11))
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(time_hora_llegada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(time_hora_salida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addComponent(jLabel12)
                .addGap(3, 3, 3)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(t_evidencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_foto))
                .addGap(25, 25, 25)
                .addComponent(b_nuevo))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(l_evidencia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(5, 5, 5))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel2)
                .addGap(3, 3, 3)
                .addComponent(t_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(jLabel3)
                .addGap(13, 13, 13)
                .addComponent(t_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(85, 85, 85)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(b_buscar)
                    .addComponent(b_eliminar)
                    .addComponent(b_editar))
                .addGap(45, 45, 45)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(b_cancelar)))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 65;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(jPanel1, gridBagConstraints);

        jScrollPaneArbol.setViewportView(a_bitacora);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 230;
        gridBagConstraints.ipady = 150;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(25, 50, 23, 0);
        getContentPane().add(jScrollPaneArbol, gridBagConstraints);

        Tabla_bitacora.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Plomero", "Fecha", "Hora de entrada", "Hora de salida", "Evidencia"
            }
        ));
        Tabla_bitacora.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Tabla_bitacoraMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(Tabla_bitacora);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 640;
        gridBagConstraints.ipady = 146;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(25, 20, 23, 18);
        getContentPane().add(jScrollPane1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    
    public void bloquear_text(){
        c_plomero.setEnabled(false);
        time_hora_llegada.setEnabled(false);
        time_hora_salida.setEnabled(false);
    }
    
    public void desbloquear_text(){
        c_plomero.setEnabled(true);
        time_hora_llegada.setEnabled(true);
        time_hora_salida.setEnabled(true);
    }
    
    public void bloquear_botones(){
        b_eliminar.setEnabled(false);
        b_editar.setEnabled(false);
        b_buscar.setEnabled(false);
        //b_nuevo.setEnabled(false);
        //b_guardar.setEnabled(false);
    }
    
    public void desbloquear_botones(){
        b_eliminar.setEnabled(true);
        b_editar.setEnabled(true);
        b_buscar.setEnabled(true);
        b_nuevo.setEnabled(true);
        //b_guardar.setEnabled(true);
    }
    
    public void fecha(){
        t_fecha.setText(java.time.LocalDate.now().toString());
    }
    
    public void limpiar_text(){
        t_fecha.setText(java.time.LocalDate.now().toString());
        t_evidencia.setText("");
        time_hora_llegada.setTime(null);
        time_hora_salida.setTime(null);
    }
    
    public void llenar_tabla(){
        if (time_hora_llegada.getTime() == null || time_hora_salida.getTime() == null) {
            return;
        }
        
        Bitacora b = new Bitacora();
        b.setTrabajador(c_plomero.getSelectedItem().toString());
        b.setFecha(java.time.LocalDate.parse(t_fecha.getText()));
        b.setHora_llegada(time_hora_llegada.getTime());
        b.setHora_salida(time_hora_salida.getTime());
        b.setEvidencia(t_evidencia.getText());
        
        listaCompleta.add(b);
        actualizarArbol();
        refrescarTablaSegunSeleccionArbol();
    }
    
    public void cargar_datos_tabla(int indice){
        this.c_plomero.setSelectedItem(String.valueOf(this.Tabla_bitacora.getValueAt(indice, 0)));
    this.t_fecha.setText(String.valueOf(this.Tabla_bitacora.getValueAt(indice, 1)));
    
    // Recuperar los Strings de la tabla
    String horaEntradaStr = String.valueOf(this.Tabla_bitacora.getValueAt(indice, 2));
    String horaSalidaStr = String.valueOf(this.Tabla_bitacora.getValueAt(indice, 3));
    
    // Formateador para interpretar el texto de la tabla
    DateTimeFormatter formateador = DateTimeFormatter.ofPattern("hh:mm a");
    
    try {
        // Convertir texto a LocalTime y mandarlo al TimePicker
        this.time_hora_llegada.setTime(LocalTime.parse(horaEntradaStr, formateador));
        this.time_hora_salida.setTime(LocalTime.parse(horaSalidaStr, formateador));
    } catch (Exception e) {
        // En caso de que la fila seleccionada no tuviera horas válidas o estuviera vacía
        this.time_hora_llegada.setTime(null);
        this.time_hora_salida.setTime(null);
    }
    
    this.t_evidencia.setText(String.valueOf(this.Tabla_bitacora.getValueAt(indice, 4)));
    cargar_foto();
    }
    
    public void llenar_text(){
        this.t_evidencia.setText(bitacora.getEvidencia());
    }
    
    public void editar_tabla(){
        if (time_hora_llegada.getTime() == null || time_hora_salida.getTime() == null) {
            return;
        }
        if (fila < 0 || fila >= listaFiltrada.size()) {
            return;
        }
        
        Bitacora b = listaFiltrada.get(fila);
        b.setTrabajador(c_plomero.getSelectedItem().toString());
        b.setFecha(java.time.LocalDate.parse(t_fecha.getText()));
        b.setHora_llegada(time_hora_llegada.getTime());
        b.setHora_salida(time_hora_salida.getTime());
        b.setEvidencia(t_evidencia.getText());
        
        actualizarArbol();
        refrescarTablaSegunSeleccionArbol();
    }
    
    public void cargar_foto(){
        
        int ancho = l_evidencia.getWidth();
        int alto = l_evidencia.getHeight();
        
        if (ancho <= 0) ancho = 260;
        if (alto <= 0) alto = 270;
        
        if (t_evidencia.getText().trim().isEmpty()) {
        l_evidencia.setIcon(null); // Quita cualquier imagen previa que se haya quedado en el contenedor
        return; // Sale del método de forma segura para evitar el error gráfico
        }
        
        ImageIcon iconoOriginal = new ImageIcon(t_evidencia.getText());

        Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        l_evidencia.setIcon(new ImageIcon(imagenEscalada));
        if (iconoOriginal.getImageLoadStatus() != java.awt.MediaTracker.COMPLETE) {
            System.out.println("¡Error! No se pudo encontrar o cargar la imagen en la ruta especificada.");
        }
    }
    
    private void b_nuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_nuevoActionPerformed
        cargar_foto();

        if ("Nuevo".equals(b_nuevo.getText()))
        {
            desbloquear_text();
            this.b_foto.setEnabled(true);
            b_nuevo.setText("Guardar");
            limpiar_text();
            cargar_foto();
            bloquear_botones();
        }
        else
        {
            //if(t_n_serie.getText().trim().isEmpty() || t_foto.getText().trim().isEmpty())
            //{
              //  return;
            //}
            llenar_tabla();
            b_nuevo.setText("Nuevo");
            b_foto.setEnabled(false);
            bloquear_text();
            desbloquear_botones();
            b_nuevo.setEnabled(true);
            limpiar_text();
            cargar_foto();
        }
    }//GEN-LAST:event_b_nuevoActionPerformed

    private void b_editarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_editarActionPerformed
        if ("Editar".equals(b_editar.getText()))
        {
            b_editar.setText("Guardar");
            b_nuevo.setEnabled(false);
            b_eliminar.setEnabled(false);
            b_buscar.setEnabled(false);
            //b_guardar.setEnabled(false);
            desbloquear_text();
            b_foto.setEnabled(true);
        }
        else
        {
            //if(t_evidencia.getText().trim().isEmpty())
            //{
              //  return;
            //}
            b_editar.setText("Editar");
            editar_tabla();
            bloquear_text();
            desbloquear_botones();
        }
    }//GEN-LAST:event_b_editarActionPerformed

    private void b_eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_eliminarActionPerformed
        fila = Tabla_bitacora.getSelectedRow();
        if (fila == -1) {
            return;
        }
        if (fila >= 0 && fila < listaFiltrada.size()) {
            Bitacora b = listaFiltrada.get(fila);
            listaCompleta.remove(b);
            listaFiltrada.remove(fila);
            
            actualizarArbol();
            refrescarTablaSegunSeleccionArbol();
            
            fila = fila - 1;
            if (fila < 0) {
                limpiar_text();
            } else {
                cargar_datos_tabla(fila);
            }
        }
    }//GEN-LAST:event_b_eliminarActionPerformed

    private void Tabla_bitacoraMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Tabla_bitacoraMouseClicked
        fila = this.Tabla_bitacora.rowAtPoint(evt.getPoint());
        cargar_datos_tabla(fila);
    }//GEN-LAST:event_Tabla_bitacoraMouseClicked

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        fecha();
        llenar_text();
        //llenar_tabla();
        bloquear_text();
        b_foto.setEnabled(false);
        bloquear_botones();
        b_nuevo.setEnabled(true);
        //b_guardar.setEnabled(true);
    }//GEN-LAST:event_formWindowOpened

    private void b_fotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_fotoActionPerformed
        String ruta = "G:\\Mi unidad\\Trabajo\\";
    JFileChooser foto = new JFileChooser("c:\\fotossoft\\empleados\\");
    FileNameExtensionFilter filtrado = new FileNameExtensionFilter("JPG, PNG & GIF", "jpg", "png", "gif");
    foto.setFileFilter(filtrado);
    
    int respuesta = foto.showOpenDialog(this);
    
    if (respuesta == JFileChooser.APPROVE_OPTION)
    {
        ruta = foto.getSelectedFile().getPath();
        this.t_evidencia.setText(ruta);
        // this.doble_diagonal();
        ImageIcon img = new ImageIcon(ruta);
        Icon micono = new ImageIcon(img.getImage().getScaledInstance(this.l_evidencia.getWidth(), this.l_evidencia.getHeight(), Image.SCALE_DEFAULT));
        this.l_evidencia.setIcon(micono);
        this.t_evidencia.setText(ruta);
    }
    }//GEN-LAST:event_b_fotoActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void c_plomeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c_plomeroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_c_plomeroActionPerformed

    private void b_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_cancelarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_b_cancelarActionPerformed

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
            java.util.logging.Logger.getLogger(V_bitacora.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(V_bitacora.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(V_bitacora.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(V_bitacora.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new V_bitacora().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabla_bitacora;
    private javax.swing.JTree a_bitacora;
    private javax.swing.JButton b_buscar;
    private javax.swing.JButton b_cancelar;
    private javax.swing.JButton b_editar;
    private javax.swing.JButton b_eliminar;
    private javax.swing.JButton b_foto;
    private javax.swing.JButton b_nuevo;
    private javax.swing.JComboBox<String> c_plomero;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPaneArbol;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JLabel l_evidencia;
    private javax.swing.JTextField t_evidencia;
    private javax.swing.JTextField t_fecha;
    private javax.swing.JTextField t_id;
    private com.github.lgooddatepicker.components.TimePicker time_hora_llegada;
    private com.github.lgooddatepicker.components.TimePicker time_hora_salida;
    // End of variables declaration//GEN-END:variables
}
