package cd.Administracion.Alejandro.Contabilidad;

import java.util.List;

import giis.demo.util.Database;

/**
 * Logica de negocio y acceso a datos para la pantalla de contabilidad mensual.
 * Obtiene para cada socio el total pendiente de pago en reservas e inscripciones
 * a actividades filtrado por mes y anho.
 *
 * Toda la logica se implementa mediante queries SQL usando Apache commons-dbutils.
 */
public class ContabilidadMensualModel {

	/** Meses disponibles para el selector (nombre visible → numero de mes) */
	public static final String[] NOMBRES_MESES = {
		"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
		"Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
	};

	public static final int ANHO_BASE = 2026;

	private Database db = new Database();

	/**
	 * Obtiene la contabilidad mensual de todos los socios para el mes y anho indicados.
	 * Para cada socio calcula el total pendiente de pago en reservas e inscripciones.
	 * Solo incluye socios que no son administradores (es_admin = 0).
	 *
	 * @param mes  numero de mes (1-12)
	 * @param anho anho (ej. 2026)
	 * @return lista de DTOs con los datos de cada socio
	 */
	public List<ContabilidadMensualDTO> getContabilidadMensual(int mes, int anho) {
		String mesStr  = String.format("%02d", mes);
		String anhoStr = String.valueOf(anho);

		String sql = "SELECT"
				+ "  CAST(s.id_socio AS TEXT) AS idSocio,"
				+ "  s.nombre,"
				+ "  COALESCE(("
				+ "    SELECT SUM(a.costo_socio)"
				+ "    FROM Inscripciones i"
				+ "    JOIN Actividades a ON i.id_actividad = a.id_actividad"
				+ "    WHERE i.id_socio = s.id_socio"
				+ "      AND i.pagado = 0"
				+ "      AND i.estado = 'admitido'"
				+ "      AND strftime('%m', i.fecha_inscripcion) = ?"
				+ "      AND strftime('%Y', i.fecha_inscripcion) = ?"
				+ "  ), 0.0) AS actividades,"
				+ "  COALESCE(("
				+ "    SELECT SUM(r.costo)"
				+ "    FROM Reservas r"
				+ "    WHERE r.id_socio = s.id_socio"
				+ "      AND r.pagado = 0"
				+ "      AND r.estado IN ('activa', 'completada')"
				+ "      AND strftime('%m', r.fecha_hora_inicio) = ?"
				+ "      AND strftime('%Y', r.fecha_hora_inicio) = ?"
				+ "  ), 0.0) AS reservas"
				+ " FROM Socios s"
				+ " WHERE s.es_admin = 0"
				+ " ORDER BY s.id_socio";

		return db.executeQueryPojo(ContabilidadMensualDTO.class, sql,
				mesStr, anhoStr, mesStr, anhoStr);
	}
}
