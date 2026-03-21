package cd.admin.luismi.cancelReser;

import javax.swing.*;

import giis.demo.util.ApplicationException;

import java.awt.event.*;
import java.util.List;

public class CancelReservaControlador {

    private CancelReservaModelo modelo;
    private CancelarReservaVista vista;

    public CancelReservaControlador() {

        modelo = new CancelReservaModelo();
        vista = new CancelarReservaVista();

        cargarSocios();
        vista.cboSocios.addActionListener(e -> cargarReservas());

        vista.tblReservas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                int fila = vista.tblReservas.getSelectedRow();
                int col = vista.tblReservas.getSelectedColumn();

                if (col == 3) { // Botón "Cancelar"

                    // El ID está en la columna oculta
                    int idReserva = (int) vista.modeloTabla.getValueAt(fila, 4);

                    String motivo = JOptionPane.showInputDialog(
                            vista, "Introduzca motivo de cancelación:");

                    if (motivo != null && !motivo.isBlank()) {
                        try {
                            modelo.cancelarReserva(idReserva, motivo);
                            cargarReservas();
                        } catch (ApplicationException ex) {
                            JOptionPane.showMessageDialog(vista, ex.getMessage());
                        }
                    }
                }
            }
        });

        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
    }

    private void cargarSocios() {
        vista.cboSocios.removeAllItems();
        modelo.getNombresSocios().forEach(vista.cboSocios::addItem);
    }

    private void cargarReservas() {
        vista.modeloTabla.setRowCount(0);

        String socio = (String) vista.cboSocios.getSelectedItem();
        if (socio == null) return;

        List<Object[]> reservas = modelo.getReservasSocio(socio);

        for (Object[] r : reservas) {
            String inicio = r[2].toString().substring(11);
            String fecha = r[2].toString().substring(0, 10);
            String fin = modelo.calcularFin(r[2].toString(), (int) r[3]);

            vista.modeloTabla.addRow(new Object[] {
                    r[1],                              // instalación
                    fecha,                             // fecha
                    inicio + " / " + fin,              // horas
                    "Cancelar",                       // botón
                    r[0]                               // id (oculto)
            });
        }
    }
}