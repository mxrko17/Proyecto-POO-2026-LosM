//Matías Cruz, Matías Baeza y Marco Basualto
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class Main {

    private static SistemaVentaPasajes sistema = new SistemaVentaPasajes();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        int opcion;

        do {
            menu();
            opcion = Integer.parseInt(sc.nextLine());

            switch (opcion) {
                case 1:
                    createCliente();
                    break;
                case 2:
                    createBus();
                    break;
                case 3:
                    createViaje();
                    break;
                case 4:
                    vendePasajes();
                    break;
                case 5:
                    listPasajerosViaje();
                    break;
                case 6:
                    listVentas();
                    break;
                case 7:
                    listViajes();
                    break;
                case 0:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }

        } while (opcion != 0);
    }

    private static void menu() {
        System.out.println("\n=== MENU PRINCIPAL ===");
        System.out.println("1. Crear Cliente");
        System.out.println("2. Crear Bus");
        System.out.println("3. Crear Viaje");
        System.out.println("4. Vender Pasajes");
        System.out.println("5. Listar Pasajeros de Viaje");
        System.out.println("6. Listar Ventas");
        System.out.println("7. Listar Viajes");
        System.out.println("0. Salir");
        System.out.print("Seleccione opción: ");
    }

    private static void createCliente() {
        System.out.println("\n--- CREAR CLIENTE ---");

        System.out.print("Tipo ID (1.RUT 2.Pasaporte): ");
        int tipo = Integer.parseInt(sc.nextLine());

        idPersona id;
        if (tipo == 1) {
            System.out.print("Ingrese RUT (12345678-9): ");
            id = Rut.of(sc.nextLine());
        } else {
            System.out.print("Número pasaporte: ");
            String num = sc.nextLine();
            System.out.print("Nacionalidad: ");
            String nac = sc.nextLine();
            id = Pasaporte.of(num, nac);
        }

        Nombre nombre = new Nombre();
        nombre.setTratamiento(Tratamiento.SR);
        System.out.print("Nombre: ");
        nombre.setNombres(sc.nextLine());
        System.out.print("Apellido paterno: ");
        nombre.setApellidoPaterno(sc.nextLine());
        System.out.print("Apellido materno: ");
        nombre.setApellidoMaterno(sc.nextLine());

        System.out.print("Teléfono: ");
        String fono = sc.nextLine();

        System.out.print("Email: ");
        String email = sc.nextLine();

        boolean ok = sistema.createCliente(id, nombre, fono, email);

        if (ok) {
            System.out.println("Cliente creado correctamente.");
        } else {
            System.out.println("Error: cliente ya existe.");
        }
    }

    private static void createBus() {
        System.out.println("\n--- CREAR BUS ---");

        System.out.print("Patente: ");
        String pat = sc.nextLine();

        System.out.print("Marca: ");
        String marca = sc.nextLine();

        System.out.print("Modelo: ");
        String modelo = sc.nextLine();

        System.out.print("Número de asientos: ");
        int asientos = Integer.parseInt(sc.nextLine());

        boolean ok = sistema.createBus(pat, marca, modelo, asientos);

        if (ok) {
            System.out.println("Bus creado correctamente.");
        } else {
            System.out.println("Error: bus ya existe.");
        }
    }

    private static void createViaje() {
        System.out.println("\n--- CREAR VIAJE ---");

        System.out.print("Fecha (YYYY-MM-DD): ");
        LocalDate fecha = LocalDate.parse(sc.nextLine());

        System.out.print("Hora (HH:MM): ");
        LocalTime hora = LocalTime.parse(sc.nextLine());

        System.out.print("Precio: ");
        int precio = Integer.parseInt(sc.nextLine());

        System.out.print("Patente bus: ");
        String pat = sc.nextLine();

        boolean ok = sistema.createViaje(fecha, hora, precio, pat);

        if (ok) {
            System.out.println("Viaje creado correctamente.");
        } else {
            System.out.println("Error al crear viaje.");
        }
    }

    private static void vendePasajes() {
        System.out.println("\n--- VENTA DE PASAJES ---");

        System.out.print("ID documento: ");
        String idDoc = sc.nextLine();

        System.out.print("Tipo (1.BOLETA 2.FACTURA): ");
        int tipoDoc = Integer.parseInt(sc.nextLine());
        TipoDocumento tipo = (tipoDoc == 1) ? TipoDocumento.BOLETA : TipoDocumento.FACTURA;

        System.out.print("Fecha venta (YYYY-MM-DD): ");
        LocalDate fechaVenta = LocalDate.parse(sc.nextLine());

        System.out.print("RUT cliente: ");
        idPersona idCliente = Rut.of(sc.nextLine());

        boolean ok = sistema.iniciaVenta(idDoc, tipo, fechaVenta, idCliente);

        if (!ok) {
            System.out.println("No se pudo iniciar la venta.");
            return;
        }

        System.out.print("Cantidad de pasajes: ");
        int cantidad = Integer.parseInt(sc.nextLine());

        System.out.print("Fecha viaje (YYYY-MM-DD): ");
        LocalDate fechaViaje = LocalDate.parse(sc.nextLine());

        String[][] viajes = sistema.getHorariosDisponibles(fechaViaje);

        for (int i = 0; i < viajes.length; i++) {
            System.out.println(i + ". Bus: " + viajes[i][0] + " Hora: " + viajes[i][1] +
                    " Precio: " + viajes[i][2] + " Disponibles: " + viajes[i][3]);
        }

        System.out.print("Seleccione viaje: ");
        int pos = Integer.parseInt(sc.nextLine());

        String pat = viajes[pos][0];
        LocalTime hora = LocalTime.parse(viajes[pos][1]);

        for (int i = 0; i < cantidad; i++) {

            String[][] asientos = sistema.listAsientosDeViaje(fechaViaje, hora, pat);

            for (int j = 0; j < asientos.length; j++) {
                System.out.print(asientos[j][0] + " ");
            }
            System.out.println();

            System.out.print("Seleccione asiento: ");
            int asiento = Integer.parseInt(sc.nextLine());

            System.out.print("RUT pasajero: ");
            idPersona idPasajero = Rut.of(sc.nextLine());

            if (sistema.getNombrePasajero(idPasajero) == null) {

                System.out.println("Pasajero no existe, creando...");

                Nombre nom = new Nombre();
                nom.setTratamiento(Tratamiento.SR);
                System.out.print("Nombre: ");
                nom.setNombres(sc.nextLine());
                System.out.print("Apellido paterno: ");
                nom.setApellidoPaterno(sc.nextLine());
                System.out.print("Apellido materno: ");
                nom.setApellidoMaterno(sc.nextLine());

                System.out.print("Teléfono: ");
                String fono = sc.nextLine();

                sistema.createPasajero(idPasajero, nom, fono, nom, fono);
            }

            sistema.vendePasaje(idDoc, tipo, fechaViaje, hora, pat, asiento, idPasajero);
        }

        int monto = sistema.getMontoVenta(idDoc, tipo);
        System.out.println("Total a pagar: " + monto);
    }

    private static void listPasajerosViaje() {
        System.out.println("\n--- LISTA PASAJEROS ---");

        System.out.print("Fecha (YYYY-MM-DD): ");
        LocalDate fecha = LocalDate.parse(sc.nextLine());

        System.out.print("Hora (HH:MM): ");
        LocalTime hora = LocalTime.parse(sc.nextLine());

        System.out.print("Patente: ");
        String pat = sc.nextLine();

        String[][] lista = sistema.listPasajeros(fecha, hora, pat);

        for (String[] fila : lista) {
            System.out.println(fila[0] + " | " + fila[1] + " | " + fila[2] + " | " + fila[3]);
        }
    }

    private static void listVentas() {
        System.out.println("\n--- LISTA VENTAS ---");

        String[][] lista = sistema.listVentas();

        for (String[] fila : lista) {
            System.out.println(fila[0] + " | " + fila[1] + " | " + fila[2] + " | $" + fila[3]);
        }
    }

    private static void listViajes() {
        System.out.println("\n--- LISTA VIAJES ---");

        String[][] lista = sistema.listViajes();

        for (String[] fila : lista) {
            System.out.println(fila[0] + " | " + fila[1] + " | " + fila[2] + " | $" + fila[3] + " | Disp: " + fila[4]);
        }
    }
}