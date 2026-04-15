package cd.admin.Alejandro.InformeOcupacion;

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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import cd.admin.Alejandro.Visualizacion.InstalacionEntity;

/**
 * Vista del informe de ocupacion de instalaciones (administracion).
 * Paleta limpia: fondo gris, titulos negros, sin codigos de color.
 */
public class InformeOcupacionView {

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

	// ── Opciones Estado ───────────────────────────────────────────────────────
	static final String ESTADO_TODOS = "Todos";
	static final String ESTADO_ALTO  = "Alta ocupacion (>80%)";
	static final String ESTADO_MEDIO = "Ocupacion media (40-80%)";
	static final String ESTADO_BAJO  = "Baja ocupacion (<40%)";

	// ── Columnas ─────────────────────────────────────────────────────────────
	static final String[] COLUMNAS = {
		"Instalacion", "Actividad",
		"Ocup. actividad (%)", "Ocup. socio (%)",
		"Reservas activas", "Plazas libres", "Estado"
	};

	// ── Componentes ───────────────────────────────────────────────────────────
	private JFrame            frame;
	private JComboBox<String> cmbInstalacion;
	private JComboBox<String> cmbActividad;
	private JTextField        txtFechaInicio;
	private JTextField        txtFechaFin;
	private JComboBox<String> cmbEstado;
	private JButton           btnGenerar;
	private JButton           btnLimpiar;
	private JButton           btnExportar;
	private JTable            tabResultados;
	private JLabel            lblPeriodo;
	private JLabel            lblKpis;

	public InformeOcupacionView() { initialize(); }

	// ── Construccion ─────────────────────────────────────────────────────────

	private void initialize() {
		frame = new JFrame("Informe de Ocupacion de Instalaciones - Administracion");
		frame.setName("InformeOcupacion");
		frame.setSize(1060, 700);
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

		JLabel lblTitulo = new JLabel("Informe de Ocupacion de Instalaciones");
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 17));
		lblTitulo.setForeground(COLOR_TEXT);
		panel.add(lblTitulo);
		panel.add(Box.createVerticalStrut(8));

		// Fila 1: Instalacion | Actividad | Socio
		JPanel fila1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
		fila1.setBackground(COLOR_SURFACE);
		fila1.add(makeLabel("Instalacion:"));
		cmbInstalacion = makeCombo("cmbInstalacion", 185);
		fila1.add(cmbInstalacion);
		fila1.add(makeLabel("Actividad:"));
		cmbActividad = makeCombo("cmbActividad", 185);
		fila1.add(cmbActividad);
		panel.add(fila1);

		// Fila 2: Fechas | Estado | Botones
		JPanel fila2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
		fila2.setBackground(COLOR_SURFACE);
		fila2.add(makeLabel("Fecha inicio (yyyy-MM-dd):"));
		txtFechaInicio = new JTextField(10);
		txtFechaInicio.setName("txtFechaInicio");
		txtFechaInicio.setFont(new Font("SansSerif", Font.PLAIN, 13));
		fila2.add(txtFechaInicio);
		fila2.add(makeLabel("Fecha fin (yyyy-MM-dd):"));
		txtFechaFin = new JTextField(10);
		txtFechaFin.setName("txtFechaFin");
		txtFechaFin.setFont(new Font("SansSerif", Font.PLAIN, 13));
		fila2.add(txtFechaFin);
		fila2.add(makeLabel("Estado:"));
		cmbEstado = new JComboBox<>(new String[] {
				ESTADO_TODOS, ESTADO_ALTO, ESTADO_MEDIO, ESTADO_BAJO });
		cmbEstado.setName("cmbEstado");
		cmbEstado.setPreferredSize(new Dimension(205, 28));
		cmbEstado.setFont(new Font("SansSerif", Font.PLAIN, 13));
		fila2.add(cmbEstado);
		btnGenerar = new JButton("Generar informe");
		btnGenerar.setName("btnGenerar");
		styleBoton(btnGenerar, COLOR_BTN_MAIN);
		fila2.add(btnGenerar);
		btnLimpiar = new JButton("Limpiar filtros");
		btnLimpiar.setName("btnLimpiar");
		styleBoton(btnLimpiar, COLOR_BTN_CLEAR);
		fila2.add(btnLimpiar);
		panel.add(fila2);
		return panel;
	}

	private JPanel buildCenter() {
		JPanel panel = new JPanel(new BorderLayout(0, 8));
		panel.setBackground(COLOR_BG);
		panel.setBorder(BorderFactory.createEmptyBorder(10, 12, 4, 12));

		// KPIs como texto plano (oculto hasta generar)
		lblKpis = new JLabel(" ");
		lblKpis.setName("lblKpis");
		lblKpis.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblKpis.setForeground(COLOR_MUTED);
		lblKpis.setBorder(BorderFactory.createEmptyBorder(0, 2, 6, 0));
		lblKpis.setVisible(false);
		panel.add(lblKpis, BorderLayout.NORTH);

		// Tabla
		tabResultados = new JTable();
		tabResultados.setName("tabResultados");
		tabResultados.setRowHeight(26);
		tabResultados.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tabResultados.setDefaultEditor(Object.class, null);
		tabResultados.setShowGrid(true);
		tabResultados.setGridColor(COLOR_BORDER);
		tabResultados.setBackground(COLOR_SURFACE);
		tabResultados.setDefaultRenderer(Object.class, new OcupacionRenderer());
		tabResultados.setFillsViewportHeight(true);
		tabResultados.getTableHeader().setBackground(COLOR_HEADER_BG);
		tabResultados.getTableHeader().setForeground(COLOR_HEADER_FG);
		tabResultados.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
		tabResultados.getTableHeader().setPreferredSize(new Dimension(0, 30));
		tabResultados.getTableHeader().setReorderingAllowed(false);

		JScrollPane scroll = new JScrollPane(tabResultados);
		scroll.setBorder(BorderFactory.createLineBorder(COLOR_BORDER));
		scroll.getViewport().setBackground(COLOR_SURFACE);
		panel.add(scroll, BorderLayout.CENTER);

		// Boton exportar
		JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 4));
		panelAcciones.setBackground(COLOR_BG);
		btnExportar = new JButton("Descargar .txt");
		btnExportar.setName("btnExportar");
		styleBoton(btnExportar, COLOR_BTN_TXT);
		btnExportar.setEnabled(false);
		panelAcciones.add(btnExportar);
		panel.add(panelAcciones, BorderLayout.SOUTH);
		return panel;
	}

	private JPanel buildSouth() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(COLOR_SURFACE);
		panel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(1, 0, 0, 0, COLOR_BORDER),
				BorderFactory.createEmptyBorder(6, 16, 6, 16)));
		lblPeriodo = new JLabel(" ");
		lblPeriodo.setName("lblPeriodo");
		lblPeriodo.setFont(new Font("SansSerif", Font.ITALIC, 12));
		lblPeriodo.setForeground(COLOR_MUTED);
		panel.add(lblPeriodo, BorderLayout.WEST);
		return panel;
	}

	// ── Renderer limpio ───────────────────────────────────────────────────────

	/**
	 * Filas alternadas blanco/gris muy claro.
	 * Columnas de porcentaje y Estado: monoespaciadas y centradas, sin colores.
	 */
	static class OcupacionRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable t, Object v,
				boolean sel, boolean foc, int row, int col) {
			JLabel lbl = (JLabel) super.getTableCellRendererComponent(
					t, v, sel, foc, row, col);
			lbl.setOpaque(true);
			lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
			lbl.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
			lbl.setForeground(COLOR_TEXT);
			lbl.setBackground(sel ? COLOR_SEL
					: row % 2 == 0 ? COLOR_SURFACE : new Color(248, 248, 248));

			switch (col) {
				case 2: case 3: // porcentajes
					lbl.setHorizontalAlignment(SwingConstants.CENTER);
					lbl.setFont(new Font("Monospaced", Font.PLAIN, 12));
					break;
				case 4: // reservas activas
					lbl.setHorizontalAlignment(SwingConstants.CENTER);
					break;
				case 5: // plazas libres
					lbl.setHorizontalAlignment(SwingConstants.CENTER);
					break;
				case 6: // estado
					lbl.setHorizontalAlignment(SwingConstants.CENTER);
					lbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
					break;
				default:
					lbl.setHorizontalAlignment(SwingConstants.LEFT);
			}
			return lbl;
		}
	}

	// ── Utilidades privadas ───────────────────────────────────────────────────

	private JLabel makeLabel(String t) {
		JLabel l = new JLabel(t);
		l.setFont(new Font("SansSerif", Font.PLAIN, 12));
		l.setForeground(COLOR_TEXT);
		return l;
	}

	private JComboBox<String> makeCombo(String name, int width) {
		JComboBox<String> c = new JComboBox<>();
		c.setName(name);
		c.setPreferredSize(new Dimension(width, 28));
		c.setFont(new Font("SansSerif", Font.PLAIN, 13));
		return c;
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

	public JFrame             getFrame()           { return frame;          }
	public JComboBox<String>  getCmbInstalacion()  { return cmbInstalacion; }
	public JComboBox<String>  getCmbActividad()    { return cmbActividad;   }
	public JTextField         getTxtFechaInicio()  { return txtFechaInicio; }
	public JTextField         getTxtFechaFin()     { return txtFechaFin;    }
	public JComboBox<String>  getCmbEstado()       { return cmbEstado;      }
	public JButton            getBtnGenerar()      { return btnGenerar;     }
	public JButton            getBtnLimpiar()      { return btnLimpiar;     }
	public JButton            getBtnExportar()     { return btnExportar;    }
	public JTable             getTabResultados()   { return tabResultados;  }

	public void setInstalaciones(List<InstalacionEntity> instalaciones) {
		cmbInstalacion.removeAllItems();
		cmbInstalacion.addItem("Todas");
		for (InstalacionEntity i : instalaciones) cmbInstalacion.addItem(i.getNombre());
	}

	public void setActividades(List<ActividadEntity> actividades) {
		cmbActividad.removeAllItems();
		cmbActividad.addItem("Todas");
		for (ActividadEntity a : actividades) cmbActividad.addItem(a.getNombre());
	}

	public void setFilas(List<OcupacionFilaDTO> filas) {
		DefaultTableModel m = new DefaultTableModel(COLUMNAS, 0) {
			@Override public boolean isCellEditable(int r, int c) { return false; }
			@Override public Class<?> getColumnClass(int c)        { return String.class; }
		};
		for (OcupacionFilaDTO f : filas) {
			int    pct    = f.getPorcentajeActividad();
			String estado = pct >= 80 ? "Alta" : pct >= 40 ? "Media" : "Baja";
			m.addRow(new Object[] {
				f.getNombreInstalacion(),
				f.getNombreActividad(),
				pct + "%",
				f.getPorcentajeSocio() + "%",
				f.getReservasActivas(),
				f.getPlazasLibres() + " / " + f.getAforoActividad(),
				estado
			});
		}
		tabResultados.setModel(m);
		int[] anchos = { 155, 145, 115, 105, 95, 85, 65 };
		for (int i = 0; i < anchos.length && i < tabResultados.getColumnCount(); i++)
			tabResultados.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
		tabResultados.revalidate();
		tabResultados.repaint();
	}

	public void setKpis(int numInst, int totalRes, int totalLibres, int mediaOcup) {
		lblKpis.setText("Instalaciones: " + numInst
				+ "   |   Reservas activas: " + totalRes
				+ "   |   Plazas libres totales: " + totalLibres
				+ "   |   Ocupacion media: " + mediaOcup + "%");
		lblKpis.setVisible(true);
	}

	public void setPeriodo(String inicio, String fin) {
		lblPeriodo.setText("Periodo consultado: " + inicio + "  ->  " + fin);
		lblPeriodo.setForeground(COLOR_MUTED);
	}

	public void setError(String msg) {
		lblPeriodo.setText("Error: " + msg);
		lblPeriodo.setForeground(new Color(180, 0, 0));
	}

	public void limpiarFiltros(String inicioDefecto, String finDefecto) {
		cmbInstalacion.setSelectedIndex(0);
		cmbActividad.setSelectedIndex(0);
		cmbEstado.setSelectedIndex(0);
		txtFechaInicio.setText(inicioDefecto);
		txtFechaFin.setText(finDefecto);
		tabResultados.setModel(new DefaultTableModel());
		lblKpis.setText(" ");
		lblKpis.setVisible(false);
		lblPeriodo.setText(" ");
		btnExportar.setEnabled(false);
	}

	public String getInstalacionSeleccionada() {
		Object s = cmbInstalacion.getSelectedItem(); return s != null ? s.toString() : "Todas";
	}
	public String getActividadSeleccionada() {
		Object s = cmbActividad.getSelectedItem(); return s != null ? s.toString() : "Todas";
	}
	public String getEstadoSeleccionado() {
		Object s = cmbEstado.getSelectedItem(); return s != null ? s.toString() : ESTADO_TODOS;
	}
}