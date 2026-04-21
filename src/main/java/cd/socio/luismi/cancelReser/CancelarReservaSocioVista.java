package cd.socio.luismi.cancelReser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CancelarReservaSocioVista extends JFrame {

    public JTable tabla;
    public DefaultTableModel modelo;

    public CancelarReservaSocioVista() {

        setTitle("Cancelar Mis Reservas");
        setSize(650, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        // ===============================
        // MODELO DE TABLA
        // ===============================
        modelo = new DefaultTableModel(
                new String[]{"Instalación", "Fecha", "Inicio / Fin", "Cancelar", "ID"},
                0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return columna == 3; // Solo el botón cancelar
            }
        };

        // ===============================
        // TABLA
        // ===============================
        tabla = new JTable(modelo);

        // Columna 4 = ID → ocultar visualmente
        tabla.removeColumn(tabla.getColumnModel().getColumn(4));

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(20, 20, 600, 320);

        add(scroll);
    }
}