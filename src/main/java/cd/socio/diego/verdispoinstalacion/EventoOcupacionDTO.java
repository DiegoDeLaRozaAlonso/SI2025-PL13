package cd.socio.diego.verdispoinstalacion;

public class EventoOcupacionDTO {
	private String horaInicio;
	private String horaFin;
	private String detalle;

	// NUEVO: para marcar si una reserva pertenece al socio logueado
	private Integer idSocio; // null para eventos que no sean reservas

	public String getHoraInicio() { return horaInicio; }
	public void setHoraInicio(String horaInicio) { this.horaInicio = horaInicio; }

	public String getHoraFin() { return horaFin; }
	public void setHoraFin(String horaFin) { this.horaFin = horaFin; }

	public String getDetalle() { return detalle; }
	public void setDetalle(String detalle) { this.detalle = detalle; }

	public Integer getIdSocio() { return idSocio; }
	public void setIdSocio(Integer idSocio) { this.idSocio = idSocio; }
}