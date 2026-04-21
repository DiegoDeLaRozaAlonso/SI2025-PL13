package cd.socio.pablo.listaEspera;

public class ListaEsperaDTO {

	private int id_actividad;
	private int id_socio;
	private String dni_no_socio;
	private String nombre;
	private String fecha_inscripcion;
	
	public ListaEsperaDTO() {}
	
	public ListaEsperaDTO(int idActividad, int idSocio, String dniNoSocio, String nombre, String fecha) {
		this.id_actividad = idActividad;
		this.id_socio = idSocio;
		this.dni_no_socio = dniNoSocio;
		this.nombre = nombre;
		this.fecha_inscripcion = fecha;
	}

	
	
	public int getId_actividad() {
		return id_actividad;
	}
	public void setId_actividad(int id_actividad) {
		this.id_actividad = id_actividad;
	}
	public int getId_socio() {
		return id_socio;
	}
	public void setId_socio(int id_socio) {
		this.id_socio = id_socio;
	}
	public String getDni_no_socio() {
		return dni_no_socio;
	}
	public void setDni_no_socio(String dni_no_socio) {
		this.dni_no_socio = dni_no_socio;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getFecha_inscripcion() {
		return fecha_inscripcion;
	}
	public void setFecha_inscripcion(String fecha_inscripcion) {
		this.fecha_inscripcion = fecha_inscripcion;
	}
}