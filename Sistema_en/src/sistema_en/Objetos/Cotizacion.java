/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistema_en.Objetos;

import java.time.LocalDate;

/**
 *
 * @author ares
 */
public class Cotizacion {
    private int id_cotizacion;
    private float subtotal;
    private float iva;
    private float total;
    private String estado;
    private int id_trabajo;
    private String tipo;
    private LocalDate fecha_creacion;
    private int archivada;
    private LocalDate fecha_archivado;
    private String nombre_cliente;
    private String telefono_cliente;
    
    
    public Cotizacion(){
        
    }

    /**
     * @return the id_cotizacion
     */
    public int getId_cotizacion() {
        return id_cotizacion;
    }

    /**
     * @param id_cotizacion the id_cotizacion to set
     */
    public void setId_cotizacion(int id_cotizacion) {
        this.id_cotizacion = id_cotizacion;
    }


    /**
     * @return the id_trabajo
     */
    
    /**
     * @return the estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }



    /**
     * @return the iva
     */
    public float getIva() {
        return iva;
    }

    /**
     * @param iva the iva to set
     */
    public void setIva(float iva) {
        this.iva = iva;
    }

    /**
     * @return the subtotal
     */
    public float getSubtotal() {
        return subtotal;
    }

    /**
     * @param subtotal the subtotal to set
     */
    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }

    /**
     * @return the total
     */
    public float getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(float total) {
        this.total = total;
    }

    /**
     * @return the id_trabajo
     */
    public int getId_trabajo() {
        return id_trabajo;
    }

    /**
     * @param id_trabajo the id_trabajo to set
     */
    public void setId_trabajo(int id_trabajo) {
        this.id_trabajo = id_trabajo;
    }

    /**
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the fecha_creacion
     */
    public LocalDate getFecha_creacion() {
        return fecha_creacion;
    }

    /**
     * @param fecha_creacion the fecha_creacion to set
     */
    public void setFecha_creacion(LocalDate fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    /**
     * @return 1 si la cotizacion de prueba esta archivada, 0 si esta activa
     */
    public int getArchivada() {
        return archivada;
    }

    /**
     * @param archivada the archivada to set
     */
    public void setArchivada(int archivada) {
        this.archivada = archivada;
    }

    /**
     * @return the fecha_archivado
     */
    public LocalDate getFecha_archivado() {
        return fecha_archivado;
    }

    /**
     * @param fecha_archivado the fecha_archivado to set
     */
    public void setFecha_archivado(LocalDate fecha_archivado) {
        this.fecha_archivado = fecha_archivado;
    }

    /**
     * @return the nombre_cliente
     */
    public String getNombre_cliente() {
        return nombre_cliente;
    }

    /**
     * @param nombre_cliente the nombre_cliente to set
     */
    public void setNombre_cliente(String nombre_cliente) {
        this.nombre_cliente = nombre_cliente;
    }

    /**
     * @return the telefono_cliente
     */
    public String getTelefono_cliente() {
        return telefono_cliente;
    }

    /**
     * @param telefono_cliente the telefono_cliente to set
     */
    public void setTelefono_cliente(String telefono_cliente) {
        this.telefono_cliente = telefono_cliente;
    }

}
