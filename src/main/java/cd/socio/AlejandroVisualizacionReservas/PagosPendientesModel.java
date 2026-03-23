package cd.socio.AlejandroVisualizacionReservas;

import java.util.ArrayList;
import java.util.List;

import giis.demo.util.ApplicationException;
import giis.demo.util.Database;

/**
 * Logica de negocio y acceso a datos para la pantalla de pagos pendientes de un socio.
 * Obtiene los datos del socio y la lista de cargos pendientes (reservas e inscripciones
 * a actividades) que aun no han sido abonados.
 *
 * Toda la logica se implementa mediante queries SQL usando Apache commons-dbutils.
 */
public class PagosPendientesModel {

	private Database db = new Database();

	/**
	 * Obtiene el nombre completo del socio a partir de su id.
	 *
	 * @param idSocio id del socio
	 * @return nombre del socio
	 */
	public String getNombreSocio(int idSocio) {
		String sql = "SELECT nombre FROM Socios WHERE id_socio = ?";
		List<Object[]> resultado = db.executeQueryArray(sql, idSocio);
		if (resultado.isEmpty())
			throw new ApplicationException("Socio no encontrado con id: " + idSocio);
		return resultado.get(0)[0].toString();
	}

	/**
	 * Obtiene todos los cargos pendientes de pago del socio indicado.
	 * Incluye tanto reservas de instalaciones como inscripciones a actividades
	 * que tienen pagado = 0.
	 *
	 * @param idSocio id del socio
	 * @return lista de cargos pendientes ordenada por fecha
	 */
	public List<CargoPendienteDTO> getCargospendientes(int idSocio) {
		List<CargoPendienteDTO> cargos = new ArrayList<>();
		cargos.addAll(getReservasPendientes(idSocio));
		cargos.addAll(getActividadesPendientes(idSocio));
		return cargos;
	}

	/** Reservas de instalaciones pendientes de pago */
	private List<CargoPendienteDTO> getReservasPendientes(int idSocio) {
		String sql = "SELECT"
				+ "  'reserva' AS tipo,"
				+ "  i.nombre AS descripcion,"
				+ "  strftime('%d/%m/%Y', r.fecha_hora_inicio) AS fecha,"
				+ "  r.costo AS importe"
				+ " FROM Reservas r"
				+ " JOIN Instalaciones i ON r.id_instalacion = i.id_instalacion"
				+ " WHERE r.id_socio = ?"
				+ "   AND r.pagado = 0"
				+ "   AND r.estado IN ('activa', 'completada')"
				+ " ORDER BY r.fecha_hora_inicio";
		return db.executeQueryPojo(CargoPendienteDTO.class, sql, idSocio);
	}

	/** Inscripciones a actividades pendientes de pago */
	private List<CargoPendienteDTO> getActividadesPendientes(int idSocio) {
		String sql = "SELECT"
				+ "  'actividad' AS tipo,"
				+ "  a.nombre AS descripcion,"
				+ "  strftime('%d/%m/%Y', i.fecha_inscripcion) AS fecha,"
				+ "  a.costo_socio AS importe"
				+ " FROM Inscripciones i"
				+ " JOIN Actividades a ON i.id_actividad = a.id_actividad"
				+ " WHERE i.id_socio = ?"
				+ "   AND i.estado = 'admitido'"
				+ "   AND i.pagado = 0"
				+ " ORDER BY i.fecha_inscripcion";
		return db.executeQueryPojo(CargoPendienteDTO.class, sql, idSocio);
	}
}
