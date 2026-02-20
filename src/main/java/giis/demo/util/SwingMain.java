package giis.demo.util;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import giis.demo.tkrun.*;
import javax.swing.JPanel;

import cd.admin.Alejandro.ResInstalacion.VisualizacionReservasController;
import cd.admin.Alejandro.ResInstalacion.VisualizacionReservasModel;
import cd.admin.Alejandro.ResInstalacion.VisualizacionReservasView;

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
		frame.setBounds(0, 0, 287, 185);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		
		JButton btnEjecutarTkrun = new JButton("Ejecutar giis.demo.tkrun");
		btnEjecutarTkrun.setBounds(51, 27, 149, 23);
		btnEjecutarTkrun.addActionListener(new ActionListener() { //NOSONAR codigo autogenerado
			public void actionPerformed(ActionEvent e) {
				CarrerasController controller=new CarrerasController(new CarrerasModel(), new CarrerasView());
				controller.initController();
			}
		});
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(btnEjecutarTkrun);
		
			
		JButton btnInicializarBaseDeDatos = new JButton("Inicializar Base de Datos en Blanco");
		btnInicializarBaseDeDatos.setBounds(28, 61, 197, 23);
		btnInicializarBaseDeDatos.addActionListener(new ActionListener() { //NOSONAR codigo autogenerado
			public void actionPerformed(ActionEvent e) {
				Database db=new Database();
				db.createDatabase(false);
			}
		});
		frame.getContentPane().add(btnInicializarBaseDeDatos);
			
		JButton btnCargarDatosIniciales = new JButton("Cargar Datos Iniciales para Pruebas");
		btnCargarDatosIniciales.setBounds(28, 95, 205, 23);
		btnCargarDatosIniciales.addActionListener(new ActionListener() { //NOSONAR codigo autogenerado
			public void actionPerformed(ActionEvent e) {
				Database db=new Database();
				db.createDatabase(false);
				db.loadDatabase();
			}
		});
		frame.getContentPane().add(btnCargarDatosIniciales);
		
		
		JButton btnVisualizacionReservas = new JButton("Ejecutar Visualizacion de Reservas");
		btnVisualizacionReservas.setBounds(28, 129, 205, 23);
		btnVisualizacionReservas.addActionListener(new ActionListener() { //NOSONAR codigo autogenerado
		    public void actionPerformed(ActionEvent e) {
		        VisualizacionReservasController controller = new VisualizacionReservasController(
		            new VisualizacionReservasModel(), new VisualizacionReservasView());
		        controller.initController();
		    }
		});
		frame.getContentPane().add(btnVisualizacionReservas);

		//JButton btnReservarActividad = new JButton("Ejecutar Reservar Actividad");
		//btnReservarActividad.setBounds(28, 163, 205, 23);
		//btnReservarActividad.addActionListener(new ActionListener() { //NOSONAR codigo autogenerado
		  //  public void actionPerformed(ActionEvent e) {
		    //    ReservarActividadController controller = new ReservarActividadController(
		      //      new ReservarActividadModel(), new ReservarActividadView());
		       // controller.initController();
		    //}
		//});
	//	frame.getContentPane().add(btnReservarActividad);
		
	}

	
	
	
	public JFrame getFrame() { return this.frame; }
	
}
