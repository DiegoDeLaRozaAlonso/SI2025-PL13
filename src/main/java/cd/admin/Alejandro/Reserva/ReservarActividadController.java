package cd.admin.Alejandro.Reserva;

import java.util.List;

import giis.demo.util.SwingUtil;

/**
 * Controlador para la pantalla de reserva de instalacion para actividades (administracion).
 * Es el punto de entrada de esta pantalla; se invoca:
 *  - instanciando el controlador con la vista y el modelo
 *  - ejecutando initController() para instalar los manejadores de eventos
 *
 * <br/>Orquesta la deteccion de conflictos en tiempo real (al cambiar cualquier campo del formulario),
 * la actualizacion del panel de resumen lateral y la creacion de la sesion al confirmar.
 */
public class ReservarActividadController {

	private ReservarActividadModel model;
	private ReservarActividadView  view;

	/** Listas cargadas una sola vez al inicializar */
	private List<ActividadEntity>   actividades;
	private List<InstalacionEntity> instalaciones;

	public ReservarActividadController(ReservarActividadModel m, ReservarActividadView v) {
		this.model = m;
		this.view  = v;
		this.initView();
	}

	/**
	 * Instala los manejadores de eventos en los componentes de la vista.
	 * Cada cambio en el formulario dispara la deteccion de conflictos y la actualizacion del resumen.
	 */
	public void initController() {
		// Cualquier cambio en los selectores del formulario actualiza conflictos y resumen
		view.getCmbActividad().addActionListener(
				e -> SwingUtil.exceptionWrapper(() -> actualizarEstado()));

		view.getCmbInstalacion().addActionListener(
				e -> SwingUtil.exceptionWrapper(() -> actualizarEstado()));

		// Al cambiar la fecha en el spinner, recalcula conflictos
		view.getSpnFecha().addChangeListener(
				e -> SwingUtil.exceptionWrapper(() -> actualizarEstado()));

		view.getCmbHoraInicio().addActionListener(
				e -> SwingUtil.exceptionWrapper(() -> actualizarEstado()));

		view.getCmbHoraFin().addActionListener(
				e -> SwingUtil.exceptionWrapper(() -> actualizarEstado()));

		// Boton confirmar
		view.getBtnConfirmar().addActionListener(
				e -> SwingUtil.exceptionWrapper(() -> confirmarReserva()));
	}

	/**
	 * Inicializacion de la vista: carga listas de actividades e instalaciones,
	 * rellena las opciones de hora y muestra el estado inicial.
	 */
	public void initView() {
		actividades   = model.getActividades();
		instalaciones = model.getInstalaciones();

		view.setActividades(actividades);
		view.setInstalaciones(instalaciones);
		view.setOpcionesHora(ReservarActividadModel.OPCIONES_HORA);

		actualizarEstado();
		view.getFrame().setVisible(true);
	}

	// ── Manejadores de eventos ────────────────────────────────────────────────

	/**
	 * Recalcula conflictos y actualiza el resumen lateral.
	 * Se invoca cada vez que cambia cualquier campo del formulario.
	 */
	private void actualizarEstado() {
		String actividad    = view.getActividadSeleccionada();
		String instalacion  = view.getInstalacionSeleccionada();
		String fecha        = view.getFecha();
		String horaInicio   = view.getHoraInicio();
		String horaFin      = view.getHoraFin();

		// Actualizar resumen lateral con los valores actuales
		int duracion = calcularDuracion(horaInicio, horaFin);
		view.actualizarResumen(actividad, instalacion, fecha, horaInicio, horaFin, duracion);

		// Comprobar si los campos minimos estan cubiertos para poder detectar conflictos
		boolean camposCompletos = !actividad.isEmpty()
				&& !instalacion.isEmpty()
				&& !fecha.isEmpty()
				&& duracion > 0;

		List<ConflictoDTO> conflictos;
		if (camposCompletos) {
			int idInstalacion = getIdInstalacion(instalacion);
			// Si el horario es invalido (fin <= inicio), el modelo lanzara excepcion
			// que SwingUtil.exceptionWrapper mostrara como mensaje informativo
			conflictos = model.detectarConflictos(idInstalacion, fecha, horaInicio, horaFin);
		} else {
			conflictos = new java.util.ArrayList<>();
		}

		view.actualizarEstadoConflictos(conflictos, camposCompletos);
	}

	/**
	 * Confirma la reserva: llama al modelo para crear la sesion y muestra el panel de exito.
	 */
	private void confirmarReserva() {
		String actividad   = view.getActividadSeleccionada();
		String instalacion = view.getInstalacionSeleccionada();
		String fecha       = view.getFecha();
		String horaInicio  = view.getHoraInicio();
		String horaFin     = view.getHoraFin();

		int idActividad   = getIdActividad(actividad);
		int idInstalacion = getIdInstalacion(instalacion);

		// El modelo valida de nuevo los conflictos antes de insertar (doble validacion)
		model.crearSesionActividad(idActividad, idInstalacion, fecha, horaInicio, horaFin);

		// Mostrar panel de exito brevemente y refrescar estado
		view.mostrarExito(true, actividad);
		actualizarEstado();

		// Ocultar mensaje de exito tras 3 segundos
		javax.swing.Timer timer = new javax.swing.Timer(3000,
				ev -> view.mostrarExito(false, ""));
		timer.setRepeats(false);
		timer.start();
	}

	// ── Utilidades internas ───────────────────────────────────────────────────

	/** Devuelve el id entero de la actividad con el nombre dado */
	private int getIdActividad(String nombre) {
		for (ActividadEntity a : actividades)
			if (a.getNombre().equals(nombre))
				return Integer.parseInt(a.getId());
		throw new giis.demo.util.ApplicationException("Actividad no encontrada: " + nombre);
	}

	/** Devuelve el id entero de la instalacion con el nombre dado */
	private int getIdInstalacion(String nombre) {
		for (InstalacionEntity i : instalaciones)
			if (i.getNombre().equals(nombre))
				return Integer.parseInt(i.getId());
		throw new giis.demo.util.ApplicationException("Instalacion no encontrada: " + nombre);
	}

	/** Calcula la duracion en horas enteras entre dos cadenas HH:mm; devuelve 0 si el horario es invalido */
	private int calcularDuracion(String horaInicio, String horaFin) {
		try {
			return ReservarActividadModel.calcularDuracionHoras(horaInicio, horaFin);
		} catch (Exception e) {
			return 0;
		}
	}
}
