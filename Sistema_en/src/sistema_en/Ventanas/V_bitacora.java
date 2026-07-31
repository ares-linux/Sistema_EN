/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package sistema_en.Ventanas;

import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import sistema_en.Objetos.Bitacora;
import java.time.LocalDate;
import javax.swing.DefaultListModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import raven.toast.Notifications;

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
            return bitacoraWrapper.getId_trabajador() + " (" + entrada + " - " + salida + ")";
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
                info[0] = b.getId_trabajador();
                info[1] = b.getFecha().toString();
                info[2] = b.getHora_llegada() != null ? b.getHora_llegada().format(formateador) : "";
                info[3] = b.getHora_salida() != null ? b.getHora_salida().format(formateador) : "";
                info[4] = b.getEvidencias() != null ? b.getEvidencias().size() + " foto(s)" : "0 foto(s)";
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
            info[0] = b.getId_trabajador();
            info[1] = b.getFecha().toString();
            info[2] = b.getHora_llegada() != null ? b.getHora_llegada().format(formateador) : "";
            info[3] = b.getHora_salida() != null ? b.getHora_salida().format(formateador) : "";
            info[4] = b.getEvidencias() != null ? b.getEvidencias().size() + " foto(s)" : "0 foto(s)";
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

    private DefaultListModel<String> modeloLista = new DefaultListModel<>();
    /**
     * Creates new form V_bitacora
     */
    public V_bitacora() {
        initComponents();
        
        L_evidencias.addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                b_eliminar_evidencia.doClick();
            }
        }
    });
        
        
        inicializarArbol();
        actualizarArbol();
        L_evidencias.setModel(modeloLista);
        L_evidencias.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                if (!evt.getValueIsAdjusting() && L_evidencias.getSelectedValue() != null) {
                    t_evidencia.setText(L_evidencias.getSelectedValue());
                    cargar_foto();
                }
            }
        });
        
        b_anterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int index = L_evidencias.getSelectedIndex();
                if (index > 0) {
                    L_evidencias.setSelectedIndex(index - 1);
                }
            }
        });
        
        b_siguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int index = L_evidencias.getSelectedIndex();
                if (index < L_evidencias.getModel().getSize() - 1) {
                    L_evidencias.setSelectedIndex(index + 1);
                }
            }
        });

        
        
        
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
        jTabbedPane1 = new javax.swing.JTabbedPane();
        P_registrar_bitacora = new javax.swing.JPanel();
        P_contenedor_registrar = new javax.swing.JPanel();
        P_formulario = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        b_nuevo = new javax.swing.JButton();
        P_evidencias = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        L_evidencias = new javax.swing.JList<>();
        t_evidencia = new javax.swing.JTextField();
        b_foto = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        b_eliminar_evidencia = new javax.swing.JButton();
        P_datos = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        T_notas = new javax.swing.JTextArea();
        jLabel10 = new javax.swing.JLabel();
        time_hora_salida = new com.github.lgooddatepicker.components.TimePicker();
        time_hora_llegada = new com.github.lgooddatepicker.components.TimePicker();
        c_plomero = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        P_imagen_evidencia = new javax.swing.JPanel();
        P_visor_imagen = new javax.swing.JPanel();
        P_controles_imagen = new javax.swing.JPanel();
        b_anterior = new javax.swing.JButton();
        b_siguiente = new javax.swing.JButton();
        b_ver_imagen = new javax.swing.JButton();
        l_evidencia = new javax.swing.JLabel();
        P_historial = new javax.swing.JPanel();
        jScrollPaneArbol = new javax.swing.JScrollPane();
        a_bitacora = new javax.swing.JTree();
        P_botones = new javax.swing.JPanel();
        b_cancelar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        b_buscar = new javax.swing.JButton();
        b_editar = new javax.swing.JButton();
        b_eliminar = new javax.swing.JButton();
        t_id = new javax.swing.JTextField();
        t_fecha = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        P_consultas = new javax.swing.JPanel();
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

        P_registrar_bitacora.setLayout(new java.awt.BorderLayout());

        P_contenedor_registrar.setLayout(new java.awt.GridBagLayout());

        P_formulario.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel1.setText("Bitacora de asistencia");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        P_formulario.add(jLabel1, gridBagConstraints);

        b_nuevo.setText("Nuevo");
        b_nuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_nuevoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 494;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        P_formulario.add(b_nuevo, gridBagConstraints);

        P_evidencias.setLayout(new java.awt.GridBagLayout());

        L_evidencias.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                L_evidenciasKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(L_evidencias);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 518;
        gridBagConstraints.ipady = 82;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        P_evidencias.add(jScrollPane3, gridBagConstraints);

        t_evidencia.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 440;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        P_evidencias.add(t_evidencia, gridBagConstraints);

        b_foto.setText("...");
        b_foto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_fotoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        P_evidencias.add(b_foto, gridBagConstraints);

        jLabel12.setText("Ruta de evidencia");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        P_evidencias.add(jLabel12, gridBagConstraints);

        b_eliminar_evidencia.setText("Eliminar");
        b_eliminar_evidencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_eliminar_evidenciaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        P_evidencias.add(b_eliminar_evidencia, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        P_formulario.add(P_evidencias, gridBagConstraints);

        P_datos.setLayout(new java.awt.GridBagLayout());

        T_notas.setColumns(20);
        T_notas.setRows(5);
        jScrollPane2.setViewportView(T_notas);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 518;
        gridBagConstraints.ipady = 39;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        P_datos.add(jScrollPane2, gridBagConstraints);

        jLabel10.setText("Hora de entrada");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        P_datos.add(jLabel10, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 183;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        P_datos.add(time_hora_salida, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 183;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        P_datos.add(time_hora_llegada, gridBagConstraints);

        c_plomero.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "PLO_00", "PLO_01", "PLO_02", "PLO_03", "PLO_04", "PLO_05", "PLO_06", "PLO_07", "PLO_08", "PLO_09", "Administrador" }));
        c_plomero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c_plomeroActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 428;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        P_datos.add(c_plomero, gridBagConstraints);

        jLabel11.setText("Hora de salida");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        P_datos.add(jLabel11, gridBagConstraints);

        jLabel13.setText("Descripcion / Notas");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        P_datos.add(jLabel13, gridBagConstraints);

        jLabel9.setText("Seleccionar plomero:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        P_datos.add(jLabel9, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        P_formulario.add(P_datos, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        P_contenedor_registrar.add(P_formulario, gridBagConstraints);

        P_imagen_evidencia.setLayout(new java.awt.BorderLayout());

        P_visor_imagen.setLayout(new java.awt.GridBagLayout());

        P_controles_imagen.setLayout(new java.awt.GridBagLayout());

        b_anterior.setText("Anterior");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        P_controles_imagen.add(b_anterior, gridBagConstraints);

        b_siguiente.setText("Siguiente");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        P_controles_imagen.add(b_siguiente, gridBagConstraints);

        b_ver_imagen.setText("Ver imagen");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        P_controles_imagen.add(b_ver_imagen, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 113;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        P_visor_imagen.add(P_controles_imagen, gridBagConstraints);

        l_evidencia.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        l_evidencia.setMaximumSize(new java.awt.Dimension(400, 550));
        l_evidencia.setMinimumSize(new java.awt.Dimension(400, 550));
        l_evidencia.setPreferredSize(new java.awt.Dimension(400, 550));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        P_visor_imagen.add(l_evidencia, gridBagConstraints);

        P_imagen_evidencia.add(P_visor_imagen, java.awt.BorderLayout.PAGE_END);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        P_contenedor_registrar.add(P_imagen_evidencia, gridBagConstraints);

        P_registrar_bitacora.add(P_contenedor_registrar, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Registrar bitacora", P_registrar_bitacora);

        P_historial.setLayout(new java.awt.BorderLayout());

        jScrollPaneArbol.setViewportView(a_bitacora);

        P_historial.add(jScrollPaneArbol, java.awt.BorderLayout.CENTER);

        P_botones.setLayout(new java.awt.GridBagLayout());

        b_cancelar.setText("Cancelar");
        b_cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_cancelarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        P_botones.add(b_cancelar, gridBagConstraints);

        jButton1.setText("Cerrar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        P_botones.add(jButton1, gridBagConstraints);

        b_buscar.setText("Buscar");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        P_botones.add(b_buscar, gridBagConstraints);

        b_editar.setText("Editar");
        b_editar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_editarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        P_botones.add(b_editar, gridBagConstraints);

        b_eliminar.setText("Eliminar");
        b_eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_eliminarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        P_botones.add(b_eliminar, gridBagConstraints);

        t_id.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 217;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        P_botones.add(t_id, gridBagConstraints);

        t_fecha.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 217;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        P_botones.add(t_fecha, gridBagConstraints);

        jLabel3.setText("Fecha");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        P_botones.add(jLabel3, gridBagConstraints);

        jLabel2.setText("Trabajo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        P_botones.add(jLabel2, gridBagConstraints);

        P_historial.add(P_botones, java.awt.BorderLayout.PAGE_START);

        jTabbedPane1.addTab("Historial", P_historial);

        P_consultas.setLayout(new java.awt.BorderLayout());

        Tabla_bitacora.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Plomero", "Fecha", "Hora de entrada", "Hora de salida", "Evidencia", "Notas"
            }
        ));
        Tabla_bitacora.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Tabla_bitacoraMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(Tabla_bitacora);

        P_consultas.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Consultas", P_consultas);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    public void bloquear_text(){
        c_plomero.setEnabled(false);
        time_hora_llegada.setEnabled(false);
        time_hora_salida.setEnabled(false);
        L_evidencias.setEnabled(false);
        T_notas.setEnabled(false);
    }
    
    public void desbloquear_text(){
        c_plomero.setEnabled(true);
        time_hora_llegada.setEnabled(true);
        time_hora_salida.setEnabled(true);
        L_evidencias.setEnabled(true);
        T_notas.setEnabled(true);
    }
    
    public void bloquear_botones_adicionales(){
        b_siguiente.setEnabled(false);
        b_ver_imagen.setEnabled(false);
        b_anterior.setEnabled(false);
        b_eliminar_evidencia.setEnabled(false);
    }
    
    public void desbloquear_botones_adicionales(){
        b_siguiente.setEnabled(true);
        b_ver_imagen.setEnabled(true);
        b_anterior.setEnabled(true);
        b_eliminar_evidencia.setEnabled(true);
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
        T_notas.setText("");
        DefaultListModel<String> modelo = (DefaultListModel<String>) L_evidencias.getModel();
        modelo.clear();
    }
    
    public void llenar_tabla(){
        if (time_hora_llegada.getTime() == null || time_hora_salida.getTime() == null) {
            return;
        }
        
        Bitacora b = new Bitacora();
        b.setId_trabajador(c_plomero.getSelectedItem().toString());
        b.setFecha(java.time.LocalDate.parse(t_fecha.getText()));
        b.setHora_llegada(time_hora_llegada.getTime());
        b.setHora_salida(time_hora_salida.getTime());
        java.util.List<String> evidencias = new java.util.ArrayList<>();
        for (int i = 0; i < modeloLista.getSize(); i++) {
            evidencias.add(modeloLista.getElementAt(i));
        }
        b.setEvidencias(evidencias);
        b.setNotas(T_notas.getText());
        
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
    
        Bitacora b = listaFiltrada.get(indice);
    
        modeloLista.clear();
        if (b.getEvidencias() != null) {
            for (String ev : b.getEvidencias()) {
                modeloLista.addElement(ev);
            }
        }
        if (modeloLista.getSize() > 0) {
            L_evidencias.setSelectedIndex(0);
            t_evidencia.setText(modeloLista.getElementAt(0));
        } else {
            t_evidencia.setText("");
        }
        cargar_foto();
        this.T_notas.setText(b.getNotas() != null ? b.getNotas() : "");
    }
    
    public void llenar_text(){
        this.T_notas.setText(bitacora.getNotas());
        //this.L_evidencias.setModel(modeloLista.addElement(element));
    }
    
    public void editar_tabla(){
        if (time_hora_llegada.getTime() == null || time_hora_salida.getTime() == null) {
            return;
        }
        if (fila < 0 || fila >= listaFiltrada.size()) {
            return;
        }
        
        Bitacora b = listaFiltrada.get(fila);
        b.setId_trabajador(c_plomero.getSelectedItem().toString());
        b.setFecha(java.time.LocalDate.parse(t_fecha.getText()));
        b.setHora_llegada(time_hora_llegada.getTime());
        b.setHora_salida(time_hora_salida.getTime());
        java.util.List<String> evidencias = new java.util.ArrayList<>();
        for (int i = 0; i < modeloLista.getSize(); i++) {
            evidencias.add(modeloLista.getElementAt(i));
        }
        b.setEvidencias(evidencias);
        b.setNotas(T_notas.getText());
        
        actualizarArbol();
        refrescarTablaSegunSeleccionArbol();
    }
    
    public void cargar_foto(){
        
        int anchoLabel = l_evidencia.getWidth();
        int altoLabel = l_evidencia.getHeight();
        
        if (anchoLabel <= 0) anchoLabel = 260;
        if (altoLabel <= 0) altoLabel = 270;
        
        if (t_evidencia.getText().trim().isEmpty()) {
            l_evidencia.setIcon(null); // Quita cualquier imagen previa que se haya quedado en el contenedor
            return; // Sale del método de forma segura para evitar el error gráfico
        }
        
        ImageIcon iconoOriginal = new ImageIcon(t_evidencia.getText());
        int anchoImagen = iconoOriginal.getIconWidth();
        int altoImagen = iconoOriginal.getIconHeight();

        if (anchoImagen <= 0 || altoImagen <= 0) {
            return;
        }

        double proporcionAncho = (double) anchoLabel / anchoImagen;
        double proporcionAlto = (double) altoLabel / altoImagen;
        double proporcionFinal = Math.min(proporcionAncho, proporcionAlto);

        int nuevoAncho = (int) (anchoImagen * proporcionFinal);
        int nuevoAlto = (int) (altoImagen * proporcionFinal);

        Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(nuevoAncho, nuevoAlto, Image.SCALE_SMOOTH);
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
            desbloquear_botones_adicionales();
            b_eliminar_evidencia.setEnabled(true);
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
            bloquear_botones_adicionales();
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
            desbloquear_botones_adicionales();
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
            bloquear_botones_adicionales();
            limpiar_text();
            cargar_foto();
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

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        fecha();
        llenar_text();
        //llenar_tabla();
        bloquear_text();
        b_foto.setEnabled(false);
        bloquear_botones();
        b_nuevo.setEnabled(true);
        bloquear_botones_adicionales();
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
        modeloLista.addElement(ruta);
        L_evidencias.setSelectedIndex(modeloLista.getSize() - 1);
        this.t_evidencia.setText(ruta);
        cargar_foto();
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

    private void Tabla_bitacoraMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Tabla_bitacoraMouseClicked
        fila = this.Tabla_bitacora.rowAtPoint(evt.getPoint());
        cargar_datos_tabla(fila);
    }//GEN-LAST:event_Tabla_bitacoraMouseClicked

    private void b_eliminar_evidenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_eliminar_evidenciaActionPerformed
        fila = L_evidencias.getSelectedIndex();

        String mensaje;
        
        if (fila != -1) {
            modeloLista.remove(fila);
            mensaje = "Evidencia eliminada.";
            Notifications.getInstance().show( Notifications.Type.SUCCESS, Notifications.Location.TOP_LEFT, mensaje);
            
        }
        
        if (fila == -1) {
            mensaje = "Seleccione una evidencia para eliminar.";
            Notifications.getInstance().show( Notifications.Type.INFO, Notifications.Location.TOP_LEFT, mensaje);
            return;
        }
        
        t_evidencia.setText("");
        cargar_foto();
    }//GEN-LAST:event_b_eliminar_evidenciaActionPerformed

    private void L_evidenciasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_L_evidenciasKeyPressed
        
    }//GEN-LAST:event_L_evidenciasKeyPressed

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
    private javax.swing.JList<String> L_evidencias;
    private javax.swing.JPanel P_botones;
    private javax.swing.JPanel P_consultas;
    private javax.swing.JPanel P_contenedor_registrar;
    private javax.swing.JPanel P_controles_imagen;
    private javax.swing.JPanel P_datos;
    private javax.swing.JPanel P_evidencias;
    private javax.swing.JPanel P_formulario;
    private javax.swing.JPanel P_historial;
    private javax.swing.JPanel P_imagen_evidencia;
    private javax.swing.JPanel P_registrar_bitacora;
    private javax.swing.JPanel P_visor_imagen;
    private javax.swing.JTextArea T_notas;
    private javax.swing.JTable Tabla_bitacora;
    private javax.swing.JTree a_bitacora;
    private javax.swing.JButton b_anterior;
    private javax.swing.JButton b_buscar;
    private javax.swing.JButton b_cancelar;
    private javax.swing.JButton b_editar;
    private javax.swing.JButton b_eliminar;
    private javax.swing.JButton b_eliminar_evidencia;
    private javax.swing.JButton b_foto;
    private javax.swing.JButton b_nuevo;
    private javax.swing.JButton b_siguiente;
    private javax.swing.JButton b_ver_imagen;
    private javax.swing.JComboBox<String> c_plomero;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPaneArbol;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTabbedPane jTabbedPane1;
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
