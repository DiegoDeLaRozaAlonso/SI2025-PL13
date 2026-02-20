package cd.admin.Alejandro.ResInstalacion;

/**
 * Datos del modelo de dominio de cada una de las instalaciones del centro.
 * IMPORTANTE: Cuando se usan los componentes de Apache Commons DbUtils debe
 * mantenerse de forma estricta el convenio de capitalizacion de Java.
 */
public class InstalacionEntity {
	private String id;
	private String nombre;
	private String tipo;
	private String capacidad;
	private String enUso;

	public String getId()        { return this.id; }
	public String getNombre()    { return this.nombre; }
	public String getTipo()      { return this.tipo; }
	public String getCapacidad() { return this.capacidad; }
	public String getEnUso()     { return this.enUso; }
	public void setId(String value)        { this.id = value; }
	public void setNombre(String value)    { this.nombre = value; }
	public void setTipo(String value)      { this.tipo = value; }
	public void setCapacidad(String value) { this.capacidad = value; }
	public void setEnUso(String value)     { this.enUso = value; }
}
