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

            if (horas <= 0) {
                JOptionPane.showMessageDialog(null,
                        "El número de horas debe ser mayor que 0");
                return;
            }

            // 🔥 VALIDAMOS AQUÍ
            LocalDate.parse(fechaTexto,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            LocalTime.parse(horaTexto,
                    DateTimeFormatter.ofPattern("HH:mm"));

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