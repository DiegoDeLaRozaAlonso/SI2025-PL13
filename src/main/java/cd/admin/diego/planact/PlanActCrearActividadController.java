package cd.admin.diego.planact;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultComboBoxModel;

import giis.demo.util.SwingUtil;
import giis.demo.util.Util;

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
	}

	private void initView() {
		horarioModel = new WeeklyScheduleTableModel();
		view.setHorarioModel(horarioModel);

		var instalaciones = model.getInstalaciones();
		var cm = SwingUtil.getComboModelFromList(instalaciones.stream().map(i -> new Object[] { i.toString() }).toList());
		view.getCbInstalacion().setModel(cm);

		var periodos = model.getPeriodosInscripcion();
		view.getCbPeriodoInscripcion().setModel(new DefaultComboBoxModel<>(
				periodos.toArray(new PeriodoInscripcionDTO[0])
		));

		// defaults
		view.setFechaInicio("2026-02-01");
		view.setFechaFin("2026-03-31");

		view.getFrame().setVisible(true);
	}

	private void crearActividad() {
		String nombre = view.getNombreActividad();
		String tipo = view.getTipoActividad();

		int idInstalacion = parseIdInstalacionSeleccionada();
		int aforo = view.getAforo();
		double pSocio = view.getPrecioSocio();
		double pNoSocio = view.getPrecioNoSocio();

		PeriodoInscripcionDTO periodo = (PeriodoInscripcionDTO) view.getCbPeriodoInscripcion().getSelectedItem();
		if (periodo == null) throw new giis.demo.util.ApplicationException("Debes seleccionar un periodo de inscripción.");
		int idPeriodo = periodo.getIdPeriodo();

		LocalDate fechaInicio = toLocalDate(Util.isoStringToDate(view.getFechaInicio()));
		LocalDate fechaFin = toLocalDate(Util.isoStringToDate(view.getFechaFin()));

		List<WeeklyScheduleTableModel.Slot> slots = horarioModel.getSelectedSlots();

		int id = model.crearActividadCompleta(
				nombre, tipo, idInstalacion, aforo, pSocio, pNoSocio,
				fechaInicio, fechaFin,
				slots,
				idPeriodo);

		throw new giis.demo.util.ApplicationException("Actividad creada correctamente. id_actividad=" + id);
	}

	private void limpiarFormulario() {
		view.setNombreActividad("");
		view.setTipoActividad("");
		view.setAforo(1);
		view.setPrecioSocio(0.0);
		view.setPrecioNoSocio(0.0);
		view.setFechaInicio("");
		view.setFechaFin("");
		horarioModel.clearAll();
	}

	private void cerrar() {
		view.getFrame().dispose();
	}

	private int parseIdInstalacionSeleccionada() {
		Object item = view.getCbInstalacion().getSelectedItem();
		if (item == null) return -1;
		String s = item.toString().trim();
		int idx = s.indexOf(" - ");
		if (idx <= 0) return -1;
		return Integer.parseInt(s.substring(0, idx).trim());
	}

	private LocalDate toLocalDate(Date d) {
		return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
}