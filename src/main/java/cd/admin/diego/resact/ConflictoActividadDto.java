package cd.admin.diego.resact;

public class ConflictoActividadDto {

	private int idActividadConflicto;
	private String fecha;
	private String hora;
	private String actividadEnConflicto;
	private boolean prioridad;

	public int getIdActividadConflicto() {
		return idActividadConflicto;
	}

	public void setIdActividadConflicto(int idActividadConflicto) {
		this.idActividadConflicto = idActividadConflicto;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getActividadEnConflicto() {
		return actividadEnConflicto;
	}

	public void setActividadEnConflicto(String actividadEnConflicto) {
		this.actividadEnConflicto = actividadEnConflicto;
	}

	public boolean isPrioridad() {
		return prioridad;
	}

	public void setPrioridad(boolean prioridad) {
		this.prioridad = prioridad;
	}
}