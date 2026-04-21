package cd.socio.pablo.inscripcionActividad;

import java.util.Date;
import java.util.List;

import cd.login.diego.UsuarioSesion;
import cd.socio.pablo.listaEspera.ListaEsperaDTO;
import giis.demo.util.ApplicationException;
import giis.demo.util.Database;
import giis.demo.util.Util;

public class InscribirSocioModel {

	private Database db = new Database();
	
	private int idActividad;
	
	/**
	 * Comprueba si hay aforo disponible para la actividad
	 * @param actividad
	 * @return 1 si hay aforo 0 si no hay
	 */
	public int compruebaAforo(ActividadDTO actividad) {
		
		String sql = "SELECT * FROM	INSCRIPCIONES WHERE id_actividad = ?";
		
		List<InscripcionDTO> lista = 
				db.executeQueryPojo(InscripcionDTO.class, sql, actividad.getId());
		
		return (lista.size() < actividad.getAforo()) ? 1 : 0;
		
	}
	
	/**
	 * Añade a la BBDD la inscripcion a una actividad del usuario
	 * @param socio
	 * @param actividad
	 * @param ins
	 */
	public void inscribirSocioActividad(UsuarioSesion socio, ActividadDTO actividad, InscripcionDTO ins) {
				
		String sql = "INSERT INTO Inscripciones "
				+ "(id_actividad, id_socio, nombre_no_socio, fecha_inscripcion, estado, pagado, tipo) "
				+ "VALUES (?, ?, NULL, ?, 'admitido', ?, 'socio')";
		db.executeUpdate(sql, actividad.getId(), socio.getId(), 
				ins.getFecha_inscripcion(), ins.isPagado());
	}
	
	/**
	 * Obtiene la lista de carreras activas en forma objetos para una fecha de inscripcion dada
	 * @return List<ActividadDTO>
	 */
	public List<ActividadDTO> getListaActividades(String fechaInicio, String fechaFin) {
		
		validaFechas(fechaInicio, fechaFin);
		
		String sql = "SELECT a.id_actividad, a.nombre, a.descripcion, a.aforo, "
	               + "a.fecha_inicio, a.fecha_fin, "
	               + "a.costo_socio AS precioSocio, "
	               + "p.fecha_inicio_socio AS fecha_inicio_periodo, "
	               + "p.fecha_fin_socio AS fecha_fin_periodo " 
	               + "FROM Actividades a "
	               + "INNER JOIN PeriodosInscripcion p ON a.id_periodo = p.id_periodo "
	               + "WHERE a.fecha_inicio <= ? AND a.fecha_fin >= ?";

		return db.executeQueryPojo(ActividadDTO.class, sql, fechaFin, fechaInicio);
	}
	
	/**
	 * Comprueba que el socio no tiene deudas pendientes
	 * @param socio
	 * @return true si es moroso y false si está al corriente de pago
	 */
	public boolean tieneDeudas(UsuarioSesion socio) {
		String sql = "SELECT debe_dinero AS debeDinero from Socios WHERE id_socio = ?";
		List<SocioDTO> lista = db.executeQueryPojo(SocioDTO.class, sql, socio.getId());
		
		return (lista.get(0).isDebeDinero()) ? true : false;
	}
	
	/**
	 * Comprueba que el socio no estuviera ya inscrito en dicha actividad
	 * @param socio
	 * @param actividad
	 * @return
	 */
	public boolean inscripcionRepetida(UsuarioSesion socio, ActividadDTO actividad) {
		//Comprobamos que no esté en inscripciones
		String sql = "SELECT * FROM Inscripciones WHERE  id_actividad = ? AND id_socio = ?";
		List<InscripcionDTO> inscripciones = db.executeQueryPojo(InscripcionDTO.class, sql, actividad.getId(), socio.getId());
		//Comprobamos que no esté en lista de espera
		String sql2 = "SELECT * FROM ListaEspera WHERE  id_actividad = ? AND id_socio = ?";
		List<ListaEsperaDTO> esperas = db.executeQueryPojo(ListaEsperaDTO.class, sql2, actividad.getId(), socio.getId());
		
		return ((inscripciones.size() == 0) && (esperas.size() == 0)) ? false : true;
	}
	
	/**
	 * Comprueba que el socio cumple con los periodos de inscripcion
	 * @param a
	 */
	public void enPlazo(ActividadDTO a) {
		Date fecha = new Date();
		
		String fechaHoy = Util.dateToIsoString(fecha);
		
		validaFecha(a.getFecha_inicio_periodo().compareTo(fechaHoy) <= 0, "No ha empezado el periodo de inscripcion");
		validaFecha(fechaHoy.compareTo(a.getFecha_fin_periodo()) <= 0, "Ya termino el periodo de inscripcion");
	}
	
	/**
	 * Comprueba que las fechas no son nulas o que la de fin ocurra antes que la de inicio
	 * @param fechaInicio
	 * @param fechaFin
	 */
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
