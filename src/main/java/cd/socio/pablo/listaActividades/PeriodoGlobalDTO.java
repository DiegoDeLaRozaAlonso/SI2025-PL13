package cd.socio.pablo.listaActividades;

public class PeriodoGlobalDTO {
	
	private String nombre;
	private String fecha_inicio;
	private String fecha_fin;

	
	/*COnstructor vació necesario para la clase DataBase*/
	public PeriodoGlobalDTO() {
		
	}

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

	public String getFecha_inicio() {
		return fecha_inicio;
	}

	public void setFecha_inicio(String fechaInicio) {
		this.fecha_inicio = fechaInicio;
	}

	public String getFecha_fin() {
		return fecha_fin;
	}

	public void setFecha_fin(String fechaFin) {
		this.fecha_fin = fechaFin;
	}

	@Override
	public String toString() {
		return this.nombre;
	}
	
	
	
}
