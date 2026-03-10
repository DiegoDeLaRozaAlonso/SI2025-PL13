package cd.socio.pablo.inscripcionActividad;

import java.util.Date;

public class ActividadDTO {
	
	private int id;
	private String nombre;
	private String desc;
	private int aforo;
	private String fecha_inicio;
	private String fecha_fin;
	private double precioSocio;
	private double precioNoSocio;
	
	public ActividadDTO(String nombre, String desc, int aforo, String fechaInicio, String fechaFin, double precioSocio,
			double precioNoSocio) {
		this.nombre = nombre;
		this.desc = desc;
		this.aforo = aforo;
		this.fecha_inicio = fechaInicio;
		this.fecha_fin = fechaFin;
		this.precioSocio = precioSocio;
		this.precioNoSocio = precioNoSocio;
	}
	
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

	public String getFecha_inicio() {
		return fecha_inicio;
	}

	public void setFecha_inicio(String fecha_inicio) {
		this.fecha_inicio = fecha_inicio;
	}

	public String getFecha_fin() {
		return fecha_fin;
	}

	public void setFecha_fin(String fecha_fin) {
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
	
	
	

}
