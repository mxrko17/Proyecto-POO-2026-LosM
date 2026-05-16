public class Auxiliar extends Tripulante {

    public Auxiliar(IdPersona id, Nombre nom, Direccion dir) {
        super(id, nom, dir);
    }

    @Override
    public void addViaje(Viaje viaje) {
        super.addViaje(viaje);
    }

    @Override
    public int getNroViajes() {
        return super.getNroViajes();
    }
}
