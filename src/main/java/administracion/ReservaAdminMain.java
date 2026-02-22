package administracion;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SwingConstants;

public class ReservaAdminMain {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ReservaAdminMain window = new ReservaAdminMain();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ReservaAdminMain() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblInstal = new JLabel("Instalaciones:");
		lblInstal.setBounds(20, 25, 90, 14);
		frame.getContentPane().add(lblInstal);
		
		JLabel lblUser = new JLabel("Usuario:");
		lblUser.setBounds(20, 50, 90, 14);
		frame.getContentPane().add(lblUser);
		
		JLabel lblDate = new JLabel("Fecha:");
		lblDate.setBounds(20, 75, 90, 14);
		frame.getContentPane().add(lblDate);
		
		JLabel lblHour = new JLabel("Hora:");
		lblHour.setBounds(20, 100, 90, 14);
		frame.getContentPane().add(lblHour);
		
		JLabel lblNunHours = new JLabel("Numero de Horas: ");
		lblNunHours.setBounds(20, 125, 90, 14);
		frame.getContentPane().add(lblNunHours);
		
		JLabel lblPayMethod = new JLabel("Forma de Pago: ");
		lblPayMethod.setBounds(20, 150, 90, 14);
		frame.getContentPane().add(lblPayMethod);
		
		JLabel lblPrice = new JLabel("Precio:");
		lblPrice.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblPrice.setBounds(20, 215, 80, 35);
		frame.getContentPane().add(lblPrice);
		
		textField = new JTextField();
		textField.setBounds(120, 22, 120, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(120, 47, 120, 20);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setBounds(120, 72, 120, 20);
		frame.getContentPane().add(textField_2);
		textField_2.setColumns(10);
		
		textField_3 = new JTextField();
		textField_3.setBounds(120, 97, 120, 20);
		frame.getContentPane().add(textField_3);
		textField_3.setColumns(10);
		
		textField_4 = new JTextField();
		textField_4.setBounds(120, 122, 120, 20);
		frame.getContentPane().add(textField_4);
		textField_4.setColumns(10);
		
		JComboBox cboxPayMethod = new JComboBox();
		cboxPayMethod.setModel(new DefaultComboBoxModel(new String[] {"Tarjeta", "Mensualidad"}));
		cboxPayMethod.setBounds(120, 146, 120, 22);
		frame.getContentPane().add(cboxPayMethod);
		
		JButton btnReserv = new JButton("Reserva");
		btnReserv.setBounds(335, 223, 89, 23);
		frame.getContentPane().add(btnReserv);
		
		JLabel lblPrecio = new JLabel("0.00€");
		lblPrecio.setHorizontalAlignment(SwingConstants.CENTER);
		lblPrecio.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblPrecio.setBounds(89, 215, 80, 35);
		frame.getContentPane().add(lblPrecio);
	}
}
