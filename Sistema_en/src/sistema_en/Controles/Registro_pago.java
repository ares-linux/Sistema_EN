/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistema_en.Controles;

import sistema_en.Objetos.Trabajador;
import java.time.LocalDate;

/**
 *
 * @author ares
 */
public class Registro_pago {
    private int id_registro_pago;
    private float monto_pagado;
    private LocalDate inicio_periodo;
    private LocalDate final_periodo;
    private String estatus_pago;
    private Trabajador id_trabajador;
    
public Registro_pago(){
    
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
     * @return the id_registro_pago
     */
    public int getId_registro_pago() {
        return id_registro_pago;
    }

    /**
     * @param id_registro_pago the id_registro_pago to set
     */
    public void setId_registro_pago(int id_registro_pago) {
        this.id_registro_pago = id_registro_pago;
    }

    /**
     * @return the monto_pagado
     */
    public float getMonto_pagado() {
        return monto_pagado;
    }

    /**
     * @param monto_pagado the monto_pagado to set
     */
    public void setMonto_pagado(float monto_pagado) {
        this.monto_pagado = monto_pagado;
    }

    /**
     * @return the inicio_periodo
     */
    public LocalDate getInicio_periodo() {
        return inicio_periodo;
    }

    /**
     * @param inicio_periodo the inicio_periodo to set
     */
    public void setInicio_periodo(LocalDate inicio_periodo) {
        this.inicio_periodo = inicio_periodo;
    }

    /**
     * @return the final_periodo
     */
    public LocalDate getFinal_periodo() {
        return final_periodo;
    }

    /**
     * @param final_periodo the final_periodo to set
     */
    public void setFinal_periodo(LocalDate final_periodo) {
        this.final_periodo = final_periodo;
    }

    /**
     * @return the estatus_pago
     */
    public String getEstatus_pago() {
        return estatus_pago;
    }

    /**
     * @param estatus_pago the estatus_pago to set
     */
    public void setEstatus_pago(String estatus_pago) {
        this.estatus_pago = estatus_pago;
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
