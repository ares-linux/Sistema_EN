/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistema_en.Objetos;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author ares
 */
public class Trabajo {
    private int id_trabajo;
    private String descripcion;
    private String direccion;
    private LocalDate fecha_cr;
    private LocalTime hora_cr;
    private String estado;
    private int id_cliente;
    private LocalDate fecha_inicio;
    private LocalDate fecha_fin;
    private String carpeta;
    private String id_carpeta;
    private String id_evento;
    
    public Trabajo(){
        
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
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

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
     * @return the direccion
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * @param direccion the direccion to set
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * @return the fecha_cr
     */
    public LocalDate getFecha_cr() {
        return fecha_cr;
    }

    /**
     * @param fecha_cr the fecha_cr to set
     */
    public void setFecha_cr(LocalDate fecha_cr) {
        this.fecha_cr = fecha_cr;
    }

    /**
     * @return the hora_cr
     */
    public LocalTime getHora_cr() {
        return hora_cr;
    }

    /**
     * @param hora_cr the hora_cr to set
     */
    public void setHora_cr(LocalTime hora_cr) {
        this.hora_cr = hora_cr;
    }

    /**
     * @return the fecha_inicio
     */
    public LocalDate getFecha_inicio() {
        return fecha_inicio;
    }

    /**
     * @param fecha_inicio the fecha_inicio to set
     */
    public void setFecha_inicio(LocalDate fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    /**
     * @return the fecha_fin
     */
    public LocalDate getFecha_fin() {
        return fecha_fin;
    }

    /**
     * @param fecha_fin the fecha_fin to set
     */
    public void setFecha_fin(LocalDate fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    /**
     * @return the id_cliente
     */
    public int getId_cliente() {
        return id_cliente;
    }

    /**
     * @param id_cliente the id_cliente to set
     */
    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    /**
     * @return the carpeta
     */
    public String getCarpeta() {
        return carpeta;
    }

    /**
     * @param carpeta the carpeta to set
     */
    public void setCarpeta(String carpeta) {
        this.carpeta = carpeta;
    }

    /**
     * @return the id_carpeta
     */
    public String getId_carpeta() {
        return id_carpeta;
    }

    /**
     * @param id_carpeta the id_carpeta to set
     */
    public void setId_carpeta(String id_carpeta) {
        this.id_carpeta = id_carpeta;
    }

    /**
     * @return the id_evento
     */
    public String getId_evento() {
        return id_evento;
    }

    /**
     * @param id_evento the id_evento to set
     */
    public void setId_evento(String id_evento) {
        this.id_evento = id_evento;
    }
    
    
}
