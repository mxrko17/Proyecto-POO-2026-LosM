package vista;

import controlador.ControladorEmpresas;
import controlador.SistemaVentaPasajes;
import excepciones.SVPException;
import utilidades.Rut;
import utilidades.idPersona;

import javax.swing.*;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VentanaCrearViaje extends JFrame {
    private JPanel crearViaje;
    private JTextField textFieldFecha;
    private JTextField textFieldHora;
    private JTextField textFieldPrecio;
    private JTextField textFieldDuracion;
    private JComboBox<String> comboBoxBus;
    private JComboBox<String> comboBoxConductor;
    private JComboBox<String> comboBoxAuxiliar;
    private JComboBox<String> comboBoxTerminalOrigen;
    private JComboBox<String> comboBoxTerminalDestino;
    private JButton crearYGuardarViajeButton;

    public VentanaCrearViaje() {
        setTitle("Formulario - Crear Nuevo Viaje");
        setContentPane(crearViaje);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setSize(450, 360);
        setResizable( false );
        poblarCombosIniciales();
        comboBoxBus.addActionListener(e -> filtrarTripulantesPorBus());
        crearYGuardarViajeButton.addActionListener(e -> guardarViaje());
    }

    private void poblarCombosIniciales() {
        String[][] empresas = ControladorEmpresas.getInstance().listEmpresas();

        comboBoxTerminalOrigen.removeAllItems();
        comboBoxTerminalDestino.removeAllItems();
        comboBoxTerminalOrigen.addItem("El Pinar (Pinto)");
        comboBoxTerminalOrigen.addItem("El Nevado Alto (Talcahuano)");
        comboBoxTerminalOrigen.addItem("Sofia III (Alhué)");
        comboBoxTerminalDestino.addItem("El Pinar (Pinto)");
        comboBoxTerminalDestino.addItem("El Nevado Alto (Talcahuano)");
        comboBoxTerminalDestino.addItem("Sofia III (Alhué)");
        comboBoxBus.removeAllItems();
        comboBoxBus.addItem("ABCD12 - Mercedes-Benz 500 (Buses Ñuble)");
        comboBoxBus.addItem("EFGH34 - Volvo B450R (Buses Ñuble)");
        comboBoxBus.addItem("IJKL56 - Scania K310 (Efe Bus)");
        comboBoxBus.addItem("MNOP78 - Marcopolo Paradiso (Buses Ñuble)");
        comboBoxBus.addItem("QRST90 - Irizar i6 (Buses Ñuble)");
        filtrarTripulantesPorBus();
    }

    private void filtrarTripulantesPorBus() {
        String busSeleccionado = (String) comboBoxBus.getSelectedItem();
        if (busSeleccionado == null) return;

        comboBoxConductor.removeAllItems();
        comboBoxAuxiliar.removeAllItems();
        if (busSeleccionado.contains("Buses Ñuble")) {
            comboBoxAuxiliar.addItem("55.555.555-5 - Juan Esteban Lagos Rios");
            comboBoxConductor.addItem("66.666.666-6 - Diego Andrés Pavez Martel");
            comboBoxConductor.addItem("77.777.777-7 - Elena Rosa Saez Torres");
        }
    }

    private void guardarViaje() {
        try {
            SimpleDateFormat fmtFecha = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat fmtHora = new SimpleDateFormat("HH:mm");
            Date fecha = fmtFecha.parse(textFieldFecha.getText().trim());
            Time hora = new Time(fmtHora.parse(textFieldHora.getText().trim()).getTime());
            int precio = Integer.parseInt(textFieldPrecio.getText().trim());
            int duracion = Integer.parseInt(textFieldDuracion.getText().trim());

            String busSel = (String) comboBoxBus.getSelectedItem();
            String condSel = (String) comboBoxConductor.getSelectedItem();
            String auxSel = (String) comboBoxAuxiliar.getSelectedItem();
            String termOrigSel = (String) comboBoxTerminalOrigen.getSelectedItem();
            String termDestSel = (String) comboBoxTerminalDestino.getSelectedItem();

            if (busSel == null || condSel == null || auxSel == null || termOrigSel == null || termDestSel == null) {
                throw new SVPException("Debe seleccionar todos los componentes del viaje.");
            }
            String patenteBus = busSel.split(" - ")[0].trim();
            String rutCond = condSel.split(" - ")[0].trim().replace(".", "");
            String rutAux = auxSel.split(" - ")[0].trim().replace(".", "");
            idPersona idConductor = Rut.of(rutCond);
            idPersona idAuxiliar = Rut.of(rutAux);
            String comunaOrigen = termOrigSel.substring(termOrigSel.indexOf("(") + 1, termOrigSel.indexOf(")")).trim();
            String comunaDestino = termDestSel.substring(termDestSel.indexOf("(") + 1, termDestSel.indexOf(")")).trim();
            idPersona[] tripulantes = new idPersona[]{ idAuxiliar, idConductor };
            String[] comunas = new String[]{ comunaOrigen, comunaDestino };
            SistemaVentaPasajes.getInstance().createViaje(fecha, hora, precio, duracion, patenteBus, tripulantes, comunas);
            JOptionPane.showMessageDialog(this, "¡Viaje registrado y guardado exitosamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (SVPException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error del Sistema", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Formato de datos no válido. Verifique Fechas (DD/MM/YYYY), Horas (HH:MM) y Precios/Duración en números.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }
}