package cd.socio.pablo.inscripcionActividad;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import giis.demo.util.SwingUtil;

public class InscribirSocioController {
	
	private ListaPeriodoModel model;
	private InscribirSocioView vista;
	
	public InscribirSocioController(ListaPeriodoModel m, InscribirSocioView v) {
		this.model = m;
		this.vista = v;
		//no hay inicializacion especifica del modelo, solo de la vista
		this.initView();
		this.initController();
	}
	
	public void initController() {
		
		vista.getBotonVolver().addActionListener(e -> SwingUtil.exceptionWrapper(() -> vista.getFrame().dispose()));
	
	}
	
	/*private void getFechas() {
		vista.getFechaInicio().getDate();
		vista.getFechaFin().getDate();
	}*/
	
	private void listaActividades() {

		String fechaInicio = vista.getFechaInicio().getDate().toString();
		String fechaFin = vista.getFechaFin().getDate().toString();
		
		if (!(fechaInicio.isBlank() || fechaFin.isBlank())) {
			List<ActividadDTO> actividades = model.getListaActividades(
					vista.getFechaInicio().getDate().toString(),
					vista.getFechaFin().getDate().toString() 
			);
			
		//Definimos las columnas de la tabla
		String[] columnas = {"nombre", "desc", "aforo", 
				"fecha_inicio", "fecha_fin", "precioSocio", "precioNoSocio"};
		
		javax.swing.table.TableModel tmodel = SwingUtil.getTableModelFromPojos(actividades, columnas);
		vista.getTable().setModel(tmodel);

		//Auto ajustamos el tamaño de las columnas
		SwingUtil.autoAdjustColumns(vista.getTable());
		
		}
	}
	
	public void initView() {
		
		//limpiamos la lista por si acaso
		vista.getLabelSocio().setText("");
		
		//Cargamos el primer periodo en la tabla para que no salga vacía
		listaActividades();
		
		// Abre la ventana (sustituye al main generado por WindowBuilder)
		vista.getFrame().setVisible(true); 
	}
}
