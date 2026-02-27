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
import org.apache.commons.dbutils.handlers.ScalarHandler;

import giis.demo.util.ApplicationException;
import giis.demo.util.Database;

public class PlanActCrearActividadModel {

	private final Database db = new Database();

	private static final String SQL_INSTALACIONES =
			"select id_instalacion as idInstalacion, nombre, tipo, capacidad " +
			"from Instalaciones where en_uso=1 order by nombre";

	private static final String SQL_PERIODOS =
			"select id_periodo as idPeriodo, nombre, descripcion, " +
			"fecha_inicio_socio as fechaInicioSocio, " +
			"fecha_fin_socio as fechaFinSocio, " +
			"fecha_fin_noSocio as fechaFinNoSocio " +
			"from PeriodosInscripcion order by nombre";

	private static final String SQL_INS_ACTIVIDAD =
			"insert into Actividades(nombre, descripcion, id_instalacion, aforo, costo_socio, costo_no_socio, fecha_inicio, fecha_fin, id_periodo) " +
			"values(?,?,?,?,?,?,?,?,?)";

	private static final String SQL_LAST_ID = "select last_insert_rowid()";

	private static final String SQL_INS_SESION =
			"insert into SesionesActividad(id_actividad, fecha, hora_inicio, hora_fin, id_instalacion) " +
			"values(?,?,?,?,?)";

	// NUEVO: comprobar nombre duplicado
	private static final String SQL_COUNT_ACTIVIDAD_NOMBRE =
			"select count(*) from Actividades where lower(trim(nombre)) = lower(trim(?))";

	// NUEVO: capacidad de instalación
	private static final String SQL_CAPACIDAD_INSTALACION =
			"select capacidad from Instalaciones where id_instalacion=?";

	public List<InstalacionDTO> getInstalaciones() {
		return db.executeQueryPojo(InstalacionDTO.class, SQL_INSTALACIONES);
	}

	public List<PeriodoInscripcionDTO> getPeriodosInscripcion() {
		return db.executeQueryPojo(PeriodoInscripcionDTO.class, SQL_PERIODOS);
	}

	/** NUEVO */
	public boolean existeActividadConNombre(String nombre) {
		Connection conn = null;
		try {
			conn = db.getConnection();
			QueryRunner qr = new QueryRunner();
			Number n = qr.query(conn, SQL_COUNT_ACTIVIDAD_NOMBRE, new ScalarHandler<>(), nombre);
			return n != null && n.intValue() > 0;
		} catch (SQLException e) {
			throw new ApplicationException(e);
		} finally {
			DbUtils.closeQuietly(conn);
		}
	}

	/** NUEVO */
	public int getCapacidadInstalacion(int idInstalacion) {
		Connection conn = null;
		try {
			conn = db.getConnection();
			QueryRunner qr = new QueryRunner();
			Number cap = qr.query(conn, SQL_CAPACIDAD_INSTALACION, new ScalarHandler<>(), idInstalacion);
			// Si está NULL, tratamos como 0 para que el controller bloquee (puedes cambiar a MAX_VALUE si prefieres)
			return cap == null ? 0 : cap.intValue();
		} catch (SQLException e) {
			throw new ApplicationException(e);
		} finally {
			DbUtils.closeQuietly(conn);
		}
	}

	public int crearActividadCompleta(
			String nombre,
			String descripcionTipo,
			int idInstalacion,
			int aforo,
			double precioSocio,
			double precioNoSocio,
			LocalDate fechaInicio,
			LocalDate fechaFin,
			List<WeeklyScheduleTableModel.Slot> slots,
			int idPeriodoInscripcion) {

		validate(nombre != null && !nombre.trim().isEmpty(), "El nombre de la actividad es obligatorio");
		validate(descripcionTipo != null && !descripcionTipo.trim().isEmpty(), "El tipo (descripción) es obligatorio");
		validate(idInstalacion > 0, "Debes seleccionar una instalación");
		validate(!slots.isEmpty(), "Debes seleccionar al menos un hueco en el horario semanal");
		validate(precioSocio >= 0 && precioNoSocio >= 0, "Los precios no pueden ser negativos");
		validate(idPeriodoInscripcion > 0, "Debes seleccionar un periodo de inscripción");
		validate(fechaFin != null && !fechaFin.isBefore(fechaInicio),
				"La fecha fin no puede ser anterior a la fecha inicio");

		Connection conn = null;
		try {
			conn = db.getConnection();
			conn.setAutoCommit(false);

			QueryRunner qr = new QueryRunner();

			qr.update(conn, SQL_INS_ACTIVIDAD,
					nombre.trim(),
					descripcionTipo.trim(),
					idInstalacion,
					aforo,
					precioSocio,
					precioNoSocio,
					fechaInicio.toString(),
					fechaFin.toString(),
					idPeriodoInscripcion);

			ArrayListHandler h = new ArrayListHandler();
			@SuppressWarnings("unchecked")
			List<Object[]> rows = (List<Object[]>) qr.query(conn, SQL_LAST_ID, h);
			int idActividad = ((Number) rows.get(0)[0]).intValue();

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

		// semana 0: lunes de la semana de fechaInicio
		LocalDate week0Monday = fechaInicio.minusDays((fechaInicio.getDayOfWeek().getValue() + 6) % 7);

		DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm");

		LocalDate cursorWeek = week0Monday;
		while (!cursorWeek.isAfter(fechaFin)) {

			for (WeeklyScheduleTableModel.Slot s : slots) {
				DayOfWeek dow = DayOfWeek.of(s.dayIndex0Mon + 1); // 1..7 (Mon..Sun)
				LocalDate sessionDate = cursorWeek.plusDays(dow.getValue() - 1);

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