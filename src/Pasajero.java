class Pasajero extends Persona {
    private Nombre nomContacto;
    private String fonoContacto;

    public Nombre getNomContacto() {
        return nomContacto;
    }

    public String getFonoContacto() {
        return fonoContacto;
    }

    public void setNomContacto(Nombre nomContacto) {
        this.nomContacto = nomContacto;
    }

    public void setFonoContacto(String fonoContacto) {
        this.fonoContacto = fonoContacto;
    }
}
