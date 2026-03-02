package cd.login.diego;

public class UsuarioSesion {

	private int id;
	private String nombre;
	private boolean admin;

	public UsuarioSesion(int id, String nombre, boolean admin) {
		this.id = id;
		this.nombre = nombre;
		this.admin = admin;
	}

	public int getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public boolean isAdmin() {
		return admin;
	}
}