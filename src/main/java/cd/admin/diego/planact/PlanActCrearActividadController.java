package cd.admin.diego.planact;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

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
		view.getBtnInfoPeriodo().addActionListener(e -> SwingUtil.exceptionWrapper(() -> mostrarInfoPeriodo()));
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

		view.setFechaInicio("2026-02-01");
		view.setFechaFin("2026-03-31");

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

		int idInstalacion = parseIdInstalacionSeleccionada();
		int aforo = view.getAforo();
		double pSocio = view.getPrecioSocio();
		double pNoSocio = view.getPrecioNoSocio();

		// 1) Restricción: precios > 0 y noSocio > socio
		if (pSocio <= 0 || pNoSocio <= 0) {
			throw new giis.demo.util.ApplicationException("Los precios no pueden ser 0 (ni negativos).");
		}
		if (pNoSocio <= pSocio) {
			throw new giis.demo.util.ApplicationException("El precio de no socio debe ser mayor que el precio de socio.");
		}

		// 2) Restricción: aforo <= capacidad instalación
		int capacidad = model.getCapacidadInstalacion(idInstalacion);
		if (capacidad > 0 && aforo > capacidad) {
			throw new giis.demo.util.ApplicationException(
					"El aforo máximo (" + aforo + ") no puede ser mayor que la capacidad de la instalación (" + capacidad + ")."
			);
		}

		// 3) Aviso si nombre duplicado
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
			if (opt != JOptionPane.YES_OPTION) {
				return; // cancelar creación
			}
		}

		PeriodoInscripcionDTO periodo = (PeriodoInscripcionDTO) view.getCbPeriodoInscripcion().getSelectedItem();
		if (periodo == null) throw new giis.demo.util.ApplicationException("Debes seleccionar un periodo de inscripción.");
		int idPeriodo = periodo.getIdPeriodo();

		LocalDate fechaInicio = toLocalDate(Util.isoStringToDate(view.getFechaInicio()));
		LocalDate fechaFin = toLocalDate(Util.isoStringToDate(view.getFechaFin()));

		// 4) Restricción: inicio <= fin (ya se valida en Model, pero lo dejamos también aquí)
		if (fechaFin.isBefore(fechaInicio)) {
			throw new giis.demo.util.ApplicationException("La fecha inicio no puede ser posterior a la fecha fin.");
		}

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