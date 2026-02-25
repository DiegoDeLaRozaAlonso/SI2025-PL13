package cd.admin.Alejandro.VisualizacionReservas;

/**
 * Datos de cada celda del calendario de visualizacion de reservas.
 * Cada celda representa el estado de una instalacion en una franja horaria de un dia concreto.
 * IMPORTANTE: Cuando se usan los componentes de Apache Commons DbUtils debe
 * mantenerse de forma estricta el convenio de capitalizacion de Java.
 */
public class ReservaCeldaDTO {
	/** Tipo de ocupacion: "libre", "socio" o "actividad" */
	private String tipo;
	/** Nombre del socio (con numero) o nombre de la actividad; vacio si tipo es libre */
	private String nombre;
	/** Fecha de la celda en formato ISO (yyyy-MM-dd) */
	private String fecha;
	/** Hora de la celda en formato HH:00 (p.e. "08:00") */
	private String hora;

	public ReservaCeldaDTO() {}

	public ReservaCeldaDTO(String tipo, String nombre, String fecha, String hora) {
		this.tipo = tipo;
		this.nombre = nombre;
		this.fecha = fecha;
		this.hora = hora;
	}

	public String getTipo()   { return this.tipo; }
	public String getNombre() { return this.nombre; }
	public String getFecha()  { return this.fecha; }
	public String getHora()   { return this.hora; }
	public void setTipo(String value)   { this.tipo = value; }
	public void setNombre(String value) { this.nombre = value; }
	public void setFecha(String value)  { this.fecha = value; }
	public void setHora(String value)   { this.hora = value; }
}
