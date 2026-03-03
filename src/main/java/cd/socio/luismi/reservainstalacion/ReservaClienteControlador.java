package cd.socio.luismi.reservainstalacion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ReservaClienteControlador {

    private ReservaClienteVista vista;
    private ReservaClienteModelo modelo;
    private String UserName;

    public ReservaClienteControlador(ReservaClienteVista vista, ReservaClienteModelo modelo, String nombreUsuario) {
		this.vista = vista;
		this.modelo = modelo;
		this.UserName = nombreUsuario;
			
		this.vista.getBtnReserv().addActionListener(e -> realizarReserva());
			
		this.vista.getTextFNumHoras().getDocument().addDocumentListener(new DocumentListener() {
			@Override public void insertUpdate(DocumentEvent e) { actualizarPrecio(); }
			@Override public void removeUpdate(DocumentEvent e) { actualizarPrecio(); }
			@Override public void changedUpdate(DocumentEvent e) { actualizarPrecio(); }
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

            String usuario = UserName;
            String instalacion = vista.getTextFInstalaciones().getText().trim();
            String fechaTexto = vista.getTextFFecha().getText().trim();
            String horaTexto = vista.getTextFHora().getText().trim();
            String horasTexto = vista.getTextFNumHoras().getText().trim();

            if (usuario.isEmpty() || instalacion.isEmpty()
                    || fechaTexto.isEmpty() || horaTexto.isEmpty()
                    || horasTexto.isEmpty()) {

                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
                return;
            }

            int horas = Integer.parseInt(horasTexto);

            if (horas < 1 || horas > 3) {
                JOptionPane.showMessageDialog(null, "Solo se pueden reservar 1, 2 o 3 horas");
                return;
            }

            LocalDate fecha = LocalDate.parse(
                    fechaTexto,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            LocalTime horaInicio = LocalTime.parse(
                    horaTexto,
                    DateTimeFormatter.ofPattern("HH:mm"));

            if (horaInicio.getMinute() != 0) {
                JOptionPane.showMessageDialog(null, "La hora debe ser en punto (ej: 17:00)");
                return;
            }

            boolean exito = modelo.guardarReserva(
                    usuario,
                    instalacion,
                    fecha,
                    horaInicio,
                    horas
            );

            if (exito) {
                JOptionPane.showMessageDialog(
                    null,
                    "Reserva realizada correctamente.\n" +
                    "Se ha generado un resguardo PDF en la carpeta 'resguardos'."
                );
            } else {
                JOptionPane.showMessageDialog(null,
                        "No se pudo realizar la reserva");
            }

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(null,
                    "Formato incorrecto. Fecha: yyyy-MM-dd | Hora: HH:mm");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "El número de horas debe ser un número válido");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}