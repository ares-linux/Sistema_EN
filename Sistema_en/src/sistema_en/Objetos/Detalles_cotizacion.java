/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistema_en.Objetos;

/**
 *
 * @author ares
 */
public class Detalles_cotizacion {
    private int id_detalle;
    private Cotizacion id_cotizacion;
    private String concepto;
    private String n_material;
    private int cantidad;
    private float precio_uni;
    private float precio;
    
    public Detalles_cotizacion(){
        
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
     * @return the id_detalle
     */
    public int getId_detalle() {
        return id_detalle;
    }

    /**
     * @param id_detalle the id_detalle to set
     */
    public void setId_detalle(int id_detalle) {
        this.id_detalle = id_detalle;
    }

    /**
     * @return the id_cotizacion
     */
    public Cotizacion getId_cotizacion() {
        return id_cotizacion;
    }

    /**
     * @param id_cotizacion the id_cotizacion to set
     */
    public void setId_cotizacion(Cotizacion id_cotizacion) {
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
    
    
    
}
