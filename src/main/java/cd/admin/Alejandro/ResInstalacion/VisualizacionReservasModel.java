package cd.admin.Alejandro.ResInstalacion;

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
 * Permite obtener la lista de instalaciones y construir el grid de ocupacion semanal,
 * combinando reservas de socios y sesiones de actividades del centro.
 *
 * <br/>La logica de negocio se realiza mediante queries SQL usando Apache commons-dbutils.
 */
public class VisualizacionReservasModel {

	/** Franjas horarias que muestra el calendario: de 8:00 a 21:00 */
	public static final String[] HORAS = {
		"08:00","09:00","10:00","11:00","12:00","13:00","14:00",
		"15:00","16:00","17:00","18:00","19:00","20:00","21:00"
	};

	/** Numero de dias que cubre el calendario (30 dias a partir de hoy) */
	public static final int DIAS_TOTALES = 30;

	/** Numero de dias mostrados por pagina (semana) */
	public static final int DIAS_POR_PAGINA = 7;

	private Database db = new Database();

	/**
	 * Obtiene todas las instalaciones activas (en uso) disponibles para seleccionar.
	 */
	public List<InstalacionEntity> getInstalaciones() {
		String sql = "SELECT id_instalacion AS id, nombre, tipo, capacidad, en_uso AS enUso"
				+ " FROM Instalaciones WHERE en_uso = 1 ORDER BY nombre";
		return db.executeQueryPojo(InstalacionEntity.class, sql);
	}

	/**
	 * Devuelve un mapa de celdas ocupadas (clave: "fecha|hora") para una instalacion
	 * y rango de fechas dado. Las celdas no presentes en el mapa son libres.
	 * Combina reservas de socios y sesiones de actividades del centro.
	 *
	 * @param idInstalacion id de la instalacion seleccionada
	 * @param fechaInicio   primer dia del rango (inclusive)
	 * @param fechaFin      ultimo dia del rango (inclusive)
	 * @return mapa "fecha|hora" -> ReservaCeldaDTO con tipo y nombre
	 */
	public Map<String, ReservaCeldaDTO> getOcupacionPorRango(int idInstalacion, Date fechaInicio, Date fechaFin) {
		validateNotNull(fechaInicio, "La fecha de inicio no puede ser nula");
		validateNotNull(fechaFin, "La fecha de fin no puede ser nula");

		String dInicio = Util.dateToIsoString(fechaInicio);
		String dFin    = Util.dateToIsoString(fechaFin);

		// Reservas individuales de socios
		String sqlSocios = "SELECT"
				+ "  strftime('%Y-%m-%d', r.fecha_hora_inicio) AS fecha,"
				+ "  strftime('%H:00', r.fecha_hora_inicio) AS hora,"
				+ "  'socio' AS tipo,"
				+ "  s.nombre || ' (S-' || printf('%03d', s.id_socio) || ')' AS nombre"
				+ " FROM Reservas r"
				+ " JOIN Socios s ON r.id_socio = s.id_socio"
				+ " WHERE r.id_instalacion = ?"
				+ "   AND r.estado IN ('activa', 'completada')"
				+ "   AND date(r.fecha_hora_inicio) BETWEEN ? AND ?";

		List<ReservaCeldaDTO> celdasSocios = db.executeQueryPojo(ReservaCeldaDTO.class, sqlSocios,
				idInstalacion, dInicio, dFin);

		// Sesiones de actividades del centro
		String sqlActividades = "SELECT"
				+ "  sa.fecha,"
				+ "  substr(sa.hora_inicio, 1, 2) || ':00' AS hora,"
				+ "  'actividad' AS tipo,"
				+ "  a.nombre"
				+ " FROM SesionesActividad sa"
				+ " JOIN Actividades a ON sa.id_actividad = a.id_actividad"
				+ " WHERE sa.id_instalacion = ?"
				+ "   AND sa.fecha BETWEEN ? AND ?";

		List<ReservaCeldaDTO> celdasActividades = db.executeQueryPojo(ReservaCeldaDTO.class, sqlActividades,
				idInstalacion, dInicio, dFin);

		// Combinar en mapa para acceso O(1) desde el controlador
		Map<String, ReservaCeldaDTO> mapa = new HashMap<>();
		for (ReservaCeldaDTO c : celdasSocios)
			mapa.put(clave(c.getFecha(), c.getHora()), c);
		for (ReservaCeldaDTO c : celdasActividades)
			mapa.put(clave(c.getFecha(), c.getHora()), c);

		return mapa;
	}

	/**
	 * Construye el grid completo de celdas (HORAS x numDias) para una semana dada.
	 * Las posiciones no ocupadas se rellenan con tipo "libre".
	 *
	 * @param idInstalacion  id de la instalacion
	 * @param fechaInicioSemana primer dia de la semana a mostrar
	 * @param numDias        numero de dias de la semana (normalmente DIAS_POR_PAGINA)
	 * @return matriz [hora][dia] de ReservaCeldaDTO
	 */
	public ReservaCeldaDTO[][] getGridSemana(int idInstalacion, Date fechaInicioSemana, int numDias) {
		Date fechaFin = sumarDias(fechaInicioSemana, numDias - 1);
		Map<String, ReservaCeldaDTO> ocupacion = getOcupacionPorRango(idInstalacion, fechaInicioSemana, fechaFin);

		ReservaCeldaDTO[][] grid = new ReservaCeldaDTO[HORAS.length][numDias];
		for (int h = 0; h < HORAS.length; h++) {
			for (int d = 0; d < numDias; d++) {
				Date diaActual = sumarDias(fechaInicioSemana, d);
				String fecha = Util.dateToIsoString(diaActual);
				String hora  = HORAS[h];
				String key   = clave(fecha, hora);
				ReservaCeldaDTO celda = ocupacion.getOrDefault(key, new ReservaCeldaDTO("libre", "", fecha, hora));
				grid[h][d] = celda;
			}
		}
		return grid;
	}

	/**
	 * Genera la lista de fechas (Date) para una semana a partir de una fecha de inicio.
	 */
	public List<Date> getFechasSemana(Date fechaInicio, int numDias) {
		List<Date> fechas = new ArrayList<>();
		for (int i = 0; i < numDias; i++)
			fechas.add(sumarDias(fechaInicio, i));
		return fechas;
	}

	// ---- utilidades internas ----

	private String clave(String fecha, String hora) {
		return fecha + "|" + hora;
	}

	private Date sumarDias(Date base, int dias) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(base);
		cal.add(Calendar.DAY_OF_MONTH, dias);
		return cal.getTime();
	}

	private void validateNotNull(Object obj, String message) {
		if (obj == null)
			throw new ApplicationException(message);
	}
}
