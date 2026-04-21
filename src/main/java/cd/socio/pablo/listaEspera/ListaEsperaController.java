package cd.socio.pablo.listaEspera;

import java.util.List;

import cd.login.diego.UsuarioSesion;
import cd.socio.pablo.inscripcionActividad.ActividadDTO;
import cd.socio.pablo.inscripcionActividad.InscribirSocioController;
import cd.socio.pablo.listaActividades.PeriodoGlobalDTO;
import giis.demo.util.SwingUtil;

public class ListaEsperaController {

	private ListaEsperaModel model;
	private ListaEsperaView vista;
	private UsuarioSesion usuario;
	private ActividadDTO actividad;
	private List<ListaEsperaDTO> listaEspera;
	
	public ListaEsperaController(ListaEsperaModel m, ListaEsperaView v, ActividadDTO a) {
		this.model = m;
		this.vista = v;
		this.actividad = a;
		this.initView();
		this.initController();
	}
	
	public void initController() {
		vista.getBotonCerrar().addActionListener(e -> SwingUtil.exceptionWrapper(() -> vista.getFrame().dispose()));
	}
	
	private void listarListaEspera() {
		 
		/*Lista de actividades de dicho periodo*/
		listaEspera = model.getListaEspera(actividad);
			
		//Definimos las columnas de la tabla
		String[] columnas = {"id_actividad", "id_socio", "dni_no_socio", "nombre", "fecha_inscripcion"};
		
		javax.swing.table.TableModel tmodel = SwingUtil.getTableModelFromPojos(listaEspera, columnas);
		vista.getTablaListaEspera().setModel(tmodel);
	
		//Auto ajustamos el tamaño de las columnas
		SwingUtil.autoAdjustColumns(vista.getTablaListaEspera());
		
	}
	
	public void initView() {
		//cogemos los periodos globales de la BBDD
		List<ListaEsperaDTO> lista = model.getListaEspera(actividad);
		
		//limpiamos la lista por si acaso
		vista.getTablaListaEspera().removeAll();
		
		//Cargamos el primer periodo en la tabla para que no salga vacía
		listarListaEspera();
		
		// Abre la ventana (sustituye al main generado por WindowBuilder)
		vista.getFrame().setVisible(true); 
	}
}
