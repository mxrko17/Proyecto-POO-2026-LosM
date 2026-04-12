class Cliente extends Persona {
    private String email;

    public Cliente (idPersona id, Nombre nom, String email) {
        this.idPersona = id;
        this.Nombre = nom;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
