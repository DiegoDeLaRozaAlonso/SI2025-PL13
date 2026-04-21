package cd.admin.diego.resact;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class ResActView {

	private JFrame frame;
	private JTable table;
	private DefaultTableModel tableModel;
	private JButton btnActualizar;
	private JButton btnAtras;
	private List<ActividadSinReservaDto> actividades;

	public ResActView() {
		this.actividades = new ArrayList<ActividadSinReservaDto>();
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Lista actividades sin reserva automática");
		frame.setBounds(100, 100, 900, 500);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(10, 10));

		JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel lblTitulo = new JLabel("Actividades sin reserva:");
		panelSuperior.add(lblTitulo);
		frame.getContentPane().add(panelSuperior, BorderLayout.NORTH);

		tableModel = new DefaultTableModel(
				new Object[][] {},
				new String[] { "Nombre", "Descripción", "Instalación", "Fecha ini", "Fecha fin" }) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table = new JTable(tableModel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowHeight(22);
		table.getTableHeader().setReorderingAllowed(false);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(850, 320));
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

		JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btnActualizar = new JButton("Actualizar");
		btnAtras = new JButton("Atrás");
		panelInferior.add(btnActualizar);
		panelInferior.add(btnAtras);
		frame.getContentPane().add(panelInferior, BorderLayout.SOUTH);
	}

	public void loadTable(List<ActividadSinReservaDto> datos) {
		this.actividades = new ArrayList<ActividadSinReservaDto>(datos);
		tableModel.setRowCount(0);

		for (ActividadSinReservaDto r : datos) {
			tableModel.addRow(new Object[] {
				r.getNombre(),
				r.getDescripcion(),
				r.getInstalacion(),
				r.getFechaInicio(),
				r.getFechaFin()
			});
		}
	}

	public ActividadSinReservaDto getActividadSeleccionada() {
		int fila = table.getSelectedRow();
		if (fila < 0 || fila >= actividades.size()) {
			return null;
		}
		return actividades.get(fila);
	}

	public JFrame getFrame() {
		return frame;
	}

	public JTable getTable() {
		return table;
	}

	public JButton getBtnActualizar() {
		return btnActualizar;
	}

	public JButton getBtnAtras() {
		return btnAtras;
	}

	public void show() {
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public void close() {
		frame.dispose();
	}
}