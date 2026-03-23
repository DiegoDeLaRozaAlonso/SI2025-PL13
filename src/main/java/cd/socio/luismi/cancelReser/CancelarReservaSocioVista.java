package cd.socio.luismi.cancelReser;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class CancelarReservaSocioVista extends JFrame {

    public JTable tabla;
    public DefaultTableModel modelo;

    public CancelarReservaSocioVista() {

        setTitle("Cancelar Mis Reservas");
        setBounds(100, 100, 650, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        modelo = new DefaultTableModel(
            new String[]{"Instalación", "Fecha", "Inicio/Fin", "Cancelar", "ID"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return c == 3; }
        };

        tabla = new JTable(modelo);
        tabla.removeColumn(tabla.getColumnModel().getColumn(4)); // Ocultar ID

        JScrollPane sp = new JScrollPane(tabla);
        sp.setBounds(20, 20, 600, 320);
        add(sp);
    }
}
