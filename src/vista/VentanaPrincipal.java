package vista;

import controlador.SistemaVentaPasajes;
import excepciones.SVPException;

import javax.swing.*;

public class VentanaPrincipal extends JFrame {
    private JButton leerGuardarRecuperarButton;
    private JButton ventaDePasajeButton;
    private JButton crearUnViajeButton;
    private JButton empresasButton;
    private JButton terminalesButton;
    private JButton ventasButton;
    private JButton salirButton;
    private JPanel menuDeOpciones;

    public VentanaPrincipal() {
        setTitle("Sistema de Venta de Pasajes - Menú Principal");
        setContentPane(menuDeOpciones);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        leerGuardarRecuperarButton.addActionListener(e -> {
            String[] opciones = {"Leer Datos Iniciales (.txt)", "Guardar Estado (.obj)", "Recuperar Estado (.obj)"};
            int sel = JOptionPane.showOptionDialog(this, "Seleccione la operación de persistencia:",
                    "Persistencia de Datos", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, opciones, opciones[0]);

            try {
                if (sel == 0) {
                    SistemaVentaPasajes.getInstance().readDatosIniciales();
                    JOptionPane.showMessageDialog(this, "Datos iniciales cargados con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else if (sel == 1) {
                    SistemaVentaPasajes.getInstance().saveDatosSistema();
                    JOptionPane.showMessageDialog(this, "Estado del sistema guardado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else if (sel == 2) {
                    SistemaVentaPasajes.getInstance().readDatosSistema();
                    JOptionPane.showMessageDialog(this, "Estado del sistema restaurado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SVPException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Persistencia", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error inesperado al procesar archivos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        ventaDePasajeButton.addActionListener(e -> new VentanaVentaPasajes().setVisible(true));
        crearUnViajeButton.addActionListener(e -> new VentanaCrearViaje().setVisible(true));
        empresasButton.addActionListener(e -> new VentanaEmpresas().setVisible(true));
        terminalesButton.addActionListener(e -> new VentanaTerminal().setVisible(true));
        ventasButton.addActionListener(e -> new VentanaVentasEmpresa().setVisible(true));
        salirButton.addActionListener(e -> System.exit(0));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}