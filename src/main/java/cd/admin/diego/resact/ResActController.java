package cd.admin.diego.resact;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import giis.demo.util.SwingUtil;

public class ResActController {

	private ResActModel model;
	private ResActView view;

	public ResActController(ResActModel model, ResActView view) {
		this.model = model;
		this.view = view;
	}

	public void initController() {
		view.getBtnAtras().addActionListener(e -> view.close());

		view.getBtnActualizar().addActionListener(
				e -> SwingUtil.exceptionWrapper(() -> cargarTabla())
		);

		view.getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				abrirPantallaConflictos();
			}
		});

		SwingUtil.exceptionWrapper(() -> cargarTabla());

		view.show();
	}

	private void cargarTabla() {
		view.loadTable(model.getActividadesSinReservaAutomatica());
	}

	private void abrirPantallaConflictos() {
		ActividadSinReservaDto actividad = view.getActividadSeleccionada();
		if (actividad == null) {
			return;
		}

		ResActConflictosController controller = new ResActConflictosController(
				model,
				new ResActConflictosView(),
				actividad
		);
		controller.initController();
	}
}