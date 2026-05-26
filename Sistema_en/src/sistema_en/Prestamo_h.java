/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistema_en;

import java.time.LocalDate;

/**
 *
 * @author ares
 */
public class Prestamo_h {

    private Herramienta id_herramienta;
    private Trabajador id_trabajador;
    private int id_prestamos_h;
    private LocalDate fecha;
    private LocalDate hora;
    private String observacones;
    private LocalDate fecha_dev;
    private int cantidad;

    public Prestamo_h() {

    }

    public void Editar() {

    }

    public void Agregar() {

    }

    public void Buscar() {

    }

    public void Eliminar() {

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
     * @return the id_prestamos_h
     */
    public int getId_prestamos_h() {
        return id_prestamos_h;
    }

    /**
     * @param id_prestamos_h the id_prestamos_h to set
     */
    public void setId_prestamos_h(int id_prestamos_h) {
        this.id_prestamos_h = id_prestamos_h;
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
     * @return the hora
     */
    public LocalDate getHora() {
        return hora;
    }

    /**
     * @param hora the hora to set
     */
    public void setHora(LocalDate hora) {
        this.hora = hora;
    }

    /**
     * @return the observacones
     */
    public String getObservacones() {
        return observacones;
    }

    /**
     * @param observacones the observacones to set
     */
    public void setObservacones(String observacones) {
        this.observacones = observacones;
    }

    /**
     * @return the fecha_dev
     */
    public LocalDate getFecha_dev() {
        return fecha_dev;
    }

    /**
     * @param fecha_dev the fecha_dev to set
     */
    public void setFecha_dev(LocalDate fecha_dev) {
        this.fecha_dev = fecha_dev;
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
     * @return the id_herramienta
     */
    public Herramienta getId_herramienta() {
        return id_herramienta;
    }

    /**
     * @param id_herramienta the id_herramienta to set
     */
    public void setId_herramienta(Herramienta id_herramienta) {
        this.id_herramienta = id_herramienta;
    }

}
