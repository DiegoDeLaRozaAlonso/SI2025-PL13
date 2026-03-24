package cd.socio.pablo.inscripcionActividad;

public class SocioDTO {
	
	private int id;
	private String nombre;
	private boolean debe_dinero;
	
	public SocioDTO(int id, String nombre) {
		super();
		this.id = id;
		this.nombre = nombre;
	}
	
	public SocioDTO() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
