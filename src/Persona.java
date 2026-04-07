public class Persona {
    private idPersona idPersona;
    private Nombre nombreCompleto;
    private String telefono;

    public Persona(idPersona idPersona, Nombre nombreCompleto) {
        this.idPersona = idPersona;
        this.nombreCompleto = nombreCompleto;
    }

    public idPersona getIdPersona() {
        return idPersona;
    }

    public Nombre getNombreCompleto() {
        return nombreCompleto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setNombreCompleto(Nombre nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String toString(){
        return "Pasajero: " + nombreCompleto + " id: " + idPersona + " telefono: " + telefono;
    }

    public boolean equals(Object otro) {

    }
}
