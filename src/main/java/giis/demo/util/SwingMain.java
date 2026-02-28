package giis.demo.util;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.*;

import cd.login.diego.*;

public class SwingMain {

	private JFrame frame;
	private UsuarioSesion sesion;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				SwingMain window = new SwingMain();
				window.frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public SwingMain() {

		frame = new JFrame();
		frame.setBounds(0, 0, 500, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		login();
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

		frame.setTitle("Bienvenido " + sesion.getNombre() +
				(sesion.isAdmin() ? " (ADMIN)" : " (SOCIO)"));

		inicializarContenido();
	}

	// ================= CONTENIDO =================
	private void inicializarContenido() {

		JPanel panelCentro = new JPanel();
		panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));

		// Botón BD
		JButton btnInit = new JButton("Inicializar Base de Datos");
		btnInit.addActionListener(e -> {
			Database db = new Database();
			db.createDatabase(false);
		});
		panelCentro.add(btnInit);

		JButton btnCargar = new JButton("Cargar Datos Iniciales");
		btnCargar.addActionListener(e -> {
			Database db = new Database();
			db.createDatabase(false);
			db.loadDatabase();
		});
		panelCentro.add(btnCargar);

		// Botón Admin
		JButton btnAdmin = new JButton("Crear Actividad (Administracion)");
		btnAdmin.addActionListener(e -> {

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
		panelCentro.add(btnAdmin);

		// Botón Socio
		JButton btnSocio = new JButton("Ver Disponibilidad Instalación (Socio)");
		btnSocio.addActionListener(e -> {

			cd.socio.diego.verdispoinstalacion.DisponibilidadController controller =
					new cd.socio.diego.verdispoinstalacion.DisponibilidadController(
							new cd.socio.diego.verdispoinstalacion.DisponibilidadModel(),
							new cd.socio.diego.verdispoinstalacion.DisponibilidadView()
					);

			controller.initController();
		});
		panelCentro.add(btnSocio);

		frame.getContentPane().removeAll();
		frame.add(panelCentro, BorderLayout.CENTER);

		// ================= BOTÓN CAMBIAR USUARIO =================
		JPanel panelInferior = new JPanel(new BorderLayout());
		JButton btnCambiar = new JButton("Cambiar de usuario");

		btnCambiar.addActionListener(e -> {
			frame.getContentPane().removeAll();
			frame.repaint();
			login();  // vuelve al login
		});

		panelInferior.add(btnCambiar, BorderLayout.EAST);
		frame.add(panelInferior, BorderLayout.SOUTH);

		frame.revalidate();
		frame.repaint();
	}
}