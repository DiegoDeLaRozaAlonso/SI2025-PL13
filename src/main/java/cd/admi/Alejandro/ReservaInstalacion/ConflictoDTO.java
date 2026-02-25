package cd.admi.Alejandro.ReservaInstalacion;

/**
 * Datos de cada conflicto detectado al intentar reservar una instalacion para una actividad.
 * Un conflicto existe cuando ya hay una reserva de socio o una sesion de actividad
 * que se solapa con el horario solicitado en la misma instalacion y fecha.
 * IMPORTANTE: Cuando se usan los componentes de Apache Commons DbUtils debe
 * mantenerse de forma estricta el convenio de capitalizacion de Java.
 */
public class ConflictoDTO {
	/** Tipo del ocupante existente: "actividad" o "socio" */
	private String tipo;
	/** Nombre de la actividad o del socio (con numero de socio) que provoca el conflicto */
	private String nombre;
	/** Hora de inicio del bloque conflictivo (formato HH:mm) */
	private String horaInicio;
	/** Hora de fin del bloque conflictivo (formato HH:mm) */
	private String horaFin;

	public ConflictoDTO() {}

	public ConflictoDTO(String tipo, String nombre, String horaInicio, String horaFin) {
		this.tipo = tipo;
		this.nombre = nombre;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
	}

	public String getTipo()       { return this.tipo; }
	public String getNombre()     { return this.nombre; }
	public String getHoraInicio() { return this.horaInicio; }
	public String getHoraFin()    { return this.horaFin; }
	public void setTipo(String value)       { this.tipo = value; }
	public void setNombre(String value)     { this.nombre = value; }
	public void setHoraInicio(String value) { this.horaInicio = value; }
	public void setHoraFin(String value)    { this.horaFin = value; }
}
