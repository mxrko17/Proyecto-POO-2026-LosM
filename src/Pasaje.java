public class Pasaje {
    private long numero;
    private int asiento;

    public Pasaje (int asiento, Viaje viaje, Pasajero pasajero, Venta venta) {
        super(asiento, viaje, pasajero, venta);
    }

    public int getNumero () {
        return numero;
    }
    public int getAsiento () {
        return asiento;
    }
    public Viaje getViaje () {
        return viaje;
    }
    public Pasajero getPasajero () {
        return pasajero;
    }
    public Venta getVenta () {
        return venta;
    }
}
