package cd.admin.diego.planact;

public class PeriodoInscripcionDTO {
	private int idPeriodo;
	private String nombre;
	private String descripcion;
	private String fechaInicioSocio;
	private String fechaFinSocio;
	private String fechaFinNoSocio;

	public int getIdPeriodo() { return idPeriodo; }
	public void setIdPeriodo(int idPeriodo) { this.idPeriodo = idPeriodo; }

	public String getNombre() { return nombre; }
	public void setNombre(String nombre) { this.nombre = nombre; }

	public String getDescripcion() { return descripcion; }
	public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

	public String getFechaInicioSocio() { return fechaInicioSocio; }
	public void setFechaInicioSocio(String fechaInicioSocio) { this.fechaInicioSocio = fechaInicioSocio; }

	public String getFechaFinSocio() { return fechaFinSocio; }
	public void setFechaFinSocio(String fechaFinSocio) { this.fechaFinSocio = fechaFinSocio; }

	public String getFechaFinNoSocio() { return fechaFinNoSocio; }
	public void setFechaFinNoSocio(String fechaFinNoSocio) { this.fechaFinNoSocio = fechaFinNoSocio; }

	@Override
	public String toString() {
		return nombre;
	}
}