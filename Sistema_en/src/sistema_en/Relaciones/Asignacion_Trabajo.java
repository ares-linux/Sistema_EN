/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistema_en.Relaciones;

import sistema_en.Objetos.Trabajador;
import sistema_en.Objetos.Trabajo;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author ares
 */
public class Asignacion_Trabajo {
    private Trabajo id_trabajo;
    private Trabajador id_trabajador;
    private LocalDate fecha;
    private LocalTime hora_llegada;
    private LocalTime hora_salida;
    private String ev_antes;
    private String ev_despues;
    
    public Asignacion_Trabajo(){
        
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
     * @return the fecha
     */
    public LocalDate getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the hora_llegada
     */
    public LocalTime getHora_llegada() {
        return hora_llegada;
    }

    /**
     * @param hora_llegada the hora_llegada to set
     */
    public void setHora_llegada(LocalTime hora_llegada) {
        this.hora_llegada = hora_llegada;
    }

    /**
     * @return the hora_salida
     */
    public LocalTime getHora_salida() {
        return hora_salida;
    }

    /**
     * @param hora_salida the hora_salida to set
     */
    public void setHora_salida(LocalTime hora_salida) {
        this.hora_salida = hora_salida;
    }

    /**
     * @return the ev_antes
     */
    public String getEv_antes() {
        return ev_antes;
    }

    /**
     * @param ev_antes the ev_antes to set
     */
    public void setEv_antes(String ev_antes) {
        this.ev_antes = ev_antes;
    }

    /**
     * @return the ev_despues
     */
    public String getEv_despues() {
        return ev_despues;
    }

    /**
     * @param ev_despues the ev_despues to set
     */
    public void setEv_despues(String ev_despues) {
        this.ev_despues = ev_despues;
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
}
