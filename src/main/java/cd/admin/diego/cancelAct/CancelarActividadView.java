package cd.admin.diego.cancelAct;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class CancelarActividadView {

	private JFrame frame;
	private JTable tablaActividades;
	private DefaultTableModel modeloTabla;

	private JComboBox<String> cbActividad;
	private JRadioButton rbActivas;
	private JRadioButton rbFuturas;

	private JButton btnFiltrar;
	private JButton btnVolver;

	public CancelarActividadView() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Cancelar actividad");
		frame.setBounds(100, 100, 950, 500);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());

		JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));

		panelSuperior.add(new JLabel("Actividad"));
		cbActividad = new JComboBox<>();
		cbActividad.setPreferredSize(new Dimension(220, 25));
		panelSuperior.add(cbActividad);

		rbActivas = new JRadioButton("Activas");
		rbFuturas = new JRadioButton("Futuras");
		rbActivas.setSelected(true);

		ButtonGroup bg = new ButtonGroup();
		bg.add(rbActivas);
		bg.add(rbFuturas);

		panelSuperior.add(rbActivas);
		panelSuperior.add(rbFuturas);

		btnFiltrar = new JButton("Filtrar");
		panelSuperior.add(btnFiltrar);

		frame.getContentPane().add(panelSuperior, BorderLayout.NORTH);

		modeloTabla = new DefaultTableModel(
			new Object[][] {},
			new String[] { "Id", "Nombre", "Instalación", "Fecha inicio", "Fecha fin", "Afectados" }
		) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tablaActividades = new JTable(modeloTabla);
		tablaActividades.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablaActividades.getTableHeader().setReorderingAllowed(false);
		tablaActividades.getColumnModel().getColumn(0).setMinWidth(0);
		tablaActividades.getColumnModel().getColumn(0).setMaxWidth(0);
		tablaActividades.getColumnModel().getColumn(0).setWidth(0);

		JScrollPane scrollPane = new JScrollPane(tablaActividades);
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

		JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btnVolver = new JButton("Volver");
		panelInferior.add(btnVolver);

		frame.getContentPane().add(panelInferior, BorderLayout.SOUTH);
	}

	public JFrame getFrame() {
		return frame;
	}

	public JTable getTablaActividades() {
		return tablaActividades;
	}

	public DefaultTableModel getModeloTabla() {
		return modeloTabla;
	}

	public JComboBox<String> getCbActividad() {
		return cbActividad;
	}

	public JRadioButton getRbActivas() {
		return rbActivas;
	}

	public JRadioButton getRbFuturas() {
		return rbFuturas;
	}

	public JButton getBtnFiltrar() {
		return btnFiltrar;
	}

	public JButton getBtnVolver() {
		return btnVolver;
	}
}