/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistema_en;

/**
 *
 * @author ares
 */
public class Cotizacion_Material {
    private int id_cotizacion_material;
    private Trabajo id_trabajo;
    private Material id_material;
    private float precio_al_momento;
    private int cantidad;
    
    public Cotizacion_Material(){
        
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
     * @return the id_cotizacion_material
     */
    public int getId_cotizacion_material() {
        return id_cotizacion_material;
    }

    /**
     * @param id_cotizacion_material the id_cotizacion_material to set
     */
    public void setId_cotizacion_material(int id_cotizacion_material) {
        this.id_cotizacion_material = id_cotizacion_material;
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
     * @return the id_material
     */
    public Material getId_material() {
        return id_material;
    }

    /**
     * @param id_material the id_material to set
     */
    public void setId_material(Material id_material) {
        this.id_material = id_material;
    }

    /**
     * @return the precio_al_momento
     */
    public float getPrecio_al_momento() {
        return precio_al_momento;
    }

    /**
     * @param precio_al_momento the precio_al_momento to set
     */
    public void setPrecio_al_momento(float precio_al_momento) {
        this.precio_al_momento = precio_al_momento;
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
}
