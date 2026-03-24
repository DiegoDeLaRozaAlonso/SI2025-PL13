package cd.admin.diego.resact;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import giis.demo.util.SwingUtil;

public class ResActConflictosController {

	private ResActModel model;
	private ResActConflictosView view;
	private ActividadSinReservaDto actividadSeleccionada;

	public ResActConflictosController(ResActModel model, ResActConflictosView view,
			ActividadSinReservaDto actividadSeleccionada) {
		this.model = model;
		this.view = view;
		this.actividadSeleccionada = actividadSeleccionada;
	}

	public void initController() {
		view.getBtnVolver().addActionListener(e -> view.close());

		view.getBtnAceptar().addActionListener(
				e -> SwingUtil.exceptionWrapper(() -> aceptar())
		);

		SwingUtil.exceptionWrapper(() -> cargarDatos());

		view.show();
	}

	private void cargarDatos() {
		view.setTituloActividad(actividadSeleccionada.getNombre());

		List<ConflictoActividadDto> conflictosActividades =
				model.getConflictosConActividades(actividadSeleccionada.getIdActividad());

		List<ConflictoReservaDto> conflictosReservas =
				model.getConflictosConReservasSocios(actividadSeleccionada.getIdActividad());

		view.loadConflictosActividades(conflictosActividades);
		view.loadConflictosReservas(conflictosReservas);
	}

	private void aceptar() {

		List<ConflictoActividadDto> seleccionados =
				view.getConflictosActividadesMarcados();

		List<ConflictoActividadDto> todosLosConflictos =
				model.getConflictosConActividades(actividadSeleccionada.getIdActividad());

		Set<Integer> actividadesMarcadas = new HashSet<Integer>();
		for (ConflictoActividadDto dto : seleccionados) {
			actividadesMarcadas.add(dto.getIdActividadConflicto());
		}

		Set<Integer> actividadesEnConflicto = new HashSet<Integer>();
		for (ConflictoActividadDto dto : todosLosConflictos) {
			actividadesEnConflicto.add(dto.getIdActividadConflicto());
		}

		if (!actividadesMarcadas.containsAll(actividadesEnConflicto)) {
			JOptionPane.showMessageDialog(
					view.getFrame(),
					"No se puede reservar la nueva actividad porque existen conflictos "
					+ "con actividades ya planificadas que no han sido marcadas con prioridad.",
					"Conflictos sin resolver",
					JOptionPane.WARNING_MESSAGE
			);
			return;
		}

		for (Integer idActividadConflicto : actividadesMarcadas) {
			model.eliminarPlanificacionActividadConflicto(
					actividadSeleccionada.getIdActividad(),
					idActividadConflicto
			);
		}

		int reservasCanceladas = model.getConflictosConReservasSocios(
				actividadSeleccionada.getIdActividad()
		).size();

		model.cancelarReservasEnConflicto(
				actividadSeleccionada.getIdActividad()
		);

		model.crearPlanificacionNuevaActividad(
				actividadSeleccionada.getIdActividad()
		);

		String nombresActividades = "";

		for (ConflictoActividadDto dto : seleccionados) {
			if (!nombresActividades.isEmpty()) {
				nombresActividades += ", ";
			}
			nombresActividades += dto.getActividadEnConflicto();
		}

		if (nombresActividades.isEmpty()) {
			nombresActividades = "Ninguna";
		}

		String mensaje =
				"Cambios realizados correctamente:\n"
				+ "- Actividades en conflicto eliminadas: " + nombresActividades + "\n";

		if (reservasCanceladas > 0) {
			mensaje += "- Reservas de socios canceladas automáticamente: "
					+ reservasCanceladas + "\n";
		}

		mensaje += "- Nueva planificación creada";

		JOptionPane.showMessageDialog(
				view.getFrame(),
				mensaje,
				"Resolución de conflictos",
				JOptionPane.INFORMATION_MESSAGE
		);

		view.close();
	}
}