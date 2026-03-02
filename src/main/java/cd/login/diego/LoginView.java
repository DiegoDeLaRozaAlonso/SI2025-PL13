package cd.login.diego;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;

public class LoginView {

	private JDialog dialog;
	private JTextField txtUsuario;
	private JPasswordField txtPassword;
	private JButton btnEntrar;
	private JButton btnCancelar;

	public LoginView(JFrame parent) {

		dialog = new JDialog(parent, "Login", true);
		dialog.setSize(300, 220);
		dialog.setLocationRelativeTo(parent);
		dialog.setLayout(new MigLayout("fillx,insets 15", "[grow]", "[][][][][]"));

		dialog.add(new JLabel("Usuario:"), "wrap");
		txtUsuario = new JTextField();
		dialog.add(txtUsuario, "growx,wrap");

		dialog.add(new JLabel("Contraseña:"), "wrap");
		txtPassword = new JPasswordField();
		dialog.add(txtPassword, "growx,wrap");

		JPanel panelBotones = new JPanel();
		btnEntrar = new JButton("Entrar");
		btnCancelar = new JButton("Cancelar");
		panelBotones.add(btnEntrar);
		panelBotones.add(btnCancelar);

		dialog.add(panelBotones, "align center,wrap");
	}

	public void show() {
		dialog.setVisible(true);
	}

	public void close() {
		dialog.dispose();
	}

	public JTextField getTxtUsuario() { return txtUsuario; }
	public JPasswordField getTxtPassword() { return txtPassword; }
	public JButton getBtnEntrar() { return btnEntrar; }
	public JButton getBtnCancelar() { return btnCancelar; }
	public JDialog getDialog() { return dialog; }
}