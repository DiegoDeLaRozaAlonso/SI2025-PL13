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
	private SocioDTO usuario;
	
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
		vista.getRadioNoSocio().addActionListener(e -> SwingUtil.exceptionWrapper(() -> alternarVistaSocio()));
		vista.getRadioSocio().addActionListener(e -> SwingUtil.exceptionWrapper(() -> alternarVistaSocio()));
	
	}
	
	
	private void alternarVistaSocio() {
	    if (vista.getRadioSocio().isSelected()) {
	        // Si es SOCIO, vuelve a aparecer la opción de mensualidad
	        vista.getRadioMensual().setVisible(true);
	    } else {
	        // Si es NO SOCIO, desaparece la mensualidad
	        vista.getRadioMensual().setVisible(false);
	        
	        // Si justo estaba seleccionada la mensualidad, la cambiamos a Tarjeta automáticamente
	        if (vista.getRadioMensual().isSelected()) {
	            vista.getRadioTarjeta().setSelected(true);
	        }
	    }
	}
	
	/**
	 * Se encarga de listar las actividades en la tabla según la fecha seleccionada
	 */
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
	 * Comprueba si está seleccionada la opción de socio o no socio
	 * @return
	 */
	private boolean esSocio() {
		return vista.getRadioSocio().isSelected();
	}
	
	
	/**
	 * Método que inscribe al Usuario generando una inscripcion
	 */
	//TODO pillar el usuario que se logea en el main
	private void crearInscripcion() {
		
		boolean esSocio = esSocio();

		//Comprobamos si está seleccionado socio
		if (esSocio) {
			usuario = (SocioDTO) vista.getComboSocio().getSelectedItem();
			
			//Cogemos al usuario seleccionado en el comboBox
			if (model.tieneDeudas(usuario)) {
				JOptionPane.showMessageDialog(
						vista.getFrame(), "Tienes deudas no puedes inscribirte","¡¡¡MOROSO!!!", JOptionPane.ERROR_MESSAGE);
				return;
			}
		} else {
			String nombre, dni, correo, telefono;
			
			nombre = vista.getNombre().getText();
			dni = vista.getDNI().getText();
			correo = vista.getCorreo().getText();
			telefono = vista.getTelefono().toString();
		}
		
		
		int filaSeleccionada = vista.getTable().getSelectedRow();
		
		if (filaSeleccionada == -1) {
	        JOptionPane.showMessageDialog(vista.getFrame(), "Por favor, selecciona una actividad de la tabla.");
	        return;
	    }
		
		ActividadDTO actividad = actividades.get(filaSeleccionada); //actividad seleccionada en la tabla
		Date hoy = new Date();//coge la fecha de hoy
		String fechaActual = Util.dateToIsoString(hoy); //convierte a String la fecha
		String estado = model.compruebaAforo(actividad);//comprueba que queda aforo disponible
		boolean estaPagado = false;
		
		model.enPlazo(actividad, esSocio); //comprueba que esté dentro del plazo
		
		if (model.inscripcionRepetida(usuario, actividad) == true) {
			JOptionPane.showMessageDialog(vista.getFrame(),
					"El usuario ya está inscrito", "Inscripcion ya hecha", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
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
		if (vista.getRadioTarjeta().isSelected()) {
			estaPagado = true;
		}
		
		InscripcionDTO ins = new InscripcionDTO(
				actividad.getId(), this.usuario.getId_socio(), fechaActual,
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
	
	/**
	 * Lo que se ve nada más se abre la página
	 */
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
