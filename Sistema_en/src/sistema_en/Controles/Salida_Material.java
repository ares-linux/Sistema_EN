/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistema_en.Controles;

import sistema_en.Objetos.Material;
import sistema_en.Objetos.Trabajador;

/**
 *
 * @author ares
 */
public class Salida_Material {
    private int id_salida_material;
    private Trabajador id_trabajador;
    private Material  id_material;
    private String fecha;
    private String hora;
    private int cantidad;
    private String motivo;
    private String destino;
    
    public Salida_Material(){
        
    }
    
    public void Editar(){
        
    }
    public void Buscar(){
        
    }
    public void Agregar(){
        
    }
    public void Eliminar(){
        
    }

    /**
     * @return the id_salida_material
     */
    public int getId_salida_material() {
        return id_salida_material;
    }

    /**
     * @param id_salida_material the id_salida_material to set
     */
    public void setId_salida_material(int id_salida_material) {
        this.id_salida_material = id_salida_material;
    }

    /**
     * @return the id_trabajador
     */
    public Trabajador getId_trabajador() {
        return id_trabajador;
    }

    /**
     * @param id_trabajador the id_trabajador to set
     */
    public void setId_trabajador(Trabajador id_trabajador) {
        this.id_trabajador = id_trabajador;
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
     * @return the fecha
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the hora
     */
    public String getHora() {
        return hora;
    }

    /**
     * @param hora the hora to set
     */
    public void setHora(String hora) {
        this.hora = hora;
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
     * @return the motivo
     */
    public String getMotivo() {
        return motivo;
    }

    /**
     * @param motivo the motivo to set
     */
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    /**
     * @return the destino
     */
    public String getDestino() {
        return destino;
    }

    /**
     * @param destino the destino to set
     */
    public void setDestino(String destino) {
        this.destino = destino;
    }
    
}
