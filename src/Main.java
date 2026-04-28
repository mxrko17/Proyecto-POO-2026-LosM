//Matias Baeza, Marco Basualto, Matias Cruz
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Main {
    private static DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SistemaVentaPasajes sistema = new SistemaVentaPasajes();
        int opcion = -1;

        do {
            System.out.println("\n===================================");
            System.out.println("    ...::: Menú principal :::...");
            System.out.println("1) Crear cliente");
            System.out.println("2) Crear bus");
            System.out.println("3) Crear viaje");
            System.out.println("4) Vender pasaje");
            System.out.println("5) Lista de pasajeros");
            System.out.println("6) Lista de ventas");
            System.out.println("7) Lista de viajes");
            System.out.println("8) Consulta Viajes disponibles por fecha");
            System.out.println("9) Salir");
            System.out.println("-----------------------------------");
            System.out.print("..:: Ingrese número de opción: ");

            if (scanner.hasNextInt()) {
                opcion = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("Error: Ingrese un número válido.");
                scanner.nextLine();
                continue;
            }

            switch (opcion) {
                case 1:
                    System.out.println("\n...:::: Crear un nuevo Cliente ::::....");
                    System.out.print("Rut[1] o Pasaporte[2] : ");
                    int tId = Integer.parseInt(scanner.nextLine());
                    idPersona idCli;
                    if (tId == 1) {
                        System.out.print("R.U.T : ");
                        idCli = Rut.of(scanner.nextLine());
                    } else {
                        System.out.print("Pasaporte (numero): ");
                        String num = scanner.nextLine();
                        System.out.print("Nacionalidad : ");
                        String nac = scanner.nextLine();
                        idCli = Pasaporte.of(num, nac);
                    }

                    System.out.print("Sr.[1] o Sra. [2] : ");
                    Tratamiento trat = (Integer.parseInt(scanner.nextLine()) == 1) ? Tratamiento.SR : Tratamiento.SRA;

                    Nombre nomCli = new Nombre();
                    nomCli.setTratamiento(trat);
                    System.out.print("Nombres : ");
                    nomCli.setNombres(scanner.nextLine());
                    System.out.print("Apellido Paterno : ");
                    nomCli.setApellidoPaterno(scanner.nextLine());
                    System.out.print("Apellido Materno : ");
                    nomCli.setApellidoMaterno(scanner.nextLine());
                    System.out.print("Telefono movil : ");
                    String tel = scanner.nextLine();
                    System.out.print("Email : ");
                    String mail = scanner.nextLine();

                    if (sistema.createCliente(idCli, nomCli, mail)) {
                        System.out.println("..:::: Cliente guardado exitosamente ::::....");
                    } else {
                        System.out.println("Error: Cliente ya existe.");
                    }
                    break;

                case 2:
                    System.out.println("\n...:::: Creación de un nuevo BUS ::::....");
                    System.out.print("Patente : ");
                    String pat = scanner.nextLine();
                    System.out.print("Marca : ");
                    String mar = scanner.nextLine();
                    System.out.print("Modelo : ");
                    String mod = scanner.nextLine();
                    System.out.print("Número de asientos : ");
                    int nAs = Integer.parseInt(scanner.nextLine());

                    if (sistema.createBus(pat, mar, mod, nAs)) {
                        System.out.println("...:::: Bus guardado exitosamente ::::....");
                    } else {
                        System.out.println("Error: Patente duplicada.");
                    }
                    break;

                case 3:
                    System.out.println("\n...:::: Creación de un nuevo Viaje ::::....");
                    System.out.print("Fecha[dd/mm/yyyy] : ");
                    LocalDate fV = LocalDate.parse(scanner.nextLine(), dateFmt);
                    System.out.print("Hora[hh:mm] : ");
                    LocalTime hV = LocalTime.parse(scanner.nextLine(), timeFmt);
                    System.out.print("Precio : ");
                    int pre = Integer.parseInt(scanner.nextLine());
                    System.out.print("Patente Bus : ");
                    String pB = scanner.nextLine();

                    if (sistema.createViaje(fV, hV, pre, pB)) {
                        System.out.println("...:::: Viaje guardado exitosamente ::::....");
                    } else {
                        System.out.println("Error: Datos inválidos o viaje duplicado.");
                    }
                    break;

                case 4:
                    System.out.println("\n...:::: Venta de pasajes ::::....");
                    System.out.println(":::: Datos de la Venta");
                    System.out.print("ID Documento : ");
                    String doc = scanner.nextLine();
                    System.out.print("Tipo documento: [1] Boleta [2] Factura : ");
                    TipoDocumento tip = (Integer.parseInt(scanner.nextLine()) == 1) ? TipoDocumento.BOLETA : TipoDocumento.FACTURA;
                    System.out.print("Fecha de venta[dd/mm/yyyy] : ");
                    LocalDate fVenta = LocalDate.parse(scanner.nextLine(), dateFmt);

                    System.out.println("\n:::: Datos del cliente");
                    System.out.print("Rut[1] o Pasaporte[2] : ");
                    int tIdC = Integer.parseInt(scanner.nextLine());
                    idPersona idC;
                    if (tIdC == 1) {
                        System.out.print("R.U.T : ");
                        idC = Rut.of(scanner.nextLine());
                    } else {
                        System.out.print("Pasaporte (numero): ");
                        String numPas = scanner.nextLine();
                        System.out.print("Nacionalidad: ");
                        String nacPas = scanner.nextLine();
                        idC = Pasaporte.of(numPas, nacPas);
                    }

                    if (!sistema.iniciaVenta(doc, tip, fVenta, idC)) {
                        System.out.println("Error: No se puede concretar la venta.");
                        break;
                    }

                    String nombreCliente = "";
                    String[][] ventasActuales = sistema.listVentas();
                    for (String[] v : ventasActuales) {
                        if (v[0].equals(doc)) {
                            nombreCliente = v[4];
                            break;
                        }
                    }
                    System.out.println("         Nombre Cliente : " + nombreCliente);

                    System.out.println("\n:::: Pasajes a vender");
                    System.out.print("Cantidad de pasajes : ");
                    int cant = Integer.parseInt(scanner.nextLine());
                    System.out.print("Fecha de viaje[dd/mm/yyyy] : ");
                    LocalDate fBusca = LocalDate.parse(scanner.nextLine(), dateFmt);

                    String[][] hors = sistema.getHorariosDisponibles(fBusca);

                    if (hors.length == 0) {
                        System.out.println("\nLo sentimos, no hay viajes disponibles para la fecha seleccionada.");
                        break;
                    }
                    System.out.println("\n:::: Listado de horarios disponibles");
                    System.out.println("*-------*------------*---------*---------*----------*");
                    System.out.println("|       | BUS        | SALIDA  | VALOR   | ASIENTOS |");
                    System.out.println("|-------+------------+---------+---------+----------|");
                    for(int i = 0; i < hors.length; i++) {
                        System.out.printf("| %2d    | %-10s | %-7s | $%6s | %8s |\n", (i+1), hors[i][0], hors[i][1], hors[i][2], hors[i][3]);
                    }
                    System.out.println("*-------*------------*---------*---------*----------*");
                    System.out.print("Seleccione viaje en [1.." + hors.length + "] : ");
                    int sel = Integer.parseInt(scanner.nextLine()) - 1;

                    String[][] map = sistema.listAsientosDeViaje(fBusca, LocalTime.parse(hors[sel][1]), hors[sel][0]);
                    System.out.println("\n:::: Asientos disponibles para el viaje seleccionado");

                    System.out.println("*---*---*---*---*---*");

                    for(int i = 0; i < map.length; i += 4) {
                        String a1 = (i < map.length) ? map[i][0] : "";
                        String a2 = (i + 1 < map.length) ? map[i+1][0] : "";
                        String a4 = (i + 2 < map.length) ? map[i+2][0] : "";
                        String a3 = (i + 3 < map.length) ? map[i+3][0] : "";

                        System.out.printf("| %2s| %2s|   | %2s| %2s|\n", a1, a2, a3, a4);

                        if (i + 4 < map.length) {
                            System.out.println("|---+---+---+---+---|");
                        } else {
                            System.out.println("*---*---*---*---*---*");
                        }
                    }
                    System.out.print("\nSeleccione sus asientos [separe por ,] : ");
                    String[] selAs = scanner.nextLine().split(",");

                    idPersona[] pasajerosComprados = new idPersona[selAs.length];

                    for(int i = 0; i < selAs.length; i++) {
                        int numAs = Integer.parseInt(selAs[i].trim());
                        System.out.println("\n:::: Datos pasajeros " + (i+1));
                        System.out.print("Rut[1] o Pasaporte[2] : ");
                        int tP = Integer.parseInt(scanner.nextLine());
                        idPersona idP;
                        if (tP == 1) {
                            System.out.print("R.U.T : ");
                            idP = Rut.of(scanner.nextLine());
                        } else {
                            System.out.print("Pasaporte (numero): ");
                            String nP = scanner.nextLine();
                            System.out.print("Nacionalidad: ");
                            String naP = scanner.nextLine();
                            idP = Pasaporte.of(nP, naP);
                        }

                        pasajerosComprados[i] = idP;

                        if(!sistema.vendePasaje(doc, fBusca, LocalTime.parse(hors[sel][1]), hors[sel][0], numAs, idP)) {
                            System.out.println("Pasajero nuevo. Creando...");
                            Nombre nomP = new Nombre();
                            System.out.print("Tratamiento (SR/SRA): ");
                            nomP.setTratamiento(Tratamiento.valueOf(scanner.nextLine().toUpperCase()));
                            System.out.print("Nombres: ");
                            nomP.setNombres(scanner.nextLine());
                            System.out.print("Telefono: ");
                            String fonoP = scanner.nextLine();

                            Nombre nomC = new Nombre();
                            System.out.print("Nombre Contacto: ");
                            nomC.setNombres(scanner.nextLine());
                            System.out.print("Telefono Contacto: ");
                            String fonoC = scanner.nextLine();

                            sistema.createPasajero(idP, nomP, fonoP, nomC, fonoC);
                            sistema.vendePasaje(doc, fBusca, LocalTime.parse(hors[sel][1]), hors[sel][0], numAs, idP);
                        }
                        System.out.println(":::: Pasaje agregado exitosamente");
                    }

                    System.out.println("\n:::: Monto total de la venta: $" + sistema.getMontoVenta(doc, tip));
                    System.out.println("...:::: Venta generada exitosamente ::::\n");

                    System.out.println(":::: Imprimiendo los pasajes\n");
                    for(int i = 0; i < selAs.length; i++) {
                        int numAs = Integer.parseInt(selAs[i].trim());
                        idPersona idPasajeroTicket = pasajerosComprados[i];
                        String nombreCompleto = sistema.getNombrePasajero(idPasajeroTicket);
                        long numPasajeSimulado = Math.abs(java.util.UUID.randomUUID().getMostSignificantBits());

                        System.out.println("---------------- PASAJE ----------------");
                        System.out.println("NUMERO DE PASAJE: " + numPasajeSimulado);
                        System.out.println("FECHA DE VIAJE  : " + fBusca.format(dateFmt));
                        System.out.println("HORA DE VIAJE   : " + hors[sel][1]);
                        System.out.println("PATENTE BUS     : " + hors[sel][0]);
                        System.out.println("ASIENTO         : " + numAs);
                        System.out.println("RUT/PASAPORTE   : " + idPasajeroTicket.toString());
                        System.out.println("NOMBRE PASAJERO : " + nombreCompleto);
                        System.out.println("----------------------------------------\n");
                    }
                    break;

                case 5:
                    System.out.println("\n...:::: Listado de pasajeros de un viaje ::::....");
                    System.out.print("Fecha del viaje[dd/mm/yyyy] : ");
                    LocalDate fPas = LocalDate.parse(scanner.nextLine(), dateFmt);
                    System.out.print("Hora del viaje[hh:mm] : ");
                    LocalTime hPas = LocalTime.parse(scanner.nextLine(), timeFmt);
                    System.out.print("Patente bus : ");
                    String patPas = scanner.nextLine();

                    String[][] lPasajeros = sistema.listPasajeros(fPas, hPas, patPas);
                    if(lPasajeros.length == 0) {
                        System.out.println("No se encontraron pasajeros o el viaje no existe.");
                    } else {
                        System.out.println("*------------*-----------------*---------------------------*---------------------------*-------------------*");
                        System.out.println("| ASIENTO/ID |    RUT/PASS     | PASAJERO                  | CONTACTO                  | TELEFONO CONTACTO |");
                        System.out.println("|------------+-----------------+---------------------------+---------------------------+-------------------|");
                        for (String[] p : lPasajeros) {
                            System.out.printf("| %-10s | %-15s | %-25s | %-25s | %-17s |\n", p[0], p[1], p[2], p[3], p[4]);
                        }
                        System.out.println("*------------*-----------------*---------------------------*---------------------------*-------------------*");
                    }
                    break;

                case 6:
                    System.out.println("\n...:::: Listado de ventas ::::....");
                    String[][] lV = sistema.listVentas();
                    if (lV.length == 0) {
                        System.out.println("No hay ventas registradas.");
                    } else {
                        System.out.println("*------------*----------*------------*------------------*--------------------------*---------------*-------------*");
                        System.out.println("| ID DOCUMENT| TIPO DOCU|   FECHA    | RUT/PASAPORTE    | CLIENTE                  | CANT BOLETOS  | TOTAL VENTA |");
                        System.out.println("|------------+----------+------------+------------------+--------------------------+---------------+-------------|");
                        for(String[] v : lV) {
                            System.out.printf("| %-10s | %-8s | %-10s | %-16s | %-24s | %13s | $%10s |\n", v[0], v[1], v[2], v[3], v[4], v[5], v[6]);
                        }
                        System.out.println("*------------*----------*------------*------------------*--------------------------*---------------*-------------*");
                    }
                    break;

                case 7:
                    System.out.println("\n...:::: Listado de viajes ::::....");
                    String[][] lVia = sistema.listViajes();
                    if (lVia.length == 0) {
                        System.out.println("No hay viajes registrados.");
                    } else {
                        System.out.println("*------------*-------*---------*------------*----------*");
                        System.out.println("|    FECHA   |  HORA |  PRECIO | DIPONIBLES | PATENTE  |");
                        System.out.println("|------------+-------+---------+------------+----------|");
                        for(String[] v : lVia) {
                            System.out.printf("| %-10s | %-5s | $%6s | %10s | %-8s |\n", v[0], v[1], v[2], v[3], v[4]);
                        }
                        System.out.println("*------------*-------*---------*------------*----------*");
                    }
                    break;

                case 8:
                    System.out.println("\n...:::: Consulta Viajes disponibles por fecha ::::....");
                    System.out.print("Ingrese fecha a consultar [dd/mm/yyyy] : ");
                    LocalDate fCons = LocalDate.parse(scanner.nextLine(), dateFmt);

                    String[][] hCons = sistema.getHorariosDisponibles(fCons);
                    if (hCons.length == 0) {
                        System.out.println("No hay viajes programados para la fecha indicada.");
                    } else {
                        System.out.println("\n:::: Se encontraron " + hCons.length + " viajes disponibles");
                        System.out.println("*-------*------------*---------*---------*----------*");
                        System.out.println("| NRO   | BUS        | SALIDA  | VALOR   | ASIENTOS |");
                        System.out.println("|-------+------------+---------+---------+----------|");
                        for(int i = 0; i < hCons.length; i++) {
                            System.out.printf("| %2d    | %-10s | %-7s | $%6s | %8s |\n", (i+1), hCons[i][0], hCons[i][1], hCons[i][2], hCons[i][3]);
                        }
                        System.out.println("*-------*------------*---------*---------*----------*");
                    }
                    break;

                case 9:
                    System.out.println("Finalizando la ejecución del programa...");
                    break;
            }
        } while (opcion != 9);
    }
}