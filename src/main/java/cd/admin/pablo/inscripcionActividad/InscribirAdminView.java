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
	private JButton bVolver, bInscribir, bListarActividades, bListaEspera;
	private JTable tablaActividades;
	private JLabel lblNewLabel_1;
	private JDateChooser dFechaInicio;
	private JDateChooser dFechaFin;
	private JRadioButton radioPagoTarjeta;
	private JRadioButton radioPagoMensual;
	private JRadioButton radioSocio;
	private JRadioButton radioNoSocio;
	private ButtonGroup grupoRadio;
	private JRadioButton radioPagoEfectivo;
	private ButtonGroup grupoSocio;
	private JComboBox comboSocios;
	private JTextField tNombre;
	private JTextField tTelefono;
	private JTextField tDNI;
	private JTextField tCorreo;

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
		frame.setBounds(100, 100, 976, 819);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		bVolver = new JButton("Volver");
		bVolver.setFont(new Font("Tahoma", Font.PLAIN, 15));
		bVolver.setBounds(502, 713, 140, 37);
		frame.getContentPane().add(bVolver);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 179, 940, 380);
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
		
		radioPagoTarjeta = new JRadioButton("Tarjeta");
		radioPagoTarjeta.setFont(new Font("Tahoma", Font.PLAIN, 16));
		radioPagoTarjeta.setBounds(262, 614, 89, 23);
		frame.getContentPane().add(radioPagoTarjeta);
		
		radioPagoMensual = new JRadioButton("Mensualidad");
		radioPagoMensual.setFont(new Font("Tahoma", Font.PLAIN, 16));
		radioPagoMensual.setBounds(488, 614, 125, 23);
		frame.getContentPane().add(radioPagoMensual);
		
		JLabel lblNewLabel_2 = new JLabel("Método de Pago");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_2.setBounds(90, 609, 156, 31);
		frame.getContentPane().add(lblNewLabel_2);
		
		
		
		comboSocios = new JComboBox();
		comboSocios.setBounds(117, 20, 198, 34);
		frame.getContentPane().add(comboSocios);
		
		radioSocio = new JRadioButton("SOCIO");
		radioSocio.setFont(new Font("Tahoma", Font.PLAIN, 16));
		radioSocio.setBounds(30, 17, 81, 37);
		frame.getContentPane().add(radioSocio);
		
		radioNoSocio = new JRadioButton("NO SOCIO");
		radioNoSocio.setFont(new Font("Tahoma", Font.PLAIN, 16));
		radioNoSocio.setBounds(437, 24, 109, 23);
		frame.getContentPane().add(radioNoSocio);
		
		grupoSocio = new ButtonGroup();
		grupoSocio.add(radioSocio);
		grupoSocio.add(radioNoSocio);
		radioSocio.setSelected(true);
		
		tNombre = new JTextField();
		tNombre.setBounds(603, 17, 112, 20);
		frame.getContentPane().add(tNombre);
		tNombre.setColumns(10);
		
		tTelefono = new JTextField();
		tTelefono.setBounds(601, 48, 114, 20);
		frame.getContentPane().add(tTelefono);
		tTelefono.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Nombre");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel.setBounds(552, 20, 51, 14);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_3 = new JLabel("DNI");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_3.setBounds(759, 22, 29, 14);
		frame.getContentPane().add(lblNewLabel_3);
		
		tDNI = new JTextField();
		tDNI.setBounds(797, 20, 112, 20);
		frame.getContentPane().add(tDNI);
		tDNI.setColumns(10);
		
		JLabel lblNewLabel_4 = new JLabel("Telefono");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_4.setBounds(547, 50, 56, 14);
		frame.getContentPane().add(lblNewLabel_4);
		
		JLabel lblNewLabel_5 = new JLabel("Email");
		lblNewLabel_5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_5.setBounds(759, 54, 40, 14);
		frame.getContentPane().add(lblNewLabel_5);
		
		tCorreo = new JTextField();
		tCorreo.setBounds(797, 51, 112, 20);
		frame.getContentPane().add(tCorreo);
		tCorreo.setColumns(10);
		
		radioPagoEfectivo = new JRadioButton("Efectivo");
		radioPagoEfectivo.setFont(new Font("Tahoma", Font.PLAIN, 16));
		radioPagoEfectivo.setBounds(374, 614, 95, 23);
		frame.getContentPane().add(radioPagoEfectivo);
		
		grupoRadio = new ButtonGroup();
		grupoRadio.add(radioPagoTarjeta);
		grupoRadio.add(radioPagoMensual);
		grupoRadio.add(radioPagoEfectivo);
		radioPagoMensual.setSelected(true);
		
		bListaEspera = new JButton("Lista de Espera");
		bListaEspera.setFont(new Font("Tahoma", Font.PLAIN, 15));
		bListaEspera.setBounds(313, 715, 156, 37);
		frame.getContentPane().add(bListaEspera);
		
	}
	
	public JFrame getFrame() {return this.frame;}
	//Tabla donde se listan las actividades
	public JTable getTable() {return this.tablaActividades;}
	//Botones
	public JButton getBotonVolver() {return this.bVolver ;}
	public JButton getBotonInscribir() {return this.bInscribir ;}
	public JButton getBotonListarActividades() {return this.bListarActividades ;}
	public JButton getBotonListaEspera() {return this.bListaEspera ;}
	//Date choosers
	public JDateChooser getFechaInicio() {return this.dFechaInicio;}
	public JDateChooser getFechaFin() {return this.dFechaFin;}
	//Radios Button
	public JRadioButton getRadioMensual() {return this.radioPagoMensual;}
	public JRadioButton getRadioEfectivo() {return this.radioPagoEfectivo;}
	public JRadioButton getRadioTarjeta() {return this.radioPagoTarjeta;}
	public JRadioButton getRadioSocio() {return this.radioSocio;}
	public JRadioButton getRadioNoSocio() {return this.radioNoSocio;}
	//ButtonGroup (agrupan los radios para que sen excluyentes)
	public ButtonGroup getGrupoRadio() {return this.grupoRadio;}
	public ButtonGroup getGrupoSocio() {return this.grupoSocio;}
	//ComboBox de los socios
	public JComboBox getComboSocio() {return this.comboSocios;}
	//JText del no socio
	public JTextField getNombre() {return this.tNombre;}
	public JTextField getDNI() {return this.tDNI;}
	public JTextField getCorreo() {return this.tCorreo;}
	public JTextField getTelefono() {return this.tTelefono;}
}
