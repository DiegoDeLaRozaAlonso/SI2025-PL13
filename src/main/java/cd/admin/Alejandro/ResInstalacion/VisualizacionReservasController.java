package cd.admin.Alejandro.ResInstalacion;

import java.util.Date;
import java.util.List;

import giis.demo.util.SwingUtil;
import giis.demo.util.Util;

/**
 * Controlador para la pantalla de visualizacion del calendario de reservas (administracion).
 * Es el punto de entrada de esta pantalla; se invoca:
 *  - instanciando el controlador con la vista y el modelo
 *  - ejecutando initController() para instalar los manejadores de eventos
 *
 * <br/>Gestiona la navegacion semanal, el cambio de instalacion y la actualizacion del grid.
 */
public class VisualizacionReservasController {

	private VisualizacionReservasModel model;
	private VisualizacionReservasView  view;

	/** Lista de instalaciones disponibles (cargada una sola vez) */
	private List<InstalacionEntity> instalaciones;

	/** Indice de la semana actualmente visible (0 = primera semana desde hoy) */
	private int semanaActual = 0;

	/** Numero total de semanas que cubre el calendario */
	private final int totalSemanas = (int) Math.ceil(
			(double) VisualizacionReservasModel.DIAS_TOTALES / VisualizacionReservasModel.DIAS_POR_PAGINA);

	/** Fecha de inicio del calendario (hoy) */
	private Date fechaBase;

	public VisualizacionReservasController(VisualizacionReservasModel m, VisualizacionReservasView v) {
		this.model = m;
		this.view  = v;
		this.initView();
	}

	/**
	 * Instala los manejadores de eventos en los componentes de la vista.
	 * Cada handler delega en un metodo privado, envuelto en exceptionWrapper
	 * para mostrar ventanas emergentes ante cualquier problema.
	 */
	public void initController() {
		view.getBtnSemanaAnterior().addActionListener(
				e -> SwingUtil.exceptionWrapper(() -> navegarSemana(-1)));

		view.getBtnSemanaSiguiente().addActionListener(
				e -> SwingUtil.exceptionWrapper(() -> navegarSemana(+1)));

		view.getCmbInstalacion().addActionListener(
				e -> SwingUtil.exceptionWrapper(() -> actualizarGrid()));
	}

	/**
	 * Inicializacion de la vista: carga instalaciones, fija la fecha base y muestra el grid inicial.
	 */
	public void initView() {
		fechaBase = Util.isoStringToDate(Util.dateToIsoString(new Date())); // hoy, sin horas

		instalaciones = model.getInstalaciones();
		view.setInstalaciones(instalaciones);

		actualizarGrid();
		view.getFrame().setVisible(true);
	}

	// ── Manejadores de eventos ───────────────────────────────────────────────

	/**
	 * Avanza o retrocede una semana en el calendario.
	 * @param delta +1 para siguiente semana, -1 para anterior
	 */
	private void navegarSemana(int delta) {
		int nueva = semanaActual + delta;
		if (nueva < 0 || nueva >= totalSemanas) return;
		semanaActual = nueva;
		actualizarGrid();
	}

	/**
	 * Actualiza el grid completo segun la instalacion seleccionada y la semana actual.
	 * Obtiene los datos del modelo y los pasa a la vista.
	 */
	private void actualizarGrid() {
		// Identificar la instalacion seleccionada
		int idInstalacion = getIdInstalacionSeleccionada();
		if (idInstalacion < 0) return; // no hay instalaciones en BD

		// Calcular el primer dia de la semana actual
		int offsetDias = semanaActual * VisualizacionReservasModel.DIAS_POR_PAGINA;
		Date inicioSemana = sumarDias(fechaBase, offsetDias);

		// Calcular el numero real de dias de esta pagina (ultima semana puede ser menor)
		int diasRestantes = VisualizacionReservasModel.DIAS_TOTALES - offsetDias;
		int numDias = Math.min(VisualizacionReservasModel.DIAS_POR_PAGINA, diasRestantes);

		// Obtener datos del modelo
		ReservaCeldaDTO[][] grid = model.getGridSemana(idInstalacion, inicioSemana, numDias);
		List<Date>          fechas = model.getFechasSemana(inicioSemana, numDias);

		// Actualizar vista
		view.setGrid(grid, fechas, VisualizacionReservasModel.HORAS);
		view.setSemanaLabel(semanaActual + 1, totalSemanas);
		view.setNavegacionHabilitada(semanaActual > 0, semanaActual < totalSemanas - 1);
	}

	/**
	 * Devuelve el id (entero) de la instalacion actualmente seleccionada en el combo.
	 * Retorna -1 si no hay instalaciones cargadas.
	 */
	private int getIdInstalacionSeleccionada() {
		String nombreSeleccionado = view.getInstalacionSeleccionada();
		for (InstalacionEntity inst : instalaciones) {
			if (inst.getNombre().equals(nombreSeleccionado)) {
				return Integer.parseInt(inst.getId());
			}
		}
		return instalaciones.isEmpty() ? -1 : Integer.parseInt(instalaciones.get(0).getId());
	}

	// ── Utilidad interna ─────────────────────────────────────────────────────

	private Date sumarDias(Date base, int dias) {
		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.setTime(base);
		cal.add(java.util.Calendar.DAY_OF_MONTH, dias);
		return cal.getTime();
	}
}
