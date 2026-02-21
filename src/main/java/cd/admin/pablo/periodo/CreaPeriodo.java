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
import com.toedter.calendar.JDayChooser;
import com.toedter.calendar.JDateChooser;

public class CreaPeriodo {

	private JFrame frame;
	private JTextField tNombre;
	private JTextField tPeriodo;
	private JDateChooser dNoSocioFin;
	private JDateChooser dSocioInicio;
	private JDateChooser dSocioFin;
	private JButton bCancelar;
	private JButton bCrear;
	
	

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
		tNombre.setBounds(175, 11, 261, 20);
		frame.getContentPane().add(tNombre);
		tNombre.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Nombre Periodo");
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		lblNewLabel.setBounds(51, 14, 97, 14);
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
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(Color.BLACK);
		separator_1.setBounds(10, 160, 452, 8);
		frame.getContentPane().add(separator_1);
		
		JLabel lblDescripcionPeriodo = new JLabel("Descripcion Periodo");
		lblDescripcionPeriodo.setFont(new Font("Arial", Font.PLAIN, 12));
		lblDescripcionPeriodo.setBounds(51, 45, 114, 14);
		frame.getContentPane().add(lblDescripcionPeriodo);
		
		tPeriodo = new JTextField();
		tPeriodo.setColumns(10);
		tPeriodo.setBounds(175, 42, 261, 20);
		frame.getContentPane().add(tPeriodo);
		
		JLabel lblNewLabel_1_1 = new JLabel("Fase 2: Inscripción de no socios");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_1.setBounds(146, 171, 202, 14);
		frame.getContentPane().add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_2_1_1 = new JLabel("Fecha Fin");
		lblNewLabel_2_1_1.setFont(new Font("Arial", Font.PLAIN, 12));
		lblNewLabel_2_1_1.setBounds(216, 192, 69, 14);
		frame.getContentPane().add(lblNewLabel_2_1_1);
		
		bCrear = new JButton("Crear Periodo");
		bCrear.setFont(new Font("Tahoma", Font.PLAIN, 14));
		bCrear.setBounds(51, 253, 134, 38);
		frame.getContentPane().add(bCrear);
		
		bCancelar = new JButton("Cancelar");
		bCancelar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		bCancelar.setBounds(290, 253, 134, 38);
		frame.getContentPane().add(bCancelar);
		
		dNoSocioFin = new JDateChooser();
		dNoSocioFin.setBounds(175, 217, 154, 20);
		frame.getContentPane().add(dNoSocioFin);
		
		dSocioInicio = new JDateChooser();
		dSocioInicio.setBounds(61, 129, 151, 20);
		frame.getContentPane().add(dSocioInicio);
		
		dSocioFin = new JDateChooser();
		dSocioFin.setBounds(290, 129, 158, 20);
		frame.getContentPane().add(dSocioFin);
	}
	public JTextField getNombre(){return this.tNombre; }
	public JTextField getDescripcion(){return this.tPeriodo; }
	public Date getFechaInicio(){return this.dSocioInicio.getDate(); }
	public Date getFechaFinSocio(){return this.dSocioFin.getDate(); }
	public Date getFechaFinNoSocio(){return this.dNoSocioFin.getDate(); }
	public JButton getBotonCrear(){return this.bCrear; }
	public JButton getBotonCancelar(){return this.bCancelar; }
	
}
