package vista;

import controlador.SistemaVentaPasajes;
import excepciones.SVPException;
import modelo.TipoDocumento;
import utilidades.Rut;
import utilidades.idPersona;

import javax.swing.*;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class VentanaVentaPasajes extends JFrame {
    private JButton generarBoletaButton;
    private JTextField textFieldRut;
    private JTextField textFieldFechaViaje;
    private JComboBox<String> comboBoxComunaOrigen;
    private JComboBox<String> comboBoxComunaDestino;
    private JButton viajeButton;
    private JButton ventaButton;
    private JTextField textFieldPasajero;
    private JPanel ventaPasajes;

    private String patenteSeleccionada;
    private Time horaSeleccionada;
    private String idDocumentoUltimaVenta = "1001";

    public VentanaVentaPasajes() {
        setTitle("Módulo de Venta de Pasajes");
        setContentPane(ventaPasajes);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setResizable( false );
        poblarComunasDisponibles();
        viajeButton.addActionListener(e -> buscarHorariosDisponibles());
        ventaButton.addActionListener(e -> realizarVentaYPago());
        generarBoletaButton.addActionListener(e -> emitirBoletaTexto());
    }

    private void poblarComunasDisponibles() {
        comboBoxComunaOrigen.removeAllItems();
        comboBoxComunaDestino.removeAllItems();

        Set<String> comunas = new HashSet<>();
        String[][] viajes = SistemaVentaPasajes.getInstance().listViajes();
        for (String[] v : viajes) {
            if (v[6] != null && !v[6].equals("N/A")) comunas.add(v[6].toUpperCase().trim());
            if (v[7] != null && !v[7].equals("N/A")) comunas.add(v[7].toUpperCase().trim());
        }
        if (comunas.isEmpty()) {
            comunas.add("PINTO");
            comunas.add("TALCAHUANO");
            comunas.add("ALHUÉ");
            comunas.add("CHILLAN");
            comunas.add("SAN CARLOS");
        }

        for (String c : comunas) {
            comboBoxComunaOrigen.addItem(c);
            comboBoxComunaDestino.addItem(c);
        }
    }

    private void buscarHorariosDisponibles() {
        try {
            SimpleDateFormat fmtFecha = new SimpleDateFormat("dd/MM/yyyy");
            Date fecha = fmtFecha.parse(textFieldFechaViaje.getText().trim());

            String origen = (String) comboBoxComunaOrigen.getSelectedItem();
            String destino = (String) comboBoxComunaDestino.getSelectedItem();

            if (origen == null || destino == null) {
                throw new SVPException("Debe seleccionar comuna de origen y destino.");
            }
            String[][] horarios = SistemaVentaPasajes.getInstance().getHorariosDisponibles(fecha, origen, destino, 1);

            if (horarios.length == 0) {
                JOptionPane.showMessageDialog(this,
                        "No se encontraron viajes disponibles para la fecha " + textFieldFechaViaje.getText() +
                                " entre " + origen + " y " + destino,
                        "Sin Disponibilidad", JOptionPane.INFORMATION_MESSAGE);
                patenteSeleccionada = null;
                horaSeleccionada = null;
            } else {
                String[] opciones = new String[horarios.length];
                for (int i = 0; i < horarios.length; i++) {
                    opciones[i] = "Bus: " + horarios[i][0] + " | Hora: " + horarios[i][1] + " | Precio: $" + horarios[i][2] + " | Asientos: " + horarios[i][3];
                }

                String seleccion = (String) JOptionPane.showInputDialog(this,
                        "Seleccione el horario deseado:", "Viajes Disponibles",
                        JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

                if (seleccion != null) {
                    String[] partes = seleccion.split("\\|");
                    patenteSeleccionada = partes[0].replace("Bus:", "").trim();
                    String horaStr = partes[1].replace("Hora:", "").trim();

                    SimpleDateFormat fmtHora = new SimpleDateFormat("HH:mm");
                    horaSeleccionada = new Time(fmtHora.parse(horaStr).getTime());

                    JOptionPane.showMessageDialog(this, "Viaje seleccionado: Bus " + patenteSeleccionada + " a las " + horaStr + " hrs.", "Viaje Fijado", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (SVPException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ingrese una fecha válida en formato DD/MM/YYYY.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void realizarVentaYPago() {
        try {
            String rutClienteStr = textFieldRut.getText().trim();
            String rutPasajeroStr = textFieldPasajero.getText().trim();
            String fechaStr = textFieldFechaViaje.getText().trim();

            if (rutClienteStr.isEmpty() || rutPasajeroStr.isEmpty() || fechaStr.isEmpty()) {
                throw new SVPException("Debe completar el RUT del cliente, el RUT del pasajero y la fecha del viaje.");
            }

            if (patenteSeleccionada == null || horaSeleccionada == null) {
                throw new SVPException("Primero debe presionar 'Viaje' (Buscar viaje) y seleccionar un horario disponible.");
            }

            SimpleDateFormat fmtFecha = new SimpleDateFormat("dd/MM/yyyy");
            Date fechaViaje = fmtFecha.parse(fechaStr);

            String comunaOrigen = (String) comboBoxComunaOrigen.getSelectedItem();
            String comunaDestino = (String) comboBoxComunaDestino.getSelectedItem();

            idPersona idCliente = Rut.of(rutClienteStr.replace(".", ""));
            idPersona idPasajero = Rut.of(rutPasajeroStr.replace(".", ""));
            idDocumentoUltimaVenta = "V-" + System.currentTimeMillis() % 10000;

            SistemaVentaPasajes.getInstance().iniciaVenta(idDocumentoUltimaVenta, TipoDocumento.BOLETA, fechaViaje, comunaOrigen, comunaDestino, idCliente, 1);
            SistemaVentaPasajes.getInstance().vendePasaje(idDocumentoUltimaVenta, TipoDocumento.BOLETA, fechaViaje, horaSeleccionada, patenteSeleccionada, 1, idPasajero);
            SistemaVentaPasajes.getInstance().pagaVenta(idDocumentoUltimaVenta, TipoDocumento.BOLETA);

            JOptionPane.showMessageDialog(this, "¡Venta realizadada y pagada exitosamente!\nFolio Boleta: " + idDocumentoUltimaVenta, "Éxito en la Venta", JOptionPane.INFORMATION_MESSAGE);
        } catch (SVPException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error en la Venta", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Verifique el formato del RUT y la fecha.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void emitirBoletaTexto() {
        try {
            if (idDocumentoUltimaVenta == null) {
                throw new SVPException("No hay ninguna venta realizada en esta sesión para generar la boleta.");
            }
            SistemaVentaPasajes.getInstance().generatePasajesVenta(idDocumentoUltimaVenta, TipoDocumento.BOLETA);
            JOptionPane.showMessageDialog(this, "Se ha generado el archivo de texto con el comprobante/boleta del pasaje.", "Pasaje Electrónico Generado", JOptionPane.INFORMATION_MESSAGE);

        } catch (SVPException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al Generar Boleta", JOptionPane.ERROR_MESSAGE);
        }
    }
}