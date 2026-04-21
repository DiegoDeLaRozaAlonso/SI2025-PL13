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
import javax.swing.table.DefaultTableModel;

public class ResActConflictosView {

	private JFrame frame;
	private JTable tableActividades;
	private JTable tableReservas;
	private DefaultTableModel modelActividades;
	private DefaultTableModel modelReservas;
	private JButton btnVolver;
	private JButton btnAceptar;
	private JLabel lblTituloActividad;
	private List<ConflictoActividadDto> conflictosActividades;

	public ResActConflictosView() {
		this.conflictosActividades = new ArrayList<ConflictoActividadDto>();
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Resolución conflictos actividad");
		frame.setBounds(100, 100, 950, 650);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(10, 10));

		JPanel panelPrincipal = new JPanel();
		panelPrincipal.setLayout(new BorderLayout(10, 10));
		frame.getContentPane().add(panelPrincipal, BorderLayout.CENTER);

		JPanel panelCentro = new JPanel();
		panelCentro.setLayout(new javax.swing.BoxLayout(panelCentro, javax.swing.BoxLayout.Y_AXIS));
		panelPrincipal.add(panelCentro, BorderLayout.CENTER);

		JPanel panelCabecera = new JPanel(new FlowLayout(FlowLayout.LEFT));
		lblTituloActividad = new JLabel("Conflictos con actividades");
		panelCabecera.add(lblTituloActividad);
		panelCentro.add(panelCabecera);

		modelActividades = new DefaultTableModel(
				new Object[][] {},
				new String[] { "Fecha", "Hora", "Actividad en conflicto", "Prioridad" }) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 3;
			}

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				if (columnIndex == 3) {
					return Boolean.class;
				}
				return String.class;
			}
		};

		tableActividades = new JTable(modelActividades);
		tableActividades.setRowHeight(22);
		tableActividades.getTableHeader().setReorderingAllowed(false);

		JScrollPane scrollActividades = new JScrollPane(tableActividades);
		scrollActividades.setPreferredSize(new Dimension(850, 180));
		panelCentro.add(scrollActividades);

		JPanel panelSeparador = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelSeparador.add(new JLabel("Coincide con reserva de socio:"));
		panelCentro.add(panelSeparador);

		modelReservas = new DefaultTableModel(
				new Object[][] {},
				new String[] { "Fecha", "Hora", "Reserva en conflicto" }) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tableReservas = new JTable(modelReservas);
		tableReservas.setRowHeight(22);
		tableReservas.getTableHeader().setReorderingAllowed(false);

		JScrollPane scrollReservas = new JScrollPane(tableReservas);
		scrollReservas.setPreferredSize(new Dimension(850, 180));
		panelCentro.add(scrollReservas);

		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btnVolver = new JButton("Volver");
		btnAceptar = new JButton("Aceptar");
		panelBotones.add(btnVolver);
		panelBotones.add(btnAceptar);

		frame.getContentPane().add(panelBotones, BorderLayout.SOUTH);
	}

	public void setTituloActividad(String nombreActividad) {
		lblTituloActividad.setText("Conflictos con actividades - " + nombreActividad);
	}

	public void loadConflictosActividades(List<ConflictoActividadDto> datos) {
		this.conflictosActividades = new ArrayList<ConflictoActividadDto>(datos);
		modelActividades.setRowCount(0);

		for (ConflictoActividadDto dto : datos) {
			modelActividades.addRow(new Object[] {
				dto.getFecha(),
				dto.getHora(),
				dto.getActividadEnConflicto(),
				Boolean.valueOf(dto.isPrioridad())
			});
		}
	}

	public void loadConflictosReservas(List<ConflictoReservaDto> datos) {
		modelReservas.setRowCount(0);

		for (ConflictoReservaDto dto : datos) {
			modelReservas.addRow(new Object[] {
				dto.getFecha(),
				dto.getHora(),
				dto.getReservaEnConflicto()
			});
		}
	}

	public List<ConflictoActividadDto> getConflictosActividadesMarcados() {
		List<ConflictoActividadDto> res = new ArrayList<ConflictoActividadDto>();

		for (int i = 0; i < modelActividades.getRowCount(); i++) {
			Boolean marcado = (Boolean) modelActividades.getValueAt(i, 3);
			if (marcado != null && marcado.booleanValue()) {
				ConflictoActividadDto original = conflictosActividades.get(i);

				ConflictoActividadDto dto = new ConflictoActividadDto();
				dto.setIdActividadConflicto(original.getIdActividadConflicto());
				dto.setFecha(original.getFecha());
				dto.setHora(original.getHora());
				dto.setActividadEnConflicto(original.getActividadEnConflicto());
				dto.setPrioridad(true);
				res.add(dto);
			}
		}

		return res;
	}

	public JFrame getFrame() {
		return frame;
	}

	public JButton getBtnVolver() {
		return btnVolver;
	}

	public JButton getBtnAceptar() {
		return btnAceptar;
	}

	public void show() {
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public void close() {
		frame.dispose();
	}
}