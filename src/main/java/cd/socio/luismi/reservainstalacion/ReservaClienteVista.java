package cd.socio.luismi.reservainstalacion;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
// Pa poder limitar lo que se escribe en X campo
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.AttributeSet;

public class ReservaClienteVista {

	private JFrame frmReservaSocio;
	private JTextField textFInstalaciones;
	private JTextField textFFecha;
	private JTextField textFHora;
	private JTextField textFNumHoras;
	private JButton btnReserv;
	private JLabel lblPrecio;

	/**
	 * Create the application.
	 * @wbp.parser.entryPoint
	 */
	public ReservaClienteVista() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmReservaSocio = new JFrame();
		frmReservaSocio.setTitle("Reserva Socio");
		frmReservaSocio.setBounds(100, 100, 450, 300);
		frmReservaSocio.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmReservaSocio.getContentPane().setLayout(null);
		
		JLabel lblInstal = new JLabel("Instalaciones:");
		lblInstal.setBounds(20, 25, 90, 14);
		frmReservaSocio.getContentPane().add(lblInstal);
		
		JLabel lblDate = new JLabel("Fecha: (yyyy-mm-dd)");
		lblDate.setBounds(20, 50, 104, 14);
		frmReservaSocio.getContentPane().add(lblDate);
		
		JLabel lblHour = new JLabel("Hora:");
		lblHour.setBounds(20, 75, 90, 14);
		frmReservaSocio.getContentPane().add(lblHour);
		
		JLabel lblNunHours = new JLabel("Numero de Horas: ");
		lblNunHours.setBounds(20, 100, 90, 14);
		frmReservaSocio.getContentPane().add(lblNunHours);
		
		JLabel lblPayMethod = new JLabel("Forma de Pago: ");
		lblPayMethod.setBounds(20, 125, 90, 14);
		frmReservaSocio.getContentPane().add(lblPayMethod);
		
		JLabel lblPrice = new JLabel("Precio:");
		lblPrice.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblPrice.setBounds(20, 215, 80, 35);
		frmReservaSocio.getContentPane().add(lblPrice);
		
		textFInstalaciones = new JTextField();
		textFInstalaciones.setBounds(134, 22, 120, 20);
		frmReservaSocio.getContentPane().add(textFInstalaciones);
		textFInstalaciones.setColumns(10);
		
		textFFecha = new JTextField();
		textFFecha.setBounds(134, 47, 120, 20);
		frmReservaSocio.getContentPane().add(textFFecha);
		textFFecha.setColumns(10);
		
		textFHora = new JTextField();
		textFHora.setBounds(134, 72, 120, 20);
		frmReservaSocio.getContentPane().add(textFHora);
		textFHora.setColumns(10);
		
		textFNumHoras = new JTextField();
		textFNumHoras.setBounds(134, 97, 120, 20);
		frmReservaSocio.getContentPane().add(textFNumHoras);
		textFNumHoras.setColumns(10);
		
		JComboBox cboxPayMethod = new JComboBox();
		cboxPayMethod.setModel(new DefaultComboBoxModel(new String[] {"En el momento de uso", "Mensualidad"}));
		cboxPayMethod.setBounds(134, 121, 150, 22);
		frmReservaSocio.getContentPane().add(cboxPayMethod);
		
		btnReserv = new JButton("Reserva");
		btnReserv.setBounds(335, 223, 89, 23);
		frmReservaSocio.getContentPane().add(btnReserv);
		
		lblPrecio = new JLabel("0.00€");
		lblPrecio.setHorizontalAlignment(SwingConstants.CENTER);
		lblPrecio.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblPrecio.setBounds(89, 215, 80, 35);
		frmReservaSocio.getContentPane().add(lblPrecio);
		
		limitarSoloNumeros(textFNumHoras);//llamada a nuestra funcion para limitar la entrada de datos
		limitarHora(textFHora); //llama pa limitar lo que se puede poner en la hora 
		limitarFecha(textFFecha); //llama pa lo mismo de arriba pero en este caso fecha
	}
	
	//Gettets y Setters 
	public JTextField getTextFInstalaciones() {
		return textFInstalaciones;
	}

	public void setTextFInstalaciones(JTextField textFInstalaciones) {
		this.textFInstalaciones = textFInstalaciones;
	}

	public JTextField getTextFFecha() {
		return textFFecha;
	}

	public void setTextFFecha(JTextField textFFecha) {
		this.textFFecha = textFFecha;
	}

	public JTextField getTextFHora() {
		return textFHora;
	}

	public void setTextFHora(JTextField textFHora) {
		this.textFHora = textFHora;
	}

	public JTextField getTextFNumHoras() {
		return textFNumHoras;
	}

	public JButton getBtnReserv() {
		return btnReserv;
	}

	public void setBtnReserv(JButton btnReserv) {
		this.btnReserv = btnReserv;
	}

	public JLabel getPrecio() {
		return lblPrecio;
	}

	public void setPrecio(double precio) {
		lblPrecio.setText(precio + " € ");
	}
	
	public JFrame getFrame() { //Necesario para conectar Modelo y Vista
	    return frmReservaSocio;
	}
	
	private void limitarSoloNumeros(JTextField campo) { //No se podra poner en una caja nada que no sean numeros

	    ((AbstractDocument) campo.getDocument()).setDocumentFilter(
	        new DocumentFilter() {

	            @Override
	            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {

	                if (string.matches("\\d+")) {
	                    super.insertString(fb, offset, string, attr);
	                }
	            }

	            @Override
	            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {

	                if (text.matches("\\d+")) {
	                    super.replace(fb, offset, length, text, attrs);
	                }
	            }
	        }
	    );
	}
	
	private void limitarHora(JTextField campo) { //Con esta limitamos los caracters que se pueden poner en hora a [0-9] y ":"

	    ((AbstractDocument) campo.getDocument()).setDocumentFilter(
	        new DocumentFilter() {

	            @Override
	            public void replace(FilterBypass fb, int offset, int length,
	                                String text, AttributeSet attrs) throws BadLocationException {

	                if (text.matches("[0-9:]+")) {
	                    super.replace(fb, offset, length, text, attrs);
	                }
	            }

	            @Override
	            public void insertString(FilterBypass fb, int offset, String text,
	                                     AttributeSet attr) throws BadLocationException {

	                if (text.matches("[0-9:]+")) {
	                    super.insertString(fb, offset, text, attr);
	                }
	            }
	        }
	    );
	}
	
	private void limitarFecha(JTextField campo) { //Y por ultimo en esta limitamos los caracters que se pueden poner en hora a [0-9] y "-"

	    ((AbstractDocument) campo.getDocument()).setDocumentFilter(
	        new DocumentFilter() {

	            @Override
	            public void replace(FilterBypass fb, int offset, int length,
	                                String text, AttributeSet attrs) throws BadLocationException {

	                if (text.matches("[0-9-]+")) {
	                    super.replace(fb, offset, length, text, attrs);
	                }
	            }

	            @Override
	            public void insertString(FilterBypass fb, int offset, String text,
	                                     AttributeSet attr) throws BadLocationException {

	                if (text.matches("[0-9-]+")) {
	                    super.insertString(fb, offset, text, attr);
	                }
	            }
	        }
	    );
	}
}
