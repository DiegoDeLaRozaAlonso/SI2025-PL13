package cd.socio.diego.misReservas;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

public class MisReservasView {

	private JFrame frame;
	private JTable tablaReservas;
	private DefaultTableModel modeloTabla;

	private JDateChooser dcFechaInicio;
	private JDateChooser dcFechaFin;

	private JRadioButton rbTodas;
	private JRadioButton rbPasadas;
	private JRadioButton rbActivas;

	private JButton btnFiltrar;
	private JButton btnVolver;
	private JButton btnDescargar;

	public MisReservasView() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Mis reservas");
		frame.setBounds(100, 100, 900, 500);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());

		JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));

		panelSuperior.add(new JLabel("Fecha inicio"));
		dcFechaInicio = new JDateChooser();
		dcFechaInicio.setDateFormatString("dd/MM/yyyy");
		dcFechaInicio.setToolTipText("Formato: dd/MM/yyyy");
		dcFechaInicio.setPreferredSize(new Dimension(120, 24));
		panelSuperior.add(dcFechaInicio);

		panelSuperior.add(new JLabel("Fecha fin"));
		dcFechaFin = new JDateChooser();
		dcFechaFin.setDateFormatString("dd/MM/yyyy");
		dcFechaFin.setToolTipText("Formato: dd/MM/yyyy");
		dcFechaFin.setPreferredSize(new Dimension(120, 24));
		panelSuperior.add(dcFechaFin);

		rbTodas = new JRadioButton("Todas");
		rbPasadas = new JRadioButton("Pasadas");
		rbActivas = new JRadioButton("Activas");
		rbTodas.setSelected(true);

		ButtonGroup group = new ButtonGroup();
		group.add(rbTodas);
		group.add(rbPasadas);
		group.add(rbActivas);

		panelSuperior.add(rbTodas);
		panelSuperior.add(rbPasadas);
		panelSuperior.add(rbActivas);

		btnFiltrar = new JButton("Filtrar");
		panelSuperior.add(btnFiltrar);

		frame.getContentPane().add(panelSuperior, BorderLayout.NORTH);

		modeloTabla = new DefaultTableModel(
			new Object[][] {},
			new String[] {"Fecha", "Hora", "Instalación", "Duración", "Precio (€)", "Pagado"}
		) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tablaReservas = new JTable(modeloTabla);
		tablaReservas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablaReservas.getTableHeader().setReorderingAllowed(false);

		JScrollPane scrollPane = new JScrollPane(tablaReservas);
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

		JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		btnVolver = new JButton("Volver");
		btnDescargar = new JButton("Descargar");

		panelInferior.add(btnVolver);
		panelInferior.add(btnDescargar);

		frame.getContentPane().add(panelInferior, BorderLayout.SOUTH);
	}

	public JFrame getFrame() {
		return frame;
	}

	public JTable getTablaReservas() {
		return tablaReservas;
	}

	public DefaultTableModel getModeloTabla() {
		return modeloTabla;
	}

	public JDateChooser getDcFechaInicio() {
		return dcFechaInicio;
	}

	public JDateChooser getDcFechaFin() {
		return dcFechaFin;
	}

	public JRadioButton getRbTodas() {
		return rbTodas;
	}

	public JRadioButton getRbPasadas() {
		return rbPasadas;
	}

	public JRadioButton getRbActivas() {
		return rbActivas;
	}

	public JButton getBtnFiltrar() {
		return btnFiltrar;
	}

	public JButton getBtnVolver() {
		return btnVolver;
	}

	public JButton getBtnDescargar() {
		return btnDescargar;
	}
}