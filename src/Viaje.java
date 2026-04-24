import java.time.LocalDate;
import java.time.LocalTime;

class Viaje {
    private LocalDate fecha;
    private LocalTime hora;
    private int precio;
    private Bus bus;
    private Pasaje[] pasajes;

    public Viaje(LocalDate fecha, LocalTime hora, int precio, Bus bus) {
        this.fecha = fecha;
        this.hora = hora;
        this.precio = precio;
        this.bus = bus;
        this.pasajes = new Pasaje[0];

        if (bus != null) {
            bus.addViaje(this);
        }
    }

    public LocalDate getFecha() { return fecha; }
    public LocalTime getHora() { return hora; }
    public int getPrecio() { return precio; }
    public void setPrecio(int precio) { this.precio = precio; }
    public Bus getBus() { return bus; }

    public String[][] getAsientos() {
        int total = bus.getNroAsientos();
        String[][] resultado = new String[total][1];

        for (int i = 0; i < total; i++) {
            int asiento = i + 1;
            boolean ocupado = false;
            for (Pasaje p : pasajes) {
                if (p.getAsiento() == asiento) {
                    ocupado = true;
                    break;
                }
            }
            resultado[i][0] = ocupado ? "*" : String.valueOf(asiento);
        }
        return resultado;
    }

    public void addPasaje(Pasaje pasaje) {
        int asiento = pasaje.getAsiento();
        for (Pasaje p : pasajes) {
            if (p.getAsiento() == asiento) return;
        }
        Pasaje[] nuevo = new Pasaje[pasajes.length + 1];
        for (int i = 0; i < pasajes.length; i++) {
            nuevo[i] = pasajes[i];
        }
        nuevo[pasajes.length] = pasaje;
        pasajes = nuevo;
    }

    public String[][] getListaPasajeros() {
        String[][] lista = new String[pasajes.length][4];

        for (int i = 0; i < pasajes.length; i++) {
            Pasajero p = pasajes[i].getPasajero();
            lista[i][0] = p.getIdPersona().toString(); // ID
            lista[i][1] = p.getNombreCompleto().toString(); // Nombre completo
            lista[i][2] = p.getNomContacto().toString(); // Nombre de contacto
            lista[i][3] = p.getFonoContacto(); // Teléfono de contacto
        }

        return lista;
    }

    public boolean existeDisponibilidad() {
        return pasajes.length < bus.getNroAsientos();
    }

    public int getNroAsientosDisponibles() {
        return bus.getNroAsientos() - pasajes.length;
    }
}