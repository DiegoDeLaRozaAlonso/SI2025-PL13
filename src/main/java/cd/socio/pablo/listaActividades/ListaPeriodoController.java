package cd.socio.pablo.listaActividades;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import giis.demo.util.SwingUtil;

public class ListaPeriodoController {
	
	private ListaPeriodoModel model;
	private ListaPeriodoView vista;
	
	public ListaPeriodoController(ListaPeriodoModel m, ListaPeriodoView v) {
		this.model = m;
		this.vista = v;
		//no hay inicializacion especifica del modelo, solo de la vista
		this.initView();
		this.initController();
	}
	
	public void initController() {
		
		vista.getComboBox().addActionListener(e -> SwingUtil.exceptionWrapper(() -> listaActividades()));
		vista.getBotonVolver().addActionListener(e -> SwingUtil.exceptionWrapper(() -> vista.getFrame().dispose()));
	
	}
	
	private void listaActividades() {
		PeriodoGlobalDTO periodo = (PeriodoGlobalDTO) vista.getComboBox().getSelectedItem();
		
		if (periodo != null) {
			List<ActividadDTO> actividades = model.getListaActividades(
					periodo.getFecha_inicio(),
					periodo.getFecha_fin() 
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
		//cogemos los periodos globales de la BBDD
		List<PeriodoGlobalDTO> lista = model.getPeriodoGlobal();
		
		//limpiamos la lista por si acaso
		vista.getComboBox().removeAllItems();
		
		for (PeriodoGlobalDTO i : lista) {
			vista.getComboBox().addItem(i);
		}
		
		//Cargamos el primer periodo en la tabla para que no salga vacía
		listaActividades();
		
		// Abre la ventana (sustituye al main generado por WindowBuilder)
		vista.getFrame().setVisible(true); 
	}
}
