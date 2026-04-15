package cd.admin.diego.resact;

import java.util.List;

import giis.demo.util.Database;

public class ResActModel {

	private Database db = new Database();

	public List<ActividadSinReservaDto> getActividadesSinReservaAutomatica() {
		String sql =
			"SELECT "
			+ "a.id_actividad AS idActividad, "
			+ "a.nombre AS nombre, "
			+ "a.descripcion AS descripcion, "
			+ "i.nombre AS instalacion, "
			+ "a.fecha_inicio AS fechaInicio, "
			+ "a.fecha_fin AS fechaFin "
			+ "FROM Actividades a "
			+ "INNER JOIN Instalaciones i ON i.id_instalacion = a.id_instalacion "
			+ "WHERE a.id_actividad NOT IN ( "
			+ "    SELECT DISTINCT pa.id_actividad "
			+ "    FROM PlanificacionActividades pa "
			+ ") "
			+ "ORDER BY a.fecha_inicio, a.nombre";

		return db.executeQueryPojo(ActividadSinReservaDto.class, sql);
	}

	public List<ConflictoActividadDto> getConflictosConActividades(int idActividad) {
		String sql =
			"SELECT DISTINCT "
			+ "a2.id_actividad AS idActividadConflicto, "
			+ "sa.fecha AS fecha, "
			+ "sa.hora_inicio AS hora, "
			+ "a2.nombre AS actividadEnConflicto, "
			+ "0 AS prioridad "
			+ "FROM SesionesActividad sa "
			+ "INNER JOIN PlanificacionActividades pa "
			+ "    ON sa.id_instalacion = pa.id_instalacion "
			+ "    AND sa.fecha = pa.fecha "
			+ "    AND sa.hora_inicio < pa.hora_fin "
			+ "    AND sa.hora_fin > pa.hora_inicio "
			+ "INNER JOIN Actividades a2 ON a2.id_actividad = pa.id_actividad "
			+ "WHERE sa.id_actividad = ? "
			+ "ORDER BY sa.fecha, sa.hora_inicio, a2.nombre";

		return db.executeQueryPojo(ConflictoActividadDto.class, sql, idActividad);
	}

	public List<ConflictoReservaDto> getConflictosConReservasSocios(int idActividad) {
		String sql =
			"SELECT "
			+ "sa.fecha AS fecha, "
			+ "sa.hora_inicio AS hora, "
			+ "('Reserva de ' || s.nombre) AS reservaEnConflicto "
			+ "FROM SesionesActividad sa "
			+ "INNER JOIN Reservas r "
			+ "    ON sa.id_instalacion = r.id_instalacion "
			+ "    AND date(r.fecha_hora_inicio) = sa.fecha "
			+ "    AND sa.hora_inicio < time(r.fecha_hora_inicio, '+' || r.duracion || ' minutes') "
			+ "    AND sa.hora_fin > time(r.fecha_hora_inicio) "
			+ "INNER JOIN Socios s ON s.id_socio = r.id_socio "
			+ "WHERE sa.id_actividad = ? "
			+ "AND r.estado = 'activa' "
			+ "ORDER BY sa.fecha, sa.hora_inicio, s.nombre";

		return db.executeQueryPojo(ConflictoReservaDto.class, sql, idActividad);
	}

	public List<NotificacionReservaCanceladaDto> getReservasCanceladasParaNotificar(int idActividadNueva) {
		String sql =
			"SELECT DISTINCT "
			+ "r.id_reserva AS idReserva, "
			+ "s.id_socio AS idSocio, "
			+ "s.nombre AS nombreSocio, "
			+ "i.nombre AS instalacion, "
			+ "sa.fecha AS fecha, "
			+ "sa.hora_inicio AS hora, "
			+ "a.nombre AS actividadNueva, "
			+ "r.pagado AS pagada, "
			+ "r.costo AS importe "
			+ "FROM SesionesActividad sa "
			+ "INNER JOIN Actividades a "
			+ "    ON a.id_actividad = sa.id_actividad "
			+ "INNER JOIN Instalaciones i "
			+ "    ON i.id_instalacion = sa.id_instalacion "
			+ "INNER JOIN Reservas r "
			+ "    ON sa.id_instalacion = r.id_instalacion "
			+ "    AND date(r.fecha_hora_inicio) = sa.fecha "
			+ "    AND sa.hora_inicio < time(r.fecha_hora_inicio, '+' || r.duracion || ' minutes') "
			+ "    AND sa.hora_fin > time(r.fecha_hora_inicio) "
			+ "INNER JOIN Socios s "
			+ "    ON s.id_socio = r.id_socio "
			+ "WHERE sa.id_actividad = ? "
			+ "  AND r.estado = 'activa' "
			+ "ORDER BY s.nombre, sa.fecha, sa.hora_inicio";

		return db.executeQueryPojo(NotificacionReservaCanceladaDto.class, sql, idActividadNueva);
	}

	public void cancelarReservasEnConflicto(int idActividadNueva) {
		String sql =
			"UPDATE Reservas "
			+ "SET estado = 'cancelada', "
			+ "    motivo_cancelacion = 'Coincidencia con una actividad' "
			+ "WHERE id_reserva IN ( "
			+ "    SELECT DISTINCT r.id_reserva "
			+ "    FROM SesionesActividad sa "
			+ "    INNER JOIN Reservas r "
			+ "        ON sa.id_instalacion = r.id_instalacion "
			+ "        AND date(r.fecha_hora_inicio) = sa.fecha "
			+ "        AND sa.hora_inicio < time(r.fecha_hora_inicio, '+' || r.duracion || ' minutes') "
			+ "        AND sa.hora_fin > time(r.fecha_hora_inicio) "
			+ "    WHERE sa.id_actividad = ? "
			+ "      AND r.estado = 'activa' "
			+ ")";

		db.executeUpdate(sql, idActividadNueva);
	}

	public void eliminarPlanificacionActividadConflicto(int idActividadNueva, int idActividadConflicto) {
		String sql =
			"DELETE FROM PlanificacionActividades "
			+ "WHERE id_planificacion IN ( "
			+ "    SELECT pa.id_planificacion "
			+ "    FROM PlanificacionActividades pa "
			+ "    INNER JOIN SesionesActividad sa "
			+ "        ON sa.id_instalacion = pa.id_instalacion "
			+ "        AND sa.fecha = pa.fecha "
			+ "        AND sa.hora_inicio < pa.hora_fin "
			+ "        AND sa.hora_fin > pa.hora_inicio "
			+ "    WHERE sa.id_actividad = ? "
			+ "      AND pa.id_actividad = ? "
			+ ")";

		db.executeUpdate(sql, idActividadNueva, idActividadConflicto);
	}

	public void crearPlanificacionNuevaActividad(int idActividadNueva) {
		String sql =
			"INSERT INTO PlanificacionActividades "
			+ "(id_actividad, fecha, hora_inicio, hora_fin, id_instalacion) "
			+ "SELECT id_actividad, fecha, hora_inicio, hora_fin, id_instalacion "
			+ "FROM SesionesActividad "
			+ "WHERE id_actividad = ?";

		db.executeUpdate(sql, idActividadNueva);
	}
}