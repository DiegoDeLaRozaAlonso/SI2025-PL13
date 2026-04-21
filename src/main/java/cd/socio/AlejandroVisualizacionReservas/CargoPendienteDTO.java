package cd.socio.AlejandroVisualizacionReservas;

/**
 * Datos de cada cargo pendiente de pago de un socio.
 * Un cargo puede ser de tipo "actividad" (inscripcion a una actividad)
 * o de tipo "reserva" (reserva de una instalacion).
 * IMPORTANTE: Cuando se usan los componentes de Apache Commons DbUtils debe
 * mantenerse de forma estricta el convenio de capitalizacion de Java.
 */
public class CargoPendienteDTO {
	/** Tipo del cargo: "actividad" o "reserva" */
	private String tipo;
	/** Nombre de la actividad o de la instalacion reservada */
	private String descripcion;
	/** Fecha del cargo en formato dd/MM/yyyy */
	private String fecha;
	/** Importe del cargo en euros */
	private double importe;

	public CargoPendienteDTO() {}

	public CargoPendienteDTO(String tipo, String descripcion, String fecha, double importe) {
		this.tipo        = tipo;
		this.descripcion = descripcion;
		this.fecha       = fecha;
		this.importe     = importe;
	}

	public String getTipo()        { return this.tipo; }
	public String getDescripcion() { return this.descripcion; }
	public String getFecha()       { return this.fecha; }
	public double getImporte()     { return this.importe; }

	public void setTipo(String value)        { this.tipo = value; }
	public void setDescripcion(String value) { this.descripcion = value; }
	public void setFecha(String value)       { this.fecha = value; }
	public void setImporte(double value)     { this.importe = value; }

	/** Devuelve el importe formateado como "12,00 €" */
	public String getImporteFormateado() {
		return String.format("%.2f €", importe).replace(".", ",");
	}
}
