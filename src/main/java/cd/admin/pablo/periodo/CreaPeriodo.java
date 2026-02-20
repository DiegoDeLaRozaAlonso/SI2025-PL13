package cd.admin.pablo.periodo;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import java.util.Date;
import java.util.Calendar;

public class CreaPeriodo {

	private JFrame frame;
	private JTextField tNombre;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField tPeriodo;
	private JTextField textField_4;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CreaPeriodo window = new CreaPeriodo();
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
	public CreaPeriodo() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 494, 361);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		tNombre = new JTextField();
		tNombre.setBounds(232, 11, 139, 20);
		frame.getContentPane().add(tNombre);
		tNombre.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Nombre Periodo");
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		lblNewLabel.setBounds(125, 14, 97, 14);
		frame.getContentPane().add(lblNewLabel);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(new Color(0, 0, 0));
		separator.setBounds(10, 70, 452, 8);
		frame.getContentPane().add(separator);
		
		JLabel lblNewLabel_1 = new JLabel("Fase 1: Inscripción de socios");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(146, 79, 183, 14);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Fecha Inicio");
		lblNewLabel_2.setFont(new Font("Arial", Font.PLAIN, 12));
		lblNewLabel_2.setBounds(103, 104, 69, 14);
		frame.getContentPane().add(lblNewLabel_2);
		
		JLabel lblNewLabel_2_1 = new JLabel("Fecha Fin");
		lblNewLabel_2_1.setFont(new Font("Arial", Font.PLAIN, 12));
		lblNewLabel_2_1.setBounds(350, 104, 69, 14);
		frame.getContentPane().add(lblNewLabel_2_1);
		
		textField_1 = new JTextField();
		textField_1.setBounds(86, 129, 99, 20);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setBounds(333, 129, 86, 20);
		frame.getContentPane().add(textField_2);
		textField_2.setColumns(10);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(Color.BLACK);
		separator_1.setBounds(10, 160, 452, 8);
		frame.getContentPane().add(separator_1);
		
		JLabel lblDescripcionPeriodo = new JLabel("Descripcion Periodo");
		lblDescripcionPeriodo.setFont(new Font("Arial", Font.PLAIN, 12));
		lblDescripcionPeriodo.setBounds(107, 45, 114, 14);
		frame.getContentPane().add(lblDescripcionPeriodo);
		
		tPeriodo = new JTextField();
		tPeriodo.setColumns(10);
		tPeriodo.setBounds(232, 42, 139, 20);
		frame.getContentPane().add(tPeriodo);
		
		JLabel lblNewLabel_1_1 = new JLabel("Fase 2: Inscripción de no socios");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_1.setBounds(146, 171, 202, 14);
		frame.getContentPane().add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_2_1_1 = new JLabel("Fecha Fin");
		lblNewLabel_2_1_1.setFont(new Font("Arial", Font.PLAIN, 12));
		lblNewLabel_2_1_1.setBounds(216, 192, 69, 14);
		frame.getContentPane().add(lblNewLabel_2_1_1);
		
		textField_4 = new JTextField();
		textField_4.setColumns(10);
		textField_4.setBounds(182, 217, 114, 20);
		frame.getContentPane().add(textField_4);
		
		JButton bCrear = new JButton("Crear Periodo");
		bCrear.setFont(new Font("Tahoma", Font.PLAIN, 14));
		bCrear.setBounds(51, 253, 134, 38);
		frame.getContentPane().add(bCrear);
		
		JButton bCancelar = new JButton("Cancelar");
		bCancelar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		bCancelar.setBounds(290, 253, 134, 38);
		frame.getContentPane().add(bCancelar);
	}
}
