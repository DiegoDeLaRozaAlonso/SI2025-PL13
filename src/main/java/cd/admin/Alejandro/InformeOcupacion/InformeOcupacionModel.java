package cd.admin.Alejandro.InformeOcupacion;

import java.util.List;

import giis.demo.util.Database;

/**
 * Acceso a los datos de instalaciones, actividades y ocupacion
 * para el informe de administracion.
 *
 * NOTA: todas las columnas INTEGER/REAL de SQLite se castean a TEXT en las queries
 * para que Apache Commons DbUtils pueda mapearlas correctamente a los setters
 * String de las entidades y DTOs.
 */
public class InformeOcupacionModel {

	private Database db = new Database();

	// ── Instalaciones ─────────────────────────────────────────────────────────

	/**
	 * Devuelve las instalaciones activas (en_uso = 1) ordenadas por nombre.
	 * Reutiliza InstalacionEntity del paquete de Visualizacion ya que la entidad
	 * es identica; si se prefiere independencia puede copiarse al paquete local.
	 */
	public List<cd.admin.Alejandro.Visualizacion.InstalacionEntity> getInstalaciones() {
		String sql = "SELECT"
				+ " CAST(id_instalacion AS TEXT) AS id,"
				+ " nombre,"
				+ " tipo,"
				+ " CAST(capacidad AS TEXT) AS capacidad,"
				+ " CAST(en_uso AS TEXT) AS enUso"
				+ " FROM Instalaciones WHERE en_uso = 1 ORDER BY nombre";
		return db.executeQueryPojo(
				cd.admin.Alejandro.Visualizacion.InstalacionEntity.class, sql);
	}

	// ── Actividades ───────────────────────────────────────────────────────────

	/**
	 * Devuelve todas las actividades ordenadas por nombre, para poblar el combo de filtros.
	 */
	public List<ActividadEntity> getActividades() {
		String sql = "SELECT"
				+ " CAST(id_actividad AS TEXT) AS id,"
				+ " nombre"
				+ " FROM Actividades ORDER BY nombre";
		return db.executeQueryPojo(ActividadEntity.class, sql);
	}

	// ── Informe de ocupacion ──────────────────────────────────────────────────

	/**
	 * Obtiene las filas del informe de ocupacion para el rango de fechas indicado
	 * y los filtros opcionales de instalacion y actividad.
	 *
	 * Cada fila agrupa una instalacion con una actividad y calcula:
	 *   - inscritosActividad : socios con estado 'admitido' en la actividad
	 *   - aforoActividad      : plazas maximas de la actividad
	 *   - reservasActivas     : reservas en estado 'activa' o 'completada' de esa
	 *                           instalacion dentro del periodo consultado
	 *   - capacidadInstalacion: capacidad maxima de la instalacion
	 *
	 * Los parametros idInstalacion e idActividad son opcionales:
	 * si valen -1 no se aplica ese filtro.
	 *
	 * @param fechaInicio  fecha inicio en formato ISO (yyyy-MM-dd)
	 * @param fechaFin     fecha fin   en formato ISO (yyyy-MM-dd)
	 * @param idInstalacion filtro de instalacion (-1 = todas)
	 * @param idActividad   filtro de actividad   (-1 = todas)
	 * @return lista de OcupacionFilaDTO ordenada por instalacion y actividad
	 */
	public List<OcupacionFilaDTO> getFilasInforme(
			String fechaInicio, String fechaFin,
			int idInstalacion, int idActividad) {

		// Subconsulta para contar reservas activas en el periodo para cada instalacion.
		// Se usa como columna correlacionada para que DbUtils la mapee con setReservasActivas.
		String subReservas =
				"(SELECT CAST(COUNT(*) AS TEXT) FROM Reservas r"
				+ " WHERE r.id_instalacion = i.id_instalacion"
				+ "   AND r.estado IN ('activa','completada')"
				+ "   AND date(r.fecha_hora_inicio) BETWEEN ? AND ?)";

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT")
		   .append(" i.nombre AS nombreInstalacion,")
		   .append(" a.nombre AS nombreActividad,")
		   .append(" CAST(a.aforo      AS TEXT) AS aforoActividad,")
		   .append(" CAST(i.capacidad  AS TEXT) AS capacidadInstalacion,")
		   .append(" CAST(COUNT(DISTINCT CASE WHEN insc.estado = 'admitido'")
		   .append(       " THEN insc.id_inscripcion END) AS TEXT) AS inscritosActividad,")
		   .append(" ").append(subReservas).append(" AS reservasActivas")
		   .append(" FROM Instalaciones i")
		   .append(" JOIN Actividades a ON a.id_instalacion = i.id_instalacion")
		   .append(" LEFT JOIN Inscripciones insc ON insc.id_actividad = a.id_actividad")
		   .append(" WHERE i.en_uso = 1")
		   .append("   AND a.fecha_fin   >= ?")   // actividad activa en el periodo
		   .append("   AND a.fecha_inicio <= ?");

		if (idInstalacion != -1)
			sql.append("   AND i.id_instalacion = ").append(idInstalacion);
		if (idActividad != -1)
			sql.append("   AND a.id_actividad = ").append(idActividad);

		sql.append(" GROUP BY i.id_instalacion, i.nombre, i.capacidad,")
		   .append(          " a.id_actividad,  a.nombre,  a.aforo")
		   .append(" ORDER BY i.nombre, a.nombre");

		// Parametros posicionales (en orden de aparicion en la query):
		// 1,2: subReservas (fechaInicio, fechaFin)
		// 3,4: clausulas WHERE del rango de actividad (fechaInicio, fechaFin)
		return db.executeQueryPojo(OcupacionFilaDTO.class, sql.toString(),
				fechaInicio, fechaFin,
				fechaInicio, fechaFin);
	}
}
