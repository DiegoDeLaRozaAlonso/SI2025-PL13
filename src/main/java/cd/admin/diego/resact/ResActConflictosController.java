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

		// Evitar duplicados
		Set<Integer> actividadesMarcadas = new HashSet<Integer>();
		for (ConflictoActividadDto dto : seleccionados) {
			actividadesMarcadas.add(dto.getIdActividadConflicto());
		}

		// 1. Eliminar planificación SOLO de las marcadas
		for (Integer idActividadConflicto : actividadesMarcadas) {
			model.eliminarPlanificacionActividadConflicto(
					actividadSeleccionada.getIdActividad(),
					idActividadConflicto
			);
		}

		// 2. Cancelar reservas automáticamente
		model.cancelarReservasEnConflicto(
				actividadSeleccionada.getIdActividad()
		);

		// 3. Crear nueva planificación
		model.crearPlanificacionNuevaActividad(
				actividadSeleccionada.getIdActividad()
		);

		// 4. Mensaje informativo
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
				+ "- Actividades en conflicto eliminadas: " + nombresActividades + "\n"
				+ "- Reservas de socios canceladas automáticamente\n"
				+ "- Nueva planificación creada";

		JOptionPane.showMessageDialog(
				view.getFrame(),
				mensaje,
				"Resolución de conflictos",
				JOptionPane.INFORMATION_MESSAGE
		);

		view.close();
	}
}