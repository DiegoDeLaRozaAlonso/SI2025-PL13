package cd.login.diego;

import giis.demo.util.SwingUtil;

public class LoginController {

	private LoginModel model;
	private LoginView view;
	private UsuarioSesion sesion;

	public LoginController(LoginModel m, LoginView v) {
		this.model = m;
		this.view = v;
	}

	public UsuarioSesion mostrarLogin() {
		initController();
		view.show();
		return sesion;
	}

	private void initController() {

		// ENTER en contraseña
		view.getTxtPassword().addActionListener(e ->
			SwingUtil.exceptionWrapper(this::login)
		);

		// Botón Entrar
		view.getBtnEntrar().addActionListener(e ->
			SwingUtil.exceptionWrapper(this::login)
		);

		// Botón Cancelar (igual que cerrar)
		view.getBtnCancelar().addActionListener(e -> {
			sesion = null;
			view.close();
		});
	}

	private void login() {

		String usuario = view.getTxtUsuario().getText().trim();
		String pass = new String(view.getTxtPassword().getPassword());

		UsuarioSesion u = model.autenticar(usuario, pass);

		if (u == null)
			return;

		sesion = u;
		view.close();
	}
}