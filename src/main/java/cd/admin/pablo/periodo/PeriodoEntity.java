package cd.admin.pablo.periodo;

import java.sql.Date;

public class PeriodoEntity {

	private String nombre;
	private String descripcion;
	private Date fechaInicio;
	private Date fechaFinSocio;
	private Date fechaFinNoSocio;


	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFinSocio() {
		return fechaFinSocio;
	}

	public void setFechaFinSocio(Date fechaFinSocio) {
		this.fechaFinSocio = fechaFinSocio;
	}

	public Date getFechaFinNoSocio() {
		return fechaFinNoSocio;
	}

	public void setFechaFinNoSocio(Date fechaFinNoSocio) {
		this.fechaFinNoSocio = fechaFinNoSocio;
	}
}
