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

import cd.Administracion.Alejandro.Contabilidad.ContabilidadMensualController;
import cd.Administracion.Alejandro.Contabilidad.ContabilidadMensualModel;
import cd.Administracion.Alejandro.Contabilidad.ContabilidadMensualView;
import cd.admin.Alejandro.InformeMorosos.InformeMorososController;
import cd.admin.Alejandro.InformeMorosos.InformeMorososModel;
import cd.admin.Alejandro.InformeMorosos.InformeMorososView;
import cd.admin.Alejandro.Reserva.ReservarActividadController;
import cd.admin.Alejandro.Reserva.ReservarActividadModel;
import cd.admin.Alejandro.Reserva.ReservarActividadView;
import cd.admin.Alejandro.Visualizacion.VisualizacionReservasController;
import cd.admin.Alejandro.Visualizacion.VisualizacionReservasModel;
import cd.admin.Alejandro.Visualizacion.VisualizacionReservasView;
import cd.admin.pablo.periodo.PeriodoController;
import cd.admin.pablo.periodo.PeriodoModel;
import cd.admin.pablo.periodo.PeriodoView;
import cd.login.diego.LoginController;
import cd.login.diego.LoginModel;
import cd.login.diego.LoginView;
import cd.login.diego.UsuarioSesion;
import cd.socio.pablo.inscripcionActividad.InscribirSocioController;
import cd.socio.pablo.inscripcionActividad.InscribirSocioModel;
import cd.socio.pablo.inscripcionActividad.InscribirSocioView;
import cd.socio.AlejandroVisualizacionReservas.PagosPendientesController;
import cd.socio.AlejandroVisualizacionReservas.PagosPendientesModel;
import cd.socio.AlejandroVisualizacionReservas.PagosPendientesView;
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
		inicializarBaseDeDatos();
		login();
	}

	private void inicializarBaseDeDatos() {
		Database db = new Database();
		db.createDatabase(true);
		db.loadDatabase();
	}

	// ================= LOGIN =================
	private void login() {
		LoginController login = new LoginController(new LoginModel(), new LoginView(frame));
		sesion = login.mostrarLogin();
		if (sesion == null) System.exit(0);
		frame.setTitle("Bienvenido " + sesion.getNombre()
				+ (sesion.isAdmin() ? " (ADMIN)" : " (SOCIO)"));
		inicializarContenido();
	}

	// ================= CONTENIDO =================
	private void inicializarContenido() {

		JPanel panelCentro = new JPanel();
		panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));

		// Inicializar BD
		JButton btnInicializarBD = new JButton("Inicializar Base de Datos en Blanco");
		btnInicializarBD.addActionListener(e -> new Database().createDatabase(false));
		panelCentro.add(btnInicializarBD);

		// Cargar datos
		JButton btnCargarDatos = new JButton("Cargar Datos Iniciales para Pruebas");
		btnCargarDatos.addActionListener(e -> {
			Database db = new Database();
			db.createDatabase(false);
			db.loadDatabase();
		});
		panelCentro.add(btnCargarDatos);

		// Ver disponibilidad (Socio)
		JButton verDisponibilidad = new JButton("Ver disponibilidad instalación (Socio)");
		verDisponibilidad.addActionListener(e -> {
			cd.socio.diego.verdispoinstalacion.DisponibilidadController controller =
					new cd.socio.diego.verdispoinstalacion.DisponibilidadController(
							new cd.socio.diego.verdispoinstalacion.DisponibilidadModel(),
							new cd.socio.diego.verdispoinstalacion.DisponibilidadView());
			controller.initController();
		});
		panelCentro.add(verDisponibilidad);

		// Planificar actividad (Admin)
		JButton planificarActividad = new JButton("Planificar actividad (Administracion)");
		planificarActividad.addActionListener(e -> {
			if (!sesion.isAdmin()) { accesoDenegado(); return; }
			cd.admin.diego.planact.PlanActCrearActividadController controller =
					new cd.admin.diego.planact.PlanActCrearActividadController(
							new cd.admin.diego.planact.PlanActCrearActividadModel(),
							new cd.admin.diego.planact.PlanActCrearActividadView());
			controller.initController();
		});
		panelCentro.add(planificarActividad);

		// Lista actividades (Admin)
		JButton btnListaActividades = new JButton("Ejecutar ListaActividades");
		btnListaActividades.addActionListener(new ActionListener() { //NOSONAR
			public void actionPerformed(ActionEvent e) {
				if (!sesion.isAdmin()) { accesoDenegado(); return; }
				new ListaPeriodoController(new ListaPeriodoModel(), new ListaPeriodoView())
						.initController();
			}
		});
		panelCentro.add(btnListaActividades);

		// Inscripcion Actividad (Socio)
		JButton btnInscribirUsuario = new JButton("Inscripcion Actividad (Socio)");
		btnInscribirUsuario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new InscribirSocioController(new InscribirSocioModel(), new InscribirSocioView(), sesion)
						.initController();
			}
		});
		panelCentro.add(btnInscribirUsuario);

		// Crear Reserva Admin
		JButton crearReservaAdmin = new JButton("Crear Reserva Admin");
		crearReservaAdmin.addActionListener(e -> {
			if (!sesion.isAdmin()) { accesoDenegado(); return; }
			administracion.ReservaAdminVista vista = new administracion.ReservaAdminVista();
			administracion.ReservaAdminControlador controller =
					new administracion.ReservaAdminControlador(vista, new administracion.ReservaAdminModelo());
			JFrame f = vista.getFrame();
			f.setLocationRelativeTo(frame);
			f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			f.setVisible(true);
		});
		panelCentro.add(crearReservaAdmin);

		// Crear Reserva Socio
		JButton crearReservaSocio = new JButton("Crear Reserva Socio");
		crearReservaSocio.addActionListener(e -> {
			if (sesion.isAdmin()) {
				JOptionPane.showMessageDialog(frame,
						"Esta ventana es para socios.", "Acceso denegado", JOptionPane.WARNING_MESSAGE);
				return;
			}
			cd.socio.luismi.reservainstalacion.ReservaClienteVista vista =
					new cd.socio.luismi.reservainstalacion.ReservaClienteVista();
			cd.socio.luismi.reservainstalacion.ReservaClienteControlador controller =
					new cd.socio.luismi.reservainstalacion.ReservaClienteControlador(
							vista, new cd.socio.luismi.reservainstalacion.ReservaClienteModelo(), sesion.getNombre());
			JFrame f = vista.getFrame();
			f.setLocationRelativeTo(frame);
			f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			f.setVisible(true);
		});
		panelCentro.add(crearReservaSocio);

		// Gestionar Periodos (Admin)
		JButton btnEjecutarPeriodo = new JButton("Gestionar Periodos (Administracion)");
		btnEjecutarPeriodo.addActionListener(e -> {
			if (!sesion.isAdmin()) { accesoDenegado(); return; }
			new PeriodoController(new PeriodoModel(), new PeriodoView()).iniciarControlador();
		});
		panelCentro.add(btnEjecutarPeriodo);

		// Reservar Actividad (Admin)
		JButton btnReservarActividad = new JButton("Reservar Actividad");
		btnReservarActividad.addActionListener(new ActionListener() { //NOSONAR
			public void actionPerformed(ActionEvent e) {
				if (!sesion.isAdmin()) { accesoDenegado(); return; }
				new ReservarActividadController(new ReservarActividadModel(), new ReservarActividadView())
						.initController();
			}
		});
		panelCentro.add(btnReservarActividad);

		// Visualizacion Reservas (Admin)
		JButton btnVisualizacionReservas = new JButton("Visualización Reservas");
		btnVisualizacionReservas.addActionListener(new ActionListener() { //NOSONAR
			public void actionPerformed(ActionEvent e) {
				if (!sesion.isAdmin()) { accesoDenegado(); return; }
				new VisualizacionReservasController(new VisualizacionReservasModel(), new VisualizacionReservasView())
						.initController();
			}
		});
		panelCentro.add(btnVisualizacionReservas);

		// Mis Pagos Pendientes (Socio)
		JButton btnPagosPendientes = new JButton("Mis Pagos Pendientes");
		btnPagosPendientes.addActionListener(new ActionListener() { //NOSONAR
			public void actionPerformed(ActionEvent e) {
				if (sesion.isAdmin()) {
					JOptionPane.showMessageDialog(frame, "Esta funcionalidad es solo para socios.",
							"Acceso denegado", JOptionPane.WARNING_MESSAGE);
					return;
				}
				new PagosPendientesController(sesion.getId(), new PagosPendientesModel(), new PagosPendientesView())
						.initController();
			}
		});
		panelCentro.add(btnPagosPendientes);

		// Contabilidad Mensual (Admin)
		JButton btnContabilidad = new JButton("Contabilidad Mensual");
		btnContabilidad.addActionListener(e -> {
			if (!sesion.isAdmin()) { accesoDenegado(); return; }
			new ContabilidadMensualController(new ContabilidadMensualModel(), new ContabilidadMensualView())
					.initController();
		});
		panelCentro.add(btnContabilidad);

		// Informe de Ocupacion (Admin)
		/* JButton btnInformeOcupacion = new JButton("Informe de Ocupacion (Administracion)");
		btnInformeOcupacion.addActionListener(e -> {
			if (!sesion.isAdmin()) { accesoDenegado(); return; }
			new InformeOcupacionController(new InformeOcupacionModel(), new InformeOcupacionView())
					.initController();
		});
		panelCentro.add(btnInformeOcupacion);
*/
	
		// Panel inferior: Cambiar de usuario
		JPanel panelInferior = new JPanel(new BorderLayout());
		JButton btnCambiarUsuario = new JButton("Cambiar de usuario");
		btnCambiarUsuario.addActionListener(e -> {
			frame.getContentPane().removeAll();
			frame.revalidate();
			frame.repaint();
			login();
		});
		panelInferior.add(btnCambiarUsuario, BorderLayout.EAST);
		JButton btnInformeMorosos = new JButton("Informe de Socios Morosos (Administracion)");
		btnInformeMorosos.addActionListener(e -> {
		    if (!sesion.isAdmin()) { accesoDenegado(); return; }
		    new InformeMorososController(new InformeMorososModel(), new InformeMorososView())
		            .initController();
		});
		panelCentro.add(btnInformeMorosos);

		// Pintar
		frame.getContentPane().removeAll();
		frame.add(panelCentro, BorderLayout.CENTER);
		frame.add(panelInferior, BorderLayout.SOUTH);
		frame.revalidate();
		frame.repaint();
	}

	private void accesoDenegado() {
		JOptionPane.showMessageDialog(frame,
				"No tienes permisos para acceder a esta funcionalidad.\n"
				+ "Solo un administrador puede acceder.",
				"Acceso denegado", JOptionPane.WARNING_MESSAGE);
	}
}