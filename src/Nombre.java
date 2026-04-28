//Matias Cruz
public class Nombre {

    private Tratamiento tratamiento;
    private String nombre,ApellidoPaterno,ApellidoMaterno;

    public Tratamiento getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(Tratamiento tratamiento) {

        this.tratamiento = tratamiento;
    }

    public String getNombres() {
        return nombre;
    }

    public void setNombres(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return ApellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        ApellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return ApellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        ApellidoMaterno = apellidoMaterno;
    }

    @Override
    public String toString() {
        String trat = (tratamiento != null) ? tratamiento.toString() : "";
        String nom = (nombre != null) ? nombre : "";
        String pat = (ApellidoPaterno != null) ? ApellidoPaterno : "";
        String mat = (ApellidoMaterno != null) ? ApellidoMaterno : "";

        return (trat + " " + nom + " " + pat + " " + mat).trim();
    }

    @Override
    public boolean equals(Object otro) {
        return super.equals(otro);
    }
}