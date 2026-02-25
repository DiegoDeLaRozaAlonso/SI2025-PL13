package cd.admin.pablo.listaPeriodo;

import cd.admin.pablo.periodo.PeriodoDTO;
import giis.demo.util.Database;

public class ListaPeriodoModel {

	private Database db = new Database();
	//private String idPeriodo;
	
	
	
	public void listarActividades(ActividadDTO a) {
		String sqlActividades = "SELECT "
				+ "nombre, fecha_inicio, fecha_fin, aforo, costo_socio, costo_no_socio "
				+ "from Actividades WHERE fecha_inicio >= ? OR fecha_fin <= ?;";
		
	}
	
	public void listarPeriodos(PeriodoDTO periodo) {
		String sqlPeriodo = "SELECT nombre from PeriodosInscripcion;";
		
	}
}
