public class Bus {
    private String patente;
    private String marca;
    private String modelo;
    private int nroAsientos;
    private Empresa empresa;
    private Viaje[] viajes;

    public Bus(String patente, int nroAsientos, Empresa emp) {
        this.patente = patente;
        this.nroAsientos = nroAsientos;
        this.empresa = emp;
        this.viajes = new Viaje[0];

        if (emp != null) {
            emp.addBus(this);
        }
    }

    public String getPatente() { return patente; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public int getNroAsientos() { return nroAsientos; }
    public Empresa getEmpresa() { return empresa; }

    public void addViaje(Viaje viaje) {
        for (Viaje v : viajes) if (v.equals(viaje)) return;
        Viaje[] nuevo = new Viaje[viajes.length + 1];
        for (int i = 0; i < viajes.length; i++) nuevo[i] = viajes[i];
        nuevo[viajes.length] = viaje;
        viajes = nuevo;
    }

    public Viaje[] getViajes() { return viajes; }
}