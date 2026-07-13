package vista;

import controlador.ControladorEmpresas;
import excepciones.SVPException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VentanaTerminal extends JFrame {
    private JPanel terminalViajes;
    private JTextField textFieldNombreTerminal;
    private JTextField textFieldFecha;
    private JButton buscarButton;
    private JTable tableTerminales;

    public VentanaTerminal() {
        setTitle("Consulta - Movimientos en Terminal");
        setContentPane(terminalViajes);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        buscarButton.addActionListener(e -> buscarMovimientos());
    }

    private void buscarMovimientos() {
        try {
            String nombreTerminal = textFieldNombreTerminal.getText().trim();
            String fechaStr = textFieldFecha.getText().trim();

            if (nombreTerminal.isEmpty() || fechaStr.isEmpty()) {
                throw new SVPException("Debe ingresar el nombre del terminal y la fecha.");
            }

            SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
            fmt.setLenient(false);
            Date fecha = fmt.parse(fechaStr);

            String[][] datos = ControladorEmpresas.getInstance().listLlegadasSalidasTerminal(nombreTerminal, fecha);
            String[] columnas = {"TIPO", "HORA", "PATENTE BUS", "EMPRESA", "PASAJEROS"};

            DefaultTableModel model = new DefaultTableModel(datos, columnas) {
                @Override
                public boolean isCellEditable(int row, int column) { return false; }
            };
            tableTerminales.setModel(model);

            if (datos.length == 0) {
                JOptionPane.showMessageDialog(this, "No se encontraron movimientos para ese terminal en la fecha indicada.", "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SVPException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Negocio", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use DD/MM/YYYY.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }
}