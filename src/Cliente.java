class Cliente extends Persona {
    private String email;
    private Nombre nom;

    public Cliente (idPersona id,Nombre nom,String email) {
        this.idPersona = id;
        this.email = email;
        this.nom=nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
