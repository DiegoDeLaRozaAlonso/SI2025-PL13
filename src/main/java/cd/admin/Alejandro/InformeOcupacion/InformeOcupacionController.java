package cd.admin.Alejandro.InformeOcupacion;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import giis.demo.util.ApplicationException;
import giis.demo.util.SwingUtil;

import cd.admin.Alejandro.Visualizacion.InstalacionEntity;

/**
 * Controlador del informe de ocupacion de instalaciones (administracion).
 * Punto de entrada: instanciar y llamar a initController().
 *
 * Flujo:
 *   1. initView()      -> carga combos y fechas por defecto.
 *   2. generarInforme()-> consulta modelo, aplica filtros de Estado y Socio
 *                         en memoria, actualiza tabla y KPIs.
 *   3. exportarTXT()   -> genera y guarda el fichero .txt con el ultimo resultado.
 *   4. limpiarFiltros()-> resetea la vista al estado inicial.
 */
public class InformeOcupacionController {

	private static final String SEP_DOBLE  = repeatChar('=', 90);
	private static final String SEP_SIMPLE = repeatChar('-', 90);

	// Fechas por defecto (inicio del mes actual y hoy)
	private final String fechaInicioDefecto;
	private final String fechaFinDefecto;

	private InformeOcupacionModel model;
	private InformeOcupacionView  view;

	/** Cache del ultimo resultado para la exportacion TXT */
	private List<OcupacionFilaDTO> ultimasFilas      = null;
	private String                 ultimaFechaInicio = "";
	private String                 ultimaFechaFin    = "";

	public InformeOcupacionController(InformeOcupacionModel m, InformeOcupacionView v) {
		this.model = m;
		this.view  = v;
		String hoy = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		this.fechaFinDefecto    = hoy;
		this.fechaInicioDefecto = hoy.substring(0, 7) + "-01";
		initView();
	}

	/** Instala los manejadores de eventos */
	public void initController() {
		view.getBtnGenerar().addActionListener(
				e -> SwingUtil.exceptionWrapper(() -> generarInforme()));

		view.getBtnLimpiar().addActionListener(
				e -> SwingUtil.exceptionWrapper(() -> limpiarFiltros()));

		view.getBtnExportar().addActionListener(
				e -> SwingUtil.exceptionWrapper(() -> exportarTXT()));
	}

	/** Carga los combos, pone las fechas por defecto y muestra la ventana */
	public void initView() {
		List<InstalacionEntity> instalaciones = model.getInstalaciones();
		if (instalaciones.isEmpty())
			throw new ApplicationException(
					"No hay instalaciones activas. "
					+ "Pulse 'Inicializar BD' y 'Cargar Datos Iniciales' en la pantalla principal.");

		view.setInstalaciones(instalaciones);
		view.setActividades(model.getActividades());
		view.getTxtFechaInicio().setText(fechaInicioDefecto);
		view.getTxtFechaFin().setText(fechaFinDefecto);
		view.getFrame().setVisible(true);
	}

	// ── Manejadores ──────────────────────────────────────────────────────────

	/**
	 * Lee los filtros, consulta el modelo y aplica el filtrado adicional
	 * de Estado y texto de Socio en memoria antes de mostrar los resultados.
	 */
	private void generarInforme() {
		String fechaInicio = view.getTxtFechaInicio().getText().trim();
		String fechaFin    = view.getTxtFechaFin().getText().trim();
		validarFechas(fechaInicio, fechaFin);

		int idInstalacion = resolverIdInstalacion(view.getInstalacionSeleccionada());
		int idActividad   = resolverIdActividad(view.getActividadSeleccionada());

		// 1. Obtener datos del modelo (filtro por instalacion y actividad en SQL)
		List<OcupacionFilaDTO> filas = model.getFilasInforme(
				fechaInicio, fechaFin, idInstalacion, idActividad);

		// 2. Filtro por Estado (en memoria: sobre el porcentaje de actividad)
		String estado = view.getEstadoSeleccionado();
		if (!InformeOcupacionView.ESTADO_TODOS.equals(estado)) {
			filas = filas.stream()
					.filter(f -> nivelCoincide(f.getPorcentajeActividad(), estado))
					.collect(Collectors.toList());
		}

		// 3. Filtro por Socio (en memoria: coincidencia de texto en nombre instalacion
		//    o actividad, ya que el informe es por instalacion/actividad, no por socio
		//    individual; un filtro mas preciso requeriria una query adicional)
		String socioTexto = view.getSocioFiltro();
		if (!socioTexto.isEmpty()) {
			String lower = socioTexto.toLowerCase();
			filas = filas.stream()
					.filter(f -> f.getNombreInstalacion().toLowerCase().contains(lower)
							  || f.getNombreActividad().toLowerCase().contains(lower))
					.collect(Collectors.toList());
		}

		// 4. Calcular KPIs
		Set<String> instalacionesDistintas = filas.stream()
				.map(OcupacionFilaDTO::getNombreInstalacion)
				.collect(Collectors.toSet());
		int totalReservas     = filas.stream().mapToInt(f -> parseIntSafe(f.getReservasActivas())).sum();
		int totalPlazasLibres = filas.stream().mapToInt(OcupacionFilaDTO::getPlazasLibres).sum();
		int mediaOcupacion    = filas.isEmpty() ? 0
				: (int) filas.stream().mapToInt(OcupacionFilaDTO::getPorcentajeActividad).average().orElse(0);

		// 5. Actualizar vista
		view.setFilas(filas);
		view.setPeriodo(fechaInicio, fechaFin);
		view.setKpis(instalacionesDistintas.size(), totalReservas, totalPlazasLibres, mediaOcupacion);
		view.getBtnExportar().setEnabled(!filas.isEmpty());

		// Guardar para exportacion
		ultimasFilas      = filas;
		ultimaFechaInicio = fechaInicio;
		ultimaFechaFin    = fechaFin;
	}

	/** Exporta el ultimo resultado a un fichero .txt en el directorio de trabajo */
	private void exportarTXT() {
		if (ultimasFilas == null || ultimasFilas.isEmpty())
			throw new ApplicationException(
					"No hay datos en el informe. Pulse 'Generar informe' primero.");

		String ts     = new SimpleDateFormat("yyyy-MM-dd_HH-mm").format(new Date());
		String nombre = "informe_ocupacion_" + ts + ".txt";

		try (FileWriter fw = new FileWriter(nombre)) {
			fw.write(generarContenidoTXT());
		} catch (IOException e) {
			throw new ApplicationException("No se pudo guardar el fichero: " + e.getMessage());
		}

		javax.swing.JOptionPane.showMessageDialog(
				view.getFrame(),
				"Informe exportado correctamente:\n" + nombre,
				"Exportacion completada",
				javax.swing.JOptionPane.INFORMATION_MESSAGE);
	}

	/** Resetea todos los filtros y limpia la tabla y KPIs */
	private void limpiarFiltros() {
		ultimasFilas = null;
		view.limpiarFiltros(fechaInicioDefecto, fechaFinDefecto);
	}

	// ── Generacion del TXT ────────────────────────────────────────────────────

	String generarContenidoTXT() {
		StringBuilder sb   = new StringBuilder();
		String        ahora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());

		sb.append(SEP_DOBLE).append("\n");
		sb.append("  INFORME DE OCUPACION DE INSTALACIONES\n");
		sb.append("  Generado el  : ").append(ahora).append("\n");
		sb.append("  Periodo      : ").append(ultimaFechaInicio)
		  .append("  ->  ").append(ultimaFechaFin).append("\n");
		sb.append(SEP_DOBLE).append("\n\n");

		// Resumen
		Set<String> instDistintas = ultimasFilas.stream()
				.map(OcupacionFilaDTO::getNombreInstalacion).collect(Collectors.toSet());
		int totalRes   = ultimasFilas.stream().mapToInt(f -> parseIntSafe(f.getReservasActivas())).sum();
		int totalLibres = ultimasFilas.stream().mapToInt(OcupacionFilaDTO::getPlazasLibres).sum();
		double media    = ultimasFilas.stream().mapToInt(OcupacionFilaDTO::getPorcentajeActividad)
				.average().orElse(0.0);

		sb.append("RESUMEN\n");
		sb.append(String.format("  Instalaciones consultadas : %d%n",  instDistintas.size()));
		sb.append(String.format("  Reservas activas (total)  : %d%n",  totalRes));
		sb.append(String.format("  Plazas libres (total)     : %d%n",  totalLibres));
		sb.append(String.format("  Ocupacion media actividad : %.1f%%%n", media));
		sb.append("\n").append(SEP_DOBLE).append("\n\n");

		// Cabecera de la tabla
		sb.append(String.format("%-22s %-20s %10s %10s %10s %12s %6s%n",
				"INSTALACION", "ACTIVIDAD",
				"OC.ACT(%)", "OC.SOC(%)", "RESERVAS", "PLAZAS", "ESTADO"));
		sb.append(SEP_SIMPLE).append("\n");

		// Filas
		for (OcupacionFilaDTO f : ultimasFilas) {
			int    pctAct = f.getPorcentajeActividad();
			String estado = pctAct >= 80 ? "Alta" : pctAct >= 40 ? "Media" : "Baja";
			String plazas = f.getPlazasLibres() + " / " + f.getAforoActividad();
			sb.append(String.format("%-22s %-20s %9d%% %9d%% %10s %12s %6s%n",
					truncar(f.getNombreInstalacion(), 21),
					truncar(f.getNombreActividad(),   19),
					pctAct,
					f.getPorcentajeSocio(),
					f.getReservasActivas(),
					plazas,
					estado));
		}
		sb.append(SEP_DOBLE).append("\n");
		sb.append("Fin del informe.\n");
		return sb.toString();
	}

	// ── Utilidades ────────────────────────────────────────────────────────────

	/** Comprueba si el porcentaje encaja con la opcion de estado seleccionada */
	private boolean nivelCoincide(int pct, String estadoFiltro) {
		switch (estadoFiltro) {
			case InformeOcupacionView.ESTADO_ALTO:  return pct >= 80;
			case InformeOcupacionView.ESTADO_MEDIO: return pct >= 40 && pct < 80;
			case InformeOcupacionView.ESTADO_BAJO:  return pct < 40;
			default: return true;
		}
	}

	private int resolverIdInstalacion(String nombreSel) {
		if ("Todas".equals(nombreSel)) return -1;
		for (InstalacionEntity inst : model.getInstalaciones())
			if (inst.getNombre().equals(nombreSel))
				return Integer.parseInt(inst.getId());
		return -1;
	}

	private int resolverIdActividad(String nombreSel) {
		if ("Todas".equals(nombreSel)) return -1;
		for (ActividadEntity act : model.getActividades())
			if (act.getNombre().equals(nombreSel))
				return Integer.parseInt(act.getId());
		return -1;
	}

	private void validarFechas(String inicio, String fin) {
		if (inicio.isEmpty() || fin.isEmpty())
			throw new ApplicationException("Debe indicar fecha de inicio y fecha de fin.");
		if (!inicio.matches("\\d{4}-\\d{2}-\\d{2}") || !fin.matches("\\d{4}-\\d{2}-\\d{2}"))
			throw new ApplicationException("Formato de fecha incorrecto. Use: yyyy-MM-dd");
		if (inicio.compareTo(fin) > 0)
			throw new ApplicationException("La fecha de inicio no puede ser posterior a la de fin.");
	}

	private int parseIntSafe(String s) {
		if (s == null || s.trim().isEmpty()) return 0;
		try { return Integer.parseInt(s.trim()); }
		catch (NumberFormatException e) { return 0; }
	}

	private String truncar(String s, int max) {
		if (s == null) return "";
		return s.length() <= max ? s : s.substring(0, max - 1) + ".";
	}

	private static String repeatChar(char c, int n) {
		StringBuilder sb = new StringBuilder(n);
		for (int i = 0; i < n; i++) sb.append(c);
		return sb.toString();
	}
}
