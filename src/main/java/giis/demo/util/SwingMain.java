package giis.demo.util;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import cd.admin.pablo.periodo.PeriodoController;
import cd.admin.pablo.periodo.PeriodoModel;
import cd.admin.pablo.periodo.PeriodoView;

import cd.login.diego.LoginController;
import cd.login.diego.LoginModel;
import cd.login.diego.LoginView;
import cd.login.diego.UsuarioSesion;

public class SwingMain {

	private JFrame frame;
	private UsuarioSesion sesion;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				SwingMain window = new SwingMain();
				window.frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace(); //NOSONAR
			}
		});
	}

	/** 
	 * Create the application.
	 */
	public SwingMain() {

		frame = new JFrame();
		frame.setBounds(0, 0, 520, 420);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		// ✅ SIEMPRE inicializa y carga datos al arrancar (para que el login funcione)
		inicializarBaseDeDatos();

		// ✅ Login
		login();
	}

	private void inicializarBaseDeDatos() {
		Database db = new Database();
		db.createDatabase(true); // borra y recrea schema
		db.loadDatabase();       // carga data.sql
	}

	// ================= LOGIN =================
	private void login() {

		LoginController login = new LoginController(
				new LoginModel(),
				new LoginView(frame)
		);

		sesion = login.mostrarLogin();

		if (sesion == null) {
			System.exit(0);
		}

		frame.setTitle("Bienvenido " + sesion.getNombre()
				+ (sesion.isAdmin() ? " (ADMIN)" : " (SOCIO)"));

		inicializarContenido();
	}

	// ================= CONTENIDO =================
	private void inicializarContenido() {

		JPanel panelCentro = new JPanel();
		panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));

		// =========================
		// Inicializar BD (botón original)
		// =========================
		JButton btnInicializarBD = new JButton("Inicializar Base de Datos en Blanco");
		btnInicializarBD.addActionListener(e -> {
			Database db = new Database();
			db.createDatabase(false);
		});
		panelCentro.add(btnInicializarBD);

		// =========================
		// Cargar datos (botón original)
		// =========================
		JButton btnCargarDatos = new JButton("Cargar Datos Iniciales para Pruebas");
		btnCargarDatos.addActionListener(e -> {
			Database db = new Database();
			db.createDatabase(false);
			db.loadDatabase();
		});
		panelCentro.add(btnCargarDatos);

		// =========================
		// Ejecutar tkrun (botón original)
		// =========================
		
		//Botón para la ejecución de creación de un nuevo periodo
		JButton btnEjecutarPeriodo = new JButton("Ejecutar Periodos");
		btnEjecutarPeriodo.setBounds(28, 27, 200, 23);
		btnEjecutarPeriodo.addActionListener(new ActionListener() { //NOSONAR codigo autogenerado
			public void actionPerformed(ActionEvent e) {
				PeriodoController controller=new PeriodoController(new PeriodoModel(), new PeriodoView());
				controller.iniciarControlador();
			}
		});
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(btnEjecutarPeriodo);
		
			
		JButton btnInicializarBaseDeDatos = new JButton("Inicializar Base de Datos en Blanco");
		btnInicializarBaseDeDatos.setBounds(28, 61, 197, 23);
		btnInicializarBaseDeDatos.addActionListener(new ActionListener() { //NOSONAR codigo autogenerado
			public void actionPerformed(ActionEvent e) {
				Database db=new Database();
				db.createDatabase(false);
			}

			cd.admin.diego.planact.PlanActCrearActividadController controller =
					new cd.admin.diego.planact.PlanActCrearActividadController(
							new cd.admin.diego.planact.PlanActCrearActividadModel(),
							new cd.admin.diego.planact.PlanActCrearActividadView()
					);
			controller.initController();
		});
		panelCentro.add(planificarActividad);

		// =========================
		// Panel inferior: Cambiar de usuario (abajo derecha)
		// =========================
		JPanel panelInferior = new JPanel(new BorderLayout());
		JButton btnCambiarUsuario = new JButton("Cambiar de usuario");
		btnCambiarUsuario.addActionListener(e -> {
			frame.getContentPane().removeAll();
			frame.repaint();
			login();
		});
		panelInferior.add(btnCambiarUsuario, BorderLayout.EAST);

		// Pintar
		frame.getContentPane().removeAll();
		frame.add(panelCentro, BorderLayout.CENTER);
		frame.add(panelInferior, BorderLayout.SOUTH);

		frame.revalidate();
		frame.repaint();
	}
}