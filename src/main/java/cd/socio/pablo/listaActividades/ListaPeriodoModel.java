package cd.socio.pablo.listaActividades;

import giis.demo.util.Database;

public class ListaPeriodoModel {

	private Database db = new Database();
	//private String idPeriodo;
	
	
	public void listarActividades(ActividadDTO a) {
		String sqlActividades = "SELECT "
				+ "nombre, fecha_inicio, fecha_fin, aforo, costo_socio, costo_no_socio "
				+ "from Actividades where fecha_inicio <= ? OR fecha_fin >= ?;";
		
	}
	
	
}
