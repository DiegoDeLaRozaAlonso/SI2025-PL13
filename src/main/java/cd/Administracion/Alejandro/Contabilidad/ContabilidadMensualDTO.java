package cd.Administracion.Alejandro.Contabilidad;

/**
 * Datos de la contabilidad mensual de un socio.
 * Contiene el total de reservas e inscripciones a actividades pendientes de pago.
 * IMPORTANTE: Cuando se usan los componentes de Apache Commons DbUtils debe
 * mantenerse de forma estricta el convenio de capitalizacion de Java.
 */
public class ContabilidadMensualDTO {
	/** Id del socio */
	private String idSocio;
	/** Nombre completo del socio */
	private String nombre;
	/** Total pendiente por inscripciones a actividades */
	private double actividades;
	/** Total pendiente por reservas de instalaciones */
	private double reservas;

	public ContabilidadMensualDTO() {}

	public String getIdSocio()     { return this.idSocio; }
	public String getNombre()      { return this.nombre; }
	public double getActividades() { return this.actividades; }
	public double getReservas()    { return this.reservas; }

	public void setIdSocio(String value)     { this.idSocio = value; }
	public void setNombre(String value)      { this.nombre = value; }
	public void setActividades(double value) { this.actividades = value; }
	public void setReservas(double value)    { this.reservas = value; }

	/** Devuelve el total (actividades + reservas) */
	public double getTotal() { return this.actividades + this.reservas; }

	/** Formatea un importe como "12,00 €" */
	public static String formatImporte(double importe) {
		return String.format("%.2f €", importe).replace(".", ",");
	}
}
