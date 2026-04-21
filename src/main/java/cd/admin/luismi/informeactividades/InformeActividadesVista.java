package cd.admin.luismi.informeactividades;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class InformeActividadesVista {

    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnGenerarInforme;
    private JButton btnExportar;
    private JTextField txtFiltroActividad;
    private JTextField txtFiltroEstado;
    private JTextField txtFiltroEdicion;
    private JTextField txtFiltroFechaInicio;
    private JTextField txtFiltroFechaFin;

    public InformeActividadesVista() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Informe de Actividades");
        frame.setBounds(100, 100, 800, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel panelTop = new JPanel(new java.awt.GridLayout(2, 1));
        frame.getContentPane().add(panelTop, BorderLayout.NORTH);

        JPanel panelFiltros1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelTop.add(panelFiltros1);

        panelFiltros1.add(new JLabel("Actividad:"));
        txtFiltroActividad = new JTextField(10);
        panelFiltros1.add(txtFiltroActividad);

        panelFiltros1.add(new JLabel("Estado:"));
        txtFiltroEstado = new JTextField(8);
        panelFiltros1.add(txtFiltroEstado);

        panelFiltros1.add(new JLabel("Edición:"));
        txtFiltroEdicion = new JTextField(5);
        panelFiltros1.add(txtFiltroEdicion);

        JPanel panelFiltros2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelTop.add(panelFiltros2);

        panelFiltros2.add(new JLabel("Fecha Inicio:"));
        txtFiltroFechaInicio = new JTextField(8);
        panelFiltros2.add(txtFiltroFechaInicio);

        panelFiltros2.add(new JLabel("Fecha Fin:"));
        txtFiltroFechaFin = new JTextField(8);
        panelFiltros2.add(txtFiltroFechaFin);

        btnGenerarInforme = new JButton("Generar informe");
        panelFiltros2.add(btnGenerarInforme);

        btnExportar = new JButton("Exportar");
        panelFiltros2.add(btnExportar);

        JScrollPane scrollPane = new JScrollPane();
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        tableModel = new DefaultTableModel(
                new Object[][] {},
                new String[] {
                        "Actividad", "Instalación", "Estado", "Fecha Ini", "Fecha Fin",
                        "Plazas", "Reservas", "% Ocupación", "Lista Espera", "Ediciones"
                }
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        scrollPane.setViewportView(table);
    }

    public JFrame getFrame() { return frame; }
    public JTable getTable() { return table; }
    public DefaultTableModel getTableModel() { return tableModel; }
    public JButton getBtnGenerarInforme() { return btnGenerarInforme; }
    public JButton getBtnExportar() { return btnExportar; }
    public JTextField getTxtFiltroActividad() { return txtFiltroActividad; }
    public JTextField getTxtFiltroEstado() { return txtFiltroEstado; }
    public JTextField getTxtFiltroEdicion() { return txtFiltroEdicion; }
    public JTextField getTxtFiltroFechaInicio() { return txtFiltroFechaInicio; }
    public JTextField getTxtFiltroFechaFin() { return txtFiltroFechaFin; }
}
