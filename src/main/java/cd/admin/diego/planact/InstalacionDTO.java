package cd.admin.diego.planact;

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
		String cap = (capacidad == null) ? "-" : capacidad.toString();
		return nombre + " (" + tipo + ") - cap. " + cap;
	}
}