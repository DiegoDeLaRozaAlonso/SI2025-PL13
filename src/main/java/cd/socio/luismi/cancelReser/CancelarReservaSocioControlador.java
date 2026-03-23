package cd.socio.luismi.cancelReser;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import giis.demo.util.ApplicationException;

public class CancelarReservaSocioControlador {

    private CancelReservaModeloSocio modelo;
    private CancelarReservaSocioVista vista;
    private int idSocio;

    public CancelarReservaSocioControlador(int idSocio) {
        this.idSocio = idSocio;

        modelo = new CancelReservaModeloSocio();
        vista = new CancelarReservaSocioVista();

        cargarTabla();

        vista.tabla.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = vista.tabla.getSelectedRow();
                int col  = vista.tabla.getSelectedColumn();

                if (col == 3) { // boton cancelar

                    int idReserva = (int) vista.modelo.getValueAt(fila, 4);

                    String motivo = JOptionPane.showInputDialog(
                        vista, "Motivo de cancelación:"
                    );

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

        vista.setVisible(true);
    }

    private void cargarTabla() {
        vista.modelo.setRowCount(0);

        for (Object[] r : modelo.getReservasSocio(idSocio)) {

            String fechaHora = r[2].toString();
            String fecha = fechaHora.substring(0, 10);
            String horaIni = fechaHora.substring(11);
            String horaFin = modelo.calcularFin(fechaHora, (int) r[3]);

            vista.modelo.addRow(new Object[]{
                r[1], fecha, horaIni + " / " + horaFin, "Cancelar", r[0]
            });
        }
    }
}
