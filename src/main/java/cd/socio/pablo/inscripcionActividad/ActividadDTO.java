package cd.socio.pablo.inscripcionActividad;

import java.util.Date;

public class ActividadDTO {
	
	private int id;
	private String nombre;
	private String desc;
	private int aforo;
	private Date fecha_inicio;
	private Date fecha_fin;
	private double precioSocio;
	private double precioNoSocio;
	private Date fecha_fin_periodo;
	
	
	//Necesario parque el DButil funcione
	public ActividadDTO() {
		
	}
	
	/*Getters y setters de la clase ActividadDTO*/
	
	

	public String getNombre() {return nombre;}

	public int getId() {return id;}

	public void setNombre(String nombre) {this.nombre = nombre;}

	public String getDesc() {return desc;}

	public void setDesc(String desc) {this.desc = desc;}

	public int getAforo() {
		return aforo;
	}

	public void setAforo(int aforo) {
		this.aforo = aforo;
	}

	public Date getFecha_inicio() {
		return fecha_inicio;
	}

	public void setFecha_inicio(Date fecha_inicio) {
		this.fecha_inicio = fecha_inicio;
	}

	public Date getFecha_fin() {
		return fecha_fin;
	}

	public void setFecha_fin(Date fecha_fin) {
		this.fecha_fin = fecha_fin;
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

	public Date getFecha_fin_periodo() {
		return fecha_fin_periodo;
	}

	public void setFecha_fin_periodo(Date fecha_fin_periodo) {
		this.fecha_fin_periodo = fecha_fin_periodo;
	}
	

}
