package cd.socio.luismi.cancelReser;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import giis.demo.util.ApplicationException;

public class CancelarReservaSocioControlador {

    private CancelReservaModeloSocio modelo;
    private CancelarReservaSocioVista vista;
    private int idSocio;

    public CancelarReservaSocioControlador(int idSocio,
                                           CancelReservaModeloSocio modelo,
                                           CancelarReservaSocioVista vista) {
        this.idSocio = idSocio;
        this.modelo = modelo;
        this.vista = vista;

        cargarTabla();

        vista.tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                int fila = vista.tabla.getSelectedRow();
                int col  = vista.tabla.getSelectedColumn();

                // columna 3 = botón “Cancelar”
                if (col == 3) {
                    int idReserva = (int) vista.modelo.getValueAt(fila, 4);

                    String motivo = JOptionPane.showInputDialog(
                            vista, "Motivo de cancelación:");

                    if (motivo != null && !motivo.isBlank()) {
                        try {
                            modelo.cancelarReserva(idReserva, idSocio, motivo);
                            cargarTabla();
                        } catch (ApplicationException ex) {
                            JOptionPane.showMessageDialog(vista, ex.getMessage());
                        }
                    }
                }
            }
        });
    }

    private void cargarTabla() {
        vista.modelo.setRowCount(0);

        for (Object[] r : modelo.getReservasSocio(idSocio)) {

            String fechaHora = r[2].toString();
            String fecha = fechaHora.substring(0, 10);
            String horaIni = fechaHora.substring(11);
            String horaFin = modelo.calcularFin(fechaHora, (int) r[3]);

            vista.modelo.addRow(new Object[]{
                r[1],                 // instalación
                fecha,                // fecha
                horaIni + " / " + horaFin, // horas
                "Cancelar",            // botón
                r[0]                  // id (oculto)
            });
        }
    }
}