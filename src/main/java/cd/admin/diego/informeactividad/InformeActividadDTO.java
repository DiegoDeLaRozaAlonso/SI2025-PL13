package cd.admin.diego.informeactividad;

public class InformeActividadDTO {

	private String nombre;
	private int edicion;
	private int plazas;
	private int inscritos;
	private double porcentajeOcupacion;
	private int enListaEspera;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getEdicion() {
		return edicion;
	}

	public void setEdicion(int edicion) {
		this.edicion = edicion;
	}

	public int getPlazas() {
		return plazas;
	}

	public void setPlazas(int plazas) {
		this.plazas = plazas;
	}

	public int getInscritos() {
		return inscritos;
	}

	public void setInscritos(int inscritos) {
		this.inscritos = inscritos;
	}

	public double getPorcentajeOcupacion() {
		return porcentajeOcupacion;
	}

	public void setPorcentajeOcupacion(double porcentajeOcupacion) {
		this.porcentajeOcupacion = porcentajeOcupacion;
	}

	public int getEnListaEspera() {
		return enListaEspera;
	}

	public void setEnListaEspera(int enListaEspera) {
		this.enListaEspera = enListaEspera;
	}
}