/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistema_en.Objetos;

import sistema_en.Objetos.Trabajo;

/**
 *
 * @author ares
 */
public class Cotizacion {
    
    
    public Trabajo id_trabajo;
    private int id_cotizacion;
    private String concepto;
    private float precio;
    private String n_material;
    private int cantidad;
    private float precio_uni;
    private float iva;
    private float subtotal;
    private float total;
    private String estado;
    
    public Cotizacion(String informacionCompleta){
        
    }
    
    public void Editar(){
        
    }
    
    public void Eliminar(){
        
    }
    
    public void Agregar(){
        
    }
    
    public void Buscar(){
        
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
     * @return the concepto
     */
    public String getConcepto() {
        return concepto;
    }

    /**
     * @param concepto the concepto to set
     */
    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    /**
     * @return the precio
     */
    public float getPrecio() {
        return precio;
    }

    /**
     * @param precio the precio to set
     */
    public void setPrecio(float precio) {
        this.precio = precio;
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
     * @return the id_trabajo
     */
    public Trabajo getId_trabajo() {
        return id_trabajo;
    }

    /**
     * @param id_trabajo the id_trabajo to set
     */
    public void setId_trabajo(Trabajo id_trabajo) {
        this.id_trabajo = id_trabajo;
    }

    /**
     * @return the n_material
     */
    public String getN_material() {
        return n_material;
    }

    /**
     * @param n_material the n_material to set
     */
    public void setN_material(String n_material) {
        this.n_material = n_material;
    }

    /**
     * @return the cantidad
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * @param cantidad the cantidad to set
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * @return the precio_uni
     */
    public float getPrecio_uni() {
        return precio_uni;
    }

    /**
     * @param precio_uni the precio_uni to set
     */
    public void setPrecio_uni(float precio_uni) {
        this.precio_uni = precio_uni;
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

    
}
