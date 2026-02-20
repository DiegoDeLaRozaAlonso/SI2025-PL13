package cd.admin.diego.planact;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;

import giis.demo.util.ApplicationException;
import giis.demo.util.Database;

public class PlanActCrearActividadModel {

	private final Database db = new Database();

	// --- Queries ---
	private static final String SQL_INSTALACIONES =
			"select id_instalacion as idInstalacion, nombre, tipo, capacidad " +
			"from Instalaciones where en_uso=1 order by nombre";

	// Inserts
	private static final String SQL_INS_ACTIVIDAD =
			"insert into Actividades(nombre, descripcion, id_instalacion, aforo, costo_socio, costo_no_socio, fecha_inicio, fecha_fin) " +
			"values(?,?,?,?,?,?,?,?)";

	private static final String SQL_LAST_ID = "select last_insert_rowid()";

	private static final String SQL_INS_PERIODO =
			"insert into PeriodosInscripcion(id_actividad, nombre, descripcion, tipo, fecha_inicio, fecha_fin) " +
			"values(?,?,?,?,?,?)";

	private static final String SQL_INS_SESION =
			"insert into SesionesActividad(id_actividad, fecha, hora_inicio, hora_fin, id_instalacion) " +
			"values(?,?,?,?,?)";

	public List<InstalacionDTO> getInstalaciones() {
		return db.executeQueryPojo(InstalacionDTO.class, SQL_INSTALACIONES);
	}

	/**
	 * Crea actividad + periodos (socio/no_socio) + sesiones según horario.
	 * Devuelve id_actividad creado.
	 */
	public int crearActividadCompleta(
			String nombre,
			String tipo,
			int idInstalacion,
			int aforo,
			double precioSocio,
			double precioNoSocio,
			LocalDate insInicio,
			LocalDate insFin,
			LocalDate fechaInicio,
			int numSemanas,
			List<WeeklyScheduleTableModel.Slot> slots) {

		validate(nombre != null && !nombre.trim().isEmpty(), "El nombre de la actividad es obligatorio");
		validate(tipo != null && !tipo.trim().isEmpty(), "El tipo de actividad es obligatorio");
		validate(idInstalacion > 0, "Debes seleccionar una instalación");
		validate(numSemanas > 0, "El número de semanas debe ser mayor que 0");
		validate(!slots.isEmpty(), "Debes seleccionar al menos un hueco en el horario semanal");
		validate(!insFin.isBefore(insInicio), "El fin de inscripción no puede ser anterior al inicio");
		validate(precioSocio >= 0 && precioNoSocio >= 0, "Los precios no pueden ser negativos");

		LocalDate fechaFin = fechaInicio.plusWeeks(numSemanas).minusDays(1);

		Connection conn = null;
		try {
			conn = db.getConnection();
			conn.setAutoCommit(false);

			QueryRunner qr = new QueryRunner();

			// 1) Actividad
			qr.update(conn, SQL_INS_ACTIVIDAD,
					nombre.trim(),
					tipo.trim(),              // uso "tipo" como descripcion (tu boceto tenía un campo tipo)
					idInstalacion,
					aforo,
					precioSocio,
					precioNoSocio,
					fechaInicio.toString(),
					fechaFin.toString());

			// 2) Id generado
			ArrayListHandler h = new ArrayListHandler();
			@SuppressWarnings("unchecked")
			List<Object[]> rows = (List<Object[]>) qr.query(conn, SQL_LAST_ID, h);
			int idActividad = ((Number) rows.get(0)[0]).intValue();

			// 3) Periodos inscripción (2 filas)
			qr.update(conn, SQL_INS_PERIODO,
					idActividad,
					"Socios",
					"Inscripción socios (" + nombre.trim() + ")",
					"socio",
					insInicio.toString(),
					insFin.toString());

			qr.update(conn, SQL_INS_PERIODO,
					idActividad,
					"No socios",
					"Inscripción no socios (" + nombre.trim() + ")",
					"no_socio",
					insInicio.toString(),
					insFin.toString());

			// 4) Sesiones (según slots)
			insertSesiones(qr, conn, idActividad, idInstalacion, fechaInicio, fechaFin, slots);

			conn.commit();
			return idActividad;

		} catch (SQLException e) {
			DbUtils.rollbackAndCloseQuietly(conn);
			throw new ApplicationException(e);
		} finally {
			DbUtils.closeQuietly(conn);
		}
	}

	private void insertSesiones(QueryRunner qr, Connection conn, int idActividad, int idInstalacion,
			LocalDate fechaInicio, LocalDate fechaFin, List<WeeklyScheduleTableModel.Slot> slots) throws SQLException {

		// definimos "semana 0" como la semana que empieza en lunes de la semana de fechaInicio
		LocalDate week0Monday = fechaInicio.minusDays((fechaInicio.getDayOfWeek().getValue() + 6) % 7);

		DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm");

		LocalDate cursorWeek = week0Monday;
		while (!cursorWeek.isAfter(fechaFin)) {

			for (WeeklyScheduleTableModel.Slot s : slots) {
				DayOfWeek dow = DayOfWeek.of(s.dayIndex0Mon + 1); // 1..5
				LocalDate sessionDate = cursorWeek.plusDays(dow.getValue() - 1);

				// dentro del rango real de la actividad
				if (sessionDate.isBefore(fechaInicio) || sessionDate.isAfter(fechaFin)) continue;

				LocalTime start = s.start;
				LocalTime end = s.endPlus1h();

				qr.update(conn, SQL_INS_SESION,
						idActividad,
						sessionDate.toString(),
						start.format(tf),
						end.format(tf),
						idInstalacion);
			}

			cursorWeek = cursorWeek.plusWeeks(1);
		}
	}

	private void validate(boolean ok, String msg) {
		if (!ok) throw new ApplicationException(msg);
	}
}