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

    public InformeActividadesVista() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Informe de Actividades");
        frame.setBounds(100, 100, 800, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel panelFiltros = new JPanel();
        frame.getContentPane().add(panelFiltros, BorderLayout.NORTH);
        panelFiltros.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JLabel lblActividad = new JLabel("Filtro Actividad (opcional):");
        panelFiltros.add(lblActividad);

        txtFiltroActividad = new JTextField();
        panelFiltros.add(txtFiltroActividad);
        txtFiltroActividad.setColumns(15);

        btnGenerarInforme = new JButton("Generar informe");
        panelFiltros.add(btnGenerarInforme);

        btnExportar = new JButton("Exportar");
        panelFiltros.add(btnExportar);

        JScrollPane scrollPane = new JScrollPane();
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        tableModel = new DefaultTableModel(
                new Object[][] {},
                new String[] {
                        "Actividad", "Instalación", "Fecha Ini", "Fecha Fin",
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
}
