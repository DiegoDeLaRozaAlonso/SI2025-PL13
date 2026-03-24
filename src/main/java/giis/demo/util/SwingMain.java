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
import cd.admin.diego.resact.ResActController;
import cd.admin.diego.resact.ResActModel;
import cd.admin.diego.resact.ResActView;

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
		JButton planificarActividad = new JButton("Planificar actividad (Administracion)");
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
		
		JButton btnReservaParaActividad = new JButton("Reserva para actividad (Administracion)");
		btnReservaParaActividad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

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

				ResActController controller = new ResActController(
						new ResActModel(),
						new ResActView()
				);
				controller.initController();
			}
		});
		panelCentro.add(btnReservaParaActividad);
		
		/*
		 * Ejecuta la lista de actividades	
		 */
		JButton btnListaActividades = new JButton("Ejecutar ListaActividades");
		btnListaActividades.addActionListener(new ActionListener() { //NOSONAR codigo autogenerado
			public void actionPerformed(ActionEvent e) {
				
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
				
				ListaPeriodoController controller=new ListaPeriodoController(new ListaPeriodoModel(), new ListaPeriodoView());
				controller.initController();
			}
		});
		panelCentro.add(btnListaActividades);
		
		
		/*
		 * Un usuario se inscribe a si mismo
		 */
		JButton btnInscribirUsuario = new JButton("Inscripcion Actividad (Socio)");
		btnInscribirUsuario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				InscribirSocioController controller = new InscribirSocioController(new InscribirSocioModel(), new InscribirSocioView(), sesion);
				controller.initController();
			}
		});
		panelCentro.add(btnInscribirUsuario);
		
		
		
		//Crear reserva admin
				JButton crearReservaAdmin = new JButton("Crear Reserva Admin");
				crearReservaAdmin.addActionListener(e -> {

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

				    // Crea vista y modelo
				    administracion.ReservaAdminVista vista = new administracion.ReservaAdminVista();
				    administracion.ReservaAdminModelo modelo = new administracion.ReservaAdminModelo();

				    // Crea el controlador (vista, modelo)
				    administracion.ReservaAdminControlador controller =
				            new administracion.ReservaAdminControlador(vista, modelo);

				    // Muestra la ventana (centrada respecto al frame principal)
				    JFrame reservaFrame = vista.getFrame();
				    reservaFrame.setLocationRelativeTo(frame);
				    reservaFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				    reservaFrame.setVisible(true);
				});
				panelCentro.add(crearReservaAdmin);
		
		//Crear reserva admin
				JButton crearReservaSocio = new JButton("Crear Reserva Socio");
				crearReservaSocio.addActionListener(e -> {

				    if (sesion.isAdmin()) {
				        JOptionPane.showMessageDialog(
				                frame,
				                "No tienes permisos para acceder a esta funcionalidad.\n"
				                + "Esta ventana es para socios.",
				                "Acceso denegado",
				                JOptionPane.WARNING_MESSAGE
				        );
				        return;
				    }
				    String nomreUsuario=sesion.getNombre();
				    // Crea vista y modelo
				    cd.socio.luismi.reservainstalacion.ReservaClienteVista vista = new cd.socio.luismi.reservainstalacion.ReservaClienteVista();
				    cd.socio.luismi.reservainstalacion.ReservaClienteModelo modelo = new cd.socio.luismi.reservainstalacion.ReservaClienteModelo();

				    // Crea el controlador (vista, modelo)
				    
				    cd.socio.luismi.reservainstalacion.ReservaClienteControlador controller =
				            new cd.socio.luismi.reservainstalacion.ReservaClienteControlador(vista, modelo, nomreUsuario);

				    // Muestra la ventana (centrada respecto al frame principal)
				    JFrame reservaFrame = vista.getFrame();
				    reservaFrame.setLocationRelativeTo(frame);
				    reservaFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				    reservaFrame.setVisible(true);
				});
				panelCentro.add(crearReservaSocio);
				
				//Mis reservas socio
				JButton misReservas = new JButton("Mis reservas (socio)");
				misReservas.addActionListener(e -> {

				    if (sesion.isAdmin()) {
				        JOptionPane.showMessageDialog(
				                frame,
				                "No tienes permisos para acceder a esta funcionalidad.\n"
				                + "Esta ventana es para socios.",
				                "Acceso denegado",
				                JOptionPane.WARNING_MESSAGE
				        );
				        return;
				    }

				    cd.socio.diego.misReservas.MisReservasView vista = new cd.socio.diego.misReservas.MisReservasView();
				    cd.socio.diego.misReservas.MisReservasModel modelo = new cd.socio.diego.misReservas.MisReservasModel();

				    cd.socio.diego.misReservas.MisReservasController controller =
				            new cd.socio.diego.misReservas.MisReservasController(modelo, vista, sesion.getId());
				});
				panelCentro.add(misReservas);

				// Cancelar reserva socio
				JButton cancelarReservaSocio = new JButton("Cancelar Mis Reservas");

				cancelarReservaSocio.addActionListener(e -> {

				    // Solo SOCIOS pueden cancelar sus propias reservas
				    if (sesion.isAdmin()) {
				        JOptionPane.showMessageDialog(
				                frame,
				                "Esta funcionalidad es solo para socios.\nUn administrador no puede cancelar aquí.",
				                "Acceso denegado",
				                JOptionPane.WARNING_MESSAGE
				        );
				        return;
				    }

				    // Obtener id del socio logueado
				    int idSocio = sesion.getId();  // ⚠️ ASEGÚRATE DE QUE TU SESION TIENE getId()

				    // Crear modelo y vista para SOCIO
				    cd.socio.luismi.cancelReser.CancelReservaModeloSocio modelo =
				            new cd.socio.luismi.cancelReser.CancelReservaModeloSocio();

				    cd.socio.luismi.cancelReser.CancelarReservaSocioVista vista =
				            new cd.socio.luismi.cancelReser.CancelarReservaSocioVista();

				    // Controlador
				    new cd.socio.luismi.cancelReser.CancelarReservaSocioControlador(
				            idSocio, modelo, vista
				    );

				    // Mostrar ventana
				    vista.setLocationRelativeTo(frame);
				    vista.setVisible(true);
				});

				panelCentro.add(cancelarReservaSocio);
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

		
		// BOTÓN RESERVAR ACTIVIDAD
		JButton btnReservarActividad = new JButton("Reservar Actividad");
		btnReservarActividad.setBounds(28, 129, 205, 23);
		btnReservarActividad.addActionListener(new ActionListener() { //NOSONAR __codigo__ __autogenerado__
		   
			public void actionPerformed(ActionEvent e) {
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
		        ReservarActividadController controller = new ReservarActividadController(
		            new ReservarActividadModel(), new ReservarActividadView());
		        controller.initController();
		    }
		});
		panelCentro.add(btnReservarActividad);


		// BOTÓN VISUALIZAR RESERVAS
		JButton btnVisualizacionReservas = new JButton("Visualización Reservas");
		btnVisualizacionReservas.setBounds(28, 163, 205, 23);
		btnVisualizacionReservas.addActionListener(new ActionListener() { //NOSONAR __codigo__ __autogenerado__
		    public void actionPerformed(ActionEvent e) {
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
		        VisualizacionReservasController controller = new VisualizacionReservasController(
		            new VisualizacionReservasModel(), new VisualizacionReservasView());
		        controller.initController();
		    }
		});
		panelCentro.add(btnVisualizacionReservas);

    // BOTÓN PAGOS PENDIENTES
		JButton btnPagosPendientes = new JButton("Mis Pagos Pendientes");
		btnPagosPendientes.addActionListener(new ActionListener() { //NOSONAR __codigo__ __autogenerado__
		    public void actionPerformed(ActionEvent e) {
		        if (sesion.isAdmin()) {
		            JOptionPane.showMessageDialog(
		                    frame,
		                    "Esta funcionalidad es solo para socios.",
		                    "Acceso denegado",
		                    JOptionPane.WARNING_MESSAGE
		            );
		            return;
		        }
		        PagosPendientesController controller = new PagosPendientesController(
		            sesion.getId(), new PagosPendientesModel(), new PagosPendientesView());
		        controller.initController();
		    }
		});
		panelCentro.add(btnPagosPendientes);
		
		JButton btnContabilidad = new JButton("Contabilidad Mensual");
		btnContabilidad.addActionListener(e -> {
		    if (!sesion.isAdmin()) {
		        JOptionPane.showMessageDialog(frame, "Solo administradores.", "Acceso denegado", JOptionPane.WARNING_MESSAGE);
		        return;
		    }
		    ContabilidadMensualController controller = new ContabilidadMensualController(
		        new ContabilidadMensualModel(), new ContabilidadMensualView());
		    controller.initController();
		});
		panelCentro.add(btnContabilidad);
		
		
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