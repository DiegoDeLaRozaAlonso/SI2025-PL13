package cd.admin.diego.informeactividad;

import java.util.ArrayList;
import java.util.List;

import giis.demo.util.Database;

public class InformeActividadModel {

	private Database db = new Database();

	public List<ActividadDTO> obtenerActividades() {
		String sql = "SELECT id_actividad AS idActividad, nombre AS nombre "
				+ "FROM Actividades ORDER BY nombre";
		return db.executeQueryPojo(ActividadDTO.class, sql);
	}

	public List<Integer> obtenerAniosDisponibles() {
		String sql = "SELECT DISTINCT CAST(strftime('%Y', fecha_inicio) AS INTEGER) AS anho "
				+ "FROM Actividades ORDER BY anho";

		List<Object[]> rows = db.executeQueryArray(sql);
		List<Integer> anios = new ArrayList<>();

		for (Object[] row : rows) {
			if (row[0] != null) {
				anios.add(Integer.parseInt(row[0].toString()));
			}
		}
		return anios;
	}

	public List<PeriodoDTO> obtenerPeriodos() {
		String sql = "SELECT id_periodo AS idPeriodo, nombre AS nombre "
				+ "FROM PeriodosInscripcion ORDER BY fecha_inicio_socio";
		return db.executeQueryPojo(PeriodoDTO.class, sql);
	}

	public List<InformeActividadDTO> obtenerInformePorFechas(String fechaInicio, String fechaFin, Integer idActividad) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();

		sql.append("SELECT ");
		sql.append(" a.nombre AS nombre, ");
		sql.append(" a.edicion AS edicion, ");
		sql.append(" a.aforo AS plazas, ");
		sql.append(" COALESCE(SUM(CASE WHEN i.estado = 'admitido' THEN 1 ELSE 0 END), 0) AS inscritos, ");
		sql.append(" CASE ");
		sql.append("   WHEN a.aforo > 0 THEN ROUND((COALESCE(SUM(CASE WHEN i.estado = 'admitido' THEN 1 ELSE 0 END), 0) * 100.0) / a.aforo, 2) ");
		sql.append("   ELSE 0 ");
		sql.append(" END AS porcentajeOcupacion, ");
		sql.append(" COALESCE(SUM(CASE WHEN i.estado = 'lista_espera' THEN 1 ELSE 0 END), 0) AS enListaEspera ");
		sql.append("FROM Actividades a ");
		sql.append("LEFT JOIN Inscripciones i ON i.id_actividad = a.id_actividad ");
		sql.append("WHERE 1=1 ");

		if (fechaInicio != null && !fechaInicio.isEmpty()) {
			sql.append("AND date(a.fecha_inicio) >= date(?) ");
			params.add(fechaInicio);
		}

		if (fechaFin != null && !fechaFin.isEmpty()) {
			sql.append("AND date(a.fecha_fin) <= date(?) ");
			params.add(fechaFin);
		}

		if (idActividad != null) {
			sql.append("AND a.id_actividad = ? ");
			params.add(idActividad);
		}

		sql.append("GROUP BY a.id_actividad, a.nombre, a.edicion, a.aforo ");
		sql.append("ORDER BY a.fecha_inicio ASC, a.id_actividad ASC");

		return db.executeQueryPojo(InformeActividadDTO.class, sql.toString(), params.toArray());
	}

	public List<InformeActividadDTO> obtenerInformePorAnho(int anho, Integer idActividad) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();

		sql.append("SELECT ");
		sql.append(" a.nombre AS nombre, ");
		sql.append(" a.edicion AS edicion, ");
		sql.append(" a.aforo AS plazas, ");
		sql.append(" COALESCE(SUM(CASE WHEN i.estado = 'admitido' THEN 1 ELSE 0 END), 0) AS inscritos, ");
		sql.append(" CASE ");
		sql.append("   WHEN a.aforo > 0 THEN ROUND((COALESCE(SUM(CASE WHEN i.estado = 'admitido' THEN 1 ELSE 0 END), 0) * 100.0) / a.aforo, 2) ");
		sql.append("   ELSE 0 ");
		sql.append(" END AS porcentajeOcupacion, ");
		sql.append(" COALESCE(SUM(CASE WHEN i.estado = 'lista_espera' THEN 1 ELSE 0 END), 0) AS enListaEspera ");
		sql.append("FROM Actividades a ");
		sql.append("LEFT JOIN Inscripciones i ON i.id_actividad = a.id_actividad ");
		sql.append("WHERE strftime('%Y', a.fecha_inicio) = ? ");
		params.add(String.valueOf(anho));

		if (idActividad != null) {
			sql.append("AND a.id_actividad = ? ");
			params.add(idActividad);
		}

		sql.append("GROUP BY a.id_actividad, a.nombre, a.edicion, a.aforo ");
		sql.append("ORDER BY a.fecha_inicio ASC, a.id_actividad ASC");

		return db.executeQueryPojo(InformeActividadDTO.class, sql.toString(), params.toArray());
	}

	public List<InformeActividadDTO> obtenerInformePorPeriodo(int idPeriodo, Integer idActividad) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();

		sql.append("SELECT ");
		sql.append(" a.nombre AS nombre, ");
		sql.append(" a.edicion AS edicion, ");
		sql.append(" a.aforo AS plazas, ");
		sql.append(" COALESCE(SUM(CASE WHEN i.estado = 'admitido' THEN 1 ELSE 0 END), 0) AS inscritos, ");
		sql.append(" CASE ");
		sql.append("   WHEN a.aforo > 0 THEN ROUND((COALESCE(SUM(CASE WHEN i.estado = 'admitido' THEN 1 ELSE 0 END), 0) * 100.0) / a.aforo, 2) ");
		sql.append("   ELSE 0 ");
		sql.append(" END AS porcentajeOcupacion, ");
		sql.append(" COALESCE(SUM(CASE WHEN i.estado = 'lista_espera' THEN 1 ELSE 0 END), 0) AS enListaEspera ");
		sql.append("FROM Actividades a ");
		sql.append("LEFT JOIN Inscripciones i ON i.id_actividad = a.id_actividad ");
		sql.append("WHERE a.id_periodo = ? ");
		params.add(idPeriodo);

		if (idActividad != null) {
			sql.append("AND a.id_actividad = ? ");
			params.add(idActividad);
		}

		sql.append("GROUP BY a.id_actividad, a.nombre, a.edicion, a.aforo ");
		sql.append("ORDER BY a.fecha_inicio ASC, a.id_actividad ASC");

		return db.executeQueryPojo(InformeActividadDTO.class, sql.toString(), params.toArray());
	}
}