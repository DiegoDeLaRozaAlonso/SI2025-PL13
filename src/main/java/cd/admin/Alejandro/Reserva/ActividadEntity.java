package cd.admin.Alejandro.Reserva;

/**
 * Datos del modelo de dominio de cada una de las actividades del centro.
 * IMPORTANTE: Cuando se usan los componentes de Apache Commons DbUtils debe
 * mantenerse de forma estricta el convenio de capitalizacion de Java:
 *  - Capitalizar todas las palabras que forman un identificador
 *    excepto la primera letra de nombres de metodos y variables.
 *  - No utilizar subrayados
 * Seguir tambien estos mismos criterios en los nombres de tablas y campos de la BD.
 */
public class ActividadEntity {
	private String id;
	private String nombre;
	private String descripcion;
	private String idInstalacion;
	private String aforo;
	private String costoSocio;
	private String costoNoSocio;
	private String fechaInicio;
	private String fechaFin;

	public String getId()            { return this.id; }
	public String getNombre()        { return this.nombre; }
	public String getDescripcion()   { return this.descripcion; }
	public String getIdInstalacion() { return this.idInstalacion; }
	public String getAforo()         { return this.aforo; }
	public String getCostoSocio()    { return this.costoSocio; }
	public String getCostoNoSocio()  { return this.costoNoSocio; }
	public String getFechaInicio()   { return this.fechaInicio; }
	public String getFechaFin()      { return this.fechaFin; }

	public void setId(String value)            { this.id = value; }
	public void setNombre(String value)        { this.nombre = value; }
	public void setDescripcion(String value)   { this.descripcion = value; }
	public void setIdInstalacion(String value) { this.idInstalacion = value; }
	public void setAforo(String value)         { this.aforo = value; }
	public void setCostoSocio(String value)    { this.costoSocio = value; }
	public void setCostoNoSocio(String value)  { this.costoNoSocio = value; }
	public void setFechaInicio(String value)   { this.fechaInicio = value; }
	public void setFechaFin(String value)      { this.fechaFin = value; }
}
