package cd.Administracion.Alejandro.Contabilidad;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

/**
 * Vista de la pantalla de contabilidad mensual (administracion).
 * Permite seleccionar un mes y formato (CSV/TXT), generar el informe
 * con la tabla de todos los socios y sus importes pendientes,
 * y descargar el fichero en el formato seleccionado.
 *
 * Sigue el patron MVC: no incluye logica de negocio ni manejadores de eventos.
 */
public class ContabilidadMensualView {

	private static final Color COLOR_HEADER_BG = new Color(220, 220, 220);
	private static final Color COLOR_BG        = new Color(240, 240, 240);

	private JFrame            frame;
	private JComboBox<String> cmbMes;
	private JButton           btnCSV;
	private JButton           btnTXT;
	private JButton           btnGenerar;
	private JButton           btnDescargar;
	private JTable            tabSocios;
	private JPanel            panelResultado;
	private JLabel            lblMeta;
	private JLabel            lblTotalGeneral;

	private String formatoActual = "CSV";

	public ContabilidadMensualView() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame("Contabilidad Mensual");
		frame.setName("ContabilidadMensual");
		frame.setBounds(0, 0, 800, 560);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		frame.getContentPane().setBackground(COLOR_BG);

		frame.getContentPane().add(buildPanelHeader(), BorderLayout.NORTH);
		frame.getContentPane().add(buildPanelCentro(), BorderLayout.CENTER);
	}

	// ── Cabecera ──────────────────────────────────────────────────────────────

	private JPanel buildPanelHeader() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(COLOR_HEADER_BG);
		panel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY),
				BorderFactory.createEmptyBorder(14, 20, 14, 20)));

		JPanel izq = new JPanel();
		izq.setOpaque(false);
		izq.setLayout(new BoxLayout(izq, BoxLayout.Y_AXIS));

		JLabel lblSub = new JLabel("ADMINISTRACIÓN");
		lblSub.setFont(new Font("SansSerif", Font.PLAIN, 11));
		lblSub.setForeground(Color.DARK_GRAY);

		JLabel lblTitulo = new JLabel("Contabilidad Mensual");
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
		lblTitulo.setForeground(Color.BLACK);

		izq.add(lblSub);
		izq.add(Box.createVerticalStrut(4));
		izq.add(lblTitulo);

		panel.add(izq, BorderLayout.CENTER);
		return panel;
	}

	// ── Centro: controles + tabla ─────────────────────────────────────────────

	private JPanel buildPanelCentro() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(COLOR_BG);
		panel.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

		panel.add(buildPanelControles());
		panel.add(Box.createVerticalStrut(14));
		panel.add(buildPanelResultado());

		return panel;
	}

	// ── Panel de controles ────────────────────────────────────────────────────

	private JPanel buildPanelControles() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.setBackground(COLOR_HEADER_BG);
		panel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.GRAY),
				BorderFactory.createEmptyBorder(12, 16, 12, 16)));
		panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);

		// Selector de mes
		JPanel subMes = new JPanel();
		subMes.setOpaque(false);
		subMes.setLayout(new BoxLayout(subMes, BoxLayout.Y_AXIS));

		JLabel lblMesLabel = new JLabel("MES");
		lblMesLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
		lblMesLabel.setForeground(Color.DARK_GRAY);

		String[] opciones = new String[ContabilidadMensualModel.NOMBRES_MESES.length];
		for (int i = 0; i < opciones.length; i++)
			opciones[i] = ContabilidadMensualModel.NOMBRES_MESES[i]
					+ " " + ContabilidadMensualModel.ANHO_BASE;
		cmbMes = new JComboBox<>(opciones);
		cmbMes.setName("cmbMes");
		cmbMes.setSelectedIndex(1); // Febrero por defecto
		cmbMes.setMaximumSize(new Dimension(180, 26));

		subMes.add(lblMesLabel);
		subMes.add(Box.createVerticalStrut(4));
		subMes.add(cmbMes);

		// Toggle CSV / TXT
		JPanel subFormato = new JPanel();
		subFormato.setOpaque(false);
		subFormato.setLayout(new BoxLayout(subFormato, BoxLayout.Y_AXIS));

		JLabel lblFormatoLabel = new JLabel("FORMATO");
		lblFormatoLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
		lblFormatoLabel.setForeground(Color.DARK_GRAY);

		JPanel toggleWrap = new JPanel(new java.awt.GridLayout(1, 2));
		toggleWrap.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		toggleWrap.setMaximumSize(new Dimension(120, 26));

		btnCSV = new JButton("CSV");
		btnCSV.setName("btnCSV");
		btnCSV.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnCSV.setBackground(Color.DARK_GRAY);
		btnCSV.setForeground(Color.WHITE);
		btnCSV.setBorderPainted(false);
		btnCSV.setFocusPainted(false);

		btnTXT = new JButton("TXT");
		btnTXT.setName("btnTXT");
		btnTXT.setFont(new Font("SansSerif", Font.PLAIN, 12));
		btnTXT.setBackground(Color.LIGHT_GRAY);
		btnTXT.setForeground(Color.DARK_GRAY);
		btnTXT.setBorderPainted(false);
		btnTXT.setFocusPainted(false);

		toggleWrap.add(btnCSV);
		toggleWrap.add(btnTXT);

		subFormato.add(lblFormatoLabel);
		subFormato.add(Box.createVerticalStrut(4));
		subFormato.add(toggleWrap);

		// Botón generar
		btnGenerar = new JButton("Generar Informe");
		btnGenerar.setName("btnGenerar");
		btnGenerar.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnGenerar.setAlignmentY(Component.BOTTOM_ALIGNMENT);

		// Botón descargar (oculto hasta generar)
		btnDescargar = new JButton("↓ Descargar CSV");
		btnDescargar.setName("btnDescargar");
		btnDescargar.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnDescargar.setVisible(false);
		btnDescargar.setAlignmentY(Component.BOTTOM_ALIGNMENT);

		panel.add(subMes);
		panel.add(Box.createHorizontalStrut(20));
		panel.add(subFormato);
		panel.add(Box.createHorizontalStrut(20));
		panel.add(btnGenerar);
		panel.add(Box.createHorizontalStrut(10));
		panel.add(btnDescargar);

		return panel;
	}

	// ── Panel de resultado ────────────────────────────────────────────────────

	private JPanel buildPanelResultado() {
		panelResultado = new JPanel();
		panelResultado.setLayout(new BoxLayout(panelResultado, BoxLayout.Y_AXIS));
		panelResultado.setBackground(COLOR_BG);
		panelResultado.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelResultado.setVisible(false);

		// Tabla estilo Swing por defecto
		tabSocios = new JTable();
		tabSocios.setName("tabSocios");
		tabSocios.setRowHeight(24);

		JScrollPane scroll = new JScrollPane(tabSocios);
		scroll.setAlignmentX(Component.LEFT_ALIGNMENT);

		// Panel total
		JPanel panelTotal = new JPanel(new BorderLayout());
		panelTotal.setBackground(COLOR_HEADER_BG);
		panelTotal.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
		panelTotal.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelTotal.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY),
				BorderFactory.createEmptyBorder(10, 16, 10, 16)));

		JLabel lblTotalTexto = new JLabel("TOTAL GENERAL");
		lblTotalTexto.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblTotalTexto.setForeground(Color.DARK_GRAY);

		lblTotalGeneral = new JLabel("0,00 €");
		lblTotalGeneral.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblTotalGeneral.setForeground(Color.BLACK);
		lblTotalGeneral.setHorizontalAlignment(SwingConstants.RIGHT);

		panelTotal.add(lblTotalTexto,   BorderLayout.WEST);
		panelTotal.add(lblTotalGeneral, BorderLayout.EAST);

		// Meta info
		lblMeta = new JLabel(" ");
		lblMeta.setFont(new Font("SansSerif", Font.PLAIN, 11));
		lblMeta.setForeground(Color.GRAY);
		lblMeta.setAlignmentX(Component.LEFT_ALIGNMENT);

		panelResultado.add(scroll);
		panelResultado.add(panelTotal);
		panelResultado.add(Box.createVerticalStrut(4));
		panelResultado.add(lblMeta);

		return panelResultado;
	}

	// ── Getters para el controlador ───────────────────────────────────────────

	public JFrame            getFrame()        { return this.frame; }
	public JComboBox<String> getCmbMes()       { return this.cmbMes; }
	public JButton           getBtnCSV()       { return this.btnCSV; }
	public JButton           getBtnTXT()       { return this.btnTXT; }
	public JButton           getBtnGenerar()   { return this.btnGenerar; }
	public JButton           getBtnDescargar() { return this.btnDescargar; }
	public String            getFormato()      { return this.formatoActual; }

	public int getMesSeleccionado() {
		return cmbMes.getSelectedIndex() + 1;
	}

	public String getMesTexto() {
		return (String) cmbMes.getSelectedItem();
	}

	/** Cambia el formato activo y actualiza el aspecto de los botones toggle */
	public void setFormato(String formato) {
		this.formatoActual = formato;
		boolean csv = "CSV".equals(formato);
		btnCSV.setBackground(csv ? Color.DARK_GRAY : Color.LIGHT_GRAY);
		btnCSV.setForeground(csv ? Color.WHITE : Color.DARK_GRAY);
		btnTXT.setBackground(csv ? Color.LIGHT_GRAY : Color.DARK_GRAY);
		btnTXT.setForeground(csv ? Color.DARK_GRAY : Color.WHITE);
		if (btnDescargar.isVisible())
			btnDescargar.setText("↓ Descargar " + formato);
	}

	/** Rellena la tabla con los datos y muestra el panel de resultado */
	public void setDatos(List<ContabilidadMensualDTO> datos, String mesTexto) {
		DefaultTableModel model = new DefaultTableModel(
				new String[]{"ID Socio", "Nombre", "Actividades pend. (€)",
						"Reservas pend. (€)", "Total (€)"}, 0) {
			@Override public boolean isCellEditable(int r, int c) { return false; }
		};

		double totalGeneral = 0;
		for (ContabilidadMensualDTO d : datos) {
			model.addRow(new Object[]{
					d.getIdSocio(),
					d.getNombre(),
					ContabilidadMensualDTO.formatImporte(d.getActividades()),
					ContabilidadMensualDTO.formatImporte(d.getReservas()),
					ContabilidadMensualDTO.formatImporte(d.getTotal())
			});
			totalGeneral += d.getTotal();
		}
		tabSocios.setModel(model);

		tabSocios.getColumnModel().getColumn(0).setPreferredWidth(70);
		tabSocios.getColumnModel().getColumn(0).setMaxWidth(80);
		tabSocios.getColumnModel().getColumn(2).setPreferredWidth(160);
		tabSocios.getColumnModel().getColumn(3).setPreferredWidth(160);
		tabSocios.getColumnModel().getColumn(4).setPreferredWidth(110);

		lblTotalGeneral.setText(ContabilidadMensualDTO.formatImporte(totalGeneral)
				+ "  —  " + mesTexto);
		lblMeta.setText(datos.size() + " socios incluidos");

		panelResultado.setVisible(true);
		btnDescargar.setVisible(true);
		btnDescargar.setText("↓ Descargar " + formatoActual);

		frame.revalidate();
		frame.repaint();
	}
}
