package cd.login.diego;

import java.util.List;
import giis.demo.util.Database;

public class LoginModel {

	private Database db = new Database();

	public UsuarioSesion autenticar(String usuario, String password) {

		String sql = "SELECT id_socio AS id, nombre AS nombre, es_admin AS admin "
				+ "FROM Socios WHERE nombre = ? AND contrasena = ?";

		List<LoginRow> res = db.executeQueryPojo(LoginRow.class, sql, usuario, password);

		if (res.isEmpty())
			return null;

		LoginRow r = res.get(0);
		return new UsuarioSesion(r.getId(), r.getNombre(), r.getAdmin() == 1);
	}

	public static class LoginRow {
		private int id;
		private String nombre;
		private int admin;

		public int getId() { return id; }
		public void setId(int id) { this.id = id; }

		public String getNombre() { return nombre; }
		public void setNombre(String nombre) { this.nombre = nombre; }

		public int getAdmin() { return admin; }
		public void setAdmin(int admin) { this.admin = admin; }
	}
}