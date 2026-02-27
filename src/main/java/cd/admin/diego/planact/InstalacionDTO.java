package cd.admin.diego.planact;

/**
 * DTO simple para poblar el combo de instalaciones.
 * OJO: nombres en camelCase para que DbUtils mapee bien.
 */
public class InstalacionDTO {
	private int idInstalacion;
	private String nombre;
	private String tipo;
	private Integer capacidad;

	public int getIdInstalacion() { return idInstalacion; }
	public void setIdInstalacion(int idInstalacion) { this.idInstalacion = idInstalacion; }

	public String getNombre() { return nombre; }
	public void setNombre(String nombre) { this.nombre = nombre; }

	public String getTipo() { return tipo; }
	public void setTipo(String tipo) { this.tipo = tipo; }

	public Integer getCapacidad() { return capacidad; }
	public void setCapacidad(Integer capacidad) { this.capacidad = capacidad; }

	@Override
	public String toString() {
		return nombre + " (" + tipo + ") - cap. " + capacidad;
	}
}