package cd.admin.diego.cancelAct;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import giis.demo.util.Database;

public class CancelarActividadModel {

	private Database db = new Database();

	public List<ActividadCancelDTO> obtenerActividades(String nombreFiltro, String tipoFiltro) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();

		sql.append("SELECT ");
		sql.append(" a.id_actividad AS idActividad, ");
		sql.append(" a.nombre AS nombre, ");
		sql.append(" a.descripcion AS descripcion, ");
		sql.append(" i.nombre AS instalacion, ");
		sql.append(" a.fecha_inicio AS fechaInicio, ");
		sql.append(" a.fecha_fin AS fechaFin, ");
		sql.append(" a.aforo AS aforo, ");
		sql.append(" COUNT(ins.id_inscripcion) AS inscritos, ");
		sql.append(" a.estado AS estado ");
		sql.append("FROM Actividades a ");
		sql.append("JOIN Instalaciones i ON i.id_instalacion = a.id_instalacion ");
		sql.append("LEFT JOIN Inscripciones ins ");
		sql.append("  ON ins.id_actividad = a.id_actividad ");
		sql.append("  AND ins.estado <> 'cancelada' ");
		sql.append("WHERE a.estado = 'activa' ");

		LocalDate hoy = LocalDate.now();

		if ("ACTIVAS".equals(tipoFiltro)) {
			sql.append("AND date(a.fecha_inicio) <= date(?) ");
			sql.append("AND date(a.fecha_fin) >= date(?) ");
			params.add(hoy.toString());
			params.add(hoy.toString());
		} else if ("FUTURAS".equals(tipoFiltro)) {
			sql.append("AND date(a.fecha_inicio) > date(?) ");
			params.add(hoy.toString());
		} else {
			sql.append("AND date(a.fecha_fin) >= date(?) ");
			params.add(hoy.toString());
		}

		if (nombreFiltro != null && !nombreFiltro.trim().isEmpty()) {
			sql.append("AND lower(a.nombre) LIKE lower(?) ");
			params.add("%" + nombreFiltro.trim() + "%");
		}

		sql.append("GROUP BY ");
		sql.append(" a.id_actividad, a.nombre, a.descripcion, i.nombre, ");
		sql.append(" a.fecha_inicio, a.fecha_fin, a.aforo, a.estado ");
		sql.append("ORDER BY a.fecha_inicio ASC, a.nombre ASC ");

		return db.executeQueryPojo(ActividadCancelDTO.class, sql.toString(), params.toArray());
	}

	public ActividadCancelDTO obtenerActividadPorId(int idActividad) {
		String sql = ""
				+ "SELECT "
				+ " a.id_actividad AS idActividad, "
				+ " a.nombre AS nombre, "
				+ " a.descripcion AS descripcion, "
				+ " i.nombre AS instalacion, "
				+ " a.fecha_inicio AS fechaInicio, "
				+ " a.fecha_fin AS fechaFin, "
				+ " a.aforo AS aforo, "
				+ " COUNT(ins.id_inscripcion) AS inscritos, "
				+ " a.estado AS estado "
				+ "FROM Actividades a "
				+ "JOIN Instalaciones i ON i.id_instalacion = a.id_instalacion "
				+ "LEFT JOIN Inscripciones ins "
				+ "  ON ins.id_actividad = a.id_actividad "
				+ "  AND ins.estado <> 'cancelada' "
				+ "WHERE a.id_actividad = ? "
				+ "GROUP BY "
				+ " a.id_actividad, a.nombre, a.descripcion, i.nombre, "
				+ " a.fecha_inicio, a.fecha_fin, a.aforo, a.estado ";

		List<ActividadCancelDTO> res = db.executeQueryPojo(ActividadCancelDTO.class, sql, idActividad);
		return res.isEmpty() ? null : res.get(0);
	}

	public List<AfectadoActividadDTO> obtenerAfectados(int idActividad) {
		String sql = ""
				+ "SELECT "
				+ " ins.id_inscripcion AS idInscripcion, "
				+ " ins.id_socio AS idSocio, "
				+ " CASE "
				+ "   WHEN ins.tipo = 'socio' THEN s.nombre "
				+ "   ELSE ins.nombre_no_socio "
				+ " END AS nombre, "
				+ " s.email AS email, "
				+ " ins.dni AS dni, "
				+ " ins.tipo AS tipo, "
				+ " ins.pagado AS pagado, "
				+ " ins.estado AS estado, "
				+ " CASE "
				+ "   WHEN ins.tipo = 'socio' THEN a.costo_socio "
				+ "   ELSE a.costo_no_socio "
				+ " END AS montoDescuento "
				+ "FROM Inscripciones ins "
				+ "JOIN Actividades a ON a.id_actividad = ins.id_actividad "
				+ "LEFT JOIN Socios s ON s.id_socio = ins.id_socio "
				+ "WHERE ins.id_actividad = ? "
				+ "AND ins.estado <> 'cancelada' "
				+ "ORDER BY nombre ASC ";

		return db.executeQueryPojo(AfectadoActividadDTO.class, sql, idActividad);
	}

	public void cancelarActividad(int idActividad, String motivo) {
		String sql = ""
				+ "UPDATE Actividades "
				+ "SET estado = 'cancelada', motivo_cancelacion = ? "
				+ "WHERE id_actividad = ? ";
		db.executeUpdate(sql, motivo, idActividad);
	}

	public void cancelarInscripciones(int idActividad) {
		String sql = ""
				+ "UPDATE Inscripciones "
				+ "SET estado = 'cancelada' "
				+ "WHERE id_actividad = ? ";
		db.executeUpdate(sql, idActividad);
	}

	public void borrarSesionesActividad(int idActividad) {
		String sql = "DELETE FROM SesionesActividad WHERE id_actividad = ? ";
		db.executeUpdate(sql, idActividad);
	}

	public void generarReducciones(int idActividad) {
		List<AfectadoActividadDTO> afectados = obtenerAfectados(idActividad);
		ActividadCancelDTO actividad = obtenerActividadPorId(idActividad);

		if (actividad == null) {
			return;
		}

		String descripcionBase = "Descuento por cancelación de actividad " + actividad.getNombre();

		for (AfectadoActividadDTO a : afectados) {
			if (a.getPagado() != 1) {
				continue;
			}

			String sql = ""
					+ "INSERT INTO Reduccion "
					+ " (id_socio, nombre_no_socio, monto, fecha_generacion, fecha_aplicacion, descripcion) "
					+ "VALUES (?, ?, ?, date('now'), NULL, ?) ";

			Object idSocio = "socio".equals(a.getTipo()) ? a.getIdSocio() : null;
			Object nombreNoSocio = "no_socio".equals(a.getTipo()) ? a.getNombre() : null;

			db.executeUpdate(sql, idSocio, nombreNoSocio, a.getMontoDescuento(), descripcionBase);
		}
	}

	public void ejecutarCancelacionCompleta(int idActividad, String motivo) {
		List<AfectadoActividadDTO> afectadosAntes = obtenerAfectados(idActividad);

		cancelarActividad(idActividad, motivo);

		for (AfectadoActividadDTO a : afectadosAntes) {
			if (a.getPagado() == 1) {
				String descripcionBase = "Descuento por cancelación de actividad #" + idActividad;
				String sql = ""
						+ "INSERT INTO Reduccion "
						+ " (id_socio, nombre_no_socio, monto, fecha_generacion, fecha_aplicacion, descripcion) "
						+ "VALUES (?, ?, ?, date('now'), NULL, ?) ";

				Object idSocio = "socio".equals(a.getTipo()) ? a.getIdSocio() : null;
				Object nombreNoSocio = "no_socio".equals(a.getTipo()) ? a.getNombre() : null;

				db.executeUpdate(sql, idSocio, nombreNoSocio, a.getMontoDescuento(), descripcionBase);
			}
		}

		cancelarInscripciones(idActividad);
		borrarSesionesActividad(idActividad);
	}
}