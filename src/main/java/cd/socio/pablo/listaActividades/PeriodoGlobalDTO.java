package cd.socio.pablo.listaActividades;

public class PeriodoGlobalDTO {
	
	private String nombre;
	private String fecha_inicio;
	private String fecha_fin;

	
	/*Constructor de la clase PeriodoGlobalDTO*/

	public PeriodoGlobalDTO(String nombre, String fechaInicio, String fechaFin) {
		this.nombre = nombre;
		this.fecha_inicio = fechaInicio;
		this.fecha_fin = fechaFin;
	}

	/*Getters y setters de PeriodoGlobalDTO*/

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getFechaInicio() {
		return fecha_inicio;
	}

	public void setFechaInicio(String fechaInicio) {
		this.fecha_inicio = fechaInicio;
	}

	public String getFechaFin() {
		return fecha_fin;
	}

	public void setFechaFin(String fechaFin) {
		this.fecha_fin = fechaFin;
	}
	
}
