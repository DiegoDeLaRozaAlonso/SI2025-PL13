package cd.admin.pablo.listaPeriodo;

import java.util.Date;

public class ActividadDTO {
	
	private String nombre;
	private String desc;
	private int aforo;
	private Date fechaInicio;
	private Date fechaFin;
	private double precioSocio;
	private double precioNoSocio;
	
	public ActividadDTO(String nombre, String desc, int aforo, Date fechaInicio, Date fechaFin, double precioSocio,
			double precioNoSocio) {
		this.nombre = nombre;
		this.desc = desc;
		this.aforo = aforo;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.precioSocio = precioSocio;
		this.precioNoSocio = precioNoSocio;
	}

	public String getNombre() {return nombre;}

	public void setNombre(String nombre) {this.nombre = nombre;}

	public String getDesc() {return desc;}

	public void setDesc(String desc) {this.desc = desc;}

	public int getAforo() {
		return aforo;
	}

	public void setAforo(int aforo) {
		this.aforo = aforo;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public double getPrecioSocio() {
		return precioSocio;
	}

	public void setPrecioSocio(double precioSocio) {
		this.precioSocio = precioSocio;
	}

	public double getPrecioNoSocio() {
		return precioNoSocio;
	}

	public void setPrecioNoSocio(double precioNoSocio) {
		this.precioNoSocio = precioNoSocio;
	}
	
	
	

}
