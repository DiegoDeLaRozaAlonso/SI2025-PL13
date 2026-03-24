package cd.admin.pablo.inscripcionActividad;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import com.toedter.calendar.JDateChooser;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JTextField;

public class InscribirAdminView {

	private JFrame frame;
	private JButton bVolver, bInscribir, bListarActividades;
	private JTable tablaActividades;
	private JLabel lblNewLabel_1;
	private JDateChooser dFechaInicio;
	private JDateChooser dFechaFin;
	private JRadioButton radioPagoEnActo;
	private JRadioButton radioPagoMensual;
	private JRadioButton radioSocio;
	private JRadioButton radioNoSocio;
	private ButtonGroup grupoRadio;
	private ButtonGroup grupoSocio;
	private JComboBox comboSocios;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InscribirAdminView window = new InscribirAdminView();
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
	public InscribirAdminView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 821, 819);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		bVolver = new JButton("Volver");
		bVolver.setFont(new Font("Tahoma", Font.PLAIN, 15));
		bVolver.setBounds(502, 713, 140, 37);
		frame.getContentPane().add(bVolver);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 179, 793, 380);
		frame.getContentPane().add(scrollPane);
		
		tablaActividades = new JTable();
		scrollPane.setViewportView(tablaActividades);
		
		bInscribir = new JButton("Inscribirse");
		bInscribir.setFont(new Font("Tahoma", Font.PLAIN, 15));
		bInscribir.setBounds(157, 713, 140, 37);
		frame.getContentPane().add(bInscribir);
		
		lblNewLabel_1 = new JLabel("Lista de Actividades");
		lblNewLabel_1.setFont(new Font("Calibri", Font.PLAIN, 34));
		lblNewLabel_1.setBounds(30, 111, 302, 58);
		frame.getContentPane().add(lblNewLabel_1);
		
		dFechaInicio = new JDateChooser();
		dFechaInicio.setBounds(464, 103, 112, 20);
		frame.getContentPane().add(dFechaInicio);
		
		dFechaFin = new JDateChooser();
		dFechaFin.setBounds(672, 103, 112, 20);
		frame.getContentPane().add(dFechaFin);
		
		JLabel lblNewLabel_2_1 = new JLabel("Fecha Fin:");
		lblNewLabel_2_1.setFont(new Font("Calibri", Font.PLAIN, 16));
		lblNewLabel_2_1.setBounds(596, 103, 71, 20);
		frame.getContentPane().add(lblNewLabel_2_1);
		
		JLabel lblNewLabel_2_1_1 = new JLabel("Fecha Inicio:");
		lblNewLabel_2_1_1.setFont(new Font("Calibri", Font.PLAIN, 16));
		lblNewLabel_2_1_1.setBounds(374, 103, 89, 20);
		frame.getContentPane().add(lblNewLabel_2_1_1);
		
		bListarActividades = new JButton("Buscar Actividades");
		bListarActividades.setBounds(512, 130, 172, 39);
		frame.getContentPane().add(bListarActividades);
		
		radioPagoEnActo = new JRadioButton("Pago en el acto");
		radioPagoEnActo.setFont(new Font("Tahoma", Font.PLAIN, 16));
		radioPagoEnActo.setBounds(502, 614, 140, 23);
		frame.getContentPane().add(radioPagoEnActo);
		
		radioPagoMensual = new JRadioButton("Mensualidad");
		radioPagoMensual.setFont(new Font("Tahoma", Font.PLAIN, 16));
		radioPagoMensual.setBounds(252, 614, 125, 23);
		frame.getContentPane().add(radioPagoMensual);
		
		JLabel lblNewLabel_2 = new JLabel("Método de Pago");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_2.setBounds(90, 609, 156, 31);
		frame.getContentPane().add(lblNewLabel_2);
		
		grupoRadio = new ButtonGroup();
		grupoRadio.add(radioPagoEnActo);
		grupoRadio.add(radioPagoMensual);
		radioPagoMensual.setSelected(true);
		
		comboSocios = new JComboBox();
		comboSocios.setBounds(117, 20, 198, 34);
		frame.getContentPane().add(comboSocios);
		
		radioSocio = new JRadioButton("SOCIO");
		radioSocio.setFont(new Font("Tahoma", Font.PLAIN, 16));
		radioSocio.setBounds(30, 17, 81, 37);
		frame.getContentPane().add(radioSocio);
		
		radioNoSocio = new JRadioButton("NO SOCIO");
		radioNoSocio.setFont(new Font("Tahoma", Font.PLAIN, 16));
		radioNoSocio.setBounds(344, 24, 109, 23);
		frame.getContentPane().add(radioNoSocio);
		
		grupoSocio = new ButtonGroup();
		grupoSocio.add(radioSocio);
		grupoSocio.add(radioNoSocio);
		radioSocio.setSelected(true);
		
		textField = new JTextField();
		textField.setBounds(512, 27, 112, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(510, 58, 114, 20);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Nombre");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel.setBounds(461, 30, 51, 14);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_3 = new JLabel("DNI");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_3.setBounds(634, 29, 29, 14);
		frame.getContentPane().add(lblNewLabel_3);
		
		textField_2 = new JTextField();
		textField_2.setBounds(672, 27, 112, 20);
		frame.getContentPane().add(textField_2);
		textField_2.setColumns(10);
		
		JLabel lblNewLabel_4 = new JLabel("Telefono");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_4.setBounds(456, 60, 56, 14);
		frame.getContentPane().add(lblNewLabel_4);
		
		JLabel lblNewLabel_5 = new JLabel("correo");
		lblNewLabel_5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_5.setBounds(634, 61, 40, 14);
		frame.getContentPane().add(lblNewLabel_5);
		
		textField_3 = new JTextField();
		textField_3.setBounds(672, 58, 112, 20);
		frame.getContentPane().add(textField_3);
		textField_3.setColumns(10);
		
	}
	
	public JFrame getFrame() {return this.frame;}
	public JTable getTable() {return this.tablaActividades;}
	public JButton getBotonVolver() {return this.bVolver ;}
	public JButton getBotonInscribir() {return this.bInscribir ;}
	public JButton getBotonListarActividades() {return this.bListarActividades ;}
	public JDateChooser getFechaInicio() {return this.dFechaInicio;}
	public JDateChooser getFechaFin() {return this.dFechaFin;}
	public JRadioButton getRadioMensual() {return this.radioPagoMensual;}
	public JRadioButton getRadioEfectivo() {return this.radioPagoEnActo;}
	public ButtonGroup getGrupoRadio() {return this.grupoRadio;}
	public ButtonGroup getGrupoSocio() {return this.grupoSocio;}
	public JComboBox getComboSocio() {return this.comboSocios;}
}
