package administracion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

            double precio = modelo.calcularPrecio(horas);
            vista.setPrecio(precio);

        } catch (NumberFormatException e) {
            vista.setPrecio(0);
        }
    }

    private void realizarReserva() {
        try {
            int horas = Integer.parseInt(vista.getTextFNumHoras().getText());

            if (horas <= 0) {
                JOptionPane.showMessageDialog(null, 
                        "El número de horas debe ser mayor que 0");
                return;
            }

            // Aquí podrías llamar a modelo.guardarReserva(...)
            JOptionPane.showMessageDialog(null, 
                    "Reserva realizada correctamente");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, 
                    "Introduce un número entero válido en horas");
        }
    }
    
    
}