package cd.admin.diego.informeactividad;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

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

import com.toedter.calendar.JDateChooser;

public class InformeActividadView {

	private JFrame frame;
	private JTable tablaInforme;
	private DefaultTableModel modeloTabla;

	private JRadioButton rbDosFechas;
	private JRadioButton rbAnho;
	private JRadioButton rbPeriodo;

	private JDateChooser dcFechaInicio;
	private JDateChooser dcFechaFin;

	private JComboBox<Integer> cbAnhos;
	private JComboBox<PeriodoDTO> cbPeriodos;
	private JComboBox<ActividadDTO> cbActividades;

	private JButton btnCancelar;
	private JButton btnGenerarInformeTxt;

	public InformeActividadView() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Informe actividades");
		frame.setBounds(50, 50, 1400, 650);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());

		JPanel panelSuperiorContenedor = new JPanel(new BorderLayout());

		JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel lblTitulo = new JLabel("Selecciona el filtro para generar el informe.");
		lblTitulo.setFont(new Font("SansSerif", Font.PLAIN, 18));
		panelTitulo.add(lblTitulo);
		panelSuperiorContenedor.add(panelTitulo, BorderLayout.NORTH);

		JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));

		panelFiltros.add(new JLabel("Actividad:"));
		cbActividades = new JComboBox<>();
		cbActividades.setPreferredSize(new Dimension(220, 24));
		panelFiltros.add(cbActividades);

		panelFiltros.add(new JLabel("Filtrar por:"));

		rbDosFechas = new JRadioButton("2 fechas");
		rbAnho = new JRadioButton("Año");
		rbPeriodo = new JRadioButton("Periodo");

		ButtonGroup group = new ButtonGroup();
		group.add(rbDosFechas);
		group.add(rbAnho);
		group.add(rbPeriodo);

		rbDosFechas.setSelected(true);

		panelFiltros.add(rbDosFechas);
		panelFiltros.add(rbAnho);
		panelFiltros.add(rbPeriodo);

		panelFiltros.add(new JLabel("Inicio:"));
		dcFechaInicio = new JDateChooser();
		dcFechaInicio.setDateFormatString("dd/MM/yyyy");
		dcFechaInicio.setToolTipText("Formato: dd/MM/yyyy");
		dcFechaInicio.setPreferredSize(new Dimension(130, 24));
		panelFiltros.add(dcFechaInicio);

		panelFiltros.add(new JLabel("Fin:"));
		dcFechaFin = new JDateChooser();
		dcFechaFin.setDateFormatString("dd/MM/yyyy");
		dcFechaFin.setToolTipText("Formato: dd/MM/yyyy");
		dcFechaFin.setPreferredSize(new Dimension(130, 24));
		panelFiltros.add(dcFechaFin);

		panelFiltros.add(new JLabel("Año:"));
		cbAnhos = new JComboBox<>();
		cbAnhos.setPreferredSize(new Dimension(100, 24));
		panelFiltros.add(cbAnhos);

		panelFiltros.add(new JLabel("Periodo:"));
		cbPeriodos = new JComboBox<>();
		cbPeriodos.setPreferredSize(new Dimension(200, 24));
		panelFiltros.add(cbPeriodos);

		panelSuperiorContenedor.add(panelFiltros, BorderLayout.CENTER);
		frame.getContentPane().add(panelSuperiorContenedor, BorderLayout.NORTH);

		modeloTabla = new DefaultTableModel(
			new Object[][] {},
			new String[] { "Nombre", "Edición", "Plazas", "Inscritos", "% Ocupación", "En lista espera" }
		) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tablaInforme = new JTable(modeloTabla);
		tablaInforme.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablaInforme.getTableHeader().setReorderingAllowed(false);
		tablaInforme.setRowHeight(24);

		JScrollPane scrollPane = new JScrollPane(tablaInforme);
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

		JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		btnCancelar = new JButton("Cancelar");
		btnGenerarInformeTxt = new JButton("Generar informe txt");

		panelInferior.add(btnCancelar);
		panelInferior.add(btnGenerarInformeTxt);

		frame.getContentPane().add(panelInferior, BorderLayout.SOUTH);
	}

	public JFrame getFrame() {
		return frame;
	}

	public JTable getTablaInforme() {
		return tablaInforme;
	}

	public DefaultTableModel getModeloTabla() {
		return modeloTabla;
	}

	public JRadioButton getRbDosFechas() {
		return rbDosFechas;
	}

	public JRadioButton getRbAnho() {
		return rbAnho;
	}

	public JRadioButton getRbPeriodo() {
		return rbPeriodo;
	}

	public JDateChooser getDcFechaInicio() {
		return dcFechaInicio;
	}

	public JDateChooser getDcFechaFin() {
		return dcFechaFin;
	}

	public JComboBox<Integer> getCbAnhos() {
		return cbAnhos;
	}

	public JComboBox<PeriodoDTO> getCbPeriodos() {
		return cbPeriodos;
	}

	public JComboBox<ActividadDTO> getCbActividades() {
		return cbActividades;
	}

	public JButton getBtnCancelar() {
		return btnCancelar;
	}

	public JButton getBtnGenerarInformeTxt() {
		return btnGenerarInformeTxt;
	}
}