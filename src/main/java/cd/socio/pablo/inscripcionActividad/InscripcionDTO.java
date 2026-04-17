package cd.socio.pablo.inscripcionActividad;

import java.util.Date;

public class InscripcionDTO {
	
	private int id_actividad;
	private int id_socio;
	private String fecha_inscripcion;
	private boolean pagado;
	private String tipo;
	private String nombre_no_socio;
	private String dni;
	
	public InscripcionDTO() {}

	public InscripcionDTO(int id_actividad, int id_socio, String fecha_inscripcion,
			boolean pagado, String tipo) {
		this.id_actividad = id_actividad;
		this.id_socio = id_socio;
		this.fecha_inscripcion = fecha_inscripcion;
		this.pagado = pagado;
		this.tipo = tipo;
	}
	
	public InscripcionDTO(int id_actividad, String nombre_no_socio, String dni, String fecha_inscripcion,
			boolean pagado, String tipo) {
		this.id_actividad = id_actividad;
		this.nombre_no_socio = nombre_no_socio;
		this.fecha_inscripcion = fecha_inscripcion;
		this.pagado = pagado;
		this.tipo = tipo;
	}


	public int getId_actividad() {
		return id_actividad;
	}

	public void setId_actividad(int id_actividad) {
		this.id_actividad = id_actividad;
	}

	public int getId_socio() {
		return id_socio;
	}

	public void setId_socio(int id_socio) {
		this.id_socio = id_socio;
	}


	public String getFecha_inscripcion() {
		return fecha_inscripcion;
	}

	public void setFecha_inscripcion(String fecha_inscripcion) {
		this.fecha_inscripcion = fecha_inscripcion;
	}

	public boolean isPagado() {
		return pagado;
	}

	public void setPagado(boolean pagado) {
		this.pagado = pagado;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNombre_no_socio() {
		return nombre_no_socio;
	}

	public void setNombre_no_socio(String nombre_no_socio) {
		this.nombre_no_socio = nombre_no_socio;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}
	
}
