package cd.admin.Alejandro.Visualizacion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import giis.demo.util.ApplicationException;
import giis.demo.util.Database;
import giis.demo.util.Util;

/**
 * Acceso a los datos de instalaciones y reservas para la pantalla de visualizacion de administracion.
 * NOTA: Todas las columnas INTEGER/REAL de SQLite se castean a TEXT en las queries para que
 * Apache Commons DbUtils pueda mapearlas correctamente a los setters String de las entidades.
 */
public class VisualizacionReservasModel {

	/** Franjas horarias visibles: de 08:00 a 21:00 */
	public static final String[] HORAS = {
		"08:00","09:00","10:00","11:00","12:00","13:00","14:00",
		"15:00","16:00","17:00","18:00","19:00","20:00","21:00"
	};

	public static final int DIAS_TOTALES    = 30;
	public static final int DIAS_POR_PAGINA = 7;

	private Database db = new Database();

	// ── Instalaciones ─────────────────────────────────────────────────────────

	/**
	 * Obtiene las instalaciones activas (en_uso = 1).
	 * Se usa CAST a TEXT para evitar errores de mapeo DbUtils con columnas INTEGER de SQLite.
	 */
	public List<InstalacionEntity> getInstalaciones() {
		String sql = "SELECT"
				+ " CAST(id_instalacion AS TEXT) AS id,"
				+ " nombre,"
				+ " tipo,"
				+ " CAST(capacidad AS TEXT) AS capacidad,"
				+ " CAST(en_uso AS TEXT) AS enUso"
				+ " FROM Instalaciones WHERE en_uso = 1 ORDER BY nombre";
		return db.executeQueryPojo(InstalacionEntity.class, sql);
	}

	// ── Ocupacion ─────────────────────────────────────────────────────────────

	/**
	 * Devuelve un mapa "fecha|hora" -> ReservaCeldaDTO con las celdas ocupadas
	 * (reservas de socios + sesiones de actividades) para una instalacion y rango de fechas.
	 */
	public Map<String, ReservaCeldaDTO> getOcupacionPorRango(int idInstalacion,
			Date fechaInicio, Date fechaFin) {
		validateNotNull(fechaInicio, "La fecha de inicio no puede ser nula");
		validateNotNull(fechaFin,    "La fecha de fin no puede ser nula");

		String dInicio = Util.dateToIsoString(fechaInicio);
		String dFin    = Util.dateToIsoString(fechaFin);

		// Reservas individuales de socios
		String sqlSocios = "SELECT"
				+ " strftime('%Y-%m-%d', r.fecha_hora_inicio) AS fecha,"
				+ " strftime('%H:00', r.fecha_hora_inicio)    AS hora,"
				+ " 'socio' AS tipo,"
				+ " s.nombre || ' (S-' || printf('%03d', s.id_socio) || ')' AS nombre"
				+ " FROM Reservas r"
				+ " JOIN Socios s ON r.id_socio = s.id_socio"
				+ " WHERE r.id_instalacion = ?"
				+ "   AND r.estado IN ('activa','completada')"
				+ "   AND date(r.fecha_hora_inicio) BETWEEN ? AND ?";

		List<ReservaCeldaDTO> celdasSocios =
				db.executeQueryPojo(ReservaCeldaDTO.class, sqlSocios, idInstalacion, dInicio, dFin);

		// Sesiones de actividades del centro
		String sqlActividades = "SELECT"
				+ " sa.fecha,"
				+ " substr(sa.hora_inicio, 1, 2) || ':00' AS hora,"
				+ " 'actividad' AS tipo,"
				+ " a.nombre"
				+ " FROM SesionesActividad sa"
				+ " JOIN Actividades a ON sa.id_actividad = a.id_actividad"
				+ " WHERE sa.id_instalacion = ?"
				+ "   AND sa.fecha BETWEEN ? AND ?";

		List<ReservaCeldaDTO> celdasActividades =
				db.executeQueryPojo(ReservaCeldaDTO.class, sqlActividades, idInstalacion, dInicio, dFin);

		Map<String, ReservaCeldaDTO> mapa = new HashMap<>();
		for (ReservaCeldaDTO c : celdasSocios)
			mapa.put(clave(c.getFecha(), c.getHora()), c);
		for (ReservaCeldaDTO c : celdasActividades)
			mapa.put(clave(c.getFecha(), c.getHora()), c);

		return mapa;
	}

	/**
	 * Construye la matriz [hora][dia] de celdas para la semana indicada.
	 * Las posiciones sin ocupacion se rellenan con tipo "libre".
	 */
	public ReservaCeldaDTO[][] getGridSemana(int idInstalacion, Date fechaInicioSemana, int numDias) {
		Date fechaFin = sumarDias(fechaInicioSemana, numDias - 1);
		Map<String, ReservaCeldaDTO> ocupacion =
				getOcupacionPorRango(idInstalacion, fechaInicioSemana, fechaFin);

		ReservaCeldaDTO[][] grid = new ReservaCeldaDTO[HORAS.length][numDias];
		for (int h = 0; h < HORAS.length; h++) {
			for (int d = 0; d < numDias; d++) {
				String fecha = Util.dateToIsoString(sumarDias(fechaInicioSemana, d));
				String hora  = HORAS[h];
				grid[h][d]   = ocupacion.getOrDefault(
						clave(fecha, hora), new ReservaCeldaDTO("libre", "", fecha, hora));
			}
		}
		return grid;
	}

	/** Lista ordenada de fechas para los dias de una semana */
	public List<Date> getFechasSemana(Date fechaInicio, int numDias) {
		List<Date> fechas = new ArrayList<>();
		for (int i = 0; i < numDias; i++)
			fechas.add(sumarDias(fechaInicio, i));
		return fechas;
	}

	// ── Utilidades ────────────────────────────────────────────────────────────

	private String clave(String fecha, String hora) { return fecha + "|" + hora; }

	private Date sumarDias(Date base, int dias) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(base);
		cal.add(Calendar.DAY_OF_MONTH, dias);
		return cal.getTime();
	}

	private void validateNotNull(Object obj, String message) {
		if (obj == null) throw new ApplicationException(message);
	}
}
