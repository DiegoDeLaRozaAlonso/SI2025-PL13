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
import javax.swing.table.DefaultTableCellRenderer;
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

	// Colores
	private static final Color COLOR_NAVY       = new Color( 26,  41,  64);
	private static final Color COLOR_GOLD       = new Color(200, 168,  75);
	private static final Color COLOR_BG         = new Color(244, 241, 236);
	private static final Color COLOR_RED        = new Color(192,  23,  75);
	private static final Color COLOR_BLUE       = new Color( 26,  86, 219);

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

	/** Formato actualmente seleccionado: "CSV" o "TXT" */
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

		frame.getContentPane().add(buildPanelHeader(),    BorderLayout.NORTH);
		frame.getContentPane().add(buildPanelCentro(),    BorderLayout.CENTER);
	}

	// ── Cabecera ──────────────────────────────────────────────────────────────

	private JPanel buildPanelHeader() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(COLOR_NAVY);
		panel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 4, 0, COLOR_GOLD),
				BorderFactory.createEmptyBorder(16, 24, 16, 24)));

		JPanel izq = new JPanel();
		izq.setOpaque(false);
		izq.setLayout(new BoxLayout(izq, BoxLayout.Y_AXIS));

		JLabel lblSub = new JLabel("ADMINISTRACIÓN");
		lblSub.setFont(new Font("Monospaced", Font.PLAIN, 11));
		lblSub.setForeground(COLOR_GOLD);

		JLabel lblTitulo = new JLabel("Contabilidad Mensual");
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
		lblTitulo.setForeground(Color.WHITE);

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
		panel.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));

		panel.add(buildPanelControles());
		panel.add(Box.createVerticalStrut(16));
		panel.add(buildPanelResultado());

		return panel;
	}

	// ── Panel de controles ────────────────────────────────────────────────────

	private JPanel buildPanelControles() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(221, 221, 221)),
				BorderFactory.createEmptyBorder(16, 20, 16, 20)));
		panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);

		// Selector de mes
		JPanel subMes = new JPanel();
		subMes.setOpaque(false);
		subMes.setLayout(new BoxLayout(subMes, BoxLayout.Y_AXIS));
		JLabel lblMesLabel = new JLabel("MES");
		lblMesLabel.setFont(new Font("Monospaced", Font.PLAIN, 11));
		lblMesLabel.setForeground(new Color(136, 136, 136));

		// Generar opciones "Enero 2026", "Febrero 2026"...
		String[] opciones = new String[ContabilidadMensualModel.NOMBRES_MESES.length];
		for (int i = 0; i < opciones.length; i++)
			opciones[i] = ContabilidadMensualModel.NOMBRES_MESES[i] + " " + ContabilidadMensualModel.ANHO_BASE;
		cmbMes = new JComboBox<>(opciones);
		cmbMes.setName("cmbMes");
		cmbMes.setSelectedIndex(1); // Febrero por defecto
		cmbMes.setFont(new Font("Monospaced", Font.PLAIN, 13));
		cmbMes.setMaximumSize(new Dimension(180, 32));

		subMes.add(lblMesLabel);
		subMes.add(Box.createVerticalStrut(4));
		subMes.add(cmbMes);

		// Toggle CSV / TXT
		JPanel subFormato = new JPanel();
		subFormato.setOpaque(false);
		subFormato.setLayout(new BoxLayout(subFormato, BoxLayout.Y_AXIS));
		JLabel lblFormatoLabel = new JLabel("FORMATO");
		lblFormatoLabel.setFont(new Font("Monospaced", Font.PLAIN, 11));
		lblFormatoLabel.setForeground(new Color(136, 136, 136));

		JPanel toggleWrap = new JPanel(new java.awt.GridLayout(1, 2));
		toggleWrap.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204)));
		toggleWrap.setMaximumSize(new Dimension(130, 32));

		btnCSV = new JButton("CSV");
		btnCSV.setName("btnCSV");
		btnCSV.setFont(new Font("Monospaced", Font.BOLD, 12));
		btnCSV.setBackground(COLOR_NAVY);
		btnCSV.setForeground(COLOR_GOLD);
		btnCSV.setBorderPainted(false);
		btnCSV.setFocusPainted(false);

		btnTXT = new JButton("TXT");
		btnTXT.setName("btnTXT");
		btnTXT.setFont(new Font("Monospaced", Font.PLAIN, 12));
		btnTXT.setBackground(new Color(250, 250, 248));
		btnTXT.setForeground(new Color(85, 85, 85));
		btnTXT.setBorderPainted(false);
		btnTXT.setFocusPainted(false);

		toggleWrap.add(btnCSV);
		toggleWrap.add(btnTXT);

		subFormato.add(lblFormatoLabel);
		subFormato.add(Box.createVerticalStrut(4));
		subFormato.add(toggleWrap);

		// Botón generar
		btnGenerar = new JButton("GENERAR INFORME");
		btnGenerar.setName("btnGenerar");
		btnGenerar.setFont(new Font("Monospaced", Font.BOLD, 13));
		btnGenerar.setBackground(COLOR_NAVY);
		btnGenerar.setForeground(COLOR_GOLD);
		btnGenerar.setBorderPainted(false);
		btnGenerar.setFocusPainted(false);
		btnGenerar.setAlignmentY(Component.BOTTOM_ALIGNMENT);

		// Botón descargar (oculto hasta generar)
		btnDescargar = new JButton("↓ DESCARGAR CSV");
		btnDescargar.setName("btnDescargar");
		btnDescargar.setFont(new Font("Monospaced", Font.BOLD, 13));
		btnDescargar.setBackground(COLOR_GOLD);
		btnDescargar.setForeground(COLOR_NAVY);
		btnDescargar.setBorderPainted(false);
		btnDescargar.setFocusPainted(false);
		btnDescargar.setVisible(false);
		btnDescargar.setAlignmentY(Component.BOTTOM_ALIGNMENT);

		panel.add(subMes);
		panel.add(Box.createHorizontalStrut(20));
		panel.add(subFormato);
		panel.add(Box.createHorizontalStrut(20));
		panel.add(btnGenerar);
		panel.add(Box.createHorizontalStrut(12));
		panel.add(btnDescargar);

		return panel;
	}

	// ── Panel de resultado (tabla + meta) ─────────────────────────────────────

	private JPanel buildPanelResultado() {
		panelResultado = new JPanel();
		panelResultado.setLayout(new BoxLayout(panelResultado, BoxLayout.Y_AXIS));
		panelResultado.setBackground(COLOR_BG);
		panelResultado.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelResultado.setVisible(false);

		// Tabla
		tabSocios = new JTable();
		tabSocios.setName("tabSocios");
		tabSocios.setFont(new Font("SansSerif", Font.PLAIN, 13));
		tabSocios.setRowHeight(38);
		tabSocios.setGridColor(new Color(238, 238, 238));
		tabSocios.setShowVerticalLines(false);
		tabSocios.getTableHeader().setBackground(COLOR_NAVY);
		tabSocios.getTableHeader().setForeground(COLOR_GOLD);
		tabSocios.getTableHeader().setFont(new Font("Monospaced", Font.PLAIN, 11));
		tabSocios.getTableHeader().setReorderingAllowed(false);
		tabSocios.setDefaultRenderer(Object.class, new ContabilidadCellRenderer());

		JScrollPane scroll = new JScrollPane(tabSocios);
		scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
		scroll.setBorder(BorderFactory.createLineBorder(new Color(221, 221, 221)));

		// Panel total inferior
		JPanel panelTotal = new JPanel(new BorderLayout());
		panelTotal.setBackground(COLOR_BG);
		panelTotal.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
		panelTotal.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelTotal.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(2, 0, 0, 0, COLOR_NAVY),
				BorderFactory.createEmptyBorder(10, 18, 10, 18)));

		JLabel lblTotalTexto = new JLabel("TOTAL GENERAL");
		lblTotalTexto.setFont(new Font("Monospaced", Font.BOLD, 13));
		lblTotalTexto.setForeground(COLOR_NAVY);

		lblTotalGeneral = new JLabel("0,00 €");
		lblTotalGeneral.setFont(new Font("Monospaced", Font.BOLD, 18));
		lblTotalGeneral.setForeground(COLOR_NAVY);
		lblTotalGeneral.setHorizontalAlignment(SwingConstants.RIGHT);

		panelTotal.add(lblTotalTexto,   BorderLayout.WEST);
		panelTotal.add(lblTotalGeneral, BorderLayout.EAST);

		// Meta info
		lblMeta = new JLabel(" ");
		lblMeta.setFont(new Font("Monospaced", Font.PLAIN, 11));
		lblMeta.setForeground(new Color(136, 136, 136));
		lblMeta.setAlignmentX(Component.LEFT_ALIGNMENT);
		lblMeta.setHorizontalAlignment(SwingConstants.RIGHT);

		panelResultado.add(scroll);
		panelResultado.add(panelTotal);
		panelResultado.add(Box.createVerticalStrut(6));
		panelResultado.add(lblMeta);

		return panelResultado;
	}

	// ── Renderer de celdas ────────────────────────────────────────────────────

	class ContabilidadCellRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			setFont(new Font("SansSerif", Font.PLAIN, 13));
			setBackground(row % 2 == 0 ? new Color(250, 250, 248) : Color.WHITE);
			setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(238, 238, 238)));
			setForeground(new Color(51, 51, 51));
			setHorizontalAlignment(SwingConstants.LEFT);

			// Columna ID: monoespaciado gris
			if (column == 0) {
				setFont(new Font("Monospaced", Font.PLAIN, 12));
				setForeground(new Color(136, 136, 136));
			}

			// Columnas Actividades y Reservas: color si > 0, gris si = 0
			if (column == 2 || column == 3) {
				setFont(new Font("Monospaced", Font.PLAIN, 13));
				boolean esCero = value != null && value.toString().startsWith("0,00");
				setForeground(esCero ? new Color(170, 170, 170)
						: (column == 2 ? COLOR_BLUE : COLOR_RED));
				setHorizontalAlignment(SwingConstants.RIGHT);
			}

			// Columna Total: negrita
			if (column == 4) {
				boolean esCero = value != null && value.toString().startsWith("0,00");
				setFont(new Font("Monospaced", Font.BOLD, 14));
				setForeground(esCero ? new Color(170, 170, 170) : COLOR_NAVY);
				setHorizontalAlignment(SwingConstants.RIGHT);
			}

			return this;
		}
	}

	// ── Metodos para el controlador ───────────────────────────────────────────

	public JFrame            getFrame()        { return this.frame; }
	public JComboBox<String> getCmbMes()       { return this.cmbMes; }
	public JButton           getBtnCSV()       { return this.btnCSV; }
	public JButton           getBtnTXT()       { return this.btnTXT; }
	public JButton           getBtnGenerar()   { return this.btnGenerar; }
	public JButton           getBtnDescargar() { return this.btnDescargar; }
	public String            getFormato()      { return this.formatoActual; }

	/** Devuelve el numero de mes seleccionado (1-12) */
	public int getMesSeleccionado() {
		return cmbMes.getSelectedIndex() + 1;
	}

	/** Devuelve el texto del mes seleccionado, ej. "Febrero 2026" */
	public String getMesTexto() {
		return (String) cmbMes.getSelectedItem();
	}

	/** Cambia el formato activo y actualiza el aspecto de los botones toggle */
	public void setFormato(String formato) {
		this.formatoActual = formato;
		boolean csv = "CSV".equals(formato);
		btnCSV.setBackground(csv ? COLOR_NAVY : new Color(250, 250, 248));
		btnCSV.setForeground(csv ? COLOR_GOLD : new Color(85, 85, 85));
		btnTXT.setBackground(csv ? new Color(250, 250, 248) : COLOR_NAVY);
		btnTXT.setForeground(csv ? new Color(85, 85, 85) : COLOR_GOLD);
		if (btnDescargar.isVisible())
			btnDescargar.setText("↓ DESCARGAR " + formato);
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

		// Ajuste de anchos
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
		btnDescargar.setText("↓ DESCARGAR " + formatoActual);

		frame.revalidate();
		frame.repaint();
	}
}
