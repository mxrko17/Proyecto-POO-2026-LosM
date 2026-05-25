public class Empresa {
    private Rut rut;
    private String nombre;
    private String url;
    private Bus[] buses;
    private Tripulante[] tripulantes;

    public Empresa(Rut rut, String nombre) {
        this.rut = rut;
        this.nombre = nombre;
        this.buses = new Bus[0];
        this.tripulantes = new Tripulante[0];
    }

    public Rut getRut() { return rut; }
    public String getNombre() { return nombre; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public void addBus(Bus bus) {
        for (Bus b : buses) if (b.equals(bus)) return;
        Bus[] nuevo = new Bus[buses.length + 1];
        for (int i = 0; i < buses.length; i++) nuevo[i] = buses[i];
        nuevo[buses.length] = bus;
        buses = nuevo;
    }

    public Bus[] getBuses() { return buses; }

    public boolean addConductor(idPersona id, Nombre nom, Direccion dir) {
        for (Tripulante t : tripulantes) {
            if (t.getIdPersona().equals(id)) return false;
        }
        Conductor c = new Conductor(id, nom, dir);
        Tripulante[] nuevo = new Tripulante[tripulantes.length + 1];
        for (int i = 0; i < tripulantes.length; i++) nuevo[i] = tripulantes[i];
        nuevo[tripulantes.length] = c;
        tripulantes = nuevo;
        return true;
    }

    public boolean addAuxiliar(idPersona id, Nombre nom, Direccion dir) {
        for (Tripulante t : tripulantes) {
            if (t.getIdPersona().equals(id)) return false;
        }
        Auxiliar a = new Auxiliar(id, nom, dir);
        Tripulante[] nuevo = new Tripulante[tripulantes.length + 1];
        for (int i = 0; i < tripulantes.length; i++) nuevo[i] = tripulantes[i];
        nuevo[tripulantes.length] = a;
        tripulantes = nuevo;
        return true;
    }

    public Tripulante[] getTripulantes() { return tripulantes; }

    public Venta[] getVentas() {
        Venta[] ventasEmpresa = new Venta[0];
        for (Bus b : buses) {
            for (Viaje vj : b.getViajes()) {
                for (Venta v : vj.getVentas()) {
                    boolean existe = false;
                    for (Venta guardada : ventasEmpresa) {
                        if (guardada.equals(v)) { existe = true; break; }
                    }
                    if (!existe) {
                        Venta[] nuevo = new Venta[ventasEmpresa.length + 1];
                        for (int i = 0; i < ventasEmpresa.length; i++) nuevo[i] = ventasEmpresa[i];
                        nuevo[ventasEmpresa.length] = v;
                        ventasEmpresa = nuevo;
                    }
                }
            }
        }
        return ventasEmpresa;
    }
}