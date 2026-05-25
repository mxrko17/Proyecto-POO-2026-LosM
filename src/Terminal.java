import java.util.Date;

public class Terminal {
    private String nombre;
    private Direccion direccion;
    private Viaje[] llegadas;
    private Viaje[] salidas;

    public Terminal(String nombre, Direccion direccion) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.llegadas = new Viaje[0];
        this.salidas = new Viaje[0];
    }

    public String getNombre() { return nombre; }
    public Direccion getDireccion() { return direccion; }
    public void setDireccion(Direccion direccion) { this.direccion = direccion; }

    public void addLlegada(Viaje viaje) {
        for (Viaje v : llegadas) {
            if (v.equals(viaje)) return;
        }
        Viaje[] nuevo = new Viaje[llegadas.length + 1];
        for (int i = 0; i < llegadas.length; i++) nuevo[i] = llegadas[i];
        nuevo[llegadas.length] = viaje;
        llegadas = nuevo;
    }

    public void addSalida(Viaje viaje) {
        for (Viaje v : salidas) {
            if (v.equals(viaje)) return;
        }
        Viaje[] nuevo = new Viaje[salidas.length + 1];
        for (int i = 0; i < salidas.length; i++) nuevo[i] = salidas[i];
        nuevo[salidas.length] = viaje;
        salidas = nuevo;
    }

    public Viaje[] getLlegadas() { return llegadas; }
    public Viaje[] getSalidas() { return salidas; }
}