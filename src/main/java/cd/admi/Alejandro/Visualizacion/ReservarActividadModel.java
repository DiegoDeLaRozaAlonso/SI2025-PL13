package cd.admi.Alejandro.Visualizacion;

import java.util.ArrayList;
import java.util.List;

import cd.admin.Alejandro.ResInstalacion.InstalacionEntity;
import giis.demo.util.ApplicationException;
import giis.demo.util.Database;

/**
 * Logica de negocio y acceso a datos para la pantalla de reserva de instalacion para actividades.
 * Permite obtener las listas de actividades e instalaciones, detectar conflictos de horario
 * y crear la sesion de actividad una vez validada la ausencia de conflictos.
 *
 * <br/>Toda la logica se implementa mediante queries SQL usando Apache commons-dbutils.
 * Las horas se manejan como strings en formato "HH:mm" para coherencia con SQLite.
 */
public class ReservarActividadModel {

	/** Opciones de hora disponibles: de 08:00 a 22:00 en intervalos de 30 minutos */
	public static final String[] OPCIONES_HORA = generarOpcionesHora();

	private Database db = new Database();

	// ── Listas para los selectores ──────────────────────────────────────────

	/**
	 * Obtiene todas las actividades del centro ordenadas por nombre.
	 */
	public List<ActividadEntity> getActividades() {
		String sql = "SELECT id_actividad AS id, nombre, descripcion,"
				+ " id_instalacion AS idInstalacion, aforo,"
				+ " costo_socio AS costoSocio, costo_no_socio AS costoNoSocio,"
				+ " fecha_inicio AS fechaInicio, fecha_fin AS fechaFin"
				+ " FROM Actividades ORDER BY nombre";
		return db.executeQueryPojo(ActividadEntity.class, sql);
	}

	/**
	 * Obtiene todas las instalaciones activas (en_uso = 1) ordenadas por nombre.
	 * Reutiliza InstalacionEntity definida para la pantalla de visualizacion.
	 */
	public List<InstalacionEntity> getInstalaciones() {
		String sql = "SELECT id_instalacion AS id, nombre, tipo, capacidad, en_uso AS enUso"
				+ " FROM Instalaciones WHERE en_uso = 1 ORDER BY nombre";
		return db.executeQueryPojo(InstalacionEntity.class, sql);
	}

	// ── Deteccion de conflictos ─────────────────────────────────────────────

	/**
	 * Detecta todos los conflictos de horario para el bloque solicitado.
	 * Comprueba tanto reservas de socios como sesiones de actividades ya existentes
	 * en la misma instalacion y fecha, aplicando solapamiento de intervalos.
	 *
	 * <br/>Dos intervalos [A_inicio, A_fin) y [B_inicio, B_fin) se solapan cuando:
	 * A_inicio &lt; B_fin AND A_fin &gt; B_inicio
	 *
	 * @param idInstalacion id de la instalacion seleccionada
	 * @param fecha         fecha en formato ISO (yyyy-MM-dd)
	 * @param horaInicio    hora de inicio en formato HH:mm
	 * @param horaFin       hora de fin en formato HH:mm
	 * @return lista de conflictos encontrados (vacia si no hay ninguno)
	 */
	public List<ConflictoDTO> detectarConflictos(int idInstalacion, String fecha,
			String horaInicio, String horaFin) {
		validateHorario(horaInicio, horaFin);

		List<ConflictoDTO> conflictos = new ArrayList<>();
		conflictos.addAll(getConflictosSocios(idInstalacion, fecha, horaInicio, horaFin));
		conflictos.addAll(getConflictosActividades(idInstalacion, fecha, horaInicio, horaFin));
		return conflictos;
	}

	/** Conflictos con reservas individuales de socios */
	private List<ConflictoDTO> getConflictosSocios(int idInstalacion, String fecha,
			String horaInicio, String horaFin) {
		// Extrae HH:mm de fecha_hora_inicio; duracion en minutos viene de la columna duracion
		String sql = "SELECT"
				+ "  'socio' AS tipo,"
				+ "  s.nombre || ' (S-' || printf('%03d', s.id_socio) || ')' AS nombre,"
				+ "  strftime('%H:%M', r.fecha_hora_inicio) AS horaInicio,"
				+ "  strftime('%H:%M', datetime(r.fecha_hora_inicio, '+' || r.duracion || ' minutes')) AS horaFin"
				+ " FROM Reservas r"
				+ " JOIN Socios s ON r.id_socio = s.id_socio"
				+ " WHERE r.id_instalacion = ?"
				+ "   AND r.estado IN ('activa','completada')"
				+ "   AND date(r.fecha_hora_inicio) = ?"
				+ "   AND strftime('%H:%M', r.fecha_hora_inicio) < ?"
				+ "   AND strftime('%H:%M', datetime(r.fecha_hora_inicio, '+' || r.duracion || ' minutes')) > ?";
		return db.executeQueryPojo(ConflictoDTO.class, sql,
				idInstalacion, fecha, horaFin, horaInicio);
	}

	/** Conflictos con sesiones de actividades ya programadas */
	private List<ConflictoDTO> getConflictosActividades(int idInstalacion, String fecha,
			String horaInicio, String horaFin) {
		String sql = "SELECT"
				+ "  'actividad' AS tipo,"
				+ "  a.nombre,"
				+ "  sa.hora_inicio AS horaInicio,"
				+ "  sa.hora_fin AS horaFin"
				+ " FROM SesionesActividad sa"
				+ " JOIN Actividades a ON sa.id_actividad = a.id_actividad"
				+ " WHERE sa.id_instalacion = ?"
				+ "   AND sa.fecha = ?"
				+ "   AND sa.hora_inicio < ?"
				+ "   AND sa.hora_fin > ?";
		return db.executeQueryPojo(ConflictoDTO.class, sql,
				idInstalacion, fecha, horaFin, horaInicio);
	}

	// ── Creacion de la sesion ───────────────────────────────────────────────

	/**
	 * Crea una nueva sesion de actividad en la instalacion indicada tras comprobar
	 * que no existen conflictos. Lanza ApplicationException si los hubiera.
	 *
	 * @param idActividad   id de la actividad seleccionada
	 * @param idInstalacion id de la instalacion seleccionada
	 * @param fecha         fecha en formato ISO (yyyy-MM-dd)
	 * @param horaInicio    hora de inicio en formato HH:mm
	 * @param horaFin       hora de fin en formato HH:mm
	 */
	public void crearSesionActividad(int idActividad, int idInstalacion,
			String fecha, String horaInicio, String horaFin) {
		validateHorario(horaInicio, horaFin);
		validateNotBlank(fecha, "La fecha no puede estar vacia");

		List<ConflictoDTO> conflictos = detectarConflictos(idInstalacion, fecha, horaInicio, horaFin);
		if (!conflictos.isEmpty())
			throw new ApplicationException(
					"No se puede crear la sesion: existen " + conflictos.size() + " conflicto(s) en este horario");

		String sql = "INSERT INTO SesionesActividad (id_actividad, fecha, hora_inicio, hora_fin, id_instalacion)"
				+ " VALUES (?, ?, ?, ?, ?)";
		db.executeUpdate(sql, idActividad, fecha, horaInicio, horaFin, idInstalacion);
	}

	/**
	 * Obtiene una actividad por su id.
	 */
	public ActividadEntity getActividad(int id) {
		String sql = "SELECT id_actividad AS id, nombre, descripcion,"
				+ " id_instalacion AS idInstalacion, aforo,"
				+ " costo_socio AS costoSocio, costo_no_socio AS costoNoSocio,"
				+ " fecha_inicio AS fechaInicio, fecha_fin AS fechaFin"
				+ " FROM Actividades WHERE id_actividad = ?";
		List<ActividadEntity> lista = db.executeQueryPojo(ActividadEntity.class, sql, id);
		if (lista.isEmpty())
			throw new ApplicationException("Id de actividad no encontrado: " + id);
		return lista.get(0);
	}

	// ── Utilidades ──────────────────────────────────────────────────────────

	/**
	 * Genera las opciones de hora de 08:00 a 22:00 en pasos de 30 minutos.
	 */
	private static String[] generarOpcionesHora() {
		List<String> opciones = new ArrayList<>();
		for (int h = 8; h <= 22; h++) {
			opciones.add(String.format("%02d:00", h));
			if (h < 22)
				opciones.add(String.format("%02d:30", h));
		}
		return opciones.toArray(new String[0]);
	}

	/**
	 * Convierte una hora en formato HH:mm a minutos desde medianoche.
	 */
	public static int horaAMinutos(String hora) {
		String[] partes = hora.split(":");
		return Integer.parseInt(partes[0]) * 60 + Integer.parseInt(partes[1]);
	}

	/**
	 * Calcula la duracion en horas (puede ser decimal) entre dos horas HH:mm.
	 */
	public static double calcularDuracionHoras(String horaInicio, String horaFin) {
		int diff = horaAMinutos(horaFin) - horaAMinutos(horaInicio);
		return diff > 0 ? diff / 60.0 : 0.0;
	}

	private void validateHorario(String horaInicio, String horaFin) {
		validateNotBlank(horaInicio, "La hora de inicio no puede estar vacia");
		validateNotBlank(horaFin, "La hora de fin no puede estar vacia");
		if (horaAMinutos(horaFin) <= horaAMinutos(horaInicio))
			throw new ApplicationException(
					"La hora de fin debe ser posterior a la hora de inicio");
	}

	private void validateNotBlank(String valor, String mensaje) {
		if (valor == null || valor.trim().isEmpty())
			throw new ApplicationException(mensaje);
	}
}
