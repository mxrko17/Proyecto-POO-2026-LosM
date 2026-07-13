package vista;

import controlador.ControladorEmpresas;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VentanaEmpresas extends JFrame {
    private JPanel ventanaEmpresas;
    private JTable tableEmpresa;
    private JButton actualizarListaButton;

    public VentanaEmpresas() {
        setTitle("Consulta - Listado de Empresas");
        setContentPane(ventanaEmpresas);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        cargarDatos();
        actualizarListaButton.addActionListener(e -> cargarDatos());
    }

    private void cargarDatos() {
        String[] columnas = {"RUT EMPRESA", "NOMBRE", "URL", "NRO. TRIPULANTES", "NRO. BUSES", "NRO. VENTAS"};
        String[][] datos = ControladorEmpresas.getInstance().listEmpresas();

        DefaultTableModel model = new DefaultTableModel(datos, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableEmpresa.setModel(model);
    }
}