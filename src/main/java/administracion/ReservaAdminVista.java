package administracion;

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

public class ReservaAdminVista {

	private JFrame frmReservaAdministracin;
	private JTextField textFInstalaciones;
	private JTextField textFUsuarios;
	private JTextField textFFecha;
	private JTextField textFHora;
	private JTextField textFNumHoras;
	private JButton btnReserv;
	private JLabel lblPrecio;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) { 

	    EventQueue.invokeLater(() -> {

	        try {
	            ReservaAdminVista vista = new ReservaAdminVista();
	            ReservaAdminModelo modelo = new ReservaAdminModelo();
	            new ReservaAdminControlador(vista, modelo);

	            vista.getFrame().setVisible(true);

	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	    });
	}

	/**
	 * Create the application.
	 */
	public ReservaAdminVista() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmReservaAdministracin = new JFrame();
		frmReservaAdministracin.setTitle("Reserva Administración");
		frmReservaAdministracin.setBounds(100, 100, 450, 300);
		frmReservaAdministracin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmReservaAdministracin.getContentPane().setLayout(null);
		
		JLabel lblInstal = new JLabel("Instalaciones:");
		lblInstal.setBounds(20, 25, 90, 14);
		frmReservaAdministracin.getContentPane().add(lblInstal);
		
		JLabel lblUser = new JLabel("Usuario:");
		lblUser.setBounds(20, 50, 90, 14);
		frmReservaAdministracin.getContentPane().add(lblUser);
		
		JLabel lblDate = new JLabel("Fecha:");
		lblDate.setBounds(20, 75, 90, 14);
		frmReservaAdministracin.getContentPane().add(lblDate);
		
		JLabel lblHour = new JLabel("Hora:");
		lblHour.setBounds(20, 100, 90, 14);
		frmReservaAdministracin.getContentPane().add(lblHour);
		
		JLabel lblNunHours = new JLabel("Numero de Horas: ");
		lblNunHours.setBounds(20, 125, 90, 14);
		frmReservaAdministracin.getContentPane().add(lblNunHours);
		
		JLabel lblPayMethod = new JLabel("Forma de Pago: ");
		lblPayMethod.setBounds(20, 150, 90, 14);
		frmReservaAdministracin.getContentPane().add(lblPayMethod);
		
		JLabel lblPrice = new JLabel("Precio:");
		lblPrice.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblPrice.setBounds(20, 215, 80, 35);
		frmReservaAdministracin.getContentPane().add(lblPrice);
		
		textFInstalaciones = new JTextField();
		textFInstalaciones.setBounds(120, 22, 120, 20);
		frmReservaAdministracin.getContentPane().add(textFInstalaciones);
		textFInstalaciones.setColumns(10);
		
		textFUsuarios = new JTextField();
		textFUsuarios.setBounds(120, 47, 120, 20);
		frmReservaAdministracin.getContentPane().add(textFUsuarios);
		textFUsuarios.setColumns(10);
		
		textFFecha = new JTextField();
		textFFecha.setBounds(120, 72, 120, 20);
		frmReservaAdministracin.getContentPane().add(textFFecha);
		textFFecha.setColumns(10);
		
		textFHora = new JTextField();
		textFHora.setBounds(120, 97, 120, 20);
		frmReservaAdministracin.getContentPane().add(textFHora);
		textFHora.setColumns(10);
		
		textFNumHoras = new JTextField();
		textFNumHoras.setBounds(120, 122, 120, 20);
		frmReservaAdministracin.getContentPane().add(textFNumHoras);
		textFNumHoras.setColumns(10);
		
		JComboBox cboxPayMethod = new JComboBox();
		cboxPayMethod.setModel(new DefaultComboBoxModel(new String[] {"Tarjeta", "Mensualidad"}));
		cboxPayMethod.setBounds(120, 146, 120, 22);
		frmReservaAdministracin.getContentPane().add(cboxPayMethod);
		
		btnReserv = new JButton("Reserva");
		btnReserv.setBounds(335, 223, 89, 23);
		frmReservaAdministracin.getContentPane().add(btnReserv);
		
		lblPrecio = new JLabel("0.00€");
		lblPrecio.setHorizontalAlignment(SwingConstants.CENTER);
		lblPrecio.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblPrecio.setBounds(89, 215, 80, 35);
		frmReservaAdministracin.getContentPane().add(lblPrecio);
		
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

	public JTextField getTextFUsuarios() {
		return textFUsuarios;
	}

	public void setTextFUsuarios(JTextField textFUsuarios) {
		this.textFUsuarios = textFUsuarios;
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
	    return frmReservaAdministracin;
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
