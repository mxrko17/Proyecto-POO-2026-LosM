public class Empresa {

    private Rut rut;
    private String nombre;
    private String url;

    private Bus[] buses;
    private Conductor[] conductores;
    private Auxiliar[] auxiliares;

    public Empresa(Rut rut, String nombre) {
        this.rut = rut;
        this.nombre = nombre;

        buses = new Bus[100];
        conductores = new Conductor[100];
        auxiliares = new Auxiliar[100];
    }

    public Rut getRut() {
        return rut;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void addBus(Bus bus) {

        for (int i = 0; i < buses.length; i++) {

            if (buses[i] == null) {
                buses[i] = bus;
                break;
            }
        }
    }

    public Bus[] getBuses() {
        return buses;
    }

    public boolean addConductor(IdPersona id, Nombre nom, Direccion dir) {

        for (int i = 0; i < conductores.length; i++) {

            if (conductores[i] != null &&
                    conductores[i].getIdPersona().equals(id)) {

                return false;
            }
        }

        for (int i = 0; i < auxiliares.length; i++) {

            if (auxiliares[i] != null &&
                    auxiliares[i].getIdPersona().equals(id)) {

                return false;
            }
        }

        for (int i = 0; i < conductores.length; i++) {

            if (conductores[i] == null) {

                conductores[i] = new Conductor(id, nom, dir);
                return true;
            }
        }

        return false;
    }

    public boolean addAuxiliar(IdPersona id, Nombre nom, Direccion dir) {

        for (int i = 0; i < auxiliares.length; i++) {

            if (auxiliares[i] != null &&
                    auxiliares[i].getIdPersona().equals(id)) {

                return false;
            }
        }

        for (int i = 0; i < conductores.length; i++) {

            if (conductores[i] != null &&
                    conductores[i].getIdPersona().equals(id)) {

                return false;
            }
        }

        for (int i = 0; i < auxiliares.length; i++) {

            if (auxiliares[i] == null) {

                auxiliares[i] = new Auxiliar(id, nom, dir);
                return true;
            }
        }

        return false;
    }

    public Tripulante[] getTripulantes() {

        int total = 0;

        for (int i = 0; i < conductores.length; i++) {

            if (conductores[i] != null) {
                total++;
            }
        }

        for (int i = 0; i < auxiliares.length; i++) {

            if (auxiliares[i] != null) {
                total++;
            }
        }

        Tripulante[] tripulantes = new Tripulante[total];

        int pos = 0;

        for (int i = 0; i < conductores.length; i++) {

            if (conductores[i] != null) {

                tripulantes[pos] = conductores[i];
                pos++;
            }
        }

        for (int i = 0; i < auxiliares.length; i++) {

            if (auxiliares[i] != null) {

                tripulantes[pos] = auxiliares[i];
                pos++;
            }
        }

        return tripulantes;
    }

    public Venta[] getVentas() {

        Venta[] ventas = new Venta[1000];
        int pos = 0;

        for (int i = 0; i < buses.length; i++) {

            if (buses[i] != null) {

                Viaje[] viajes = buses[i].getViajes();

                for (int j = 0; j < viajes.length; j++) {

                    if (viajes[j] != null) {

                        Venta[] ventasViaje = viajes[j].getVentas();

                        for (int k = 0; k < ventasViaje.length; k++) {

                            if (ventasViaje[k] != null) {

                                boolean repetida = false;

                                for (int x = 0; x < pos; x++) {

                                    if (ventas[x].equals(ventasViaje[k])) {
                                        repetida = true;
                                    }
                                }

                                if (!repetida) {

                                    ventas[pos] = ventasViaje[k];
                                    pos++;
                                }
                            }
                        }
                    }
                }
            }
        }

        Venta[] resultado = new Venta[pos];

        for (int i = 0; i < pos; i++) {
            resultado[i] = ventas[i];
        }

        return resultado;
    }
}