package cd.socio.diego.verdispoinstalacion;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import giis.demo.util.Database;

public class DisponibilidadModel {

	private final Database db = new Database();
	private static final DateTimeFormatter HHMM = DateTimeFormatter.ofPattern("HH:mm");

	// La BD ya está inicializada desde SwingMain. NO cargar scripts aquí.
	public DisponibilidadModel() {
	}

	public List<InstalacionDTO> getInstalacionesEnUso() {
		String sql = ""
				+ "SELECT id_instalacion AS idInstalacion, nombre AS nombre "
				+ "FROM Instalaciones "
				+ "WHERE en_uso = 1 "
				+ "ORDER BY nombre";
		return db.executeQueryPojo(InstalacionDTO.class, sql);
	}

	public TableModel getEmptyTableModel() {
		return new DefaultTableModel(new Object[][] {}, new String[] { "Hora", "Estado", "Detalle" }) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
	}

	// CAMBIO: añadimos idSocioLogueado
	public TableModel getDisponibilidadTableModel(int idInstalacion, LocalDate fecha, int idSocioLogueado) {

		HorarioDTO horario = getHorarioInstalacion(idInstalacion, fecha);
		DefaultTableModel tm = baseTableModel();

		if (horario == null) {
			tm.addRow(new Object[] { "-", "SIN HORARIO", "No hay horario definido para esta instalación" });
			return tm;
		}

		LocalTime apertura = parseTime(horario.getHoraApertura());
		LocalTime cierre = parseTime(horario.getHoraCierre());

		// Si apertura y cierre son 00:00 → instalación cerrada
		if (apertura.equals(LocalTime.MIDNIGHT) && cierre.equals(LocalTime.MIDNIGHT)) {
			tm.addRow(new Object[] { "-", "CERRADA", "La instalación está cerrada según horario" });
			return tm;
		}

		// Ajuste a franjas "en punto"
		apertura = ajustarAperturaAEnPunto(apertura);
		cierre = ajustarCierreAEnPunto(cierre);

		if (!apertura.isBefore(cierre)) {
			tm.addRow(new Object[] { "-", "SIN FRANJAS", "No hay franjas completas 'en punto' en el horario" });
			return tm;
		}

		List<EventoOcupacionDTO> eventos = getEventosOcupacion(idInstalacion, fecha);

		// franjas de 1h: [h:00, h+1:00)
		for (LocalTime t = apertura; t.isBefore(cierre); t = t.plusHours(1)) {
			LocalTime tFin = t.plusHours(1);

			EventoOcupacionDTO e = findOverlap(eventos, t, tFin);
			if (e == null) {
				tm.addRow(new Object[] { t.format(HHMM), "LIBRE", "" });
			} else {
				String detalle = e.getDetalle();

				// ✅ Si el evento es una reserva y es del socio logueado, lo marcamos
				if (e.getIdSocio() != null && e.getIdSocio().intValue() == idSocioLogueado) {
					detalle = detalle + " — Esta reserva es tuya";
				}

				tm.addRow(new Object[] { t.format(HHMM), "OCUPADA", detalle });
			}
		}

		return tm;
	}

	private DefaultTableModel baseTableModel() {
		return new DefaultTableModel(new Object[][] {}, new String[] { "Hora", "Estado", "Detalle" }) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
	}

	private HorarioDTO getHorarioInstalacion(int idInstalacion, LocalDate fecha) {

		String tipo = getTipoHorarioPorFecha(fecha);

		String sql = ""
				+ "SELECT hora_apertura AS horaApertura, "
				+ "hora_cierre AS horaCierre "
				+ "FROM HorariosInstalacion "
				+ "WHERE id_instalacion = ? "
				+ "AND tipo_horario = ?";

		List<HorarioDTO> res = db.executeQueryPojo(HorarioDTO.class, sql, idInstalacion, tipo);
		return res.isEmpty() ? null : res.get(0);
	}

	private String getTipoHorarioPorFecha(LocalDate fecha) {

		Month m = fecha.getMonth();

		if (m == Month.JUNE || m == Month.JULY || m == Month.AUGUST)
			return "verano";

		if (m == Month.DECEMBER || m == Month.JANUARY || m == Month.FEBRUARY)
			return "invierno";

		return "otoño";
	}

	private List<EventoOcupacionDTO> getEventosOcupacion(int idInstalacion, LocalDate fecha) {

		List<EventoOcupacionDTO> eventos = new ArrayList<>();

		// 1) Reservas (activa o completada)
		// CAMBIO: traemos id_socio AS idSocio
		String sqlReservas = ""
				+ "SELECT "
				+ "time(fecha_hora_inicio) AS horaInicio, "
				+ "time(datetime(fecha_hora_inicio, '+' || duracion || ' minutes')) AS horaFin, "
				+ "('Reserva #' || id_reserva || ' (' || estado || ')') AS detalle, "
				+ "id_socio AS idSocio "
				+ "FROM Reservas "
				+ "WHERE id_instalacion = ? "
				+ "AND date(fecha_hora_inicio) = ? "
				+ "AND estado IN ('activa','completada')";

		eventos.addAll(db.executeQueryPojo(EventoOcupacionDTO.class, sqlReservas, idInstalacion, fecha.toString()));

		// 2) SesionesActividad (idSocio no aplica)
		String sqlSesiones = ""
				+ "SELECT "
				+ "sa.hora_inicio AS horaInicio, "
				+ "sa.hora_fin AS horaFin, "
				+ "('Sesión: ' || a.nombre) AS detalle, "
				+ "NULL AS idSocio "
				+ "FROM SesionesActividad sa "
				+ "JOIN Actividades a ON a.id_actividad = sa.id_actividad "
				+ "WHERE sa.id_instalacion = ? "
				+ "AND sa.fecha = ?";

		eventos.addAll(db.executeQueryPojo(EventoOcupacionDTO.class, sqlSesiones, idInstalacion, fecha.toString()));

		// 3) PlanificacionActividades (idSocio no aplica)
		String sqlPlan = ""
				+ "SELECT "
				+ "pa.hora_inicio AS horaInicio, "
				+ "pa.hora_fin AS horaFin, "
				+ "('Planificación: ' || a.nombre) AS detalle, "
				+ "NULL AS idSocio "
				+ "FROM PlanificacionActividades pa "
				+ "JOIN Actividades a ON a.id_actividad = pa.id_actividad "
				+ "WHERE pa.id_instalacion = ? "
				+ "AND pa.fecha = ?";

		eventos.addAll(db.executeQueryPojo(EventoOcupacionDTO.class, sqlPlan, idInstalacion, fecha.toString()));

		return eventos;
	}

	private EventoOcupacionDTO findOverlap(List<EventoOcupacionDTO> eventos, LocalTime slotIni, LocalTime slotFin) {
		for (EventoOcupacionDTO e : eventos) {
			LocalTime eIni = parseTime(e.getHoraInicio());
			LocalTime eFin = parseTime(e.getHoraFin());
			// overlap si: eIni < slotFin && eFin > slotIni
			if (eIni.isBefore(slotFin) && eFin.isAfter(slotIni))
				return e;
		}
		return null;
	}

	private LocalTime ajustarAperturaAEnPunto(LocalTime t) {
		// Si tiene minutos/segundos, sube a la siguiente hora en punto
		LocalTime trunc = t.truncatedTo(ChronoUnit.HOURS);
		if (t.equals(trunc))
			return t;
		return trunc.plusHours(1);
	}

	private LocalTime ajustarCierreAEnPunto(LocalTime t) {
		// Baja a la hora en punto anterior (trunca)
		return t.truncatedTo(ChronoUnit.HOURS);
	}

	private LocalTime parseTime(String t) {
		if (t == null)
			return LocalTime.MIDNIGHT;

		String s = t.trim();

		// SQLite puede devolver "HH:MM" o "HH:MM:SS"
		if (s.length() == 5)
			return LocalTime.parse(s);

		if (s.length() >= 8)
			return LocalTime.parse(s.substring(0, 8));

		return LocalTime.parse(s);
	}
}