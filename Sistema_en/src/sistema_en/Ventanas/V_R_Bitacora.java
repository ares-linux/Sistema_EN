/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package sistema_en.Ventanas;

import com.formdev.flatlaf.FlatLaf;
import dao.BitacoraDAO;
import dao.EvidenciaDAO;
import dao.TrabajadorDAO;
import dao.TrabajoDAO;
import java.awt.Color;
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
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import raven.toast.Notifications;
import sistema_en.Carpetas;
import sistema_en.EjecutorGoogle;
import sistema_en.GoogleServicesProvider;
import sistema_en.Objetos.Evidencia;
import sistema_en.Objetos.Trabajo;
import sistema_en.Ventanas.dialogos.D_buscar_bitacora;
import utilidades.UtilidadesRecursos;

/**
 *
 * @author zahid
 */
public class V_R_Bitacora extends javax.swing.JFrame {
    
    Bitacora bitacora = new Bitacora();
    int fila;
    int id_trabajo;
    List<Evidencia> listaTemporalFotos = new ArrayList<>();
    List<Evidencia> listaFotosNuevas = new ArrayList<>();
    List<Integer> listaIdsFotosEliminadas = new ArrayList<>();
    List<Evidencia> evidenciasDisponiblesDrive = new ArrayList<>();
    List<Evidencia> evidenciasVisibles = new ArrayList<>();
    private final DefaultListModel<String> modeloLista = new DefaultListModel<>();
    
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
        DefaultTableModel modelo = (DefaultTableModel) Tabla_bitacoras.getModel();
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
        DefaultTableModel modelo = (DefaultTableModel) Tabla_bitacoras.getModel();
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
                    Tabla_bitacoras.setRowSelectionInterval(i, i);
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
    public V_R_Bitacora() {
        initComponents();
        
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        
        this.addWindowListener(new java.awt.event.WindowAdapter() {
        @Override
        public void windowClosing(java.awt.event.WindowEvent e) {   
        if ("Guardar".equals(b_nuevo.getText()) || "Guardar".equals(b_editar.getText())) {
            //javax.swing.JOptionPane.showMessageDialog(null, "Guarde los cambios realizados antes de salir.");
            b_cancelar.doClick(); 
  
            if (!b_cancelar.isEnabled()) {
            dispose(); 
            }
        } else {
            dispose();
        }
        
    }
    });


    
        
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
        public int recibirTrabajo(int id_trabajo_i){
        cargarTrabajo(id_trabajo_i); 
        return id_trabajo_i;
        
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
        T_pestañas = new javax.swing.JTabbedPane();
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
        l_evidencias = new javax.swing.JLabel();
        b_eliminar_evidencia = new javax.swing.JButton();
        P_datos = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        T_notas = new javax.swing.JTextArea();
        l_entrada = new javax.swing.JLabel();
        time_hora_salida = new com.github.lgooddatepicker.components.TimePicker();
        time_hora_llegada = new com.github.lgooddatepicker.components.TimePicker();
        c_plomero = new javax.swing.JComboBox<>();
        l_salida = new javax.swing.JLabel();
        l_notas = new javax.swing.JLabel();
        l_plomero = new javax.swing.JLabel();
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
        b_cerrar = new javax.swing.JButton();
        b_editar = new javax.swing.JButton();
        t_id = new javax.swing.JTextField();
        t_fecha = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        P_tabla = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabla_bitacoras = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        b_buscar = new javax.swing.JButton();

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

        l_evidencias.setText("Ruta de evidencia");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        P_evidencias.add(l_evidencias, gridBagConstraints);

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

        l_entrada.setText("Hora de entrada");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        P_datos.add(l_entrada, gridBagConstraints);
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

        l_salida.setText("Hora de salida");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        P_datos.add(l_salida, gridBagConstraints);

        l_notas.setText("Descripcion / Notas");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        P_datos.add(l_notas, gridBagConstraints);

        l_plomero.setText("Seleccionar plomero:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        P_datos.add(l_plomero, gridBagConstraints);

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

        T_pestañas.addTab("Registrar bitacora", P_registrar_bitacora);

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

        b_cerrar.setText("Cerrar");
        b_cerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_cerrarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        P_botones.add(b_cerrar, gridBagConstraints);

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

        T_pestañas.addTab("Historial", P_historial);

        P_tabla.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setMaximumSize(new java.awt.Dimension(450, 450));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(450, 450));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(450, 450));

        Tabla_bitacoras.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Plomero", "Fecha", "Hora de entrada", "Hora de salida", "Evidencia", "Notas"
            }
        ));
        Tabla_bitacoras.setMaximumSize(new java.awt.Dimension(450, 450));
        Tabla_bitacoras.setMinimumSize(new java.awt.Dimension(450, 450));
        Tabla_bitacoras.setPreferredSize(new java.awt.Dimension(450, 450));
        Tabla_bitacoras.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Tabla_bitacorasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(Tabla_bitacoras);

        P_tabla.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        b_buscar.setText("Buscar");
        b_buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_buscarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        jPanel1.add(b_buscar, gridBagConstraints);

        P_tabla.add(jPanel1, java.awt.BorderLayout.PAGE_START);

        T_pestañas.addTab("Tabla", P_tabla);

        getContentPane().add(T_pestañas, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private final void configurar_tabla(){
        Tabla_bitacoras.setDefaultEditor(Object.class, null);
        Tabla_bitacoras.setSelectionMode( ListSelectionModel.SINGLE_SELECTION);
        Tabla_bitacoras.getTableHeader().setReorderingAllowed(false);
        Tabla_bitacoras.getTableHeader().setResizingAllowed(false);
    }
    
    private void cargarTrabajo(int id_trabajo_i){
        
        id_trabajo = id_trabajo_i;
        String id = String.valueOf(id_trabajo);
        t_id.setText(id);
        
        DefaultTableModel modelo = (DefaultTableModel) this.Tabla_bitacoras.getModel();
        modelo.setRowCount(0); 
        
        BitacoraDAO dao = new BitacoraDAO();
        List<Bitacora> lista = dao.obtenerBitacoras(id_trabajo);

        for (Bitacora b : lista) {
            modelo.addRow(new Object[]{
                b.getId_trabajador(),
                b.getFecha(),
                b.getHora_llegada(),
                b.getHora_salida(),
                b.getEvidencias(),
                b.getNotas()
            });
        }

        this.Tabla_bitacoras.setModel(modelo);
        
        cargarEvidenciasDeDrive();
        
    }
    
    // cargarEvidenciasDeDrive(): Obtiene las imágenes disponibles de la carpeta
    // "Evidencias" del trabajo en Google Drive (si está conectado) y las cachea.
    private void cargarEvidenciasDeDrive() {
        cargarEvidenciasDeDrive(() -> {}, () -> {});
    }

    private void cargarEvidenciasDeDrive(Runnable alListar, Runnable alSiempre) {
        synchronized (evidenciasDisponiblesDrive) {
            evidenciasDisponiblesDrive.clear();
        }
        if (!GoogleServicesProvider.estaConectado()) {
            alSiempre.run();
            alListar.run();
            return;
        }

        // Es una lectura, asi que puede reintentarse: si el DNS de la red deja de
        // responder un instante, la lista se carga igual al segundo intento. La
        // obtencion de la carpeta de evidencias se resuelve ahora dentro del
        // proveedor, que antes esta vista armaba su propio servicio de Drive.
        EjecutorGoogle.ejecutar(() -> {

            TrabajoDAO trabajoDao = new TrabajoDAO();
            Trabajo trabajo = trabajoDao.obtenerTrabajoIdCarpeta(id_trabajo);
            if (trabajo == null || trabajo.getId_carpeta() == null) {
                return List.<Evidencia>of();
            }

            String idEvidencias = GoogleServicesProvider.obtenerIdCarpetaEvidencias(trabajo.getId_carpeta(), false);
            if (idEvidencias == null) {
                return List.<Evidencia>of();
            }

            return GoogleServicesProvider.listarArchivosDeCarpeta(idEvidencias, true);
        }, disponibles -> {

            synchronized (evidenciasDisponiblesDrive) {
                evidenciasDisponiblesDrive.clear();
                evidenciasDisponiblesDrive.addAll(disponibles);
            }

            if (!isDisplayable()) {
                return;
            }

            alSiempre.run();
            alListar.run();
        }, error -> {

            System.out.println("No se pudieron cargar las evidencias desde Drive: "
                    + EjecutorGoogle.mensajeDeError(error));

            if (!isDisplayable()) {
                return;
            }

            alSiempre.run();
            JOptionPane.showMessageDialog(this, EjecutorGoogle.mensajeDeError(error),
                    "Evidencias en Drive", JOptionPane.ERROR_MESSAGE);
        });
    }

    // obtenerCarpetaEvidenciasLocal(): Devuelve la carpeta local base donde se
    // guardan las evidencias descargadas del trabajo (ej: ~/Imágenes/Sistema_EN/Trabajo_42/evidencias/).
    private String obtenerCarpetaEvidenciasLocal() {
        try {
            java.nio.file.Path base = Carpetas.ObtenerImagenes.obtenerCarpetaImagenes();
            java.nio.file.Path ruta = base.resolve("Sistema_EN").resolve("Trabajo_" + id_trabajo).resolve("evidencias");
            return ruta.toString();
        } catch (Exception e) {
            return System.getProperty("user.home");
        }
    }
    
    
    public void creacion_exitosa(){       
            String mensaje;
            mensaje = "La bitacora fue registrado con éxito.";
            Notifications.getInstance().show( Notifications.Type.SUCCESS, Notifications.Location.TOP_LEFT, mensaje);
    }
    
    public void edicion_exitosa(){
            String mensaje;
            mensaje = "✓ Cambios guardados con éxito.";
            Notifications.getInstance().show( Notifications.Type.SUCCESS, Notifications.Location.TOP_LEFT, mensaje); 
    }
    
    public void eliminacion_exitosa(){
            String mensaje;
            mensaje = "Bitacora eliminado correctamente";
            Notifications.getInstance().show( Notifications.Type.SUCCESS, Notifications.Location.TOP_LEFT, mensaje); 
    }
    
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
        b_editar.setEnabled(false);
        b_buscar.setEnabled(false);
        //b_nuevo.setEnabled(false);
        //b_guardar.setEnabled(false);
    }
    
    public void desbloquear_botones(){
        b_editar.setEnabled(true);
        b_buscar.setEnabled(true);
        b_nuevo.setEnabled(true);
        //b_guardar.setEnabled(true);
    }
    
    public void fecha(){
        t_fecha.setText(java.time.LocalDate.now().toString());
    }
    
    public void cargar_Trabajadores_Combo() {
        TrabajadorDAO dao = new TrabajadorDAO();
        java.util.List<String[]> trabajadores = dao.obtenerTrabajadoresj();
        
        c_plomero.removeAllItems();
        
        for (String[] t : trabajadores) {
            // t[0] = id_trabajador, t[1] = nombre
            c_plomero.addItem(t[0] + " - " + t[1]);
        }
        
        c_plomero.setSelectedIndex(-1);
    }
    
    public void limpiar_text(){
        t_fecha.setText(java.time.LocalDate.now().toString());
        t_evidencia.setText("");
        time_hora_llegada.setTime(null);
        time_hora_salida.setTime(null);
        T_notas.setText("");
        DefaultListModel<String> modelo = (DefaultListModel<String>) L_evidencias.getModel();
        modelo.clear();
        evidenciasVisibles.clear();
    }
    
    public void llenar_tabla(){
        BitacoraDAO dao = new BitacoraDAO();
        java.util.List<Bitacora> lista = dao.obtenerBitacoras(id_trabajo);
        
        if (lista != null) {
            listaCompleta.clear();
            listaCompleta.addAll(lista);
        }
        
        actualizarArbol();
        refrescarTablaSegunSeleccionArbol();
    }
    
    public void cargar_datos_tabla(int indice){
        this.c_plomero.setSelectedItem(String.valueOf(this.Tabla_bitacoras.getValueAt(indice, 0)));
        this.t_fecha.setText(String.valueOf(this.Tabla_bitacoras.getValueAt(indice, 1)));
    
        // Recuperar los Strings de la tabla
        String horaEntradaStr = String.valueOf(this.Tabla_bitacoras.getValueAt(indice, 2));
        String horaSalidaStr = String.valueOf(this.Tabla_bitacoras.getValueAt(indice, 3));
    
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
    
        evidenciasVisibles.clear();
        modeloLista.clear();
        if (b.getEvidencias() != null) {
            for (Evidencia ev : b.getEvidencias()) {
                evidenciasVisibles.add(ev);
                modeloLista.addElement(ev.getNombre_archivo() != null ? ev.getNombre_archivo() : "");
            }
        }
        if (modeloLista.getSize() > 0) {
            L_evidencias.setSelectedIndex(0);
            t_evidencia.setText(evidenciasVisibles.get(0).getRuta_local() != null ? evidenciasVisibles.get(0).getRuta_local() : "");
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
        
        if (c_plomero.getSelectedItem() != null) {
            String seleccion = c_plomero.getSelectedItem().toString();
            String idExtraido = seleccion.split(" - ")[0].trim(); // Extrae lo que está antes del guion
            b.setId_trabajador(Integer.parseInt(idExtraido));
        }
        
        //b.setId_trabajador(c_plomero.getSelectedItem().toString());
        b.setFecha(java.time.LocalDate.parse(t_fecha.getText()));
        b.setHora_llegada(time_hora_llegada.getTime());
        b.setHora_salida(time_hora_salida.getTime());
        b.setEvidencias(new java.util.ArrayList<>(evidenciasVisibles));
        b.setNotas(T_notas.getText());
        
        actualizarArbol();
        refrescarTablaSegunSeleccionArbol();
    }
    
    public void cargar_foto(){
        
        int anchoLabel = l_evidencia.getWidth();
        int altoLabel = l_evidencia.getHeight();
        
        if (anchoLabel <= 0) anchoLabel = 260;
        if (altoLabel <= 0) altoLabel = 270;
        
        int indice = L_evidencias.getSelectedIndex();
        if (indice < 0 || indice >= evidenciasVisibles.size()) {
            String texto = t_evidencia.getText();
            if (texto != null && !texto.trim().isEmpty()) {
                mostrarImagenRuta(texto, anchoLabel, altoLabel);
            } else {
                mostrarEvidenciaPorDefecto(anchoLabel, altoLabel);
            }
            return;
        }
        
        Evidencia ev = evidenciasVisibles.get(indice);
        String ruta = ev.getRuta_local();
        
        // Si no existe localmente pero tenemos su id de Drive, la descargamos.
        if ((ruta == null || !new java.io.File(ruta).exists())
                && ev.getId_google_drive() != null && !ev.getId_google_drive().isEmpty()) {
            String carpeta = obtenerCarpetaEvidenciasLocal();
            String nombre = ev.getNombre_archivo() != null ? ev.getNombre_archivo() : ("evidencia_" + ev.getId_google_drive());
            ruta = carpeta + java.io.File.separator + nombre;
            descargarYMostrar(ev, ruta, anchoLabel, altoLabel);
            return;
        }
        
        pintarEvidencia(ruta, anchoLabel, altoLabel);
    }

    /**
     * Descarga la evidencia desde Drive y recien despues la muestra.
     *
     * La descarga no puede quedar en medio de este metodo: secalled desde el
     * hilo de eventos cada vez que cambia la seleccion de la lista, y bajar un
     * archivo freezes la ventana sin que el usuario entienda que esta pasando.
     *
     * @param ev          evidencia seleccionada
     * @param ruta        ruta local donde quedara el archivo
     * @param anchoLabel  ancho del area de imagen
     * @param altoLabel   alto del area de imagen
     */
    private void descargarYMostrar(Evidencia ev, String ruta, int anchoLabel, int altoLabel) {
        String idDrive = ev.getId_google_drive();

        // Es una lectura, asi que puede reintentarse: si el DNS de la red falla
        // un instante, la descarga se reintenta sola.
        EjecutorGoogle.ejecutar(
                () -> GoogleServicesProvider.verificarYDescargarEvidencia(idDrive, ruta),
                descargada -> {

                    if (!isDisplayable()) {
                        return;
                    }

                    if (!descargada) {
                        t_evidencia.setText("");
                        l_evidencia.setIcon(null);
                        return;
                    }

                    ev.setRuta_local(ruta);
                    pintarEvidencia(ruta, anchoLabel, altoLabel);
                },
                error -> {

                    if (!isDisplayable()) {
                        return;
                    }
                    t_evidencia.setText("");
                    l_evidencia.setIcon(null);
                    System.out.println("No se pudo descargar la evidencia: " + EjecutorGoogle.mensajeDeError(error));
                });
    }

    /**
     * Muestra en el area de imagen la evidencia indicada. Se ejecuta en el hilo
     * de eventos, tanto si el archivo estaba local como si recien se descargo.
     */
    private void pintarEvidencia(String ruta, int anchoLabel, int altoLabel) {
        String pathMostrar = (ruta != null) ? ruta : "";
        t_evidencia.setText(pathMostrar);
        
        if (pathMostrar.trim().isEmpty()) {
            mostrarEvidenciaPorDefecto(anchoLabel, altoLabel);
            return;
        }
        
        mostrarImagenRuta(pathMostrar, anchoLabel, altoLabel);
    }
    
    private void mostrarImagenRuta(String ruta, int anchoLabel, int altoLabel) {
        ImageIcon iconoOriginal = new ImageIcon(ruta);

        if (iconoOriginal.getIconWidth() <= 0 || iconoOriginal.getIconHeight() <= 0) {
            l_evidencia.setIcon(null);
            return;
        }

        l_evidencia.setIcon(escalarEnLabel(iconoOriginal, anchoLabel, altoLabel));

        if (iconoOriginal.getImageLoadStatus() != java.awt.MediaTracker.COMPLETE) {
            System.out.println("¡Error! No se pudo encontrar o cargar la imagen en la ruta especificada.");
        }
    }

    /**
     * Cuando no hay evidencia cargada se muestra la camara del tema, para que el
     * area de la imagen no quede como un recuadro vacio.
     *
     * @param anchoLabel ancho del area de imagen
     * @param altoLabel  alto del area de imagen
     */
    private void mostrarEvidenciaPorDefecto(int anchoLabel, int altoLabel) {
        String ruta = FlatLaf.isLafDark()
                ? UtilidadesRecursos.FOTO_CAMARA_CLARO
                : UtilidadesRecursos.FOTO_CAMARA_OSCURO;

        ImageIcon iconoOriginal = UtilidadesRecursos.icono(ruta);

        if (iconoOriginal == null) {
            l_evidencia.setIcon(null);
            return;
        }

        l_evidencia.setIcon(escalarEnLabel(iconoOriginal, anchoLabel, altoLabel));
    }

    /**
     * Escala una imagen para que entre completa en el area del label sin deformarla.
     *
     * @param original   imagen a escalar
     * @param anchoLabel ancho del area de imagen
     * @param altoLabel  alto del area de imagen
     * @return la imagen escalada
     */
    private ImageIcon escalarEnLabel(ImageIcon original, int anchoLabel, int altoLabel) {
        int anchoImagen = original.getIconWidth();
        int altoImagen = original.getIconHeight();

        double proporcionAncho = (double) anchoLabel / anchoImagen;
        double proporcionAlto = (double) altoLabel / altoImagen;
        double proporcionFinal = Math.min(proporcionAncho, proporcionAlto);

        int nuevoAncho = (int) (anchoImagen * proporcionFinal);
        int nuevoAlto = (int) (altoImagen * proporcionFinal);

        return new ImageIcon(original.getImage().getScaledInstance(nuevoAncho, nuevoAlto, Image.SCALE_SMOOTH));
    }
    
    public void aviso_labels(){
        
        boolean error = false;
        
        if (c_plomero.getSelectedItem() == null) {
            l_plomero.setForeground(Color.decode("#CE1126"));
            error = true;
        }
        else{
            l_plomero.setForeground(null);
        }          
                
        if (time_hora_llegada.getTime() == null) {
            l_entrada.setForeground(Color.decode("#CE1126"));
            error = true;
        }
        else{
            l_entrada.setForeground(null);
        }
        
        if (time_hora_salida.getTime() == null) {
            l_salida.setForeground(Color.decode("#CE1126"));
            error = true;
        }
        else{
            l_salida.setForeground(null);
        }
        
        /*
        if (T_notas.getText().trim().isEmpty()) {
            l_notas.setForeground(Color.decode("#CE1126"));
            error = false;
        }

        if (!T_notas.getText().trim().isEmpty()) {
            l_notas.setForeground(null);
            error = true;
        }
        
        if (modeloLista.isEmpty()) {
            l_evidencias.setForeground(Color.decode("#CE1126"));
            error = false;
        }
        else{
            l_evidencias.setForeground(null);
        }
        */
                
        
    }
    
    private void b_nuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_nuevoActionPerformed
        

        if ("Nuevo".equals(b_nuevo.getText()))
        {
            listaTemporalFotos = new ArrayList<>();
            evidenciasVisibles.clear();
            cargar_foto();
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
            if(time_hora_llegada.getTime() == null || time_hora_salida.getTime() == null || c_plomero.getSelectedItem() == null)
            {
                aviso_labels();
                return;
            }
            
            String fecha = t_fecha.getText();
            String hora_llegada = time_hora_llegada.getText();
            String hora_salida = time_hora_salida.getText();
            String notas = T_notas.getText();
            
            // Extraer el id del trabajador del combo
            int idTrabajador = 0;
            String seleccionPlomero = (String) c_plomero.getSelectedItem();
            if (seleccionPlomero != null && !seleccionPlomero.isEmpty()) {
                idTrabajador = Integer.parseInt(seleccionPlomero.split(" - ")[0].trim());
            }
            
            boolean guardadoExitoso = false;
            
            BitacoraDAO dao = new BitacoraDAO();
            int id_bitacora = dao.insertarBitacora(fecha, hora_llegada, hora_salida, notas, idTrabajador, id_trabajo);
            
            if (id_bitacora > 0) {
                EvidenciaDAO evidenciaDAO = new EvidenciaDAO();
                guardadoExitoso = true;
                
                for (Evidencia ev : listaTemporalFotos) {
                    boolean insertoFoto = evidenciaDAO.insertarEvidencia(
                        id_bitacora,      // FK de la Bitácora recien creada
            ev.getId_google_drive(), // ID de Google Drive
             ev.getNombre_archivo(),   // Nombre del archivo
               ev.getRuta_local()       // Ruta local
                    );

                    if (!insertoFoto) {
                        guardadoExitoso = false;
                    }
                }
                
            }
            
            if (guardadoExitoso) {
                creacion_exitosa();
                llenar_tabla();
                b_nuevo.setText("Nuevo");
                b_foto.setEnabled(false);
                bloquear_text();
                desbloquear_botones();
                bloquear_botones_adicionales();
                b_nuevo.setEnabled(true);
                limpiar_text();
                cargar_foto();
                c_plomero.setSelectedItem(null);
            }
            else{
                JOptionPane.showMessageDialog(this, "Error al guardar el registro en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_b_nuevoActionPerformed

    private void b_editarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_editarActionPerformed
        if ("Editar".equals(b_editar.getText()))
        {
            listaFotosNuevas = new ArrayList<>();
            listaIdsFotosEliminadas = new ArrayList<>();

            int filaSel = Tabla_bitacoras.getSelectedRow();
            if (filaSel >= 0 && filaSel < listaFiltrada.size()) {
                Bitacora b = listaFiltrada.get(filaSel);
                evidenciasVisibles.clear();
                modeloLista.clear();
                if (b.getEvidencias() != null) {
                    for (Evidencia ev : b.getEvidencias()) {
                        evidenciasVisibles.add(ev);
                        modeloLista.addElement(ev.getNombre_archivo() != null ? ev.getNombre_archivo() : "");
                    }
                }
                if (modeloLista.getSize() > 0) {
                    L_evidencias.setSelectedIndex(0);
                    cargar_foto();
                }
            }

            b_editar.setText("Guardar");
            b_nuevo.setEnabled(false);
            b_cancelar.setEnabled(true);
            desbloquear_text();
            desbloquear_botones_adicionales();
        }
        else
        {
            if(time_hora_llegada.getTime() == null || time_hora_salida.getTime() == null || c_plomero.getSelectedItem() == null)
            {
                aviso_labels();
                return;
            }
            
            
            String fecha = t_fecha.getText();
            String hora_llegada = time_hora_llegada.getText();
            String hora_salida = time_hora_salida.getText();
            String notas = T_notas.getText();
                        
            // Extraer el id del trabajador del combo
            int idTrabajador = 0;
            String seleccionPlomero = (String) c_plomero.getSelectedItem();
            if (seleccionPlomero != null && !seleccionPlomero.isEmpty()) {
                idTrabajador = Integer.parseInt(seleccionPlomero.split(" - ")[0].trim());
            }
            
            int filaSeleccionada = Tabla_bitacoras.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un registro para editar.");
                return;
            }
            int id_bitacora = listaFiltrada.get(filaSeleccionada).getId_bitacora();
            boolean exito = false;
            BitacoraDAO dao = new BitacoraDAO();
            boolean actualizadoExitoso = dao.actualizarBitacora(fecha, hora_llegada, hora_salida, notas, idTrabajador, id_bitacora);
            
            if(actualizadoExitoso){
                EvidenciaDAO evidenciaDAO = new EvidenciaDAO();
                exito = true;

                // Guardar solo las fotos NUEVAS agregadas durante la edición
                for (Evidencia ev : listaFotosNuevas) {
                    boolean insertoFoto = evidenciaDAO.insertarEvidencia(
                        id_bitacora, 
                        ev.getId_google_drive(), 
                        ev.getNombre_archivo(), 
                        ev.getRuta_local()
                    );
                    if (!insertoFoto) {
                        exito = false;
                    }
                }
                
                // Eliminar las evidencias marcadas para borrar durante la edición
                for (Integer id_evidencia_eliminar : listaIdsFotosEliminadas) {
                    evidenciaDAO.eliminarEvidencia(id_evidencia_eliminar);
                }
            
            if (exito) { 
                b_editar.setText("Editar");
                edicion_exitosa();
                editar_tabla();
                bloquear_text();
                desbloquear_botones();
                bloquear_botones_adicionales();
                limpiar_text();
                cargar_foto();
            } else { 
                JOptionPane.showMessageDialog(this, "Error al actualizar el registro en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            } 
        }
        }
    }//GEN-LAST:event_b_editarActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        fecha();
        configurar_tabla();
        cargar_Trabajadores_Combo();
        llenar_text();
        llenar_tabla();
        bloquear_text();
        b_foto.setEnabled(false);
        //bloquear_botones();
        //b_nuevo.setEnabled(true);
        //b_buscar.setEnabled(true);
        bloquear_botones_adicionales();
        //b_guardar.setEnabled(true);
    }//GEN-LAST:event_formWindowOpened

    private void b_fotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_fotoActionPerformed
        String[] opciones = { "Seleccionar de Drive", "Subir imagen local" };
        int eleccion = JOptionPane.showOptionDialog(
                this,
                "¿Cómo deseas agregar la evidencia?",
                "Agregar evidencia",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (eleccion == 0) {
            boolean fotoHabilitada = b_foto.isEnabled();
            b_foto.setEnabled(false);
            setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
            cargarEvidenciasDeDrive(
                    this::seleccionarDeDrive,
                    () -> {
                        setCursor(null);
                        b_foto.setEnabled(fotoHabilitada);
                    });
        } else if (eleccion == 1) {
            subirImagenLocal();
        }
    }//GEN-LAST:event_b_fotoActionPerformed

    // seleccionarDeDrive(): Muestra un diálogo con los archivos disponibles de la
    // carpeta "Evidencias" del trabajo en Drive y los agrega a la lista temporal.
    private void seleccionarDeDrive() {
        if (!GoogleServicesProvider.estaConectado()) {
            JOptionPane.showMessageDialog(this,
                    "Google Drive no está conectado.",
                    "Drive no disponible",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Evidencia> disponibles;
        synchronized (evidenciasDisponiblesDrive) {
            disponibles = new ArrayList<>(evidenciasDisponiblesDrive);
        }

        if (disponibles.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay imágenes disponibles en la carpeta Evidencias de Drive.\nUsa la opción 'Subir imagen local'.",
                    "Sin archivos",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        DefaultListModel<String> nombresModelo = new DefaultListModel<>();
        for (Evidencia e : disponibles) {
            nombresModelo.addElement(e.getNombre_archivo() != null ? e.getNombre_archivo() : "");
        }

        javax.swing.JList<String> listaNombres = new javax.swing.JList<>(nombresModelo);
        listaNombres.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        Object[] mensaje = { "Seleccione una imagen de Drive:", new javax.swing.JScrollPane(listaNombres) };
        int opcion = JOptionPane.showConfirmDialog(this, mensaje, "Evidencias en Drive",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (opcion == JOptionPane.OK_OPTION && listaNombres.getSelectedIndex() >= 0) {
            Evidencia seleccionada = disponibles.get(listaNombres.getSelectedIndex());
            String carpeta = obtenerCarpetaEvidenciasLocal();
            String nombre = seleccionada.getNombre_archivo();
            String rutaLocal = carpeta + java.io.File.separator + nombre;

            Evidencia ev = new Evidencia();
            ev.setId_google_drive(seleccionada.getId_google_drive());
            ev.setNombre_archivo(nombre);
            ev.setRuta_local(rutaLocal);

            agregarEvidenciaALista(ev);
        }
    }
    // subirImagenLocal(): Abre el JFileChooser, sube la imagen a la carpeta
    // "Evidencias" del trabajo en Drive y agrega la Evidencia a la lista temporal.
    private void subirImagenLocal() {
        String directorioActual = System.getProperty("user.dir");
        JFileChooser foto = new JFileChooser(directorioActual);

        FileNameExtensionFilter filtrado = new FileNameExtensionFilter("JPG, PNG & GIF", "jpg", "png", "gif");
        foto.setFileFilter(filtrado);

        int respuesta = foto.showOpenDialog(this);

        if (respuesta == JFileChooser.APPROVE_OPTION) {
            String rutaLocal = foto.getSelectedFile().getPath();
            String nombre = new java.io.File(rutaLocal).getName();

            // Sin reintento: subir dos veces la misma imagen dejaria una copia
            // repetida en la carpeta de evidencias del trabajo.
            EjecutorGoogle.ejecutarSinReintento(
                    () -> subirImagenAEvidenciasDrive(rutaLocal),
                    idDrive -> {

                        if (!isDisplayable()) {
                            return;
                        }

                        if (idDrive == null) {
                            JOptionPane.showMessageDialog(this,
                                    "No se pudo subir la imagen a Google Drive.",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        Evidencia ev = new Evidencia();
                        ev.setId_google_drive(idDrive);
                        ev.setNombre_archivo(nombre);
                        ev.setRuta_local(rutaLocal);

                        agregarEvidenciaALista(ev);
                    },
                    error -> {

                        if (!isDisplayable()) {
                            return;
                        }
                        JOptionPane.showMessageDialog(this, EjecutorGoogle.mensajeDeError(error),
                                "Subir imagen", JOptionPane.ERROR_MESSAGE);
                    });
        }
    }

    /**
     * Busca la carpeta "Evidencias" del trabajo y sube la imagen local.
     *
     * Se ejecuta en el hilo de Google, por eso devuelve el id y no manipula la
     * interfaz. La carpeta se resuelve con el metodo del proveedor, que esta
     * vista ya no vuelve a armar por su cuenta.
     *
     * @param rutaLocalImagen archivo local a subir
     * @return id del archivo en Drive, o null si no se pudo subir
     */
    private String subirImagenAEvidenciasDrive(String rutaLocalImagen) {
        TrabajoDAO trabajoDao = new TrabajoDAO();
        Trabajo trabajo = trabajoDao.obtenerTrabajoIdCarpeta(id_trabajo);
        if (trabajo == null || trabajo.getId_carpeta() == null) {
            return null;
        }

        String idEvidencias = GoogleServicesProvider.obtenerIdCarpetaEvidencias(trabajo.getId_carpeta(), true);
        if (idEvidencias == null) {
            return null;
        }

        String idArchivo = GoogleServicesProvider.subirImagenACarpeta(rutaLocalImagen, idEvidencias);

        // Recargamos las evidencias disponibles de Drive tras subir una nueva.
        List<Evidencia> disponibles = GoogleServicesProvider.listarArchivosDeCarpeta(idEvidencias, true);
        synchronized (evidenciasDisponiblesDrive) {
            evidenciasDisponiblesDrive.clear();
            evidenciasDisponiblesDrive.addAll(disponibles);
        }

        return idArchivo;
    }

    // agregarEvidenciaALista(): Agrega una Evidencia a la vista (modeloLista y
    // evidenciasVisibles) y, si estamos en modo Nuevo, a listaTemporalFotos.
    private void agregarEvidenciaALista(Evidencia ev) {
        String nombre = ev.getNombre_archivo() != null ? ev.getNombre_archivo() : "";

        boolean esNuevo = "Guardar".equals(b_nuevo.getText());
        boolean esEditar = "Guardar".equals(b_editar.getText());

        if (esNuevo) {
            listaTemporalFotos.add(ev);
        } else if (esEditar) {
            listaFotosNuevas.add(ev);
        }

        evidenciasVisibles.add(ev);
        modeloLista.addElement(nombre);
        L_evidencias.setSelectedIndex(modeloLista.getSize() - 1);
        t_evidencia.setText(ev.getRuta_local() != null ? ev.getRuta_local() : nombre);
        cargar_foto();
    }

    private boolean datos_moficados() {
        int fila = Tabla_bitacoras.getSelectedRow();
        if (fila == -1) return false;

        String id_trabajador_original = String.valueOf(Tabla_bitacoras.getValueAt(fila, 0));
        String fecha_original = String.valueOf(Tabla_bitacoras.getValueAt(fila, 1));
        String notas_original = String.valueOf(Tabla_bitacoras.getValueAt(fila, 5));
        
        if (notas_original.equals("null")) notas_original = "";

        String seleccionPlomero = c_plomero.getSelectedItem() != null ? c_plomero.getSelectedItem().toString() : "";
        String idTrabajadorActual = "";
        if (!seleccionPlomero.isEmpty()) {
            idTrabajadorActual = seleccionPlomero.split(" - ")[0].trim();
        }

        String fechaActual = t_fecha.getText() != null ? t_fecha.getText() : "";
        String notasActual = T_notas.getText() != null ? T_notas.getText() : "";

        return !idTrabajadorActual.equals(id_trabajador_original)
                || !fechaActual.equals(fecha_original)
                || !notasActual.equals(notas_original);
    }
    
    private void b_cerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_cerrarActionPerformed
          if ("Guardar".equals(b_nuevo.getText()) || "Guardar".equals(b_editar.getText())) {
            b_cancelar.doClick(); 
                
            if (!b_cancelar.isEnabled()) {
                this.dispose(); 
            }
        } else {
            this.dispose();
        }
    }//GEN-LAST:event_b_cerrarActionPerformed

    private void c_plomeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c_plomeroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_c_plomeroActionPerformed

    private void b_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_cancelarActionPerformed
        // 1. PASO CLAVE: Detectar en qué modo estábamos antes de cancelar
        boolean es_nuevo = "Guardar".equals(b_nuevo.getText());
        boolean es_editar = "Guardar".equals(b_editar.getText());

        if (es_nuevo || es_editar) {
    
        // --- MODO NUEVO ---
            if (es_nuevo) {
                // Validación: Si los campos NO están vacíos, pedimos confirmación
                if (!campos_vacios()) { 
                    int respuesta = JOptionPane.showConfirmDialog(this, 
                     "¿Desea cancelar el registro? Se perderán los datos introducidos.", 
                     "Confirmar cancelación", JOptionPane.YES_NO_OPTION);
            
                if (respuesta != JOptionPane.YES_OPTION) {
                    return; // El usuario dijo "No", nos salimos y no cancelamos nada
                }
            }
            // Si estaban vacíos o el usuario dijo "Sí", el código sigue aquí abajo...
        }
    
        // --- MODO EDITAR ---
        else if (es_editar) {
           
            if (datos_moficados()) { 
                int respuesta = JOptionPane.showConfirmDialog(this, 
                     "¿Desea cancelar la edición? Los cambios realizados no se guardarán.", 
                     "Confirmar cancelación", JOptionPane.YES_NO_OPTION);
            
                if (respuesta != JOptionPane.YES_OPTION) {
                    return; // El usuario dijo "No", nos salimos
                }
            }
            // Si no se modificó nada o el usuario dijo "Sí", el código sigue aquí abajo...
        }
                 
        bloquear_text();        
        desbloquear_botones();  
        restablecer_color();    
        b_nuevo.setText("Nuevo");
        b_editar.setText("Editar");
        b_foto.setEnabled(false);
        b_cancelar.setEnabled(false);
        b_nuevo.setEnabled(true);
        b_nuevo.setBackground(null);
        b_cancelar.setBackground(null);
        b_editar.setBackground(null);

        int filaSeleccionada = Tabla_bitacoras.getSelectedRow();
        if (filaSeleccionada != -1) {
            cargar_datos_tabla(filaSeleccionada);
        } else {
            limpiar_text();
        }
        b_cancelar.setEnabled(false);
        Tabla_bitacoras.clearSelection();
        limpiar_text();
        Tabla_bitacoras.setEnabled(true);
        }
    }//GEN-LAST:event_b_cancelarActionPerformed

    public void restablecer_color(){
        l_plomero.setForeground(null);
        l_entrada.setForeground(null);
        l_salida.setForeground(null);
        l_notas.setForeground(null);
        l_evidencias.setForeground(null);
    }
    
    private boolean campos_vacios() {
        
    return //t_id.getText().trim().isEmpty()
        c_plomero.getSelectedItem() == null
        && time_hora_llegada.getTime() == null
        && time_hora_salida.getTime() == null
        && T_notas.getText().trim().isEmpty()
        && modeloLista.isEmpty();
    }
    
    private void Tabla_bitacorasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Tabla_bitacorasMouseClicked
        fila = this.Tabla_bitacoras.rowAtPoint(evt.getPoint());
        cargar_datos_tabla(fila);
    }//GEN-LAST:event_Tabla_bitacorasMouseClicked

    private void b_eliminar_evidenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_eliminar_evidenciaActionPerformed
        int indice = L_evidencias.getSelectedIndex();
        if (indice < 0 || indice >= evidenciasVisibles.size()) {
            Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_LEFT,
                    "Seleccione una evidencia para eliminar.");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Deseas eliminar esta evidencia?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        Evidencia ev = evidenciasVisibles.get(indice);
        boolean esNuevo = "Guardar".equals(b_nuevo.getText());
        boolean esEditar = "Guardar".equals(b_editar.getText());

        if (esNuevo) {
            // En modo Nuevo: solo quitamos de la lista temporal.
            listaTemporalFotos.remove(ev);
        } else if (esEditar) {
            // En modo Editar: si la evidencia ya existía en BD, la marcamos para eliminarla.
            if (ev.getId_evidencia() > 0) {
                listaIdsFotosEliminadas.add(ev.getId_evidencia());
            } else {
                listaFotosNuevas.remove(ev);
            }
        } else {
            // Sin modo de edición activo: eliminarla directamente de la BD.
            if (ev.getId_evidencia() > 0) {
                EvidenciaDAO evidenciaDAO = new EvidenciaDAO();
                evidenciaDAO.eliminarEvidencia(ev.getId_evidencia());
            }
        }

        evidenciasVisibles.remove(indice);
        modeloLista.remove(indice);
        t_evidencia.setText("");
        cargar_foto();

        Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_LEFT,
                "Evidencia eliminada.");
    }//GEN-LAST:event_b_eliminar_evidenciaActionPerformed

    private void L_evidenciasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_L_evidenciasKeyPressed
        
    }//GEN-LAST:event_L_evidenciasKeyPressed

    private void b_buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_buscarActionPerformed
        D_buscar_bitacora bitacora = new D_buscar_bitacora(this, true);
        bitacora.setLocationRelativeTo(V_R_Bitacora.this);
        bitacora.recibirTrabajo(id_trabajo);
        bitacora.setVisible(true);
        
        fila = bitacora.getFila();
        if (fila >= 0 && fila < Tabla_bitacoras.getRowCount()) {
            Tabla_bitacoras.setRowSelectionInterval(fila, fila);
        }
        cargar_datos_tabla(fila);      
        
        T_pestañas.setSelectedIndex(0);
        
    }//GEN-LAST:event_b_buscarActionPerformed

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
            java.util.logging.Logger.getLogger(V_R_Bitacora.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(V_R_Bitacora.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(V_R_Bitacora.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(V_R_Bitacora.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new V_R_Bitacora().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> L_evidencias;
    private javax.swing.JPanel P_botones;
    private javax.swing.JPanel P_contenedor_registrar;
    private javax.swing.JPanel P_controles_imagen;
    private javax.swing.JPanel P_datos;
    private javax.swing.JPanel P_evidencias;
    private javax.swing.JPanel P_formulario;
    private javax.swing.JPanel P_historial;
    private javax.swing.JPanel P_imagen_evidencia;
    private javax.swing.JPanel P_registrar_bitacora;
    private javax.swing.JPanel P_tabla;
    private javax.swing.JPanel P_visor_imagen;
    private javax.swing.JTextArea T_notas;
    private javax.swing.JTabbedPane T_pestañas;
    private javax.swing.JTable Tabla_bitacoras;
    private javax.swing.JTree a_bitacora;
    private javax.swing.JButton b_anterior;
    private javax.swing.JButton b_buscar;
    private javax.swing.JButton b_cancelar;
    private javax.swing.JButton b_cerrar;
    private javax.swing.JButton b_editar;
    private javax.swing.JButton b_eliminar_evidencia;
    private javax.swing.JButton b_foto;
    private javax.swing.JButton b_nuevo;
    private javax.swing.JButton b_siguiente;
    private javax.swing.JButton b_ver_imagen;
    private javax.swing.JComboBox<String> c_plomero;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPaneArbol;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JLabel l_entrada;
    private javax.swing.JLabel l_evidencia;
    private javax.swing.JLabel l_evidencias;
    private javax.swing.JLabel l_notas;
    private javax.swing.JLabel l_plomero;
    private javax.swing.JLabel l_salida;
    private javax.swing.JTextField t_evidencia;
    private javax.swing.JTextField t_fecha;
    private javax.swing.JTextField t_id;
    private com.github.lgooddatepicker.components.TimePicker time_hora_llegada;
    private com.github.lgooddatepicker.components.TimePicker time_hora_salida;
    // End of variables declaration//GEN-END:variables
}
