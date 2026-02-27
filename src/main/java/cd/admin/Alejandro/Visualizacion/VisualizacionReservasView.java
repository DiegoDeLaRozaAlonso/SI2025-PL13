package cd.admin.Alejandro.Visualizacion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
 * Vista de la pantalla de visualizacion del calendario de reservas (administracion).
 * Muestra un grid semanal (filas=horas, columnas=dias) con tres estados:
 * libre (verde), reserva de socio (azul) y actividad del centro (morado).
 */
public class VisualizacionReservasView {

	// Paleta de colores
	static final Color COLOR_LIBRE      = new Color(220, 252, 231);
	static final Color COLOR_LIBRE_BRD  = new Color( 74, 222, 128);
	static final Color COLOR_SOCIO      = new Color( 96, 165, 250);
	static final Color COLOR_SOCIO_BRD  = new Color( 37,  99, 235);
	static final Color COLOR_ACTIVIDAD  = new Color(167, 139, 250);
	static final Color COLOR_ACT_BRD    = new Color(124,  58, 237);
	static final Color COLOR_CABECERA   = new Color( 37,  99, 235);
	static final Color COLOR_HEADER_FG  = Color.WHITE;

	private JFrame           frame;
	private JComboBox<String> cmbInstalacion;
	private JButton          btnSemanaAnterior;
	private JButton          btnSemanaSiguiente;
	private JLabel           lblSemana;
	private JTable           tabGrid;
	private JScrollPane      scrollGrid;

	public VisualizacionReservasView() {
		initialize();
	}

	private void initialize() {
		// ── Frame ────────────────────────────────────────────────────────────
		frame = new JFrame("Visualizacion de Reservas - Administracion");
		frame.setName("VisualizacionReservas");
		frame.setSize(1000, 680);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		// Layout raiz: NORTH=cabecera, CENTER=contenido principal
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		frame.getContentPane().setBackground(new Color(249, 250, 251));

		// ── NORTH: titulo + selector de instalacion ───────────────────────────
		JPanel panelNorth = new JPanel();
		panelNorth.setLayout(new BoxLayout(panelNorth, BoxLayout.Y_AXIS));
		panelNorth.setBackground(Color.WHITE);
		panelNorth.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(229, 231, 235)),
				BorderFactory.createEmptyBorder(10, 16, 10, 16)));

		JLabel lblTitulo = new JLabel("Visualizacion de Reservas - Administracion");
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 17));
		lblTitulo.setForeground(new Color(31, 41, 55));
		panelNorth.add(lblTitulo);
		panelNorth.add(Box.createVerticalStrut(8));

		JPanel panelInstalacion = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		panelInstalacion.setBackground(Color.WHITE);
		panelInstalacion.add(new JLabel("Instalacion:") {{
			setFont(new Font("SansSerif", Font.BOLD, 13));
		}});
		cmbInstalacion = new JComboBox<>();
		cmbInstalacion.setName("cmbInstalacion");
		cmbInstalacion.setPreferredSize(new Dimension(230, 28));
		cmbInstalacion.setFont(new Font("SansSerif", Font.PLAIN, 13));
		panelInstalacion.add(cmbInstalacion);
		panelNorth.add(panelInstalacion);

		frame.getContentPane().add(panelNorth, BorderLayout.NORTH);

		// ── CENTER: leyenda + navegacion + tabla ──────────────────────────────
		JPanel panelCenter = new JPanel(new BorderLayout(0, 6));
		panelCenter.setBackground(new Color(249, 250, 251));
		panelCenter.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

		// -- Leyenda + navegacion (zona superior del CENTER)
		JPanel panelTop = new JPanel(new BorderLayout(0, 6));
		panelTop.setBackground(new Color(249, 250, 251));

		// Leyenda
		JPanel panelLeyenda = buildPanelLeyenda();
		panelTop.add(panelLeyenda, BorderLayout.NORTH);

		// Navegacion semanal
		JPanel panelNav = new JPanel(new BorderLayout(10, 0));
		panelNav.setBackground(new Color(249, 250, 251));
		panelNav.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));

		btnSemanaAnterior = new JButton("<- Semana Anterior");
		btnSemanaAnterior.setName("btnSemanaAnterior");
		styleBotonNav(btnSemanaAnterior);

		lblSemana = new JLabel("Semana 1 de 5", SwingConstants.CENTER);
		lblSemana.setName("lblSemana");
		lblSemana.setFont(new Font("SansSerif", Font.BOLD, 14));
		lblSemana.setForeground(new Color(55, 65, 81));

		btnSemanaSiguiente = new JButton("Semana Siguiente ->");
		btnSemanaSiguiente.setName("btnSemanaSiguiente");
		styleBotonNav(btnSemanaSiguiente);

		panelNav.add(btnSemanaAnterior,  BorderLayout.WEST);
		panelNav.add(lblSemana,          BorderLayout.CENTER);
		panelNav.add(btnSemanaSiguiente, BorderLayout.EAST);
		panelTop.add(panelNav, BorderLayout.CENTER);

		panelCenter.add(panelTop, BorderLayout.NORTH);

		// -- Tabla calendario (ocupa todo el espacio restante)
		// Subclase anonima para activar los tooltips celda a celda.
		// Sin esto, Swing ignora el setToolTipText del renderer.
		tabGrid = new JTable() {
			@Override
			public String getToolTipText(java.awt.event.MouseEvent e) {
				int row = rowAtPoint(e.getPoint());
				int col = columnAtPoint(e.getPoint());
				if (row < 0 || col <= 0) return null; // columna 0 = horas, sin tooltip
				Object value = getModel().getValueAt(row, col);
				if (!(value instanceof ReservaCeldaDTO)) return null;
				ReservaCeldaDTO celda = (ReservaCeldaDTO) value;
				switch (celda.getTipo()) {
					case "socio":
						return "<html>"
								+ "<b style='color:#1d4ed8'>&#128100; Reserva de Socio</b><br/>"
								+ "<span style='font-size:12px'>" + celda.getNombre() + "</span><br/>"
								+ "<span style='color:#6b7280;font-size:11px'>"
								+ celda.getHora() + " &nbsp;&bull;&nbsp; " + celda.getFecha()
								+ "</span></html>";
					case "actividad":
						return "<html>"
								+ "<b style='color:#7c3aed'>&#9889; Actividad del Centro</b><br/>"
								+ "<span style='font-size:12px'>" + celda.getNombre() + "</span><br/>"
								+ "<span style='color:#6b7280;font-size:11px'>"
								+ celda.getHora() + " &nbsp;&bull;&nbsp; " + celda.getFecha()
								+ "</span></html>";
					default:
						return "<html><span style='color:#16a34a'>&#128994; Libre</span><br/>"
								+ "<span style='color:#6b7280;font-size:11px'>"
								+ celda.getHora() + " &nbsp;&bull;&nbsp; " + celda.getFecha()
								+ "</span></html>";
				}
			}
		};
		// Registrar la tabla en el ToolTipManager para que los tooltips se activen
		javax.swing.ToolTipManager.sharedInstance().registerComponent(tabGrid);
		tabGrid.setName("tabGrid");
		tabGrid.setRowHeight(34);
		tabGrid.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tabGrid.setDefaultEditor(Object.class, null); // readonly
		tabGrid.setShowGrid(true);
		tabGrid.setGridColor(new Color(229, 231, 235));
		tabGrid.setDefaultRenderer(Object.class, new ReservaCellRenderer());
		tabGrid.setFillsViewportHeight(true);

		// Cabecera
		tabGrid.getTableHeader().setBackground(COLOR_CABECERA);
		tabGrid.getTableHeader().setForeground(COLOR_HEADER_FG);
		tabGrid.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
		tabGrid.getTableHeader().setPreferredSize(new Dimension(0, 62));
		tabGrid.getTableHeader().setDefaultRenderer(new CabeceraRenderer());
		tabGrid.getTableHeader().setReorderingAllowed(false);

		scrollGrid = new JScrollPane(tabGrid);
		scrollGrid.setBorder(BorderFactory.createLineBorder(new Color(209, 213, 219)));
		scrollGrid.getViewport().setBackground(Color.WHITE);

		panelCenter.add(scrollGrid, BorderLayout.CENTER);

		frame.getContentPane().add(panelCenter, BorderLayout.CENTER);
	}

	private void styleBotonNav(JButton btn) {
		btn.setBackground(COLOR_CABECERA);
		
		btn.setForeground(Color.WHITE);
		btn.setFont(new Font("SansSerif", Font.BOLD, 12));
		btn.setFocusPainted(false);
		btn.setPreferredSize(new Dimension(170, 32));
	}

	private JPanel buildPanelLeyenda() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 4));
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(229, 231, 235)),
				BorderFactory.createEmptyBorder(4, 12, 4, 12)));

		panel.add(buildItemLeyenda(COLOR_LIBRE,     COLOR_LIBRE_BRD,  "",  "Libre"));
		panel.add(buildItemLeyenda(COLOR_SOCIO,     COLOR_SOCIO_BRD,  "S", "Reserva de Socio"));
		panel.add(buildItemLeyenda(COLOR_ACTIVIDAD, COLOR_ACT_BRD,    "A", "Actividad del Centro"));
		return panel;
	}

	private JPanel buildItemLeyenda(Color bg, Color border, String icono, String texto) {
		JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
		item.setBackground(Color.WHITE);

		JLabel cuadro = new JLabel(icono, SwingConstants.CENTER);
		cuadro.setOpaque(true);
		cuadro.setBackground(bg);
		cuadro.setForeground(Color.WHITE);
		cuadro.setFont(new Font("SansSerif", Font.BOLD, 10));
		cuadro.setBorder(BorderFactory.createLineBorder(border, 2));
		cuadro.setPreferredSize(new Dimension(26, 26));

		JLabel etiq = new JLabel(texto);
		etiq.setFont(new Font("SansSerif", Font.BOLD, 12));
		etiq.setForeground(new Color(55, 65, 81));

		item.add(cuadro);
		item.add(etiq);
		return item;
	}

	// ── Renderer celdas ───────────────────────────────────────────────────────

	static class ReservaCellRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {

			JLabel lbl = (JLabel) super.getTableCellRendererComponent(
					table, value, isSelected, hasFocus, row, column);
			lbl.setHorizontalAlignment(SwingConstants.CENTER);
			lbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
			lbl.setOpaque(true);

			// Columna 0: etiqueta de hora
			if (column == 0) {
				lbl.setText(value != null ? value.toString() : "");
				lbl.setBackground(new Color(248, 250, 252));
				lbl.setForeground(new Color(55, 65, 81));
				lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
				lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(209, 213, 219)));
				lbl.setToolTipText(null);
				return lbl;
			}

			// Resto: celdas de estado
			if (value instanceof ReservaCeldaDTO) {
				ReservaCeldaDTO celda = (ReservaCeldaDTO) value;
				switch (celda.getTipo()) {
					case "socio":
						lbl.setBackground(isSelected ? COLOR_SOCIO_BRD : COLOR_SOCIO);
						lbl.setForeground(Color.WHITE);
						lbl.setBorder(BorderFactory.createLineBorder(COLOR_SOCIO_BRD, 2));
						lbl.setText("");
						break;
					case "actividad":
						lbl.setBackground(isSelected ? COLOR_ACT_BRD : COLOR_ACTIVIDAD);
						lbl.setForeground(Color.WHITE);
						lbl.setBorder(BorderFactory.createLineBorder(COLOR_ACT_BRD, 2));
						lbl.setText("");
						break;
					default: // libre
						lbl.setBackground(isSelected ? COLOR_LIBRE_BRD : COLOR_LIBRE);
						lbl.setForeground(new Color(22, 163, 74));
						lbl.setBorder(BorderFactory.createLineBorder(COLOR_LIBRE_BRD, 1));
						lbl.setText("");
						lbl.setToolTipText("Libre");
						break;
				}
			} else {
				lbl.setBackground(Color.WHITE);
				lbl.setForeground(Color.BLACK);
				lbl.setText(value != null ? value.toString() : "");
				lbl.setBorder(BorderFactory.createEmptyBorder());
				lbl.setToolTipText(null);
			}
			return lbl;
		}

		private String abreviar(String texto, int max) {
			if (texto == null || texto.isEmpty()) return "";
			return texto.length() <= max ? texto : texto.substring(0, max) + "...";
		}
	}

	// ── Renderer cabecera ─────────────────────────────────────────────────────

	static class CabeceraRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {

			JLabel lbl = (JLabel) super.getTableCellRendererComponent(
					table, value, isSelected, hasFocus, row, column);
			lbl.setHorizontalAlignment(SwingConstants.CENTER);
			lbl.setBackground(COLOR_CABECERA);
			lbl.setForeground(COLOR_HEADER_FG);
			lbl.setOpaque(true);
			lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(59, 130, 246)));
			lbl.setFont(new Font("SansSerif", column == 0 ? Font.BOLD : Font.PLAIN, 12));
			lbl.setText(value != null ? value.toString() : "");
			return lbl;
		}
	}

	// ── API para el Controlador ───────────────────────────────────────────────

	public JFrame             getFrame()               { return this.frame; }
	public JComboBox<String>  getCmbInstalacion()      { return this.cmbInstalacion; }
	public JButton            getBtnSemanaAnterior()   { return this.btnSemanaAnterior; }
	public JButton            getBtnSemanaSiguiente()  { return this.btnSemanaSiguiente; }
	public JLabel             getLblSemana()           { return this.lblSemana; }
	public JTable             getTabGrid()             { return this.tabGrid; }

	/** Rellena el combo con los nombres de las instalaciones */
	public void setInstalaciones(List<InstalacionEntity> instalaciones) {
		cmbInstalacion.removeAllItems();
		for (InstalacionEntity inst : instalaciones)
			cmbInstalacion.addItem(inst.getNombre());
	}

	public String getInstalacionSeleccionada() {
		Object sel = cmbInstalacion.getSelectedItem();
		return sel != null ? sel.toString() : "";
	}

	public void setSemanaLabel(int semanaActual, int totalSemanas) {
		lblSemana.setText("Semana " + semanaActual + " de " + totalSemanas);
	}

	public void setNavegacionHabilitada(boolean anterior, boolean siguiente) {
		btnSemanaAnterior.setEnabled(anterior);
		btnSemanaSiguiente.setEnabled(siguiente);
		btnSemanaAnterior.setBackground(anterior  ? COLOR_CABECERA : new Color(156, 163, 175));
		btnSemanaSiguiente.setBackground(siguiente ? COLOR_CABECERA : new Color(156, 163, 175));
	}

	/**
	 * Carga el grid en la tabla: primera columna = horas (String),
	 * resto de columnas = ReservaCeldaDTO que el renderer colorea segun tipo.
	 */
	public void setGrid(ReservaCeldaDTO[][] grid, List<Date> fechas, String[] horas) {
		int numDias = fechas.size();

		// Formatear cada fecha como HTML directamente aqui para que el modelo
		// no llame a Date.toString() y corrompa el valor
		SimpleDateFormat fmtDiaSem = new SimpleDateFormat("EEEE", new Locale("es", "ES"));
		SimpleDateFormat fmtDia    = new SimpleDateFormat("d",    new Locale("es", "ES"));
		SimpleDateFormat fmtMes    = new SimpleDateFormat("MMMM", new Locale("es", "ES"));

		Object[] columnNames = new Object[numDias + 1];
		columnNames[0] = "Hora";
		for (int d = 0; d < numDias; d++) {
			Date fecha = fechas.get(d);
			String diaSem = fmtDiaSem.format(fecha);
			diaSem = Character.toUpperCase(diaSem.charAt(0)) + diaSem.substring(1);
			columnNames[d + 1] = "<html><center>"
					+ "<b>" + diaSem + "</b><br/>"
					+ "<span style='font-size:15px'><b>" + fmtDia.format(fecha) + "</b></span><br/>"
					+ "<span style='font-size:10px'>" + fmtMes.format(fecha) + "</span>"
					+ "</center></html>";
		}

		// Datos: primera celda de cada fila = String de hora; resto = ReservaCeldaDTO
		Object[][] data = new Object[horas.length][numDias + 1];
		for (int h = 0; h < horas.length; h++) {
			data[h][0] = horas[h];
			for (int d = 0; d < numDias; d++)
				data[h][d + 1] = grid[h][d];
		}

		DefaultTableModel model = new DefaultTableModel(data, columnNames) {
			@Override public boolean isCellEditable(int r, int c) { return false; }
			@Override public Class<?> getColumnClass(int c)        { return Object.class; }
		};

		tabGrid.setModel(model);

		// Columna de hora: ancho fijo estrecho
		tabGrid.getColumnModel().getColumn(0).setPreferredWidth(65);
		tabGrid.getColumnModel().getColumn(0).setMinWidth(55);
		tabGrid.getColumnModel().getColumn(0).setMaxWidth(80);
		// Columnas de dias: ancho minimo pero se expanden con la ventana
		for (int d = 1; d <= numDias; d++) {
			tabGrid.getColumnModel().getColumn(d).setPreferredWidth(130);
			tabGrid.getColumnModel().getColumn(d).setMinWidth(90);
		}

		// Reasignar renderer de cabecera a cada columna (se pierden al cambiar modelo)
		CabeceraRenderer hdrRenderer = new CabeceraRenderer();
		for (int i = 0; i < tabGrid.getColumnCount(); i++)
			tabGrid.getColumnModel().getColumn(i).setHeaderRenderer(hdrRenderer);

		// Forzar repintado
		tabGrid.revalidate();
		tabGrid.repaint();
		tabGrid.getTableHeader().revalidate();
		tabGrid.getTableHeader().repaint();
	}
}