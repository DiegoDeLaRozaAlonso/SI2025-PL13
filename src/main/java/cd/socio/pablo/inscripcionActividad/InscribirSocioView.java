package cd.socio.pablo.inscripcionActividad;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import com.toedter.calendar.JDateChooser;
import javax.swing.JRadioButton;

public class InscribirSocioView {

	private JFrame frame;
	private JButton bVolver;
	private JButton bInscribir;
	private JButton bListarActividades;
	private JTable tablaActividades;
	private JLabel labelSocio;
	private JLabel lblNewLabel_1;
	private JDateChooser dFechaInicio;
	private JDateChooser dFechaFin;
	private JRadioButton radioPagoEnActo;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InscribirSocioView window = new InscribirSocioView();
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
	public InscribirSocioView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 821, 728);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null); 
		
		JLabel lblNewLabel = new JLabel("Socio:");
		lblNewLabel.setFont(new Font("Calibri", Font.PLAIN, 25));
		lblNewLabel.setBounds(10, 11, 81, 47);
		frame.getContentPane().add(lblNewLabel);
		
		bVolver = new JButton("Volver");
		bVolver.setFont(new Font("Tahoma", Font.PLAIN, 15));
		bVolver.setBounds(502, 608, 140, 37);
		frame.getContentPane().add(bVolver);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 153, 793, 380);
		frame.getContentPane().add(scrollPane);
		
		tablaActividades = new JTable();
		scrollPane.setViewportView(tablaActividades);
		
		labelSocio = new JLabel("");
		labelSocio.setFont(new Font("Calibri", Font.PLAIN, 23));
		labelSocio.setBounds(101, 11, 259, 47);
		frame.getContentPane().add(labelSocio);
		
		bInscribir = new JButton("Inscribirse");
		bInscribir.setFont(new Font("Tahoma", Font.PLAIN, 15));
		bInscribir.setBounds(157, 608, 140, 37);
		frame.getContentPane().add(bInscribir);
		
		lblNewLabel_1 = new JLabel("Lista de Actividades");
		lblNewLabel_1.setFont(new Font("Calibri", Font.PLAIN, 34));
		lblNewLabel_1.setBounds(10, 69, 302, 58);
		frame.getContentPane().add(lblNewLabel_1);
		
		dFechaInicio = new JDateChooser();
		dFechaInicio.setBounds(444, 61, 112, 20);
		frame.getContentPane().add(dFechaInicio);
		
		dFechaFin = new JDateChooser();
		dFechaFin.setBounds(672, 61, 112, 20);
		frame.getContentPane().add(dFechaFin);
		
		JLabel lblNewLabel_2_1 = new JLabel("Fecha Fin:");
		lblNewLabel_2_1.setFont(new Font("Calibri", Font.PLAIN, 16));
		lblNewLabel_2_1.setBounds(581, 58, 81, 25);
		frame.getContentPane().add(lblNewLabel_2_1);
		
		JLabel lblNewLabel_2_1_1 = new JLabel("Fecha Inicio:");
		lblNewLabel_2_1_1.setFont(new Font("Calibri", Font.PLAIN, 16));
		lblNewLabel_2_1_1.setBounds(344, 58, 95, 25);
		frame.getContentPane().add(lblNewLabel_2_1_1);
		
		bListarActividades = new JButton("Buscar Actividades");
		bListarActividades.setBounds(512, 88, 172, 39);
		frame.getContentPane().add(bListarActividades);
		
		JRadioButton radioPagoEnActo = new JRadioButton("Pago en el acto");
		radioPagoEnActo.setFont(new Font("Tahoma", Font.PLAIN, 16));
		radioPagoEnActo.setBounds(512, 554, 140, 23);
		frame.getContentPane().add(radioPagoEnActo);
		
		JRadioButton radioMensualidad = new JRadioButton("Mensualidad");
		radioMensualidad.setFont(new Font("Tahoma", Font.PLAIN, 16));
		radioMensualidad.setBounds(172, 554, 125, 23);
		frame.getContentPane().add(radioMensualidad);
		
		JLabel lblNewLabel_2 = new JLabel("Método de Pago");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_2.setBounds(10, 549, 156, 31);
		frame.getContentPane().add(lblNewLabel_2);
	}
	
	public JFrame getFrame() {return this.frame;}
	public JTable getTable() {return this.tablaActividades;}
	public JLabel getLabelSocio() {return this.labelSocio;}
	public JButton getBotonVolver() {return this.bVolver ;}
	public JButton getBotonInscribir() {return this.bInscribir ;}
	public JButton getBotonListarActividades() {return this.bListarActividades ;}
	public JDateChooser getFechaInicio() {return this.dFechaInicio;}
	public JDateChooser getFechaFin() {return this.dFechaFin;}
}
