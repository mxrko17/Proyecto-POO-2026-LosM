public class Tripulante extends Persona{
    private Dirrecion direccion;
    private int nroViajes;

    public Tripulante (IdPersona id, Nombre nom, Dirrecion dir){
        super(id,nom);
        this.direccion=direccion;
        this.nroViajes=0;
    }

    public Direccion getDirrecion(){
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public void addViaje(Viaje viaje) {
        nroViajes++;
    }

    public int getNroViajes() {
        return nroViajes;
    }
}
}
