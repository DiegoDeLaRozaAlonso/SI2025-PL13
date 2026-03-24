package cd.admin.pablo.inscripcionActividad;

import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import cd.login.diego.UsuarioSesion;
import cd.socio.pablo.inscripcionActividad.ActividadDTO;
import cd.socio.pablo.inscripcionActividad.InscripcionDTO;
import cd.socio.pablo.inscripcionActividad.SocioDTO;
import giis.demo.util.SwingUtil;
import giis.demo.util.Util;

public class InscribirAdminController {
	
	private InscribirAdminModel model;
	private InscribirAdminView vista;
	private List<ActividadDTO> actividades;
	private List<SocioDTO> listaSocios;
	private UsuarioSesion usuario;
	
	public InscribirAdminController(InscribirAdminModel m, InscribirAdminView v) {
		this.model = m;
		this.vista = v;
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
				"fecha_inicio", "fecha_fin", "precioSocio", 
				"fecha_inicio_periodo", "fecha_fin_periodo", "fecha_fin_no_socio"};
		
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
		
		ActividadDTO actividad = actividades.get(filaSeleccionada);
		Date hoy = new Date();
		String fechaActual = Util.dateToIsoString(hoy);
		String estado = model.compruebaAforo(actividad);
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
		
		if (model.inscripcionRepetida(usuario, actividad) == true) {
			JOptionPane.showMessageDialog(vista.getFrame(), 
					"El usuario ya está inscrito", "Inscripcion ya hecha", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		InscripcionDTO ins = new InscripcionDTO(
				actividad.getId(), this.usuario.getId(), fechaActual,
				estado, estaPagado, "socio");
		
		if(model.inscribirSocioActividad(usuario, actividad, ins) == 1) {
			JOptionPane.showMessageDialog(
					vista.getFrame(), "Inscripcion en "+ actividad.getNombre() +" realizada con exito");
		}
		else {
			JOptionPane.showMessageDialog(
					vista.getFrame(), ""+actividad.getNombre() +" tiene aforo completo seras añadido a lista de espera");
		}

	}
	
	public void initView() {
		//Cogemos los socios de la BBDD
		listaSocios = model.getSocios();
		
		//limpiamos la lista por si acaso
		vista.getComboSocio().removeAllItems();
		
		for (SocioDTO socio : listaSocios) {
			vista.getComboSocio().addItem(socio);
		}
		
		// Abre la ventana (sustituye al main generado por WindowBuilder)
		vista.getFrame().setVisible(true); 
	}
}
