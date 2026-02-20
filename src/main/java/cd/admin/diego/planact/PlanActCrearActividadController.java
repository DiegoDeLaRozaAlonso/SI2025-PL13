package cd.admin.diego.planact;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.swing.ComboBoxModel;

import giis.demo.util.SwingUtil;
import giis.demo.util.Util;

/**
 * Controlador MVC: instala listeners y coordina View <-> Model.
 */
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
		view.getBtnAtras().addActionListener(e -> SwingUtil.exceptionWrapper(() -> cerrar()));
	}

	private void initView() {
		// horario por defecto
		horarioModel = new WeeklyScheduleTableModel();
		view.setHorarioModel(horarioModel);

		// cargar combo instalaciones
		List<InstalacionDTO> instalaciones = model.getInstalaciones();
		ComboBoxModel<Object> cm = SwingUtil.getComboModelFromList(
				instalaciones.stream().map(i -> new Object[] { i.toString() }).toList()
		);
		view.getCbInstalacion().setModel(cm);

		// valores iniciales razonables
		view.setInscripcionInicio("2026-01-01");
		view.setInscripcionFin("2026-01-15");
		view.setFechaInicio("2026-02-01");
		view.setNumSemanas(8);

		view.getFrame().setVisible(true);
	}

	private void crearActividad() {
		String nombre = view.getNombreActividad();
		String tipo = view.getTipoActividad();

		int idInstalacion = parseIdInstalacionSeleccionada();
		int aforo = view.getAforo();
		double pSocio = view.getPrecioSocio();
		double pNoSocio = view.getPrecioNoSocio();

		LocalDate insIni = toLocalDate(Util.isoStringToDate(view.getInscripcionInicio()));
		LocalDate insFin = toLocalDate(Util.isoStringToDate(view.getInscripcionFin()));

		LocalDate fechaInicio = toLocalDate(Util.isoStringToDate(view.getFechaInicio()));
		int numSemanas = view.getNumSemanas();

		List<WeeklyScheduleTableModel.Slot> slots = horarioModel.getSelectedSlots();

		int id = model.crearActividadCompleta(
				nombre, tipo, idInstalacion, aforo, pSocio, pNoSocio,
				insIni, insFin,
				fechaInicio, numSemanas,
				slots);

		// mensaje estándar: lanza ApplicationException si quieres modal info, pero SwingUtil ya muestra.
		throw new giis.demo.util.ApplicationException("Actividad creada correctamente. id_actividad=" + id);
	}

	private void limpiarFormulario() {
		view.setNombreActividad("");
		view.setTipoActividad("");
		view.setAforo(1);
		view.setPrecioSocio(0.0);
		view.setPrecioNoSocio(0.0);
		view.setInscripcionInicio("");
		view.setInscripcionFin("");
		view.setFechaInicio("");
		view.setNumSemanas(1);
		horarioModel.clearAll();
	}

	private void cerrar() {
		view.getFrame().dispose();
	}

	private int parseIdInstalacionSeleccionada() {
		Object item = view.getCbInstalacion().getSelectedItem();
		if (item == null) return -1;
		// formato: "id - nombre (tipo) ..."
		String s = item.toString().trim();
		int idx = s.indexOf(" - ");
		if (idx <= 0) return -1;
		return Integer.parseInt(s.substring(0, idx).trim());
	}

	private LocalDate toLocalDate(Date d) {
		return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
}