package cd.socio.pablo.inscripcionActividad;

public class SocioDTO {
	
	private int id_socio;
	private String nombre;
	private boolean debe_dinero;
	
	public SocioDTO(int id, String nombre) {
		super();
		this.id_socio = id;
		this.nombre = nombre;
	}
	
	public SocioDTO() {
		
	}

	public int getId_socio() {
		return id_socio;
	}

	public void setId_socio(int id) {
		this.id_socio = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public boolean isDebeDinero() {
		return debe_dinero;
	}

	public void setDebeDinero(boolean debeDinero) {
		this.debe_dinero = debeDinero;
	}

	@Override
	public String toString() {
		return nombre;
	}
	
	
}
