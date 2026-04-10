package cd.admin.Alejandro.InformeOcupacion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
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
 * Incluye filtros por instalacion, actividad, socio, fechas y estado de ocupacion,
 * un panel de 4 KPIs resumen y una tabla detallada con columna de estado coloreada.
 */
public class InformeOcupacionView {

	// ── Paleta de colores ─────────────────────────────────────────────────────
	static final Color COLOR_CABECERA  = new Color( 37,  99, 235);
	static final Color COLOR_HEADER_FG = Color.WHITE;
	static final Color COLOR_ALTO      = new Color(254, 226, 226);
	static final Color COLOR_ALTO_FG   = new Color(185,  28,  28);
	static final Color COLOR_MEDIO     = new Color(254, 243, 199);
	static final Color COLOR_MEDIO_FG  = new Color(161, 100,   0);
	static final Color COLOR_BAJO      = new Color(220, 252, 231);
	static final Color COLOR_BAJO_FG   = new Color( 22, 163,  74);
	static final Color COLOR_BG        = new Color(249, 250, 251);

	// ── Opciones del combo Estado ─────────────────────────────────────────────
	static final String ESTADO_TODOS = "Todos";
	static final String ESTADO_ALTO  = "Alta ocupacion (>80%)";
	static final String ESTADO_MEDIO = "Ocupacion media (40-80%)";
	static final String ESTADO_BAJO  = "Baja ocupacion (<40%)";

	// ── Columnas de la tabla ──────────────────────────────────────────────────
	static final String[] COLUMNAS = {
		"Instalacion", "Actividad",
		"Ocup. por actividad", "Ocup. por socio",
		"Reservas activas", "Plazas libres", "Estado"
	};

	// ── Componentes ───────────────────────────────────────────────────────────
	private JFrame            frame;
	private JComboBox<String> cmbInstalacion;
	private JComboBox<String> cmbActividad;
	private JTextField        txtSocio;
	private JTextField        txtFechaInicio;
	private JTextField        txtFechaFin;
	private JComboBox<String> cmbEstado;
	private JButton           btnGenerar;
	private JButton           btnLimpiar;
	private JButton           btnExportar;
	private JTable            tabResultados;
	private JLabel            lblPeriodo;
	private JPanel            panelKpis;
	private JLabel            lblKpiInstalaciones;
	private JLabel            lblKpiReservas;
	private JLabel            lblKpiPlazas;
	private JLabel            lblKpiOcupacion;

	public InformeOcupacionView() {
		initialize();
	}

	// ── Construccion de la UI ─────────────────────────────────────────────────

	private void initialize() {
		frame = new JFrame("Informe de Ocupacion de Instalaciones - Administracion");
		frame.setName("InformeOcupacion");
		frame.setSize(1060, 700);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		frame.getContentPane().setBackground(COLOR_BG);

		frame.getContentPane().add(buildPanelNorth(),  BorderLayout.NORTH);
		frame.getContentPane().add(buildPanelCenter(), BorderLayout.CENTER);
		frame.getContentPane().add(buildPanelSouth(),  BorderLayout.SOUTH);
	}

	/** Panel superior: titulo + dos filas de filtros */
	private JPanel buildPanelNorth() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(229, 231, 235)),
				BorderFactory.createEmptyBorder(10, 16, 10, 16)));

		JLabel lblTitulo = new JLabel("Informe de Ocupacion de Instalaciones");
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 17));
		lblTitulo.setForeground(new Color(31, 41, 55));
		panel.add(lblTitulo);
		panel.add(Box.createVerticalStrut(8));

		// Fila 1: Instalacion | Actividad | Socio
		JPanel fila1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
		fila1.setBackground(Color.WHITE);

		fila1.add(makeLabel("Instalacion:"));
		cmbInstalacion = makeCombo("cmbInstalacion", 185);
		fila1.add(cmbInstalacion);

		fila1.add(makeLabel("Actividad:"));
		cmbActividad = makeCombo("cmbActividad", 185);
		fila1.add(cmbActividad);

		fila1.add(makeLabel("Socio:"));
		txtSocio = new JTextField(14);
		txtSocio.setName("txtSocio");
		txtSocio.setFont(new Font("SansSerif", Font.PLAIN, 13));
		fila1.add(txtSocio);

		panel.add(fila1);

		// Fila 2: Fecha inicio | Fecha fin | Estado | Botones
		JPanel fila2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
		fila2.setBackground(Color.WHITE);

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
		styleBoton(btnGenerar, COLOR_CABECERA);
		fila2.add(btnGenerar);

		btnLimpiar = new JButton("Limpiar filtros");
		btnLimpiar.setName("btnLimpiar");
		styleBoton(btnLimpiar, new Color(107, 114, 128));
		fila2.add(btnLimpiar);

		panel.add(fila2);
		return panel;
	}

	/** Panel central: KPIs + tabla + boton exportar */
	private JPanel buildPanelCenter() {
		JPanel panel = new JPanel(new BorderLayout(0, 8));
		panel.setBackground(COLOR_BG);
		panel.setBorder(BorderFactory.createEmptyBorder(10, 12, 4, 12));

		// -- KPIs (4 cajas en fila, ocultas hasta generar) --
		panelKpis = new JPanel(new GridLayout(1, 4, 12, 0));
		panelKpis.setBackground(COLOR_BG);
		panelKpis.setVisible(false);

		lblKpiInstalaciones = new JLabel("—", SwingConstants.CENTER);
		lblKpiReservas       = new JLabel("—", SwingConstants.CENTER);
		lblKpiPlazas         = new JLabel("—", SwingConstants.CENTER);
		lblKpiOcupacion      = new JLabel("—", SwingConstants.CENTER);

		panelKpis.add(buildKpiBox(lblKpiInstalaciones, "Instalaciones"));
		panelKpis.add(buildKpiBox(lblKpiReservas,       "Reservas activas"));
		panelKpis.add(buildKpiBox(lblKpiPlazas,         "Plazas libres totales"));
		panelKpis.add(buildKpiBox(lblKpiOcupacion,      "Ocupacion media"));

		panel.add(panelKpis, BorderLayout.NORTH);

		// -- Tabla --
		tabResultados = new JTable();
		tabResultados.setName("tabResultados");
		tabResultados.setRowHeight(30);
		tabResultados.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tabResultados.setDefaultEditor(Object.class, null);
		tabResultados.setShowGrid(true);
		tabResultados.setGridColor(new Color(229, 231, 235));
		tabResultados.setDefaultRenderer(Object.class, new OcupacionCellRenderer());
		tabResultados.setFillsViewportHeight(true);
		tabResultados.getTableHeader().setBackground(COLOR_CABECERA);
		tabResultados.getTableHeader().setForeground(COLOR_HEADER_FG);
		tabResultados.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
		tabResultados.getTableHeader().setPreferredSize(new Dimension(0, 36));
		tabResultados.getTableHeader().setReorderingAllowed(false);

		JScrollPane scroll = new JScrollPane(tabResultados);
		scroll.setBorder(BorderFactory.createLineBorder(new Color(209, 213, 219)));
		scroll.getViewport().setBackground(Color.WHITE);
		panel.add(scroll, BorderLayout.CENTER);

		// -- Boton exportar (bajo la tabla, alineado a la derecha) --
		JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 4));
		panelAcciones.setBackground(COLOR_BG);
		btnExportar = new JButton("Descargar .txt");
		btnExportar.setName("btnExportar");
		styleBoton(btnExportar, new Color(22, 163, 74));
		btnExportar.setEnabled(false);
		panelAcciones.add(btnExportar);
		panel.add(panelAcciones, BorderLayout.SOUTH);

		return panel;
	}

	/** Panel inferior: etiqueta de periodo */
	private JPanel buildPanelSouth() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(229, 231, 235)),
				BorderFactory.createEmptyBorder(6, 16, 6, 16)));
		lblPeriodo = new JLabel(" ");
		lblPeriodo.setName("lblPeriodo");
		lblPeriodo.setFont(new Font("SansSerif", Font.ITALIC, 12));
		lblPeriodo.setForeground(new Color(107, 114, 128));
		panel.add(lblPeriodo, BorderLayout.WEST);
		return panel;
	}

	/** Caja KPI individual: valor grande + etiqueta pequena */
	private JPanel buildKpiBox(JLabel lblValor, String etiqueta) {
		JPanel box = new JPanel();
		box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
		box.setBackground(Color.WHITE);
		box.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(229, 231, 235)),
				BorderFactory.createEmptyBorder(10, 14, 10, 14)));

		lblValor.setFont(new Font("SansSerif", Font.BOLD, 26));
		lblValor.setForeground(new Color(31, 41, 55));
		lblValor.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel lbl = new JLabel(etiqueta, SwingConstants.CENTER);
		lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lbl.setForeground(new Color(107, 114, 128));
		lbl.setAlignmentX(Component.CENTER_ALIGNMENT);

		box.add(lblValor);
		box.add(Box.createVerticalStrut(2));
		box.add(lbl);
		return box;
	}

	// ── Renderer de celdas ────────────────────────────────────────────────────

	static class OcupacionCellRenderer extends DefaultTableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int col) {

			JLabel lbl = (JLabel) super.getTableCellRendererComponent(
					table, value, isSelected, hasFocus, row, col);
			lbl.setOpaque(true);
			lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
			lbl.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));

			String texto = value != null ? value.toString().trim() : "";

			if (col == 2 || col == 3) {
				int pct = parsePct(texto);
				colorearNivel(lbl, pct, isSelected);
				lbl.setHorizontalAlignment(SwingConstants.CENTER);
				lbl.setFont(new Font("SansSerif", Font.BOLD, 12));

			} else if (col == 6) {
				// Columna Estado: pill
				lbl.setHorizontalAlignment(SwingConstants.CENTER);
				lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
				if ("Alta".equals(texto)) {
					lbl.setBackground(isSelected ? COLOR_ALTO_FG : COLOR_ALTO);
					lbl.setForeground(COLOR_ALTO_FG);
				} else if ("Media".equals(texto)) {
					lbl.setBackground(isSelected ? COLOR_MEDIO_FG : COLOR_MEDIO);
					lbl.setForeground(COLOR_MEDIO_FG);
				} else {
					lbl.setBackground(isSelected ? COLOR_BAJO_FG : COLOR_BAJO);
					lbl.setForeground(COLOR_BAJO_FG);
				}

			} else if (col == 4 || col == 5) {
				lbl.setHorizontalAlignment(SwingConstants.CENTER);
				resetDefault(lbl, isSelected);

			} else {
				lbl.setHorizontalAlignment(SwingConstants.LEFT);
				resetDefault(lbl, isSelected);
			}
			return lbl;
		}

		private void colorearNivel(JLabel lbl, int pct, boolean sel) {
			if (pct >= 80) {
				lbl.setBackground(sel ? COLOR_ALTO_FG  : COLOR_ALTO);
				lbl.setForeground(COLOR_ALTO_FG);
			} else if (pct >= 40) {
				lbl.setBackground(sel ? COLOR_MEDIO_FG : COLOR_MEDIO);
				lbl.setForeground(COLOR_MEDIO_FG);
			} else {
				lbl.setBackground(sel ? COLOR_BAJO_FG  : COLOR_BAJO);
				lbl.setForeground(COLOR_BAJO_FG);
			}
		}

		private void resetDefault(JLabel lbl, boolean sel) {
			lbl.setBackground(sel ? new Color(219, 234, 254) : Color.WHITE);
			lbl.setForeground(new Color(31, 41, 55));
		}

		private int parsePct(String s) {
			try { return Integer.parseInt(s.replace("%", "").trim()); }
			catch (NumberFormatException e) { return 0; }
		}
	}

	// ── Utilidades privadas ───────────────────────────────────────────────────

	private JLabel makeLabel(String texto) {
		JLabel l = new JLabel(texto);
		l.setFont(new Font("SansSerif", Font.BOLD, 12));
		l.setForeground(new Color(55, 65, 81));
		return l;
	}

	private JComboBox<String> makeCombo(String name, int width) {
		JComboBox<String> cmb = new JComboBox<>();
		cmb.setName(name);
		cmb.setPreferredSize(new Dimension(width, 28));
		cmb.setFont(new Font("SansSerif", Font.PLAIN, 13));
		return cmb;
	}

	private void styleBoton(JButton btn, Color bg) {
		btn.setBackground(bg);
		btn.setForeground(Color.WHITE);
		btn.setFont(new Font("SansSerif", Font.BOLD, 13));
		btn.setFocusPainted(false);
		btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		btn.setBorder(BorderFactory.createEmptyBorder(5, 14, 5, 14));
	}

	// ── API para el Controlador ───────────────────────────────────────────────

	public JFrame             getFrame()           { return frame;          }
	public JComboBox<String>  getCmbInstalacion()  { return cmbInstalacion; }
	public JComboBox<String>  getCmbActividad()    { return cmbActividad;   }
	public JTextField         getTxtSocio()        { return txtSocio;       }
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
		for (InstalacionEntity inst : instalaciones)
			cmbInstalacion.addItem(inst.getNombre());
	}

	public void setActividades(List<ActividadEntity> actividades) {
		cmbActividad.removeAllItems();
		cmbActividad.addItem("Todas");
		for (ActividadEntity act : actividades)
			cmbActividad.addItem(act.getNombre());
	}

	/**
	 * Carga las filas filtradas en la tabla.
	 * Plazas libres con formato "N / aforo". Columna Estado: "Alta"/"Media"/"Baja".
	 */
	public void setFilas(List<OcupacionFilaDTO> filas) {
		DefaultTableModel modelo = new DefaultTableModel(COLUMNAS, 0) {
			@Override public boolean isCellEditable(int r, int c) { return false; }
			@Override public Class<?> getColumnClass(int c)        { return String.class; }
		};
		for (OcupacionFilaDTO f : filas) {
			int    pctAct = f.getPorcentajeActividad();
			String estado = pctAct >= 80 ? "Alta" : pctAct >= 40 ? "Media" : "Baja";
			modelo.addRow(new Object[] {
				f.getNombreInstalacion(),
				f.getNombreActividad(),
				pctAct + "%",
				f.getPorcentajeSocio() + "%",
				f.getReservasActivas(),
				f.getPlazasLibres() + " / " + f.getAforoActividad(),
				estado
			});
		}
		tabResultados.setModel(modelo);
		int[] anchos = { 165, 155, 115, 105, 95, 85, 65 };
		for (int i = 0; i < anchos.length && i < tabResultados.getColumnCount(); i++)
			tabResultados.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
		tabResultados.revalidate();
		tabResultados.repaint();
	}

	/** Actualiza los 4 KPIs y muestra el panel */
	public void setKpis(int numInstalaciones, int totalReservas,
			int totalPlazasLibres, int mediaOcupacion) {
		lblKpiInstalaciones.setText(String.valueOf(numInstalaciones));
		lblKpiReservas.setText(String.valueOf(totalReservas));
		lblKpiPlazas.setText(String.valueOf(totalPlazasLibres));
		lblKpiOcupacion.setText(mediaOcupacion + "%");
		panelKpis.setVisible(true);
		panelKpis.revalidate();
	}

	public void setPeriodo(String fechaInicio, String fechaFin) {
		lblPeriodo.setText("Periodo consultado: " + fechaInicio + "  ->  " + fechaFin);
		lblPeriodo.setForeground(new Color(107, 114, 128));
	}

	public void setError(String mensaje) {
		lblPeriodo.setText("Error: " + mensaje);
		lblPeriodo.setForeground(COLOR_ALTO_FG);
	}

	/** Limpia filtros, oculta KPIs y vacia la tabla */
	public void limpiarFiltros(String fechaInicioDefecto, String fechaFinDefecto) {
		cmbInstalacion.setSelectedIndex(0);
		cmbActividad.setSelectedIndex(0);
		cmbEstado.setSelectedIndex(0);
		txtSocio.setText("");
		txtFechaInicio.setText(fechaInicioDefecto);
		txtFechaFin.setText(fechaFinDefecto);
		tabResultados.setModel(new DefaultTableModel());
		panelKpis.setVisible(false);
		lblPeriodo.setText(" ");
		btnExportar.setEnabled(false);
	}

	// getters de filtros
	public String getInstalacionSeleccionada() {
		Object s = cmbInstalacion.getSelectedItem(); return s != null ? s.toString() : "Todas";
	}
	public String getActividadSeleccionada() {
		Object s = cmbActividad.getSelectedItem(); return s != null ? s.toString() : "Todas";
	}
	public String getSocioFiltro()     { return txtSocio.getText().trim(); }
	public String getEstadoSeleccionado() {
		Object s = cmbEstado.getSelectedItem(); return s != null ? s.toString() : ESTADO_TODOS;
	}
}
