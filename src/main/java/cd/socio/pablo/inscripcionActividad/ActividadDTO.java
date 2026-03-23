package cd.socio.pablo.inscripcionActividad;

import java.util.Date;

public class ActividadDTO {
	
	private int id_actividad;
	private String nombre;
	private String descripcion;
	private int aforo;
	private String fecha_inicio;
	private String fecha_fin;
	private double precioSocio;
	private double precioNoSocio;
	private String fecha_inicio_periodo;
	private String fecha_fin_periodo;
	
	//Necesario parque el DButil funcione
	public ActividadDTO() {
		
	}
	
	/*Getters y setters de la clase ActividadDTO*/
	
	

	public String getNombre() {return nombre;}

	public String getFecha_inicio_periodo() {
		return fecha_inicio_periodo;
	}

	public void setFecha_inicio_periodo(String fecha_inicio_periodo) {
		this.fecha_inicio_periodo = fecha_inicio_periodo;
	}

	
	public int getId_actividad() {
		return id_actividad;
	}

	public int getId() {return id_actividad;}

	public void setId_actividad(int id_actividad) {
		this.id_actividad = id_actividad;
	}

	public void setNombre(String nombre) {this.nombre = nombre;}

	public String getDescripcion() {return descripcion;}

	public void setDescripcion(String desc) {this.descripcion = desc;}

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

	public String getFecha_fin_periodo() {
		return fecha_fin_periodo;
	}

	public void setFecha_fin_periodo(String fecha_fin_periodo) {
		this.fecha_fin_periodo = fecha_fin_periodo;
	}

}
