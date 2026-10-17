/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistema_en.Objetos;

/**
 *
 * @author ares
 */
public class Trabajador {
    private int id_trabajador;
    private String nombre;
    private String sexo;
    private String foto;
    private String puesto;
    private String f_nac;
    private String curp;
    private String rfc;
    
    
    public Trabajador(){
        
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
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the sexo
     */
    public String getSexo() {
        return sexo;
    }

    /**
     * @param sexo the sexo to set
     */
    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    /**
     * @return the foto
     */
    public String getFoto() {
        return foto;
    }

    /**
     * @param foto the foto to set
     */
    public void setFoto(String foto) {
        this.foto = foto;
    }

    /**
     * @return the puesto
     */
    public String getPuesto() {
        return puesto;
    }

    /**
     * @param puesto the puesto to set
     */
    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    /**
     * @return the rfc
     */
    public String getRfc() {
        return rfc;
    }

    /**
     * @param rfc the rfc to set
     */
    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    /**
     * @return the f_nac
     */
    public String getF_nac() {
        return f_nac;
    }

    /**
     * @param f_nac the f_nac to set
     */
    public void setF_nac(String f_nac) {
        this.f_nac = f_nac;
    }

    /**
     * @return the curp
     */
    public String getCurp() {
        return curp;
    }

    /**
     * @param curp the curp to set
     */
    public void setCurp(String curp) {
        this.curp = curp;
    }
}
