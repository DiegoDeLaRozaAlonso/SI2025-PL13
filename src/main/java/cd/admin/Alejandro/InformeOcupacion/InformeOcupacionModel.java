package cd.admin.Alejandro.InformeOcupacion;

import java.util.List;

import giis.demo.util.Database;

/**
 * Acceso a los datos de instalaciones, actividades y ocupacion.
 *
 * CORRECCIONES respecto a la version anterior:
 *   - La subconsulta correlacionada con ? para contar reservas causaba problemas
 *     con el binding de parametros en SQLite/JDBC. Se sustituye por un LEFT JOIN
 *     a una subconsulta agrupada, mucho mas estable.
 *   - Los parametros posicionales ahora son solo 4, todos en el WHERE principal.
 */
public class InformeOcupacionModel {

	private Database db = new Database();

	// ── Combos ────────────────────────────────────────────────────────────────

	public List<cd.admin.Alejandro.Visualizacion.InstalacionEntity> getInstalaciones() {
		String sql = "SELECT"
				+ " CAST(id_instalacion AS TEXT) AS id,"
				+ " nombre, tipo,"
				+ " CAST(capacidad AS TEXT) AS capacidad,"
				+ " CAST(en_uso    AS TEXT) AS enUso"
				+ " FROM Instalaciones WHERE en_uso = 1 ORDER BY nombre";
		return db.executeQueryPojo(
				cd.admin.Alejandro.Visualizacion.InstalacionEntity.class, sql);
	}

	public List<ActividadEntity> getActividades() {
		String sql = "SELECT CAST(id_actividad AS TEXT) AS id, nombre"
				+ " FROM Actividades ORDER BY nombre";
		return db.executeQueryPojo(ActividadEntity.class, sql);
	}

	// ── Informe ───────────────────────────────────────────────────────────────

	/**
	 * Devuelve las filas del informe de ocupacion.
	 *
	 * Se incluyen actividades cuyo periodo se solapa con [fechaInicio, fechaFin]:
	 *   a.fecha_inicio <= fechaFin  AND  a.fecha_fin >= fechaInicio
	 *
	 * Las reservas activas/completadas se cuentan via LEFT JOIN a una subconsulta
	 * agrupada por instalacion (sin subconsulta correlacionada para evitar
	 * problemas de binding con SQLite/JDBC).
	 *
	 * Parametros: fechaInicio, fechaFin, fechaInicio, fechaFin (4 en total).
	 */
	public List<OcupacionFilaDTO> getFilasInforme(
			String fechaInicio, String fechaFin,
			int idInstalacion, int idActividad) {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT")
		   .append(" i.nombre                          AS nombreInstalacion,")
		   .append(" a.nombre                          AS nombreActividad,")
		   .append(" CAST(a.aforo     AS TEXT)         AS aforoActividad,")
		   .append(" CAST(i.capacidad AS TEXT)         AS capacidadInstalacion,")
		   .append(" CAST(COUNT(DISTINCT CASE WHEN insc.estado = 'admitido'")
		   .append(       " THEN insc.id_inscripcion END) AS TEXT) AS inscritosActividad,")
		   .append(" CAST(COALESCE(res.cnt, 0) AS TEXT) AS reservasActivas")
		   .append(" FROM Instalaciones i")
		   .append(" JOIN Actividades a ON a.id_instalacion = i.id_instalacion")
		   .append(" LEFT JOIN Inscripciones insc ON insc.id_actividad = a.id_actividad")
		   // ── Subconsulta agrupada: reservas del periodo por instalacion ──────
		   .append(" LEFT JOIN (")
		   .append("   SELECT id_instalacion, COUNT(*) AS cnt")
		   .append("   FROM Reservas")
		   .append("   WHERE estado IN ('activa','completada')")
		   .append("     AND date(fecha_hora_inicio) BETWEEN ? AND ?")
		   .append(" ) res ON res.id_instalacion = i.id_instalacion")
		   // ── Filtros ──────────────────────────────────────────────────────────
		   .append(" WHERE i.en_uso = 1")
		   .append("   AND a.fecha_inicio <= ?")   // actividad empieza antes del fin del periodo
		   .append("   AND a.fecha_fin    >= ?");  // actividad termina despues del inicio del periodo

		if (idInstalacion != -1)
			sql.append(" AND i.id_instalacion = ").append(idInstalacion);
		if (idActividad != -1)
			sql.append(" AND a.id_actividad = ").append(idActividad);

		sql.append(" GROUP BY i.id_instalacion, i.nombre, i.capacidad,")
		   .append(          " a.id_actividad,  a.nombre,  a.aforo, res.cnt")
		   .append(" ORDER BY i.nombre, a.nombre");

		// Parametros en orden de aparicion:
		// 1. fechaInicio -> BETWEEN ? (subconsulta reservas)
		// 2. fechaFin    -> AND ?     (subconsulta reservas)
		// 3. fechaFin    -> a.fecha_inicio <= ?
		// 4. fechaInicio -> a.fecha_fin    >= ?
		return db.executeQueryPojo(OcupacionFilaDTO.class, sql.toString(),
				fechaInicio, fechaFin,
				fechaFin, fechaInicio);
	}
}
