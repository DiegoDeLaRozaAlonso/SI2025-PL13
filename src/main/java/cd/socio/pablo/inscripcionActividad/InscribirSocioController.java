package cd.socio.pablo.inscripcionActividad;

import java.util.Date;
import java.util.List;

import cd.login.diego.UsuarioSesion;
import giis.demo.util.SwingUtil;
import giis.demo.util.Util;

public class InscribirSocioController {
	
	private InscribirSocioModel model;
	private InscribirSocioView vista;
	private List<ActividadDTO> actividades;
	private UsuarioSesion usuario;
	
	public InscribirSocioController(InscribirSocioModel m, InscribirSocioView v, UsuarioSesion usuario) {
		this.model = m;
		this.vista = v;
		this.usuario = usuario;
		//no hay inicializacion especifica del modelo, solo de la vista
		this.initView(this.usuario);
		this.initController();
	}
	
	
	public void initController() {
		
		vista.getBotonVolver().addActionListener(e -> SwingUtil.exceptionWrapper(() -> vista.getFrame().dispose()));
		vista.getBotonListarActividades().addActionListener(e -> SwingUtil.exceptionWrapper(() -> listaActividades()));
		vista.getBotonInscribir().addActionListener(e -> SwingUtil.exceptionWrapper(() -> inscribirUsuario()));
	
	}
	
	private void listaActividades() {
		Date dateInicio = vista.getFechaInicio().getDate();
		Date dateFin = vista.getFechaFin().getDate();
		String fechaInicio = Util.dateToIsoString(dateInicio);
		String fechaFin = Util.dateToIsoString(dateFin);
		
		
		/*if(dateInicio != null) {
			fechaInicio = dateInicio.toString();
		}
		
		if(dateFin != null) {
			fechaFin = dateFin.toString();
		}*/
		
		/*Lista de actividades de dicho periodo*/
		actividades = model.getListaActividades(fechaInicio, fechaFin);
			
		//Definimos las columnas de la tabla
		String[] columnas = {"nombre", "desc", "aforo", 
				"fecha_inicio", "fecha_fin", "precioSocio", "fecha_fin_periodo"};
		
		javax.swing.table.TableModel tmodel = SwingUtil.getTableModelFromPojos(actividades, columnas);
		vista.getTable().setModel(tmodel);

		//Auto ajustamos el tamaño de las columnas
		SwingUtil.autoAdjustColumns(vista.getTable());
		
	}
	
	
	
	/**
	 * Método que inscribe al Usuario generando una inscripcion
	 */
	//TODO pillar el usuario que se logea en el main
	private void inscribirUsuario() {
		int filaSeleccionada = vista.getTable().getSelectedRow();
		ActividadDTO actividad = actividades.get(filaSeleccionada);
		Date fechaActual = new Date();
 
		InscripcionDTO ins = new InscripcionDTO(
				actividad.getId(), usuario.getId(), fechaActual,
				"admitido", false, "socio");
	}
	
	public void initView(UsuarioSesion s) {
		
		//limpiamos la lista por si acaso
		vista.getLabelSocio().setText(s.getNombre()+"");
		
		/*//Cargamos el primer periodo en la tabla para que no salga vacía
		listaActividades();*/
		
		// Abre la ventana (sustituye al main generado por WindowBuilder)
		vista.getFrame().setVisible(true); 
	}
}
