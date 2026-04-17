package cd.socio.pablo.inscripcionActividad;

import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import cd.login.diego.UsuarioSesion;
import cd.socio.pablo.listaEspera.ListaEsperaDTO;
import cd.socio.pablo.listaEspera.ListaEsperaModel;
import giis.demo.util.SwingUtil;
import giis.demo.util.Util;

public class InscribirSocioController {
	
	private InscribirSocioModel model;
	private InscribirSocioView vista;
	private List<ActividadDTO> actividades;
	private UsuarioSesion usuario;
	private ActividadDTO actividad;
	private ListaEsperaModel modelEspera;
	
	public InscribirSocioController(InscribirSocioModel m, InscribirSocioView v, UsuarioSesion usuario, ListaEsperaModel l) {
		this.model = m;
		this.vista = v;
		this.usuario = usuario;
		this.modelEspera = l;
		//no hay inicializacion especifica del modelo, solo de la vista
		this.initView();
	}
	
	public ActividadDTO getActividad() {
		return this.actividad;
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
				"fecha_inicio", "fecha_fin", "precioSocio", "fecha_inicio_periodo", "fecha_fin_periodo"};
		
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
		
		actividad = actividades.get(filaSeleccionada);
		Date hoy = new Date();
		String fechaActual = Util.dateToIsoString(hoy);
		boolean estaPagado = false;
		
		model.enPlazo(actividad); //comprueba que no esté inscrito ya en la actividad
		
		if (vista.getRadioEfectivo().isSelected()) {
			String tarjeta = JOptionPane.showInputDialog(
					vista.getFrame(), "Introduzca una tarjeta de crédito", "Procesar pago", JOptionPane.QUESTION_MESSAGE);
			
			//Comprobamos que se ha introducido una tarjeta de crédito
			if(tarjeta == null || tarjeta.trim().isEmpty()) {
				return; //Si se ha dejado el campo vació se cancela la operación
			}
			//Si llegamos aqui es que se ha introducido una tarjeta
			estaPagado = true;
		}
		
		//comprobamos que el usuario no estuviera ya apuntado
		if (model.inscripcionRepetida(usuario, actividad) == true) {
			JOptionPane.showMessageDialog(vista.getFrame(), 
					"El usuario ya está inscrito", "Inscripcion ya hecha", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		InscripcionDTO ins = new InscripcionDTO(
				actividad.getId(), this.usuario.getId(), fechaActual,
				estaPagado, "socio");
		
		if(model.compruebaAforo(actividad) == 1) {
			//Inscribimos al usuario
			model.inscribirSocioActividad(usuario, actividad, ins);
			JOptionPane.showMessageDialog(
					vista.getFrame(), "Inscripcion en "+ actividad.getNombre() +" realizada con exito");
		}
		else {
			int respuesta = JOptionPane.showConfirmDialog(
					vista.getFrame(), 
					""+actividad.getNombre() +" tiene aforo completo, hay " + modelEspera.numeroListaEspera(actividad) + " socios en lista de espera",
					"Aforo Completo",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE);
			if (respuesta == JOptionPane.YES_OPTION) {
				modelEspera.insertarEnListaEspera(usuario, actividad, ins);
				JOptionPane.showMessageDialog(vista.getFrame(), 
						"Ha sido añadido a la lista de espera de " + actividad.getNombre());
			}
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
