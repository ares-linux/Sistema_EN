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
public class Bitacora {
    private int id_bitacora;
    private String trabajador;
    private LocalDate fecha;
    private LocalTime hora_llegada;
    private LocalTime hora_salida;
    private String evidencia;
    
    public Bitacora(){
        
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
     * @return the trabajador
     */
    public String getTrabajador() {
        return trabajador;
    }

    /**
     * @param trabajador the trabajador to set
     */
    public void setTrabajador(String trabajador) {
        this.trabajador = trabajador;
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
     * @return the evidencia
     */
    public String getEvidencia() {
        return evidencia;
    }

    /**
     * @param evidencia the evidencia to set
     */
    public void setEvidencia(String evidencia) {
        this.evidencia = evidencia;
    }

    /**
     * @return the id_bitacora
     */
    public int getId_bitacora() {
        return id_bitacora;
    }

    /**
     * @param id_bitacora the id_bitacora to set
     */
    public void setId_bitacora(int id_bitacora) {
        this.id_bitacora = id_bitacora;
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
    
    
}
