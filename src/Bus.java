//Matias Baeza
class Bus {
    private String patente;
    private String marca;
    private String modelo;
    private int nroAsientos;
    private Viaje[] viajes;

    public Bus(String patente, int nroAsientos) {
        this.patente = patente;
        this.nroAsientos = nroAsientos;
        this.viajes = new Viaje[0];
    }

    public String getPatente() {
        return patente;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getNroAsientos() {
        return nroAsientos;
    }

    public void addViaje(Viaje viaje) {
        Viaje[] nuevo = new Viaje[viajes.length + 1];
        for (int i = 0; i < viajes.length; i++) {
            nuevo[i] = viajes[i];
        }
        nuevo[viajes.length] = viaje;
        viajes = nuevo;
    }
}