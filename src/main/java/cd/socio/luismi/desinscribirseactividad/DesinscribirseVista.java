package cd.socio.luismi.desinscribirseactividad;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DesinscribirseVista extends JFrame {
    private JTable tablaActividades;
    private DefaultTableModel tableModel;
    private JButton btnActualizar;

    public DesinscribirseVista() {
        setTitle("Desinscribirse de Actividades");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel();
        panelSuperior.add(new JLabel("Tus Inscripciones a Actividades"));
        add(panelSuperior, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"Nombre Actividad", "Estado", "Precio", "Cancelar"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Solo la columna del botón es editable
                return column == 3;
            }
        };

        tablaActividades = new JTable(tableModel);
        tablaActividades.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(tablaActividades);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel();
        btnActualizar = new JButton("Actualizar");
        panelInferior.add(btnActualizar);
        add(panelInferior, BorderLayout.SOUTH);
    }

    public JTable getTablaActividades() {
        return tablaActividades;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public JButton getBtnActualizar() {
        return btnActualizar;
    }
}
