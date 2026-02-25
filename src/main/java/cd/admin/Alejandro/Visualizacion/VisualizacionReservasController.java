package cd.admin.Alejandro.Visualizacion;

import java.util.Date;
import java.util.List;

import giis.demo.util.ApplicationException;
import giis.demo.util.SwingUtil;
import giis.demo.util.Util;

/**
 * Controlador para la pantalla de visualizacion del calendario de reservas (administracion).
 * Punto de entrada: instanciar y llamar a initController().
 */
public class VisualizacionReservasController {

	private VisualizacionReservasModel model;
	private VisualizacionReservasView  view;

	private List<InstalacionEntity> instalaciones;
	private int semanaActual = 0;
	private final int totalSemanas = (int) Math.ceil(
			(double) VisualizacionReservasModel.DIAS_TOTALES
			       / VisualizacionReservasModel.DIAS_POR_PAGINA);
	private Date fechaBase;

	public VisualizacionReservasController(VisualizacionReservasModel m, VisualizacionReservasView v) {
		this.model = m;
		this.view  = v;
		this.initView();
	}

	/** Instala los manejadores de eventos */
	public void initController() {
		view.getBtnSemanaAnterior().addActionListener(
				e -> SwingUtil.exceptionWrapper(() -> navegarSemana(-1)));

		view.getBtnSemanaSiguiente().addActionListener(
				e -> SwingUtil.exceptionWrapper(() -> navegarSemana(+1)));

		// Al cambiar la instalacion en el combo, recarga el grid
		view.getCmbInstalacion().addActionListener(
				e -> SwingUtil.exceptionWrapper(() -> actualizarGrid()));
	}

	/** Carga instalaciones, inicializa la fecha base y muestra el grid */
	public void initView() {
		// Fecha de hoy como base del calendario
		fechaBase = Util.isoStringToDate(Util.dateToIsoString(new Date()));

		// Cargar instalaciones en el combo
		instalaciones = model.getInstalaciones();
		if (instalaciones.isEmpty())
			throw new ApplicationException(
					"No hay instalaciones activas en la base de datos. "
					+ "Pulse 'Inicializar BD' y 'Cargar Datos Iniciales' en la pantalla principal.");

		view.setInstalaciones(instalaciones);

		// Mostrar grid de la primera semana con la primera instalacion
		actualizarGrid();

		view.getFrame().setVisible(true);
	}

	// ── Manejadores ───────────────────────────────────────────────────────────

	private void navegarSemana(int delta) {
		int nueva = semanaActual + delta;
		if (nueva < 0 || nueva >= totalSemanas) return;
		semanaActual = nueva;
		actualizarGrid();
	}

	/**
	 * Recalcula y muestra el grid para la instalacion y semana actuales.
	 */
	private void actualizarGrid() {
		// Obtener id de la instalacion seleccionada
		String nombreSel = view.getInstalacionSeleccionada();
		if (nombreSel.isEmpty()) return;
		int idInstalacion = getIdInstalacion(nombreSel);

		// Primer dia de la semana actual
		int offsetDias = semanaActual * VisualizacionReservasModel.DIAS_POR_PAGINA;
		Date inicioSemana = sumarDias(fechaBase, offsetDias);

		// Numero real de dias de esta pagina (la ultima puede ser menor que 7)
		int diasRestantes = VisualizacionReservasModel.DIAS_TOTALES - offsetDias;
		int numDias = Math.min(VisualizacionReservasModel.DIAS_POR_PAGINA, diasRestantes);

		// Obtener datos y actualizar vista
		ReservaCeldaDTO[][] grid  = model.getGridSemana(idInstalacion, inicioSemana, numDias);
		List<Date>          fechas = model.getFechasSemana(inicioSemana, numDias);

		view.setGrid(grid, fechas, VisualizacionReservasModel.HORAS);
		view.setSemanaLabel(semanaActual + 1, totalSemanas);
		view.setNavegacionHabilitada(semanaActual > 0, semanaActual < totalSemanas - 1);
	}

	// ── Utilidades ────────────────────────────────────────────────────────────

	private int getIdInstalacion(String nombre) {
		for (InstalacionEntity inst : instalaciones)
			if (inst.getNombre().equals(nombre))
				return Integer.parseInt(inst.getId());
		throw new ApplicationException("Instalacion no encontrada: " + nombre);
	}

	private Date sumarDias(Date base, int dias) {
		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.setTime(base);
		cal.add(java.util.Calendar.DAY_OF_MONTH, dias);
		return cal.getTime();
	}
}
