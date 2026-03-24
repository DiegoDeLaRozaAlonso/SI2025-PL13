package cd.socio.AlejandroVisualizacionReservas;

import java.util.List;

import giis.demo.util.SwingUtil;

/**
 * Controlador para la pantalla de pagos pendientes de un socio.
 * Es el punto de entrada de esta pantalla; se invoca:
 *  - instanciando el controlador con el id del socio, la vista y el modelo
 *  - ejecutando initController() para instalar los manejadores de eventos
 */
public class PagosPendientesController {

	private PagosPendientesModel model;
	private PagosPendientesView  view;
	private int idSocio;

	public PagosPendientesController(int idSocio, PagosPendientesModel m, PagosPendientesView v) {
		this.idSocio = idSocio;
		this.model   = m;
		this.view    = v;
		this.initView();
	}

	/** Instala los manejadores de eventos (actualmente la pantalla es de solo lectura) */
	public void initController() {
		// Pantalla de solo lectura: no hay eventos de usuario que gestionar
	}

	/**
	 * Carga los datos del socio y sus cargos pendientes, los muestra en la vista
	 * y hace visible la ventana.
	 */
	public void initView() {
		String nombre = model.getNombreSocio(idSocio);
		List<CargoPendienteDTO> cargos = model.getCargospendientes(idSocio);

		view.setSocio(idSocio, nombre);
		view.setCargos(cargos);

		view.getFrame().setVisible(true);
	}
}
