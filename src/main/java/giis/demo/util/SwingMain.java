package giis.demo.util;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
import cd.socio.pablo.listaActividades.ListaPeriodoController;
import cd.socio.pablo.listaActividades.ListaPeriodoModel;
import cd.socio.pablo.listaActividades.ListaPeriodoView;

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
		JButton btnEjecutarTkrun = new JButton("Ejecutar giis.demo.tkrun");
		btnEjecutarTkrun.addActionListener(e -> {
			giis.demo.tkrun.CarrerasController controller =
					new giis.demo.tkrun.CarrerasController(
							new giis.demo.tkrun.CarrerasModel(),
							new giis.demo.tkrun.CarrerasView()
					);
			controller.initController();
		});
		panelCentro.add(btnEjecutarTkrun);

		// =========================
		// Ver disponibilidad (Socio) (botón original)
		// =========================
		JButton verDisponibilidad = new JButton("Ver disponibilidad instalación (Socio)");
		verDisponibilidad.addActionListener(e -> {
			cd.socio.diego.verdispoinstalacion.DisponibilidadController controller =
					new cd.socio.diego.verdispoinstalacion.DisponibilidadController(
							new cd.socio.diego.verdispoinstalacion.DisponibilidadModel(),
							new cd.socio.diego.verdispoinstalacion.DisponibilidadView()
					);
			controller.initController();
		});
		panelCentro.add(verDisponibilidad);

		// =========================
		// Crear/Planificar actividad (Administracion) (botón original + bloqueo)
		// =========================
		JButton planificarActividad = new JButton("Crear/Planificar actividad (Administracion)");
		planificarActividad.addActionListener(e -> {

			if (!sesion.isAdmin()) {
				JOptionPane.showMessageDialog(
						frame,
						"No tienes permisos para acceder a esta funcionalidad.\n"
						+ "Solo un administrador puede acceder.",
						"Acceso denegado",
						JOptionPane.WARNING_MESSAGE
				);
				return;
			}
			
			cd.admin.diego.planact.PlanActCrearActividadController controller =
					new cd.admin.diego.planact.PlanActCrearActividadController(
							new cd.admin.diego.planact.PlanActCrearActividadModel(),
							new cd.admin.diego.planact.PlanActCrearActividadView()
					);
			controller.initController();
		});
		panelCentro.add(planificarActividad);
		
		/*
		 * Ejecuta la lista de actividades	
		 */
			
		JButton btnListaActividades = new JButton("Ejecutar ListaActividades");
		//btnListaActividades.setBounds(51, 27, 149, 23);
		btnListaActividades.addActionListener(new ActionListener() { //NOSONAR codigo autogenerado
			public void actionPerformed(ActionEvent e) {
				ListaPeriodoController controller=new ListaPeriodoController(new ListaPeriodoModel(), new ListaPeriodoView());
				controller.initController();
			}
		});
		//frame.getContentPane().setLayout(null);
		//frame.getContentPane().add(btnListaActividades);
		panelCentro.add(btnListaActividades);

		// =========================
		// Gestionar Periodos (Administracion) (NUEVO botón + bloqueo)
		// =========================
		JButton btnEjecutarPeriodo = new JButton("Gestionar Periodos (Administracion)");
		btnEjecutarPeriodo.addActionListener(e -> {

			if (!sesion.isAdmin()) {
				JOptionPane.showMessageDialog(
						frame,
						"No tienes permisos para acceder a esta funcionalidad.\n"
						+ "Solo un administrador puede acceder.",
						"Acceso denegado",
						JOptionPane.WARNING_MESSAGE
				);
				return;
			}

			PeriodoController controller = new PeriodoController(
					new PeriodoModel(),
					new PeriodoView()
			);
			controller.iniciarControlador();
		});
		panelCentro.add(btnEjecutarPeriodo);

		// =========================
		// Panel inferior: Cambiar de usuario (abajo derecha)
		// =========================
		JPanel panelInferior = new JPanel(new BorderLayout());
		JButton btnCambiarUsuario = new JButton("Cambiar de usuario");
		btnCambiarUsuario.addActionListener(e -> {
			frame.getContentPane().removeAll();
			frame.revalidate();
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