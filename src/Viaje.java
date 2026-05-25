import java.sql.Time;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Viaje {
    private Date fecha;
    private Time hora;
    private int precio;
    private int duracion;
    private Bus bus;
    private Auxiliar auxiliar;
    private Conductor[] conductores;
    private Terminal terminalSalida;
    private Terminal terminalLlegada;
    private Pasaje[] pasajes;

    public Viaje(Date fecha, Time hora, int precio, int dur, Bus bus, Auxiliar aux, Conductor[] conds, Terminal sale, Terminal llega) {
        this.fecha = fecha;
        this.hora = hora;
        this.precio = precio;
        this.duracion = dur;
        this.bus = bus;
        this.auxiliar = aux;
        this.conductores = conds;
        this.terminalSalida = sale;
        this.terminalLlegada = llega;
        this.pasajes = new Pasaje[0];

        if (bus != null) bus.addViaje(this);
        if (sale != null) sale.addSalida(this);
        if (llega != null) llega.addLlegada(this);
        if (aux != null) aux.addViaje(this);
        if (conds != null) {
            for (Conductor c : conds) {
                if (c != null) c.addViaje(this);
            }
        }
    }

    public Date getFecha() { return fecha; }
    public Time getHora() { return hora; }
    public int getPrecio() { return precio; }
    public void setPrecio(int precio) { this.precio = precio; }
    public void setDuracion(int duracion) { this.duracion = duracion; }

    public LocalDateTime getFechaHoraTermino() {
        LocalDateTime inicio = LocalDateTime.of(fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), hora.toLocalTime());
        return inicio.plusMinutes(duracion);
    }

    public void addPasaje(Pasaje pasaje) {
        for (Pasaje p : pasajes) if (p.equals(pasaje)) return;
        Pasaje[] nuevo = new Pasaje[pasajes.length + 1];
        for (int i = 0; i < pasajes.length; i++) nuevo[i] = pasajes[i];
        nuevo[pasajes.length] = pasaje;
        pasajes = nuevo;
    }

    public boolean existeDisponibilidad(int nroAsientos) {
        return (bus.getNroAsientos() - pasajes.length) >= nroAsientos;
    }

    public String[] getAsientos() {
        String[] resultado = new String[bus.getNroAsientos()];
        for (int i = 0; i < bus.getNroAsientos(); i++) resultado[i] = String.valueOf(i + 1);
        for (Pasaje p : pasajes) resultado[p.getAsiento() - 1] = "*";
        return resultado;
    }

    public Bus getBus() { return bus; }

    public String[][] getListaPasajeros() {
        String[][] lista = new String[pasajes.length][4];
        for (int i = 0; i < pasajes.length; i++) {
            Pasajero p = pasajes[i].getPasajero();
            lista[i][0] = p.getIdPersona().toString();
            lista[i][1] = p.getNombreCompleto().toString();
            lista[i][2] = p.getNomContacto() != null ? p.getNomContacto().toString() : "";
            lista[i][3] = p.getFonoContacto();
        }
        return lista;
    }

    public int getNroAsientosDisponibles() {
        return bus.getNroAsientos() - pasajes.length;
    }

    public Venta[] getVentas() {
        Venta[] ventasViaje = new Venta[0];
        for (Pasaje p : pasajes) {
            boolean existe = false;
            for (Venta v : ventasViaje) {
                if (v.equals(p.getVenta())) { existe = true; break; }
            }
            if (!existe) {
                Venta[] nuevo = new Venta[ventasViaje.length + 1];
                for (int i = 0; i < ventasViaje.length; i++) nuevo[i] = ventasViaje[i];
                nuevo[ventasViaje.length] = p.getVenta();
                ventasViaje = nuevo;
            }
        }
        return ventasViaje;
    }

    public Tripulante[] getTripulantes() {
        int cantCond = (conductores != null) ? conductores.length : 0;
        Tripulante[] trips = new Tripulante[1 + cantCond];
        trips[0] = auxiliar;
        for (int i = 0; i < cantCond; i++) {
            trips[i + 1] = conductores[i];
        }
        return trips;
    }

    public Terminal getTerminalLlegada() { return terminalLlegada; }
    public Terminal getTerminalSalida() { return terminalSalida; }
}