package cd.socio.diego.verdispoinstalacion;

public class InstalacionDTO {
	private int idInstalacion;
	private String nombre;

	public InstalacionDTO() { }

	public InstalacionDTO(int idInstalacion, String nombre) {
		this.idInstalacion = idInstalacion;
		this.nombre = nombre;
	}

	public int getIdInstalacion() { return idInstalacion; }
	public void setIdInstalacion(int idInstalacion) { this.idInstalacion = idInstalacion; }

	public String getNombre() { return nombre; }
	public void setNombre(String nombre) { this.nombre = nombre; }

	@Override
	public String toString() {
		return nombre;
	}
}