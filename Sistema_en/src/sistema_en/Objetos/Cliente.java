/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistema_en.Objetos;

/**
 *
 * @author ares
 */
public class Cliente {
    private int id_cliente;
    private String nombre_c;
    private String telefono;
    private String u_modificacion;
    private String fecha_registro;
    
    public Cliente(){
        
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
     * @return the nombre_c
     */
    public String getNombre_c() {
        return nombre_c;
    }

    /**
     * @param nombre_c the nombre_c to set
     */
    public void setNombre_c(String nombre_c) {
        this.nombre_c = nombre_c;
    }

    /**
     * @return the telefono
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * @param telefono the telefono to set
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * @return the u_modificacion
     */
    public String getU_modificacion() {
        return u_modificacion;
    }

    /**
     * @param u_modificacion the u_modificacion to set
     */
    public void setU_modificacion(String u_modificacion) {
        this.u_modificacion = u_modificacion;
    }

    /**
     * @return the fecha_registro
     */
    public String getFecha_registro() {
        return fecha_registro;
    }

    /**
     * @param fecha_registro the fecha_registro to set
     */
    public void setFecha_registro(String fecha_registro) {
        this.fecha_registro = fecha_registro;
    }
    
    
}
