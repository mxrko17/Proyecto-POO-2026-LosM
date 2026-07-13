package vista;

import controlador.ControladorEmpresas;
import excepciones.SVPException;
import utilidades.Rut;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VentanaVentasEmpresa extends JFrame {
    private JPanel ventasEmpresa;
    private JTextField textFieldRutEmpresa;
    private JButton buscarButton;
    private JTable tableVentasEmpresas;

    public VentanaVentasEmpresa() {
        setTitle("Consulta - Ventas de Empresa");
        setContentPane(ventasEmpresa);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        buscarButton.addActionListener(e -> buscarVentas());
    }

    private void buscarVentas() {
        try {
            String rutStr = textFieldRutEmpresa.getText().trim();
            if (rutStr.isEmpty()) throw new SVPException("Ingrese el R.U.T. de la empresa.");

            Rut rut = Rut.of(rutStr.replace(".", ""));
            String[][] datos = ControladorEmpresas.getInstance().listVentasEmpresa(rut);
            String[] columnas = {"FECHA", "TIPO", "MONTO PAGADO", "TIPO PAGO"};

            DefaultTableModel model = new DefaultTableModel(datos, columnas) {
                @Override
                public boolean isCellEditable(int row, int column) { return false; }
            };
            tableVentasEmpresas.setModel(model);

            if (datos.length == 0) {
                JOptionPane.showMessageDialog(this, "La empresa no registra ventas.", "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SVPException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Formato de R.U.T. inválido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }
}