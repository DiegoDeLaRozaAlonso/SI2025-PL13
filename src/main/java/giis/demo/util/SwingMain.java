package giis.demo.util;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import giis.demo.tkrun.*;
import cd.admin.Alejandro.Reserva.ReservarActividadView;
import cd.admin.Alejandro.Visualizacion.*;

/**
 * Punto de entrada principal que incluye botones para la ejecucion de las pantallas 
 * de las aplicaciones de ejemplo
 * y acciones de inicializacion de la base de datos.
 * No sigue MVC pues es solamente temporal para que durante el desarrollo se tenga posibilidad
 * de realizar acciones de inicializacion
 */
public class SwingMain {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() { //NOSONAR codigo autogenerado
			public void run() {
				try {
					SwingMain window = new SwingMain();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace(); //NOSONAR codigo autogenerado
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SwingMain() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Main");
		frame.setBounds(0, 0, 300, 260); // Aumentado tamaño
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		// BOTÓN TKRUN
		JButton btnEjecutarTkrun = new JButton("Ejecutar giis.demo.tkrun");
		btnEjecutarTkrun.setBounds(50, 20, 200, 23);
		btnEjecutarTkrun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CarrerasController controller =
						new CarrerasController(new CarrerasModel(), new CarrerasView());
				controller.initController();
			}
		});
		frame.getContentPane().add(btnEjecutarTkrun);

		// BOTÓN INICIALIZAR BD
		JButton btnInicializarBaseDeDatos = new JButton("Inicializar Base de Datos");
		btnInicializarBaseDeDatos.setBounds(50, 55, 200, 23);
		btnInicializarBaseDeDatos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Database db = new Database();
				db.createDatabase(false);
			}
		});
		frame.getContentPane().add(btnInicializarBaseDeDatos);

		// BOTÓN CARGAR DATOS
		JButton btnCargarDatosIniciales = new JButton("Cargar Datos Iniciales");
		btnCargarDatosIniciales.setBounds(50, 90, 200, 23);
		btnCargarDatosIniciales.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Database db = new Database();
				db.createDatabase(false);
				db.loadDatabase();
			}
		});
		frame.getContentPane().add(btnCargarDatosIniciales);

		// BOTÓN RESERVAR ACTIVIDAD
		JButton btnReservarActividad = new JButton("Reservar Actividad");
		btnReservarActividad.setBounds(50, 125, 200, 23);
		btnReservarActividad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ReservarActividadView();
			}
		});
		frame.getContentPane().add(btnReservarActividad);

		// BOTÓN VISUALIZAR RESERVAS
		JButton btnVisualizarReservas = new JButton("Visualizar Reservas");
		btnVisualizarReservas.setBounds(50, 160, 200, 23);
		btnVisualizarReservas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new VisualizacionReservasView();
			}
		});
		frame.getContentPane().add(btnVisualizarReservas);
	}

	public JFrame getFrame() {
		return this.frame;
	}
}