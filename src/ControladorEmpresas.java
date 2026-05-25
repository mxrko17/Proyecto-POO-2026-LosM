import excepciones.SistemaVentaPasajesException;
import java.util.Date;
import java.util.Optional;

public class ControladorEmpresas {
    private static ControladorEmpresas instance;
    private Empresa[] empresas;
    private Terminal[] terminales;

    private ControladorEmpresas() {
        empresas = new Empresa[0];
        terminales = new Terminal[0];
    }

    public static ControladorEmpresas getInstance() {
        if (instance == null) {
            instance = new ControladorEmpresas();
        }
        return instance;
    }

    public void createEmpresa(Rut rut, String nombre, String url) {
        if (findEmpresa(rut).isPresent()) {
            throw new SistemaVentaPasajesException("Ya existe empresa con el rut indicado");
        }
        Empresa e = new Empresa(rut, nombre);
        e.setUrl(url);

        Empresa[] nuevo = new Empresa[empresas.length + 1];
        for (int i = 0; i < empresas.length; i++) nuevo[i] = empresas[i];
        nuevo[empresas.length] = e;
        empresas = nuevo;
    }

    public void createBus(String pat, String marca, String modelo, int nroAsientos, Rut rutEmp) {
        Optional<Empresa> emp = findEmpresa(rutEmp);
        if (!emp.isPresent()) {
            throw new SistemaVentaPasajesException("No existe empresa con el rut indicado");
        }
        if (findBus(pat).isPresent()) {
            throw new SistemaVentaPasajesException("Ya existe bus con la patente indicada");
        }

        Bus b = new Bus(pat, nroAsientos, emp.get());
        b.setMarca(marca);
        b.setModelo(modelo);
    }

    public void createTerminal(String nombre, Direccion direccion) {
        if (findTerminal(nombre).isPresent()) {
            throw new SistemaVentaPasajesException("Ya existe terminal con el nombre indicado");
        }
        if (findTerminalPorComuna(direccion.getComuna()).isPresent()) {
            throw new SistemaVentaPasajesException("Ya existe terminal en la comuna indicada");
        }

        Terminal t = new Terminal(nombre, direccion);
        Terminal[] nuevo = new Terminal[terminales.length + 1];
        for (int i = 0; i < terminales.length; i++) nuevo[i] = terminales[i];
        nuevo[terminales.length] = t;
        terminales = nuevo;
    }

    public void hireConductorForEmpresa(Rut rutEmp, idPersona id, Nombre nom, Direccion dir) {
        Optional<Empresa> emp = findEmpresa(rutEmp);
        if (!emp.isPresent()) {
            throw new SistemaVentaPasajesException("No existe empresa con el rut indicado");
        }
        if (!emp.get().addConductor(id, nom, dir)) {
            throw new SistemaVentaPasajesException("Ya está contratado conductor/auxiliar con el id dado en la empresa señalada");
        }
    }

    public void hireAuxiliarForEmpresa(Rut rutEmp, idPersona id, Nombre nom, Direccion dir) {
        Optional<Empresa> emp = findEmpresa(rutEmp);
        if (!emp.isPresent()) {
            throw new SistemaVentaPasajesException("No existe empresa con el rut indicado");
        }
        if (!emp.get().addAuxiliar(id, nom, dir)) {
            throw new SistemaVentaPasajesException("Ya está contratado auxiliar/conductor con el id dado en la empresa señalada");
        }
    }

    public String[][] listEmpresas() {
        String[][] list = new String[empresas.length][6];
        for (int i = 0; i < empresas.length; i++) {
            Empresa e = empresas[i];
            String rutFormateado = String.format("%,d", e.getRut().getNumero()).replace(',', '.') + "-" + e.getRut().getDv();
            list[i][0] = rutFormateado;
            list[i][1] = e.getNombre();
            list[i][2] = e.getUrl();
            list[i][3] = String.valueOf(e.getTripulantes().length);
            list[i][4] = String.valueOf(e.getBuses().length);
            list[i][5] = String.valueOf(e.getVentas().length);
        }
        return list;
    }

    public String[][] listLlegadasSalidasTerminal(String nombre, Date fecha) {
        Optional<Terminal> t = findTerminal(nombre);
        if (!t.isPresent()) {
            throw new SistemaVentaPasajesException("No existe terminal con el nombre indicado");
        }

        String[][] resultado = new String[0][5];
        java.text.SimpleDateFormat fmtHoraSalida = new java.text.SimpleDateFormat("HH:mm");
        java.time.format.DateTimeFormatter fmtHoraLlegada = java.time.format.DateTimeFormatter.ofPattern("HH:mm");

        for (Viaje v : t.get().getSalidas()) {
            if (v.getFecha().equals(fecha)) {
                String[][] nuevo = new String[resultado.length + 1][5];
                for (int i = 0; i < resultado.length; i++) nuevo[i] = resultado[i];
                nuevo[resultado.length] = new String[]{
                        "Salida", fmtHoraSalida.format(v.getHora()), v.getBus().getPatente(),
                        v.getBus().getEmpresa().getNombre(), String.valueOf(v.getBus().getNroAsientos() - v.getNroAsientosDisponibles())
                };
                resultado = nuevo;
            }
        }

        for (Viaje v : t.get().getLlegadas()) {
            if (v.getFecha().equals(fecha)) {
                String[][] nuevo = new String[resultado.length + 1][5];
                for (int i = 0; i < resultado.length; i++) nuevo[i] = resultado[i];
                nuevo[resultado.length] = new String[]{
                        "Llegada", v.getFechaHoraTermino().toLocalTime().format(fmtHoraLlegada), v.getBus().getPatente(),
                        v.getBus().getEmpresa().getNombre(), String.valueOf(v.getBus().getNroAsientos() - v.getNroAsientosDisponibles())
                };
                resultado = nuevo;
            }
        }
        return resultado;
    }

    public String[][] listVentasEmpresa(Rut rut) {
        Optional<Empresa> emp = findEmpresa(rut);
        if (!emp.isPresent()) {
            throw new SistemaVentaPasajesException("No existe empresa con el rut indicado");
        }

        Venta[] ventas = emp.get().getVentas();
        String[][] result = new String[ventas.length][4];
        java.text.SimpleDateFormat fmtFecha = new java.text.SimpleDateFormat("dd/MM/yyyy");

        for (int i = 0; i < ventas.length; i++) {
            result[i][0] = fmtFecha.format(ventas[i].getFecha());
            result[i][1] = ventas[i].getTipo().toString().toLowerCase();
            result[i][2] = String.format("%,d", ventas[i].getMontoPagado()).replace(',', '.');
            result[i][3] = ventas[i].getTipoPago() != null ? ventas[i].getTipoPago() : "Pendiente";
        }
        return result;
    }

    protected Optional<Empresa> findEmpresa(Rut rut) {
        for (Empresa e : empresas) {
            if (e.getRut().equals(rut)) return Optional.of(e);
        }
        return Optional.empty();
    }

    protected Optional<Terminal> findTerminal(String nombre) {
        for (Terminal t : terminales) {
            if (t.getNombre().equalsIgnoreCase(nombre)) return Optional.of(t);
        }
        return Optional.empty();
    }

    protected Optional<Terminal> findTerminalPorComuna(String comuna) {
        for (Terminal t : terminales) {
            if (t.getDireccion().getComuna().equalsIgnoreCase(comuna)) return Optional.of(t);
        }
        return Optional.empty();
    }

    protected Optional<Bus> findBus(String patente) {
        for (Empresa e : empresas) {
            for (Bus b : e.getBuses()) {
                if (b.getPatente().equalsIgnoreCase(patente)) return Optional.of(b);
            }
        }
        return Optional.empty();
    }

    protected Optional<Conductor> findConductor(idPersona id, Rut rutEmpresa) {
        Optional<Empresa> emp = findEmpresa(rutEmpresa);
        if (emp.isPresent()) {
            for (Tripulante t : emp.get().getTripulantes()) {
                if (t instanceof Conductor && t.getIdPersona().equals(id)) return Optional.of((Conductor) t);
            }
        }
        return Optional.empty();
    }

    protected Optional<Auxiliar> findAuxiliar(idPersona id, Rut rutEmpresa) {
        Optional<Empresa> emp = findEmpresa(rutEmpresa);
        if (emp.isPresent()) {
            for (Tripulante t : emp.get().getTripulantes()) {
                if (t instanceof Auxiliar && t.getIdPersona().equals(id)) return Optional.of((Auxiliar) t);
            }
        }
        return Optional.empty();
    }
}