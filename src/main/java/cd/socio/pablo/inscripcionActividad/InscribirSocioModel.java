package cd.socio.pablo.inscripcionActividad;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cd.admin.pablo.periodo.PeriodoDTO;
import giis.demo.tkrun.CarreraDisplayDTO;
import giis.demo.util.ApplicationException;
import giis.demo.util.Database;
import giis.demo.util.Util;

public class InscribirSocioModel {

	private Database db = new Database();
	
	private String fechaFin;
	
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
	
	public void insertarInscripcion(InscripcionDTO ins) {
		
		/*validaParametros(
				
		);*/
		
		convierteFecha(ins);
	}
	
	private void convierteFecha(InscripcionDTO periodo){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		this.fechaFin = sdf.format(periodo.getFecha_inscripcion());
		
	}
	
	public String compruebaAforo(ActividadDTO actividad) {
		
		String sql = "SELECT * FROM	INSCRIPCIONES WHERE id_actividad = ?";
		
		List<ActividadDTO> lista = 
				db.executeQueryPojo(ActividadDTO.class, sql, actividad.getId());
		
		return (lista.size() < actividad.getAforo()) ? "admitido" : "lista_espera";
		
	}
	
	public void inscribirSocioActividad(SocioDTO socio, ActividadDTO actividad, InscripcionDTO ins) {
		String sql = "INSERT INTO Inscripciones "
				+ "(id_actividad, id_socio, nombre_no_socio, fecha_inscripcion, estado, pagado, tipo) "
				+ "VALUES (?, ?, NULL, ?, ?, ?, 'socio')";
		db.executeUpdate(sql, actividad.getId(), socio.getId(), 
				ins.getFecha_inscripcion(), compruebaAforo(actividad), 0);
	}
	
	/**
	 * Obtiene la lista de carreras activas en forma objetos para una fecha de inscripcion dada
	 * @return List<ActividadDTO>
	 */
	public List<ActividadDTO> getListaActividades(String fechaInicio, String fechaFin) {
		
		validaFechas(fechaInicio, fechaFin);
//		String sql = "SELECT nombre, descripcion AS desc, aforo, "
//	               + "fecha_inicio, fecha_fin, "
//	               + "costo_socio AS precioSocio, costo_no_socio AS precioNoSocio "
//	               + "FROM Actividades WHERE fecha_inicio <= ? AND fecha_fin >= ?";
		
		String sql = "SELECT a.nombre, a.descripcion, a.aforo, "
	               + "a.fecha_inicio, a.fecha_fin, "
	               + "a.costo_socio AS precioSocio, a.costo_no_socio AS precioNoSocio, "
	               + "p.fecha_fin AS fecha_fin_periodo " 
	               + "FROM Actividades a "
	               + "INNER JOIN PeriodosGlobales p ON a.id_periodo = p.id_periodo_global "
	               + "WHERE a.fecha_inicio <= ? AND a.fecha_fin >= ?";

		return db.executeQueryPojo(ActividadDTO.class, sql, fechaFin, fechaInicio);
	}
	
	private void validaFechas(String fechaInicio, String fechaFin) {
	
		validateNotNull(fechaInicio, "La fecha de fin del periodo de SOCIOS no puede ser nula");
		validateNotNull(fechaFin, "La fecha de fin del periodo de NO SOCIOS no puede ser nula");
		validaFecha(fechaInicio.compareTo(fechaFin) <= 0, "La fecha de inicio no puede ser posterior a la de fin de SOCIO");
	}
	
	private void validaFecha(boolean condition, String message) {
		if (!condition)
			throw new ApplicationException(message);
	}
	
	/* De uso general para validacion de objetos */
	private void validateNotNull(Object obj, String msg) {
		if (obj == null)
			throw new ApplicationException(msg);
	}
	
	
}
