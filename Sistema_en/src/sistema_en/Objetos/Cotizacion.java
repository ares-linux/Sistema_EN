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

    
}
