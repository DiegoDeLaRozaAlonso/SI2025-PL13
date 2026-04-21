package cd.admin.diego.cancelAct;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class ConfirmarCancelacionView {

	private JFrame frame;

	private JLabel lblNombreValor;
	private JLabel lblFechaInicioValor;
	private JLabel lblFechaFinValor;
	private JLabel lblInstalacionValor;
	private JLabel lblAforoValor;
	private JLabel lblInscritosValor;

	private JTable tablaAfectados;
	private DefaultTableModel modeloTablaAfectados;

	private JTextArea txtMotivo;

	private JButton btnCancelar;
	private JButton btnContinuar;

	public ConfirmarCancelacionView() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Confirmar cancelación de actividad");
		frame.setBounds(120, 120, 900, 520);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());

		JPanel panelDatos = new JPanel(new GridLayout(3, 4, 10, 10));
		panelDatos.setBorder(new EmptyBorder(10, 10, 10, 10));

		panelDatos.add(new JLabel("Actividad:"));
		lblNombreValor = new JLabel("");
		panelDatos.add(lblNombreValor);

		panelDatos.add(new JLabel("Instalación:"));
		lblInstalacionValor = new JLabel("");
		panelDatos.add(lblInstalacionValor);

		panelDatos.add(new JLabel("Fecha inicio:"));
		lblFechaInicioValor = new JLabel("");
		panelDatos.add(lblFechaInicioValor);

		panelDatos.add(new JLabel("Fecha fin:"));
		lblFechaFinValor = new JLabel("");
		panelDatos.add(lblFechaFinValor);

		panelDatos.add(new JLabel("Aforo:"));
		lblAforoValor = new JLabel("");
		panelDatos.add(lblAforoValor);

		panelDatos.add(new JLabel("Inscritos afectados:"));
		lblInscritosValor = new JLabel("");
		panelDatos.add(lblInscritosValor);

		frame.getContentPane().add(panelDatos, BorderLayout.NORTH);

		modeloTablaAfectados = new DefaultTableModel(
			new Object[][] {},
			new String[] { "Nombre", "Tipo", "Email", "DNI", "Pagado" }
		) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tablaAfectados = new JTable(modeloTablaAfectados);
		tablaAfectados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablaAfectados.getTableHeader().setReorderingAllowed(false);

		JScrollPane scrollTabla = new JScrollPane(tablaAfectados);
		scrollTabla.setPreferredSize(new Dimension(700, 220));

		JPanel panelCentro = new JPanel(new BorderLayout());
		panelCentro.setBorder(new EmptyBorder(0, 10, 10, 10));
		panelCentro.add(scrollTabla, BorderLayout.CENTER);

		JPanel panelMotivo = new JPanel(new BorderLayout());
		panelMotivo.setBorder(new EmptyBorder(0, 10, 10, 10));
		panelMotivo.add(new JLabel("Motivo de la cancelación:"), BorderLayout.NORTH);

		txtMotivo = new JTextArea(5, 20);
		txtMotivo.setLineWrap(true);
		txtMotivo.setWrapStyleWord(true);

		JScrollPane scrollMotivo = new JScrollPane(txtMotivo);
		panelMotivo.add(scrollMotivo, BorderLayout.CENTER);

		panelCentro.add(panelMotivo, BorderLayout.SOUTH);

		frame.getContentPane().add(panelCentro, BorderLayout.CENTER);

		JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btnCancelar = new JButton("Cancelar");
		btnContinuar = new JButton("Continuar");

		panelInferior.add(btnCancelar);
		panelInferior.add(btnContinuar);

		frame.getContentPane().add(panelInferior, BorderLayout.SOUTH);
	}

	public JFrame getFrame() {
		return frame;
	}

	public JLabel getLblNombreValor() {
		return lblNombreValor;
	}

	public JLabel getLblFechaInicioValor() {
		return lblFechaInicioValor;
	}

	public JLabel getLblFechaFinValor() {
		return lblFechaFinValor;
	}

	public JLabel getLblInstalacionValor() {
		return lblInstalacionValor;
	}

	public JLabel getLblAforoValor() {
		return lblAforoValor;
	}

	public JLabel getLblInscritosValor() {
		return lblInscritosValor;
	}

	public JTable getTablaAfectados() {
		return tablaAfectados;
	}

	public DefaultTableModel getModeloTablaAfectados() {
		return modeloTablaAfectados;
	}

	public JTextArea getTxtMotivo() {
		return txtMotivo;
	}

	public JButton getBtnCancelar() {
		return btnCancelar;
	}

	public JButton getBtnContinuar() {
		return btnContinuar;
	}
}