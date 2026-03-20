package cd.socio.pablo.inscripcionActividad;

import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

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
		this.initView();
	}
	
	
	public void initController() {
		
		vista.getBotonVolver().addActionListener(e -> SwingUtil.exceptionWrapper(() -> vista.getFrame().dispose()));
		vista.getBotonListarActividades().addActionListener(e -> SwingUtil.exceptionWrapper(() -> listaActividades()));
		vista.getBotonInscribir().addActionListener(e -> SwingUtil.exceptionWrapper(() -> crearInscripcion()));
	
	}
	
	private void listaActividades() {
		Date dateInicio = vista.getFechaInicio().getDate();
		Date dateFin = vista.getFechaFin().getDate();
		String fechaInicio = null;
		String fechaFin = null;
		
		
		if(dateInicio != null) {
			fechaInicio = Util.dateToIsoString(dateInicio);
		}
		
		if(dateFin != null) {
			fechaFin = Util.dateToIsoString(dateFin);
		}
		
		/*Lista de actividades de dicho periodo*/
		actividades = model.getListaActividades(fechaInicio, fechaFin);
			
		//Definimos las columnas de la tabla
		String[] columnas = {"nombre", "descripcion", "aforo", 
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
	private void crearInscripcion() {
		
		System.out.println(""+model.tieneDeudas(usuario));
		
		if (model.tieneDeudas(usuario)) {
			JOptionPane.showMessageDialog(
					vista.getFrame(), "Tienes deudas no puedes inscribirte","¡¡¡MOROSO!!!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		int filaSeleccionada = vista.getTable().getSelectedRow();
		
		if (filaSeleccionada == -1) {
	        JOptionPane.showMessageDialog(vista.getFrame(), "Por favor, selecciona una actividad de la tabla.");
	        return;
	    }
		ActividadDTO actividad = actividades.get(filaSeleccionada);
		Date hoy = new Date();
		String fechaActual = Util.dateToIsoString(hoy);
		String estado = model.compruebaAforo(actividad);
		boolean estaPagado = false;
		
		if (vista.getRadioEfectivo().isSelected()) {
			estaPagado = true;
		}
 
		InscripcionDTO ins = new InscripcionDTO(
				actividad.getId(), this.usuario.getId(), fechaActual,
				estado, estaPagado, "socio");
		
		if(model.inscribirSocioActividad(usuario, actividad, ins) == 1) {
			JOptionPane.showMessageDialog(
					vista.getFrame(), "Inscripcion en"+ actividad.getNombre() +" realizada con exito");
		}
		else {
			JOptionPane.showMessageDialog(
					vista.getFrame(), ""+actividad.getNombre() +" tiene aforo completo seras añadido a lista de espera");
		}

	}
	
	public void initView() {
		
		//Ponemos el nombre del usuario que se está inscribiendo
		vista.getLabelSocio().setText(this.usuario.getNombre()+"");
		
		/*//Cargamos el primer periodo en la tabla para que no salga vacía
		listaActividades();*/
		
		// Abre la ventana (sustituye al main generado por WindowBuilder)
		vista.getFrame().setVisible(true); 
	}
}
