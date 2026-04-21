package cd.admin.Alejandro.InformeMorosos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Vista del informe de socios morosos (administracion).
 * Paleta limpia: fondo gris claro, titulos negros, sin codigos de color.
 *
 * Layout:
 *   NORTH  - titulo + filtros + botones
 *   CENTER - JSplitPane:
 *              TOP    - tabla resumen (un socio por fila)
 *              BOTTOM - tabla detalle (pagos pendientes del socio seleccionado)
 *   SOUTH  - KPIs y fecha de consulta
 */
public class InformeMorososView {

	// ── Paleta ────────────────────────────────────────────────────────────────
	static final Color COLOR_BG        = new Color(240, 240, 240);
	static final Color COLOR_SURFACE   = Color.WHITE;
	static final Color COLOR_BORDER    = new Color(200, 200, 200);
	static final Color COLOR_HEADER_BG = new Color( 60,  60,  60);
	static final Color COLOR_HEADER_FG = Color.WHITE;
	static final Color COLOR_TEXT      = new Color( 30,  30,  30);
	static final Color COLOR_MUTED     = new Color(100, 100, 100);
	static final Color COLOR_SEL       = new Color(220, 220, 220);
	static final Color COLOR_BTN_MAIN  = new Color( 60,  60,  60);
	static final Color COLOR_BTN_TXT   = new Color( 80, 120,  80);
	static final Color COLOR_BTN_CLEAR = new Color(130, 130, 130);

	// ── Columnas ─────────────────────────────────────────────────────────────
	static final String[] COLS_RESUMEN = {
		"Nº Socio", "Nombre", "Pagos pendientes", "Importe total pendiente"
	};
	static final String[] COLS_DETALLE = {
		"Concepto", "Fecha emisión / inscripción", "Fecha vencimiento / fin actividad", "Importe"
	};

	// ── Componentes ───────────────────────────────────────────────────────────
	private JFrame     frame;
	private JTextField txtBusqueda;
	private JTextField txtImporteMin;
	private JButton    btnGenerar;
	private JButton    btnLimpiar;
	private JButton    btnExportar;
	private JTable     tabResumen;
	private JTable     tabDetalle;
	private JLabel     lblDetalleTitulo;
	private JLabel     lblKpis;
	private JLabel     lblFecha;

	public InformeMorososView() { initialize(); }

	// ── Construccion ─────────────────────────────────────────────────────────

	private void initialize() {
		frame = new JFrame("Informe de Socios Morosos - Administracion");
		frame.setName("InformeMorosos");
		frame.setSize(1000, 650);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		frame.getContentPane().setBackground(COLOR_BG);

		frame.getContentPane().add(buildNorth(),  BorderLayout.NORTH);
		frame.getContentPane().add(buildCenter(), BorderLayout.CENTER);
		frame.getContentPane().add(buildSouth(),  BorderLayout.SOUTH);
	}

	private JPanel buildNorth() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(COLOR_SURFACE);
		panel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER),
				BorderFactory.createEmptyBorder(10, 16, 10, 16)));

		JLabel lblTitulo = new JLabel("Informe de Socios Morosos");
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 17));
		lblTitulo.setForeground(COLOR_TEXT);
		panel.add(lblTitulo);

		JLabel lblSub = new JLabel(
				"Incluye recibos mensuales y cuotas de actividades finalizadas sin abonar.");
		lblSub.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblSub.setForeground(COLOR_MUTED);
		panel.add(lblSub);
		panel.add(Box.createVerticalStrut(8));

		JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
		fila.setBackground(COLOR_SURFACE);

		fila.add(makeLabel("Nombre o nº socio:"));
		txtBusqueda = new JTextField(16);
		txtBusqueda.setName("txtBusqueda");
		txtBusqueda.setFont(new Font("SansSerif", Font.PLAIN, 13));
		fila.add(txtBusqueda);

		fila.add(makeLabel("Importe mínimo (€):"));
		txtImporteMin = new JTextField(7);
		txtImporteMin.setName("txtImporteMin");
		txtImporteMin.setFont(new Font("SansSerif", Font.PLAIN, 13));
		fila.add(txtImporteMin);

		btnGenerar = new JButton("Generar informe");
		btnGenerar.setName("btnGenerar");
		styleBoton(btnGenerar, COLOR_BTN_MAIN);
		fila.add(btnGenerar);

		btnLimpiar = new JButton("Limpiar");
		btnLimpiar.setName("btnLimpiar");
		styleBoton(btnLimpiar, COLOR_BTN_CLEAR);
		fila.add(btnLimpiar);

		btnExportar = new JButton("Descargar .txt");
		btnExportar.setName("btnExportar");
		styleBoton(btnExportar, COLOR_BTN_TXT);
		btnExportar.setEnabled(false);
		fila.add(btnExportar);

		panel.add(fila);
		return panel;
	}

	private JSplitPane buildCenter() {
		// ── Tabla resumen ──────────────────────────────────────────────────
		tabResumen = buildTabla("tabResumen");
		tabResumen.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tabResumen.setDefaultRenderer(Object.class, new SimpleRenderer(true));

		JScrollPane scrollResumen = new JScrollPane(tabResumen);
		scrollResumen.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(COLOR_BORDER), "Socios morosos"));
		scrollResumen.getViewport().setBackground(COLOR_SURFACE);

		// ── Tabla detalle ──────────────────────────────────────────────────
		lblDetalleTitulo = new JLabel(
				"Selecciona un socio para ver sus pagos pendientes");
		lblDetalleTitulo.setFont(new Font("SansSerif", Font.ITALIC, 12));
		lblDetalleTitulo.setForeground(COLOR_MUTED);
		lblDetalleTitulo.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 0));

		tabDetalle = buildTabla("tabDetalle");
		tabDetalle.setDefaultRenderer(Object.class, new SimpleRenderer(false));

		JScrollPane scrollDetalle = new JScrollPane(tabDetalle);
		scrollDetalle.setBorder(BorderFactory.createEmptyBorder());
		scrollDetalle.getViewport().setBackground(COLOR_SURFACE);

		JPanel panelDetalle = new JPanel(new BorderLayout());
		panelDetalle.setBackground(COLOR_SURFACE);
		panelDetalle.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(COLOR_BORDER), "Detalle de pagos pendientes"));
		panelDetalle.add(lblDetalleTitulo, BorderLayout.NORTH);
		panelDetalle.add(scrollDetalle,    BorderLayout.CENTER);

		JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollResumen, panelDetalle);
		split.setDividerLocation(280);
		split.setResizeWeight(0.55);
		split.setBorder(BorderFactory.createEmptyBorder(8, 12, 4, 12));
		split.setBackground(COLOR_BG);
		return split;
	}

	private JPanel buildSouth() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(COLOR_SURFACE);
		panel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(1, 0, 0, 0, COLOR_BORDER),
				BorderFactory.createEmptyBorder(6, 16, 6, 16)));

		lblFecha = new JLabel(" ");
		lblFecha.setName("lblFecha");
		lblFecha.setFont(new Font("SansSerif", Font.ITALIC, 12));
		lblFecha.setForeground(COLOR_MUTED);

		lblKpis = new JLabel(" ");
		lblKpis.setName("lblKpis");
		lblKpis.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblKpis.setForeground(COLOR_TEXT);
		lblKpis.setHorizontalAlignment(SwingConstants.RIGHT);

		panel.add(lblFecha, BorderLayout.WEST);
		panel.add(lblKpis,  BorderLayout.EAST);
		return panel;
	}

	// ── Renderer ─────────────────────────────────────────────────────────────

	static class SimpleRenderer extends DefaultTableCellRenderer {
		private final boolean importeNegrita;
		SimpleRenderer(boolean importeNegrita) { this.importeNegrita = importeNegrita; }

		@Override
		public Component getTableCellRendererComponent(JTable t, Object v,
				boolean sel, boolean foc, int row, int col) {
			JLabel lbl = (JLabel) super.getTableCellRendererComponent(t, v, sel, foc, row, col);
			lbl.setOpaque(true);
			lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
			lbl.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
			lbl.setForeground(COLOR_TEXT);
			lbl.setBackground(sel ? COLOR_SEL
					: row % 2 == 0 ? COLOR_SURFACE : new Color(248, 248, 248));

			int lastCol = t.getColumnCount() - 1;
			if (col == lastCol) {
				lbl.setHorizontalAlignment(SwingConstants.RIGHT);
				lbl.setFont(new Font("Monospaced",
						importeNegrita ? Font.BOLD : Font.PLAIN, 12));
			} else if (col == 0) {
				lbl.setHorizontalAlignment(SwingConstants.CENTER);
			} else {
				lbl.setHorizontalAlignment(SwingConstants.LEFT);
			}
			return lbl;
		}
	}

	// ── Utilidades privadas ───────────────────────────────────────────────────

	private JTable buildTabla(String name) {
		JTable tab = new JTable();
		tab.setName(name);
		tab.setRowHeight(26);
		tab.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tab.setDefaultEditor(Object.class, null);
		tab.setShowGrid(true);
		tab.setGridColor(COLOR_BORDER);
		tab.setBackground(COLOR_SURFACE);
		tab.setFillsViewportHeight(true);
		tab.getTableHeader().setBackground(COLOR_HEADER_BG);
		tab.getTableHeader().setForeground(COLOR_HEADER_FG);
		tab.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
		tab.getTableHeader().setPreferredSize(new Dimension(0, 30));
		tab.getTableHeader().setReorderingAllowed(false);
		return tab;
	}

	private JLabel makeLabel(String t) {
		JLabel l = new JLabel(t);
		l.setFont(new Font("SansSerif", Font.PLAIN, 12));
		l.setForeground(COLOR_TEXT);
		return l;
	}

	private void styleBoton(JButton btn, Color bg) {
		btn.setBackground(bg);
		btn.setForeground(Color.WHITE);
		btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
		btn.setFocusPainted(false);
		btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		btn.setBorder(BorderFactory.createEmptyBorder(5, 14, 5, 14));
	}

	// ── API para el Controlador ───────────────────────────────────────────────

	public JFrame     getFrame()         { return frame;        }
	public JTextField getTxtBusqueda()   { return txtBusqueda;  }
	public JTextField getTxtImporteMin() { return txtImporteMin;}
	public JButton    getBtnGenerar()    { return btnGenerar;   }
	public JButton    getBtnLimpiar()    { return btnLimpiar;   }
	public JButton    getBtnExportar()   { return btnExportar;  }
	public JTable     getTabResumen()    { return tabResumen;   }
	public JTable     getTabDetalle()    { return tabDetalle;   }

	public void setResumen(List<String[]> filas) {
		DefaultTableModel m = new DefaultTableModel(COLS_RESUMEN, 0) {
			@Override public boolean isCellEditable(int r, int c) { return false; }
			@Override public Class<?> getColumnClass(int c)        { return String.class; }
		};
		for (String[] f : filas) m.addRow(f);
		tabResumen.setModel(m);
		int[] anchos = { 80, 250, 130, 180 };
		for (int i = 0; i < anchos.length && i < tabResumen.getColumnCount(); i++)
			tabResumen.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
		tabResumen.revalidate();
		tabResumen.repaint();
	}

	public void setDetalle(String nombreSocio, List<String[]> filas) {
		lblDetalleTitulo.setText("Pagos pendientes de: " + nombreSocio);
		lblDetalleTitulo.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblDetalleTitulo.setForeground(COLOR_TEXT);

		DefaultTableModel m = new DefaultTableModel(COLS_DETALLE, 0) {
			@Override public boolean isCellEditable(int r, int c) { return false; }
			@Override public Class<?> getColumnClass(int c)        { return String.class; }
		};
		for (String[] f : filas) m.addRow(f);
		tabDetalle.setModel(m);
		int[] anchos = { 220, 160, 180, 110 };
		for (int i = 0; i < anchos.length && i < tabDetalle.getColumnCount(); i++)
			tabDetalle.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
		tabDetalle.revalidate();
		tabDetalle.repaint();
	}

	public void setKpis(int numSocios, int numPagos, double totalGlobal, double media) {
		lblKpis.setText(String.format(
				"Socios morosos: %d   |   Pagos pendientes: %d   |   Total: %.2f €   |   Media: %.2f €",
				numSocios, numPagos, totalGlobal, media));
	}

	public void setFecha(String fecha) {
		lblFecha.setText("Fecha de consulta: " + fecha);
		lblFecha.setForeground(COLOR_MUTED);
	}

	public void setError(String msg) {
		lblFecha.setText("Error: " + msg);
		lblFecha.setForeground(new Color(180, 0, 0));
	}

	public void limpiar() {
		txtBusqueda.setText("");
		txtImporteMin.setText("");
		tabResumen.setModel(new DefaultTableModel());
		tabDetalle.setModel(new DefaultTableModel());
		lblDetalleTitulo.setText("Selecciona un socio para ver sus pagos pendientes");
		lblDetalleTitulo.setFont(new Font("SansSerif", Font.ITALIC, 12));
		lblDetalleTitulo.setForeground(COLOR_MUTED);
		lblKpis.setText(" ");
		lblFecha.setText(" ");
		btnExportar.setEnabled(false);
	}

	public String getBusqueda()   { return txtBusqueda.getText().trim();   }
	public String getImporteMin() { return txtImporteMin.getText().trim(); }
}
