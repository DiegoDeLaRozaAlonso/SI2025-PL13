package cd.socio.diego.misReservas;

import java.util.ArrayList;
import java.util.List;

import giis.demo.util.Database;

public class MisReservasModel {

	private Database db = new Database();

	public List<ReservaDTO> obtenerReservas(int idSocio, String fechaInicio, String fechaFin, String filtroEstado) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();

		sql.append("SELECT ");
		sql.append(" r.id_reserva AS idReserva, ");
		sql.append(" date(r.fecha_hora_inicio) AS fecha, ");
		sql.append(" time(r.fecha_hora_inicio) AS hora, ");
		sql.append(" i.nombre AS instalacion, ");
		sql.append(" r.duracion AS duracion, ");
		sql.append(" r.costo AS precio, ");
		sql.append(" r.pagado AS pagado, ");
		sql.append(" r.estado AS estado ");
		sql.append("FROM Reservas r ");
		sql.append("JOIN Instalaciones i ON i.id_instalacion = r.id_instalacion ");
		sql.append("WHERE r.id_socio = ? ");

		params.add(idSocio);

		if (fechaInicio != null && !fechaInicio.isEmpty()) {
			sql.append("AND date(r.fecha_hora_inicio) >= ? ");
			params.add(fechaInicio);
		}
		if (fechaFin != null && !fechaFin.isEmpty()) {
			sql.append("AND date(r.fecha_hora_inicio) <= ? ");
			params.add(fechaFin);
		}
		if ("PASADAS".equals(filtroEstado)) {
			sql.append("AND datetime(r.fecha_hora_inicio) < datetime('now') ");
		} else if ("ACTIVAS".equals(filtroEstado)) {
			sql.append("AND datetime(r.fecha_hora_inicio) >= datetime('now') ");
			sql.append("AND r.estado = 'activa' ");
		}

		sql.append("ORDER BY r.fecha_hora_inicio ASC");

		return db.executeQueryPojo(ReservaDTO.class, sql.toString(), params.toArray());
	}
}