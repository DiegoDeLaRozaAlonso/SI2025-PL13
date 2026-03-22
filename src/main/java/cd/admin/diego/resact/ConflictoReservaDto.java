package cd.admin.diego.resact;

public class ConflictoReservaDto {

	private String fecha;
	private String hora;
	private String reservaEnConflicto;

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

	public String getReservaEnConflicto() {
		return reservaEnConflicto;
	}

	public void setReservaEnConflicto(String reservaEnConflicto) {
		this.reservaEnConflicto = reservaEnConflicto;
	}
}