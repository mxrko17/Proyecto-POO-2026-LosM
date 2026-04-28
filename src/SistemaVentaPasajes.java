//Marco Basualto, Matias Baeza, Matias Cruz
import java.time.LocalDate;
import java.time.LocalTime;

public class SistemaVentaPasajes{

    private Cliente[] clientes;
    private Pasajero[] pasajeros;
    private Bus[] buses;
    private Viaje[] viajes;
    private Venta[] ventas;

    public SistemaVentaPasajes(){
        this.clientes = new Cliente[0];
        this.pasajeros = new Pasajero[0];
        this.buses = new Bus[0];
        this.viajes = new Viaje[0];
        this.ventas = new Venta[0];
    }

    public boolean createCliente(idPersona id, Nombre nom, String email){

        if (findCliente(id) != null){
            return false;
        }

        Cliente nuevoCliente = new Cliente(id, nom, email);

        Cliente[] nuevoArr = new Cliente[clientes.length + 1];
        for (int i = 0; i < clientes.length; i++) {
            nuevoArr[i] = clientes[i];
        }
        nuevoArr[clientes.length] = nuevoCliente;
        this.clientes = nuevoArr;

        return true;
    }

    public boolean createPasajero(idPersona id, Nombre nom, String fono, Nombre nomContacto, String fonoContacto){
        if (findPasajero(id) != null){
            return false;
        }

        Pasajero nuevoPasajero = new Pasajero(id, nom, fono, nomContacto, fonoContacto);

        Pasajero[] nuevoArr = new Pasajero[pasajeros.length + 1];
        for (int i = 0; i < pasajeros.length; i++){
            nuevoArr[i] = pasajeros[i];
        }
        nuevoArr[pasajeros.length] = nuevoPasajero;
        this.pasajeros = nuevoArr;

        return true;
    }

    public boolean createBus(String patente, String marca, String modelo, int nroAsientos){
        if (findBus(patente) != null){
            return false;
        }

        Bus nuevoBus = new Bus(patente, nroAsientos);
        nuevoBus.setMarca(marca);
        nuevoBus.setModelo(modelo);

        Bus[] nuevoArr = new Bus[buses.length + 1];
        for (int i = 0; i < buses.length; i++){
            nuevoArr[i] = buses[i];
        }
        nuevoArr[buses.length] = nuevoBus;
        this.buses = nuevoArr;

        return true;
    }

    public boolean createViaje(LocalDate fecha, LocalTime hora, int precio, String patBus){
        if (findViaje(fecha.toString(), hora.toString(), patBus) != null){
            return false;
        }

        Bus busAsociado = findBus(patBus);
        if (busAsociado == null){
            return false;
        }

        Viaje nuevoViaje = new Viaje(fecha, hora, precio, busAsociado);

        Viaje[] nuevoArr = new Viaje[viajes.length + 1];
        for (int i = 0; i < viajes.length; i++) {
            nuevoArr[i] = viajes[i];
        }
        nuevoArr[viajes.length] = nuevoViaje;
        this.viajes = nuevoArr;

        return true;
    }

    public boolean iniciaVenta(String idDoc, TipoDocumento tipo, LocalDate fechaVenta, idPersona idCliente){

        if (findVenta(idDoc, tipo) != null){
            return false;
        }

        Cliente cliente = findCliente(idCliente);
        if (cliente == null){
            return false;
        }

        Venta nuevaVenta = new Venta(idDoc, tipo, fechaVenta, cliente);
        Venta[] nuevoArr = new Venta[ventas.length + 1];
        for (int i = 0; i < ventas.length; i++){
            nuevoArr[i] = ventas[i];
        }
        nuevoArr[ventas.length] = nuevaVenta;
        this.ventas = nuevoArr;

        return true;
    }

    public String[][] getHorariosDisponibles(LocalDate fechaViaje){
        int contador = 0;
        for (Viaje v : viajes){
            if (v.getFecha().equals(fechaViaje)){
                contador++;
            }
        }

        if (contador == 0) return new String[0][0];
        String[][] matriz = new String[contador][4];
        int indice = 0;

        for (Viaje v : viajes) {
            if (v.getFecha().equals(fechaViaje)){
                matriz[indice][0] = v.getBus().getPatente();
                matriz[indice][1] = v.getHora().toString();
                matriz[indice][2] = String.valueOf(v.getPrecio());
                matriz[indice][3] = String.valueOf(v.getNroAsientosDisponibles());
                indice++;
            }
        }
        return matriz;
    }

    public String[][] listAsientosDeViaje(LocalDate fecha, LocalTime hora, String patBus){
        Viaje v = findViaje(fecha.toString(), hora.toString(), patBus);
        if (v != null){
            return v.getAsientos();
        }
        return new String[0][0];
    }

    public int getMontoVenta(String idDocumento, TipoDocumento tipo){
        Venta v = findVenta(idDocumento, tipo);
        if (v != null){
            return v.getMonto();
        }
        return 0;
    }

    public String getNombrePasajero(idPersona idPasajero){
        Pasajero p = findPasajero(idPasajero);
        if (p != null){
            return p.getNombreCompleto().toString();
        }
        return null;
    }

    public boolean vendePasaje(String idDoc, LocalDate fecha, LocalTime hora, String patBus, int asiento, idPersona idPasajero){
        Venta ventaEncontrada = null;
        for (Venta v : ventas) {
            if (v.getIdDocumento().equals(idDoc)){
                ventaEncontrada = v;
                break;
            }
        }
        if (ventaEncontrada == null) return false;
        Viaje viaje = findViaje(fecha.toString(), hora.toString(), patBus);
        if (viaje == null) return false;

        Pasajero pasajero = findPasajero(idPasajero);
        if (pasajero == null) return false;

        ventaEncontrada.createPasaje(asiento, viaje, pasajero);
        return true;
    }
    public String[][] listVentas() {
        String[][] lista = new String[ventas.length][7];
        java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (int i = 0; i < ventas.length; i++){
            Venta v = ventas[i];
            lista[i][0] = v.getIdDocumento();
            lista[i][1] = v.getTipo().toString();
            lista[i][2] = v.getFecha().format(fmt);
            lista[i][3] = v.getCliente().getIdPersona().toString();
            lista[i][4] = v.getCliente().getNombreCompleto().toString();

            int cant = 0;
            if (v.getPasajes() != null) {
                cant = v.getPasajes().length;
            }
            lista[i][5] = String.valueOf(cant);
            lista[i][6] = String.valueOf(v.getMonto());
        }
        return lista;
    }

    public String[][] listViajes(){
        String[][] lista = new String[viajes.length][5];
        java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (int i = 0; i < viajes.length; i++){
            Viaje v = viajes[i];
            lista[i][0] = v.getFecha().format(fmt);
            lista[i][1] = v.getHora().toString();
            lista[i][2] = String.valueOf(v.getPrecio());
            lista[i][3] = String.valueOf(v.getNroAsientosDisponibles());
            lista[i][4] = v.getBus().getPatente();
        }
        return lista;
    }

    public String[][] listPasajeros(LocalDate fecha, LocalTime hora, String patBus){
        Viaje v = findViaje(fecha.toString(), hora.toString(), patBus);
        if (v != null){
            return v.getListaPasajeros();
        }
        return new String[0][0];
    }

    private Cliente findCliente(idPersona id){
        for (Cliente c : clientes){
            if (c.getIdPersona().equals(id)){
                return c;
            }
        }
        return null;
    }

    private Venta findVenta(String idDocumento, TipoDocumento tipoDocumento){
        for (Venta v : ventas) {
            if (v.getIdDocumento().equals(idDocumento) && v.getTipo() == tipoDocumento){
                return v;
            }
        }
        return null;
    }

    private Bus findBus(String patente){
        for (Bus b : buses) {
            if (b.getPatente().equals(patente)){
                return b;
            }
        }
        return null;
    }

    private Viaje findViaje(String fecha, String hora, String patenteBus){
        LocalDate f = LocalDate.parse(fecha);
        LocalTime h = LocalTime.parse(hora);

        for (Viaje v : viajes) {
            if (v.getFecha().equals(f) && v.getHora().equals(h) && v.getBus().getPatente().equals(patenteBus)){
                return v;
            }
        }
        return null;
    }

    private Pasajero findPasajero(idPersona idPersona){
        for (Pasajero p : pasajeros) {
            if (p.getIdPersona().equals(idPersona)){
                return p;
            }
        }
        return null;
    }
}