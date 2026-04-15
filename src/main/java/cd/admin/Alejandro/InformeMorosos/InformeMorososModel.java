package cd.admin.Alejandro.InformeMorosos;

import java.util.List;

import giis.demo.util.Database;

/**
 * Acceso a los datos de socios morosos.
 *
 * Fuentes de deuda consideradas:
 *   1. Recibos mensuales con pagado = 0.
 *   2. Inscripciones con pagado = 0, estado = 'admitido' y cuya actividad
 *      finalizo ANTES del mes en curso (si la actividad sigue activa el
 *      socio podria estar en plazo de pago, por lo que no se considera moroso).
 *
 * NOTA: todas las columnas INTEGER/REAL de SQLite se castean a TEXT en las
 * queries para que Apache Commons DbUtils pueda mapearlas correctamente.
 */
public class InformeMorososModel {

	private Database db = new Database();

	/**
	 * Devuelve todos los pagos pendientes (recibos + inscripciones) de socios
	 * no administradores, uno por fila, con los datos del socio.
	 *
	 * El filtro de texto (nombre o id) es opcional; si se deja vacio devuelve todos.
	 *
	 * @param textoBusqueda nombre o id del socio (vacio = todos)
	 * @return lista de MorososFilaDTO ordenada por socio
	 */
	public List<MorososFilaDTO> getPagosPendientes(String textoBusqueda) {

		String filtroSocio = "";
		if (textoBusqueda != null && !textoBusqueda.trim().isEmpty()) {
			String like = textoBusqueda.trim();
			filtroSocio = " AND (s.nombre LIKE '%" + like + "%'"
					    + " OR CAST(s.id_socio AS TEXT) LIKE '%" + like + "%')";
		}

		// ── 1. Recibos mensuales impagados ───────────────────────────────────
		// Se incluyen todos los recibos con pagado = 0, sin filtro de mes,
		// porque un recibo de enero no pagado en abril es claramente una deuda.
		String sqlRecibos =
				"SELECT"
				+ " CAST(s.id_socio AS TEXT)   AS idSocio,"
				+ " s.nombre                    AS nombreSocio,"
				+ " 'recibo'                    AS tipo,"
				+ " 'Cuota ' ||"
				+ "   CASE r.mes"
				+ "     WHEN 1  THEN 'Enero'"
				+ "     WHEN 2  THEN 'Febrero'"
				+ "     WHEN 3  THEN 'Marzo'"
				+ "     WHEN 4  THEN 'Abril'"
				+ "     WHEN 5  THEN 'Mayo'"
				+ "     WHEN 6  THEN 'Junio'"
				+ "     WHEN 7  THEN 'Julio'"
				+ "     WHEN 8  THEN 'Agosto'"
				+ "     WHEN 9  THEN 'Septiembre'"
				+ "     WHEN 10 THEN 'Octubre'"
				+ "     WHEN 11 THEN 'Noviembre'"
				+ "     WHEN 12 THEN 'Diciembre'"
				+ "     ELSE CAST(r.mes AS TEXT)"
				+ "   END || ' ' || CAST(r.anho AS TEXT) AS concepto,"
				+ " r.fecha_emision             AS fechaEmision,"
				+ " r.fecha_vencimiento         AS fechaVencimiento,"
				+ " CAST(r.total AS TEXT)       AS total"
				+ " FROM Recibos r"
				+ " JOIN Socios s ON r.id_socio = s.id_socio"
				+ " WHERE r.pagado = 0"
				+ "   AND s.es_admin = 0"
				+ filtroSocio;

		// ── 2. Inscripciones impagadas de actividades YA FINALIZADAS ─────────
		// Condicion clave: strftime('%Y-%m', a.fecha_fin) < strftime('%Y-%m','now')
		// Solo se consideran inscripciones 'admitido'; las de lista_espera
		// no han sido aceptadas formalmente y no generan deuda.
		String sqlInscripciones =
				"SELECT"
				+ " CAST(s.id_socio AS TEXT)         AS idSocio,"
				+ " s.nombre                          AS nombreSocio,"
				+ " 'inscripcion'                     AS tipo,"
				+ " 'Inscripcion: ' || a.nombre       AS concepto,"
				+ " i.fecha_inscripcion               AS fechaEmision,"
				+ " a.fecha_fin                       AS fechaVencimiento,"
				+ " CAST(a.costo_socio AS TEXT)       AS total"
				+ " FROM Inscripciones i"
				+ " JOIN Socios s    ON i.id_socio     = s.id_socio"
				+ " JOIN Actividades a ON i.id_actividad = a.id_actividad"
				+ " WHERE i.pagado  = 0"
				+ "   AND i.estado  = 'admitido'"
				+ "   AND i.id_socio IS NOT NULL"
				+ "   AND s.es_admin = 0"
				+ "   AND strftime('%Y-%m', a.fecha_fin) < strftime('%Y-%m','now')"
				+ filtroSocio;

		String sql = "SELECT * FROM (" + sqlRecibos
				+ " UNION ALL "
				+ sqlInscripciones + ") ORDER BY nombreSocio, fechaVencimiento";

		return db.executeQueryPojo(MorososFilaDTO.class, sql);
	}
}
