package cd.socio.pablo.inscripcionActividad;

import java.util.Date;

public class InscripcionDTO {
	
	private int id_actividad;
	private int id_socio;
	private Date fecha_inscripcion;
	private String estado;
	private boolean pagado;
	private String tipo;
	
	public InscripcionDTO() {}

	public InscripcionDTO(int id_actividad, int id_socio, Date fecha_inscripcion,
			String estado, boolean pagado, String tipo) {
		this.id_actividad = id_actividad;
		this.id_socio = id_socio;
		this.fecha_inscripcion = fecha_inscripcion;
		this.estado = estado;
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


	public Date getFecha_inscripcion() {
		return fecha_inscripcion;
	}

	public void setFecha_inscripcion(Date fecha_inscripcion) {
		this.fecha_inscripcion = fecha_inscripcion;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
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
	
}
