package cd.admin.pablo.listaPeriodo;

import giis.demo.util.Database;

public class ListaPeriodoModel {

	private Database db = new Database();
	//private String idPeriodo;
	
	String sqlPeriodo = "SELECT nombre from PeriodosInscripcion;";
	
	String sqlActividades = "SELECT "
			+ "nombre, fecha_inicio, fecha_fin, aforo, costo_socio, costo_no_socio "
			+ "from Actividades where id_periodo = ?;";
}
