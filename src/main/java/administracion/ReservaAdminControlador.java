package administracion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ReservaAdminControlador {

    private ReservaAdminVista vista;
    private ReservaAdminModelo modelo;

    public ReservaAdminControlador(ReservaAdminVista vista, ReservaAdminModelo modelo) {
        this.vista = vista;
        this.modelo = modelo;

        this.vista.getBtnReserv().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarReserva();
            }
        });

        

        this.vista.getTextFNumHoras().getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizarPrecio();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizarPrecio();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizarPrecio();
            }
        });
    }

    private void actualizarPrecio() {

        String texto = vista.getTextFNumHoras().getText();

        if (texto.isEmpty()) {
            vista.setPrecio(0);
            return;
        }

        try {
            int horas = Integer.parseInt(texto);

            if (horas <= 0) {
                vista.setPrecio(0);
                return;
            }

            String nombreInstalacion = vista.getTextFInstalaciones().getText();
            double precio = modelo.calcularPrecio(nombreInstalacion, horas);
            vista.setPrecio(precio);

        } catch (NumberFormatException e) {
            vista.setPrecio(0);
        }
    }

    private void realizarReserva() {

        try {

            String usuario = vista.getTextFUsuarios().getText().trim();
            String instalacion = vista.getTextFInstalaciones().getText().trim();
            String fechaTexto = vista.getTextFFecha().getText().trim();
            String horaTexto = vista.getTextFHora().getText().trim();

            int horas = Integer.parseInt(
                    vista.getTextFNumHoras().getText().trim());

            // ✅ Solo 1, 2 o 3 horas
            if (horas < 1 || horas > 3) {
                JOptionPane.showMessageDialog(null,
                        "Solo se pueden reservar 1, 2 o 3 horas");
                return;
            }

            LocalDate fecha = LocalDate.parse(
                    fechaTexto,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            LocalTime horaInicio = LocalTime.parse(
                    horaTexto,
                    DateTimeFormatter.ofPattern("HH:mm"));

            // Hora en punto
            if (horaInicio.getMinute() != 0) {
                JOptionPane.showMessageDialog(null,
                        "La hora debe ser en punto (ej: 17:00)");
                return;
            }

            // Entre 08:00 y 20:00
            LocalTime apertura = LocalTime.of(8, 0);
            LocalTime cierre = LocalTime.of(20, 0);

            if (horaInicio.isBefore(apertura) || horaInicio.isAfter(cierre)) {
                JOptionPane.showMessageDialog(null,
                        "Las reservas solo pueden empezar entre 08:00 y 20:00");
                return;
            }

            // Que no termine después de las 20:00
            LocalTime horaFin = horaInicio.plusHours(horas);

            if (horaFin.isAfter(cierre)) {
                JOptionPane.showMessageDialog(null,
                        "La reserva no puede terminar después de las 20:00");
                return;
            }

            boolean exito = modelo.guardarReserva(
                    usuario, instalacion, fechaTexto, horaTexto, horas);

            if (exito) {
                JOptionPane.showMessageDialog(null,
                        "Reserva realizada correctamente");
            } else {
                JOptionPane.showMessageDialog(null,
                        "No se pudo realizar la reserva (usuario, instalación o solapamiento)");
            }

        } catch (DateTimeParseException e) {

            JOptionPane.showMessageDialog(null,
                    "Revisa fecha (yyyy-MM-dd) y hora (HH:mm)");

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null,
                    "Error inesperado: " + e.getMessage());
        }
    }
    
    
}