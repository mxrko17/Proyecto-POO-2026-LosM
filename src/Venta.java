import java.time.LocalDate;

public class Venta {
    private String idDocumento;
    private TipoDocumento tipo;
    private LocalDate fecha;
    private Cliente cliente;
    private Pasaje[] pasajes;

    public Venta(String id, TipoDocumento tipo, LocalDate fec, Cliente cli) {
        this.idDocumento = id;
        this.tipo = tipo;
        this.fecha = fec;
        this.cliente = cli;
        this.pasajes = new Pasaje[0];

        if (cli != null) {
            cli.addVenta(this);
        }
    }

    public String getIdDocumento() { return idDocumento; }
    public TipoDocumento getTipo() { return tipo; }
    public LocalDate getFecha() { return fecha; }
    public Cliente getCliente() { return cliente; }
    public Pasaje[] getPasajes() { return pasajes; }

    public void createPasaje(int asiento, Viaje viaje, Pasajero pasajero) {
        Pasaje nuevoPasaje = new Pasaje(asiento, viaje, pasajero, this);

        Pasaje[] nuevoArr = new Pasaje[pasajes.length + 1];
        for (int i = 0; i < pasajes.length; i++) {
            nuevoArr[i] = pasajes[i];
        }
        nuevoArr[pasajes.length] = nuevoPasaje;
        this.pasajes = nuevoArr;
    }

    public int getMonto() {
        int total = 0;
        for (Pasaje p : pasajes) {
            total += p.getViaje().getPrecio();
        }
        return total;
    }
}