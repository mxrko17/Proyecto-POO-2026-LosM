//Matías Cruz, Matías Baeza y Marco Basualto
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class SistemaVentaPasajes {

    private ArrayList<Cliente> clientes;
    private ArrayList<Pasajero> pasajeros;
    private ArrayList<Bus> buses;
    private ArrayList<Viaje> viajes;
    private ArrayList<Venta> ventas;

    public SistemaVentaPasajes() {
        clientes = new ArrayList<>();
        pasajeros = new ArrayList<>();
        buses = new ArrayList<>();
        viajes = new ArrayList<>();
        ventas = new ArrayList<>();
    }

    public boolean createCliente(idPersona id, Nombre nom, String fono, String email) {

        if (findCliente(id) != null) {
            return false;
        }

        Cliente nuevo = new Cliente(id, nom, email);
        nuevo.setTelefono(fono);
        clientes.add(nuevo);

        return true;
    }

    public boolean createPasajero(idPersona id, Nombre nom, String fono, Nombre nomContacto, String fonoContacto) {

        if (findPasajero(id) != null) {
            return false;
        }

        Pasajero nuevo = new Pasajero(id, nom, fono, nomContacto, fonoContacto);
        pasajeros.add(nuevo);

        return true;
    }

    public boolean createBus(String patente, String marca, String modelo, int nroAsientos) {

        if (findBus(patente) != null) {
            return false;
        }

        Bus nuevo = new Bus(patente, nroAsientos);
        nuevo.setMarca(marca);
        nuevo.setModelo(modelo);
        buses.add(nuevo);

        return true;
    }

    public boolean createViaje(LocalDate fecha, LocalTime hora, int precio, String patBus) {

        Bus bus = findBus(patBus);

        if (bus == null) {
            return false;
        }

        for (Viaje v : viajes) {
            if (v.getBus().getPatente().equals(patBus) && v.getFecha().equals(fecha) && v.getHora().equals(hora)) {

                return false;
            }
        }

        Viaje nuevo = new Viaje(fecha, hora, precio, bus);
        viajes.add(nuevo);

        return true;
    }

    public boolean iniciaVenta(String idDoc, TipoDocumento tipo, LocalDate fecha, idPersona idCliente) {

        if (findVenta(idDoc, tipo) != null) {
            return false;
        }

        Cliente cliente = findCliente(idCliente);

        if (cliente == null) {
            return false;
        }

        Venta nueva = new Venta(idDoc, tipo, fecha, cliente);
        ventas.add(nueva);

        return true;
    }

    public String[][] getHorariosDisponibles(LocalDate fechaViaje) {

        int count = 0;

        for (Viaje v : viajes) {
            if (v.getFecha().equals(fechaViaje) && v.existeDisponibilidad()) {
                count++;
            }
        }

        String[][] datos = new String[count][4];
        int i = 0;

        for (Viaje v : viajes) {
            if (v.getFecha().equals(fechaViaje) && v.existeDisponibilidad()) {

                datos[i][0] = v.getBus().getPatente();
                datos[i][1] = v.getHora().toString();
                datos[i][2] = String.valueOf(v.getPrecio());
                datos[i][3] = String.valueOf(v.getNroAsientosDisponibles());

                i++;
            }
        }

        return datos;
    }

    public String[][] listAsientosDeViaje(LocalDate fecha, LocalTime hora, String patBus) {

        Viaje v = findViaje(fecha.toString(), hora.toString(), patBus);

        if (v == null) {
            return new String[0][0];
        }

        return v.getAsientos();
    }

    public int getMontoVenta(String idDocumento, TipoDocumento tipo) {

        Venta v = findVenta(idDocumento, tipo);

        if (v == null) {
            return 0;
        }

        return v.getMonto();
    }

    public String getNombrePasajero(idPersona idPasajero) {

        Pasajero p = findPasajero(idPasajero);

        if (p == null) {
            return null;
        }

        return p.getNombreCompleto().toString();
    }

    public boolean vendePasaje(String idDoc, TipoDocumento tipo, LocalDate fecha, LocalTime hora, String patBus, int asiento, idPersona idPasajero) {

        Venta venta = findVenta(idDoc, tipo);

        if (venta == null) {
            return false;
        }

        Viaje viaje = findViaje(fecha.toString(), hora.toString(), patBus);

        if (viaje == null) {
            return false;
        }

        Pasajero pasajero = findPasajero(idPasajero);

        if (pasajero == null) {
            return false;
        }

        if (!viaje.existeDisponibilidad()) {
            return false;
        }

        String[][] asientos = viaje.getAsientos();

        if (asientos[asiento - 1][0].equals("*")) {
            return false;
        }

        venta.createPasaje(asiento, viaje, pasajero);

        return true;
    }

    public String[][] listPasajeros(LocalDate fecha, LocalTime hora, String patBus) {

        Viaje v = findViaje(fecha.toString(), hora.toString(), patBus);

        if (v == null) {
            return new String[0][0];
        }

        return v.getListaPasajeros();
    }

    public String[][] listVentas() {

        String[][] datos = new String[ventas.size()][4];

        for (int i = 0; i < ventas.size(); i++) {

            Venta v = ventas.get(i);

            datos[i][0] = v.getIdDocumento();
            datos[i][1] = v.getTipo().toString();
            datos[i][2] = v.getFecha().toString();
            datos[i][3] = String.valueOf(v.getMonto());
        }

        return datos;
    }

    public String[][] listViajes() {

        String[][] datos = new String[viajes.size()][5];

        for (int i = 0; i < viajes.size(); i++) {

            Viaje v = viajes.get(i);

            datos[i][0] = v.getFecha().toString();
            datos[i][1] = v.getHora().toString();
            datos[i][2] = v.getBus().getPatente();
            datos[i][3] = String.valueOf(v.getPrecio());
            datos[i][4] = String.valueOf(v.getNroAsientosDisponibles());
        }

        return datos;
    }

    private Cliente findCliente(idPersona id) {

        for (Cliente c : clientes) {
            if (c.getIdPersona().equals(id)) {
                return c;
            }
        }

        return null;
    }

    private Venta findVenta(String idDocumento, TipoDocumento tipoDocumento) {

        for (Venta v : ventas) {
            if (v.getIdDocumento().equals(idDocumento) && v.getTipo().equals(tipoDocumento)) {

                return v;
            }
        }

        return null;
    }

    private Bus findBus(String patente) {

        for (Bus b : buses) {
            if (b.getPatente().equals(patente)) {
                return b;
            }
        }

        return null;
    }

    private Viaje findViaje(String fecha, String hora, String patenteBus) {

        LocalDate f = LocalDate.parse(fecha);
        LocalTime h = LocalTime.parse(hora);

        for (Viaje v : viajes) {
            if (v.getFecha().equals(f) && v.getHora().equals(h) && v.getBus().getPatente().equals(patenteBus)) {

                return v;
            }
        }

        return null;
    }

    private Pasajero findPasajero(idPersona idPersona) {

        for (Pasajero p : pasajeros) {
            if (p.getIdPersona().equals(idPersona)) {
                return p;
            }
        }

        return null;
    }
}