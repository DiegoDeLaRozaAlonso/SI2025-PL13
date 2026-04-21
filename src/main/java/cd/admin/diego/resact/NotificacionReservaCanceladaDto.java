package cd.admin.diego.resact;

public class NotificacionReservaCanceladaDto {

	private int idReserva;
	private int idSocio;
	private String nombreSocio;
	private String instalacion;
	private String fecha;
	private String hora;
	private String actividadNueva;
	private boolean pagada;
	private double importe;

	public int getIdReserva() {
		return idReserva;
	}

	public void setIdReserva(int idReserva) {
		this.idReserva = idReserva;
	}

	public int getIdSocio() {
		return idSocio;
	}

	public void setIdSocio(int idSocio) {
		this.idSocio = idSocio;
	}

	public String getNombreSocio() {
		return nombreSocio;
	}

	public void setNombreSocio(String nombreSocio) {
		this.nombreSocio = nombreSocio;
	}

	public String getInstalacion() {
		return instalacion;
	}

	public void setInstalacion(String instalacion) {
		this.instalacion = instalacion;
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

	public String getActividadNueva() {
		return actividadNueva;
	}

	public void setActividadNueva(String actividadNueva) {
		this.actividadNueva = actividadNueva;
	}

	public boolean isPagada() {
		return pagada;
	}

	public void setPagada(boolean pagada) {
		this.pagada = pagada;
	}

	public double getImporte() {
		return importe;
	}

	public void setImporte(double importe) {
		this.importe = importe;
	}
}