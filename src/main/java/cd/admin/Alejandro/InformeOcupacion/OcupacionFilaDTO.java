package cd.admin.Alejandro.InformeOcupacion;

/**
 * DTO de cada fila del informe de ocupacion de instalaciones.
 * Cada fila representa la ocupacion de una instalacion para una actividad concreta.
 *
 * IMPORTANTE: todos los campos numericos son String porque Apache Commons DbUtils
 * requiere que el tipo Java coincida con lo que devuelve SQLite al usar CAST a TEXT.
 * Los calculos de porcentaje y plazas libres se realizan en el controlador.
 */
public class OcupacionFilaDTO {

	/** Nombre de la instalacion */
	private String nombreInstalacion;

	/** Nombre de la actividad que ocupa la instalacion */
	private String nombreActividad;

	/** Numero de inscritos con estado 'admitido' en la actividad */
	private String inscritosActividad;

	/** Aforo maximo de la actividad (plazas totales de la actividad) */
	private String aforoActividad;

	/** Capacidad maxima de la instalacion */
	private String capacidadInstalacion;

	/**
	 * Numero de reservas individuales de socios en estado 'activa' o 'completada'
	 * para esta instalacion en el periodo consultado.
	 */
	private String reservasActivas;

	public OcupacionFilaDTO() {}

	// ── Getters ──────────────────────────────────────────────────────────────

	public String getNombreInstalacion()  { return nombreInstalacion;  }
	public String getNombreActividad()    { return nombreActividad;    }
	public String getInscritosActividad() { return inscritosActividad; }
	public String getAforoActividad()     { return aforoActividad;     }
	public String getCapacidadInstalacion() { return capacidadInstalacion; }
	public String getReservasActivas()    { return reservasActivas;    }

	// ── Setters ──────────────────────────────────────────────────────────────

	public void setNombreInstalacion(String value)    { this.nombreInstalacion    = value; }
	public void setNombreActividad(String value)      { this.nombreActividad      = value; }
	public void setInscritosActividad(String value)   { this.inscritosActividad   = value; }
	public void setAforoActividad(String value)       { this.aforoActividad       = value; }
	public void setCapacidadInstalacion(String value) { this.capacidadInstalacion = value; }
	public void setReservasActivas(String value)      { this.reservasActivas      = value; }

	// ── Calculos derivados (no mapeados por DbUtils) ──────────────────────────

	/**
	 * Porcentaje de ocupacion por actividad: inscritos admitidos / aforo actividad * 100.
	 * Devuelve 0 si el aforo es 0 o nulo.
	 */
	public int getPorcentajeActividad() {
		int ins   = parseIntSafe(inscritosActividad);
		int aforo = parseIntSafe(aforoActividad);
		return aforo > 0 ? Math.min(100, ins * 100 / aforo) : 0;
	}

	/**
	 * Porcentaje de ocupacion por socio: reservas activas / capacidad instalacion * 100.
	 * Devuelve 0 si la capacidad es 0 o nula.
	 */
	public int getPorcentajeSocio() {
		int res = parseIntSafe(reservasActivas);
		int cap = parseIntSafe(capacidadInstalacion);
		return cap > 0 ? Math.min(100, res * 100 / cap) : 0;
	}

	/**
	 * Plazas libres de la actividad: aforo - inscritos admitidos.
	 * Nunca negativo.
	 */
	public int getPlazasLibres() {
		int aforo = parseIntSafe(aforoActividad);
		int ins   = parseIntSafe(inscritosActividad);
		return Math.max(0, aforo - ins);
	}

	// ── Utilidad interna ──────────────────────────────────────────────────────

	private int parseIntSafe(String s) {
		if (s == null || s.trim().isEmpty()) return 0;
		try { return Integer.parseInt(s.trim()); }
		catch (NumberFormatException e) { return 0; }
	}
}
