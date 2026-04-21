package cd.admin.luismi.cancelReser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CancelarReservaVista extends JFrame {

    public JComboBox<String> cboSocios;
    public JTable tblReservas;
    public DefaultTableModel modeloTabla;

    public CancelarReservaVista() {

        setTitle("Cancelar Reserva - Administración");
        setBounds(100, 100, 650, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        JLabel lblSocio = new JLabel("Seleccione Socio:");
        lblSocio.setBounds(20, 20, 120, 25);
        add(lblSocio);

        cboSocios = new JComboBox<>();
        cboSocios.setBounds(150, 20, 300, 25);
        add(cboSocios);

        String[] columnas = {"Instalación", "Fecha", "Hora Ini/Fin", "Opciones", "ID"};

        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int col) {
                return col == 3;
            }
        };

        tblReservas = new JTable(modeloTabla);

        // Ocultar columna ID
        tblReservas.removeColumn(tblReservas.getColumnModel().getColumn(4));

        JScrollPane sp = new JScrollPane(tblReservas);
        sp.setBounds(20, 70, 600, 320);
        add(sp);
    }
}