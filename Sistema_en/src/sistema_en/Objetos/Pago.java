/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistema_en.Objetos;

import java.time.LocalDate;

/**
 *
 * @author ares
 */
public class Pago {
    private int id_pago;
    private int id_trabajo;
    private float monto;
    private String concepto;
    private LocalDate fecha_pago;
    
    public Pago(){
        
    }
    
    
    public void Editar(){
        
    }
        
    public void Agregar(){
        
    }

    /**
     * @return the id_pago
     */
    public int getId_pago() {
        return id_pago;
    }

    /**
     * @param id_pago the id_pago to set
     */
    public void setId_pago(int id_pago) {
        this.id_pago = id_pago;
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
     * @return the monto
     */
    public float getMonto() {
        return monto;
    }

    /**
     * @param monto the monto to set
     */
    public void setMonto(float monto) {
        this.monto = monto;
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
     * @return the fecha_pago
     */
    public LocalDate getFecha_pago() {
        return fecha_pago;
    }

    /**
     * @param fecha_pago the fecha_pago to set
     */
    public void setFecha_pago(LocalDate fecha_pago) {
        this.fecha_pago = fecha_pago;
    }
    
}
