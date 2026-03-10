package cd.socio.pablo.inscripcionActividad;

import java.util.Date;
import java.util.List;

import giis.demo.util.SwingUtil;

public class InscribirSocioController {
	
	private InscribirSocioModel model;
	private InscribirSocioView vista;
	
	public InscribirSocioController(InscribirSocioModel m, InscribirSocioView v) {
		this.model = m;
		this.vista = v;
		//no hay inicializacion especifica del modelo, solo de la vista
		this.initView();
		this.initController();
	}
	
	
	public void initController() {
		
		vista.getBotonVolver().addActionListener(e -> SwingUtil.exceptionWrapper(() -> vista.getFrame().dispose()));
		vista.getBotonListarActividades().addActionListener(e -> SwingUtil.exceptionWrapper(() -> listaActividades()));
		vista.getBotonInscribir().addActionListener(e -> SwingUtil.exceptionWrapper(() -> vista.getFrame().dispose()));
	
	}
	
	private void listaActividades() {
		/*Date dateInicio = vista.getFechaInicio().getDate();
		Date dateFin = vista.getFechaFin().getDate();
		String fechaInicio;
		String fechaFin;

		if(dateInicio != null) {
			fechaInicio = dateInicio.toString();
		}
		
		if(dateFin != null) {
			fechaFin = dateFin.toString();
		}
		*/
		List<ActividadDTO> actividades = model.getListaActividades(
				vista.getFechaInicio().getDate().toString(),
				vista.getFechaFin().getDate().toString() 
		);
			
		//Definimos las columnas de la tabla
		String[] columnas = {"nombre", "desc", "aforo", 
				"fecha_inicio", "fecha_fin", "precioSocio", "precioNoSocio"};
		
		javax.swing.table.TableModel tmodel = SwingUtil.getTableModelFromPojos(actividades, columnas);
		vista.getTable().setModel(tmodel);

		//Auto ajustamos el tamaño de las columnas
		SwingUtil.autoAdjustColumns(vista.getTable());
		
	}
	
	public void initView() {
		
		//limpiamos la lista por si acaso
		vista.getLabelSocio().setText("");
		
		//Cargamos el primer periodo en la tabla para que no salga vacía
		listaActividades();
		
		// Abre la ventana (sustituye al main generado por WindowBuilder)
		vista.getFrame().setVisible(true); 
	}
}
