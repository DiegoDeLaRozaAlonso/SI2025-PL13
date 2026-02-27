package cd.socio.pablo.listaActividades;

import java.util.Date;
import java.util.List;

import giis.demo.tkrun.CarreraDisplayDTO;
import giis.demo.util.ApplicationException;
import giis.demo.util.Database;
import giis.demo.util.Util;

public class ListaPeriodoModel {

	private Database db = new Database();
	//private String idPeriodo;
	
	
	public void listarActividades(ActividadDTO a) {
		String sqlActividades = "SELECT "
				+ "nombre, fecha_inicio, fecha_fin, aforo, costo_socio, costo_no_socio "
				+ "from Actividades where fecha_inicio <= ? OR fecha_fin >= ?;";
		
	}
	
	/**
	 * Obtiene la lista de carreras activas en forma objetos para una fecha de inscripcion dada
	 */
	public List<ActividadDTO> getListaCarreras(PeriodoDTO periodo, PeriodoDTO periodoFin) {
		validateNotNull(periodoInicio, "");
		validateNotNull(periodoFin, "");
		String sql = "SELECT id,descr," 
				+ " case when ?<inicio then ''" // antes de inscripcion
				+ "   when ?<=fin then '(Abierta)'" // fase 1
				+ "   when ?<fecha then '(Abierta)'" // fase 2
				+ "   when ?=fecha then '(Abierta)'" // fase 3
				+ "   else '' " // despues de fin carrera
				+ " end as abierta" 
				+ " from carreras  where fecha>=? order by id";
		String d = Util.dateToIsoString(periodoInicio);
		String d2 = Util.dateToIsoString(periodoFin);
		return db.executeQueryPojo(ActividadDTO.class, sql, d, d, d, d, d);
	}
	
	/* De uso general para validacion de objetos */
	private void validateNotNull(Object obj, String msg) {
		if (obj == null)
			throw new ApplicationException(msg);
	}
	
	
}
