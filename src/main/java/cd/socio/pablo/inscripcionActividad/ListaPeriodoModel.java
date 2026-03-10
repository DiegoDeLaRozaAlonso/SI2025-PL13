package cd.socio.pablo.inscripcionActividad;

import java.util.Date;
import java.util.List;
import giis.demo.tkrun.CarreraDisplayDTO;
import giis.demo.util.ApplicationException;
import giis.demo.util.Database;
import giis.demo.util.Util;

public class ListaPeriodoModel {

	private Database db = new Database();
	
	
	/*
	 * Ya no se usan ya que sacamos las fechas directemente del objeto
	 */
	
	/*public String getFechaInicio(String periodo) {
		String sql = "SELECT fecha_inicio FROM Periodos WHERE nombre = ?";
		
		List<PeriodoGlobalDTO> lista = db.executeQueryPojo(PeriodoGlobalDTO.class, sql, periodo);
		
		return (!lista.isEmpty()) ? lista.get(0).getFecha_inicio() : null;
	}
	
	public String getFechaFin(String periodo) {
		String sql = "SELECT fecha_fin FROM Periodos WHERE nombre = ?";
		
		List<PeriodoGlobalDTO> lista = db.executeQueryPojo(PeriodoGlobalDTO.class, sql, periodo);
		
		return (!lista.isEmpty()) ? lista.get(0).getFecha_fin() : null;
	}*/
	
	/*public List<PeriodoGlobalDTO> getPeriodoGlobal(){
		
		String sql = "SELECT nombre, fecha_inicio, fecha_fin from PeriodosGlobales";
		
		return db.executeQueryPojo(PeriodoGlobalDTO.class, sql);
	}*/
	
	/**
	 * Obtiene la lista de carreras activas en forma objetos para una fecha de inscripcion dada
	 */
	public List<ActividadDTO> getListaActividades(String fechaInicio, String fechaFin) {
		validateNotNull(fechaInicio, "La fecha de Inicio no puede ser nula");
		validateNotNull(fechaFin, "La fecha de Fin no puede ser nula");
		String sql = "SELECT nombre, descripcion AS desc, aforo, "
	               + "fecha_inicio, fecha_fin, "
	               + "costo_socio AS precioSocio, costo_no_socio AS precioNoSocio "
	               + "FROM Actividades WHERE fecha_inicio <= ? AND fecha_fin >= ?";
		//String d = Util.dateToIsoString(periodo);
		return db.executeQueryPojo(ActividadDTO.class, sql, fechaFin, fechaInicio);
		//return db.executeQueryPojo(ActividadDTO.class, sql, d, d, d, d, d);
	}
	
	/* De uso general para validacion de objetos */
	private void validateNotNull(Object obj, String msg) {
		if (obj == null)
			throw new ApplicationException(msg);
	}
	
	
}
