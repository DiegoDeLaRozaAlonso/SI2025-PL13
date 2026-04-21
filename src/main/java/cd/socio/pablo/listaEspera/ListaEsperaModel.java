package cd.socio.pablo.listaEspera;

import java.util.List;

import cd.login.diego.UsuarioSesion;
import cd.socio.pablo.inscripcionActividad.ActividadDTO;
import cd.socio.pablo.inscripcionActividad.InscripcionDTO;
import cd.socio.pablo.inscripcionActividad.SocioDTO;
import giis.demo.util.Database;

public class ListaEsperaModel {
	
	private Database db = new Database();
	
	
	/**
	 * Método para conseguir la lista de espera de una actividad
	 * @param actividad
	 * @return devuelve una cadena de tipo List 
	 */
	public List<ListaEsperaDTO> getListaEspera(ActividadDTO actividad) {
		
		String sql = "SELECT * FROM LISTAESPERA WHERE id_actividad = ?";
		
		List<ListaEsperaDTO> lista =
				db.executeQueryPojo(ListaEsperaDTO.class, sql, actividad.getId());
		
		return lista;
	}
	
	/**
	 * Recoge el número de personas que están en lista de espera de una actividad
	 * @param actividad
	 * @return número de personas en lista de espera de dicha actividad
	 */
	public int numeroListaEspera(ActividadDTO actividad) {
		
		String sql = "SELECT * FROM ListaEspera WHERE id_actividad = ?";
		
		List<InscripcionDTO> lista = 
				db.executeQueryPojo(InscripcionDTO.class, sql, actividad.getId());
		
		return (!lista.isEmpty()) ? lista.size() : 0;
	}
	
	/**
	 * Añade al socio a la lista de espera (ModoSocio)
	 * @return
	 */
	public void insertarEnListaEspera(UsuarioSesion socio, ActividadDTO actividad, InscripcionDTO inscripcion) {
		
		String sql = "INSERT INTO ListaEspera "
				+ "(id_actividad, id_socio, nombre, fecha_inscripcion) "
				+ "VALUES (?, ?, ?, ?)";
		db.executeUpdate(sql, actividad.getId(), socio.getId(), socio.getNombre(), inscripcion.getFecha_inscripcion());
	}
	
	/**
	 * Añade al socio a la lista de espera (Modo Admin)
	 * @return
	 */
	public void insertarEnListaEspera(SocioDTO socio, ActividadDTO actividad, InscripcionDTO inscripcion) {
		
		String sql = "INSERT INTO ListaEspera "
				+ "(id_actividad, id_socio, nombre, fecha_inscripcion) "
				+ "VALUES (?, ?, ?, ?)";
		db.executeUpdate(sql, actividad.getId(), socio.getId_socio(), socio.getNombre(), inscripcion.getFecha_inscripcion());
	}
 
}
