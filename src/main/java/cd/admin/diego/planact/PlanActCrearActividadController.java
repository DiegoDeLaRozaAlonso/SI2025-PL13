package cd.admin.diego.planact;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

import giis.demo.util.SwingUtil;

public class PlanActCrearActividadController {

	private final PlanActCrearActividadModel model;
	private final PlanActCrearActividadView view;

	private WeeklyScheduleTableModel horarioModel;

	public PlanActCrearActividadController(PlanActCrearActividadModel m, PlanActCrearActividadView v) {
		this.model = m;
		this.view = v;
		initView();
	}

	public void initController() {
		view.getBtnCrear().addActionListener(e -> SwingUtil.exceptionWrapper(() -> crearActividad()));
		view.getBtnBorrarTodo().addActionListener(e -> SwingUtil.exceptionWrapper(() -> limpiarFormulario()));
		view.getBtnCerrar().addActionListener(e -> SwingUtil.exceptionWrapper(() -> cerrar()));
		view.getBtnInfoPeriodo().addActionListener(e -> SwingUtil.exceptionWrapper(() -> mostrarInfoPeriodo()));
	}

	private void initView() {
		horarioModel = new WeeklyScheduleTableModel();
		view.setHorarioModel(horarioModel);

		// ✅ CAMBIO: el combo guarda InstalacionDTO (no Strings)
		List<InstalacionDTO> instalaciones = model.getInstalaciones();
		view.getCbInstalacion().setModel(new DefaultComboBoxModel<>(
				instalaciones.toArray(new InstalacionDTO[0])
		));

		// Periodos (ya era correcto, guarda DTO)
		List<PeriodoInscripcionDTO> periodos = model.getPeriodosInscripcion();
		view.getCbPeriodoInscripcion().setModel(new DefaultComboBoxModel<>(
				periodos.toArray(new PeriodoInscripcionDTO[0])
		));

		// Defaults con fechas reales
		view.setFechaInicioDate(java.sql.Date.valueOf(LocalDate.of(2026, 2, 1)));
		view.setFechaFinDate(java.sql.Date.valueOf(LocalDate.of(2026, 3, 31)));

		view.getFrame().setVisible(true);
	}

	private void mostrarInfoPeriodo() {
		PeriodoInscripcionDTO p = (PeriodoInscripcionDTO) view.getCbPeriodoInscripcion().getSelectedItem();
		if (p == null) throw new giis.demo.util.ApplicationException("Debes seleccionar un periodo de inscripción.");
		PlanActPeriodoInfoDialog dlg = new PlanActPeriodoInfoDialog(view.getFrame(), p);
		dlg.setVisible(true);
	}

	private void crearActividad() {
		String nombre = view.getNombreActividad();
		String tipo = view.getTipoActividad();

		// ✅ CAMBIO: leer id desde el DTO seleccionado (no parsear strings)
		int idInstalacion = getIdInstalacionSeleccionada();

		int aforo = view.getAforo();
		double pSocio = view.getPrecioSocio();
		double pNoSocio = view.getPrecioNoSocio();

		// Precios
		if (pSocio <= 0 || pNoSocio <= 0) {
			throw new giis.demo.util.ApplicationException("Los precios no pueden ser 0 (ni negativos).");
		}
		if (pNoSocio <= pSocio) {
			throw new giis.demo.util.ApplicationException("El precio de no socio debe ser mayor que el precio de socio.");
		}

		// Aforo <= capacidad
		int capacidad = model.getCapacidadInstalacion(idInstalacion);
		if (capacidad > 0 && aforo > capacidad) {
			throw new giis.demo.util.ApplicationException(
					"El aforo máximo (" + aforo + ") no puede ser mayor que la capacidad de la instalación (" + capacidad + ")."
			);
		}

		// Aviso nombre duplicado
		if (nombre != null && !nombre.trim().isEmpty() && model.existeActividadConNombre(nombre)) {
			int opt = JOptionPane.showConfirmDialog(
					view.getFrame(),
					"Ya existe una actividad con el mismo nombre.\n"
					+ "No es recomendable porque puede generar confusión.\n\n"
					+ "¿Quieres continuar igualmente?",
					"Nombre duplicado",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE
			);
			if (opt != JOptionPane.YES_OPTION) return;
		}

		PeriodoInscripcionDTO periodo = (PeriodoInscripcionDTO) view.getCbPeriodoInscripcion().getSelectedItem();
		if (periodo == null) throw new giis.demo.util.ApplicationException("Debes seleccionar un periodo de inscripción.");
		int idPeriodo = periodo.getIdPeriodo();

		// Fechas desde calendario
		Date ini = view.getFechaInicioDate();
		Date fin = view.getFechaFinDate();
		if (ini == null || fin == null) {
			throw new giis.demo.util.ApplicationException("Debes seleccionar la fecha de inicio y la fecha de fin.");
		}
		LocalDate fechaInicio = toLocalDate(ini);
		LocalDate fechaFin = toLocalDate(fin);

		if (fechaFin.isBefore(fechaInicio)) {
			throw new giis.demo.util.ApplicationException("La fecha de inicio no puede ser posterior a la fecha de fin.");
		}

		List<WeeklyScheduleTableModel.Slot> slots = horarioModel.getSelectedSlots();

		int id = model.crearActividadCompleta(
				nombre, tipo, idInstalacion, aforo, pSocio, pNoSocio,
				fechaInicio, fechaFin,
				slots,
				idPeriodo);

		throw new giis.demo.util.ApplicationException("Actividad creada correctamente. id_actividad=" + id);
	}

	private int getIdInstalacionSeleccionada() {
		InstalacionDTO inst = (InstalacionDTO) view.getCbInstalacion().getSelectedItem();
		if (inst == null) throw new giis.demo.util.ApplicationException("Debes seleccionar una instalación.");
		return inst.getIdInstalacion();
	}

	private void limpiarFormulario() {
		view.setNombreActividad("");
		view.setTipoActividad("");
		view.setAforo(1);
		view.setPrecioSocio(0.0);
		view.setPrecioNoSocio(0.0);
		view.setFechaInicioDate(null);
		view.setFechaFinDate(null);
		horarioModel.clearAll();
	}

	private void cerrar() {
		view.getFrame().dispose();
	}

	private LocalDate toLocalDate(Date d) {
		return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
}