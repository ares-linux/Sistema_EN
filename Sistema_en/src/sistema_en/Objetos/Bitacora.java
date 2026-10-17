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
    private LocalDate fecha;
    private LocalTime hora_llegada;
    private LocalTime hora_salida;
    private java.util.List<Evidencia> evidencias;
    private String notas;
    private int id_trabajador;
    private int id_trabajo;
    
    public Bitacora(){
        
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
     * @return the notas
     */
    public String getNotas() {
        return notas;
    }

    /**
     * @param notas the notas to set
     */
    public void setNotas(String notas) {
        this.notas = notas;
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

    /**
     * @return the id_trabajador
     */
    public int getId_trabajador() {
        return id_trabajador;
    }

    /**
     * @param id_trabajador the id_trabajador to set
     */
    public void setId_trabajador(int id_trabajador) {
        this.id_trabajador = id_trabajador;
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
     * @return the evidencias
     */
    public java.util.List<Evidencia> getEvidencias() {
        return evidencias;
    }

    /**
     * @param evidencias the evidencias to set
     */
    public void setEvidencias(java.util.List<Evidencia> evidencias) {
        this.evidencias = evidencias;
    }

}
