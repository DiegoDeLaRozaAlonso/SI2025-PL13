package cd.admin.Alejandro.ResInstalacion;

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
 * Vista de la pantalla de visualizacion del calendario de reservas para la administracion del centro.
 * Muestra un grid semanal (filas = horas, columnas = dias) con tres estados posibles por celda:
 * libre (verde), reserva de socio (azul) y actividad del centro (morado).
 *
 * <br/>Generada siguiendo el patron MVC: no incluye logica de negocio ni manejadores de eventos;
 * estos van en el controlador. Se han anyadido al final los metodos de acceso necesarios para el controlador.
 */
public class VisualizacionReservasView {

	// Colores que replican los del prototipo HTML
	static final Color COLOR_LIBRE      = new Color(220, 252, 231);  // verde claro
	static final Color COLOR_LIBRE_BRD  = new Color(74,  222, 128);  // borde verde
	static final Color COLOR_SOCIO      = new Color(96,  165, 250);  // azul
	static final Color COLOR_SOCIO_BRD  = new Color(37,  99,  235);  // borde azul oscuro
	static final Color COLOR_ACTIVIDAD  = new Color(167, 139, 250);  // morado
	static final Color COLOR_ACT_BRD    = new Color(124,  58, 237);  // borde morado oscuro
	static final Color COLOR_CABECERA   = new Color(37,   99, 235);  // azul cabecera tabla
	static final Color COLOR_HEADER_FG  = Color.WHITE;

	private JFrame frame;
	private JComboBox<String>  cmbInstalacion;
	private JButton            btnSemanaAnterior;
	private JButton            btnSemanaSiguiente;
	private JLabel             lblSemana;
	private JTable             tabGrid;
	private JScrollPane        scrollGrid;

	// Leyenda (solo visual, no interactiva)
	private JPanel panelLeyenda;

	public VisualizacionReservasView() {
		initialize();
	}

	private void initialize() {
		// ── Frame principal ─────────────────────────────────────────────────
		frame = new JFrame("Visualizacion de Reservas - Administracion");
		frame.setName("VisualizacionReservas");
		frame.setBounds(0, 0, 900, 600);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 8));
		frame.getContentPane().setBackground(new Color(249, 250, 251));

		// ── Panel superior: titulo + selector de instalacion ────────────────
		JPanel panelHeader = new JPanel();
		panelHeader.setLayout(new BoxLayout(panelHeader, BoxLayout.Y_AXIS));
		panelHeader.setBackground(Color.WHITE);
		panelHeader.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(229, 231, 235)),
				BorderFactory.createEmptyBorder(12, 16, 12, 16)));

		JLabel lblTitulo = new JLabel("Visualizacion de Reservas - Administracion");
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
		lblTitulo.setForeground(new Color(31, 41, 55));
		lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelHeader.add(lblTitulo);
		panelHeader.add(Box.createVerticalStrut(8));

		JPanel panelInstalacion = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		panelInstalacion.setBackground(Color.WHITE);
		panelInstalacion.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel lblInstalacion = new JLabel("Instalacion:");
		lblInstalacion.setFont(new Font("SansSerif", Font.BOLD, 13));
		cmbInstalacion = new JComboBox<>();
		cmbInstalacion.setName("cmbInstalacion");
		cmbInstalacion.setPreferredSize(new Dimension(220, 28));
		panelInstalacion.add(lblInstalacion);
		panelInstalacion.add(cmbInstalacion);
		panelHeader.add(panelInstalacion);

		frame.getContentPane().add(panelHeader, BorderLayout.NORTH);

		// ── Panel central: leyenda + navegacion + tabla ─────────────────────
		JPanel panelCentral = new JPanel();
		panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
		panelCentral.setBackground(new Color(249, 250, 251));
		panelCentral.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

		// Leyenda
		panelLeyenda = buildPanelLeyenda();
		panelLeyenda.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelCentral.add(panelLeyenda);
		panelCentral.add(Box.createVerticalStrut(8));

		// Navegacion semanal
		JPanel panelNav = new JPanel(new BorderLayout(10, 0));
		panelNav.setBackground(new Color(249, 250, 251));
		panelNav.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		panelNav.setAlignmentX(Component.LEFT_ALIGNMENT);

		btnSemanaAnterior = new JButton("<- Semana Anterior");
		btnSemanaAnterior.setName("btnSemanaAnterior");
		btnSemanaAnterior.setBackground(COLOR_CABECERA);
		btnSemanaAnterior.setForeground(Color.WHITE);
		btnSemanaAnterior.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnSemanaAnterior.setFocusPainted(false);

		lblSemana = new JLabel("Semana 1 de 5", SwingConstants.CENTER);
		lblSemana.setName("lblSemana");
		lblSemana.setFont(new Font("SansSerif", Font.BOLD, 13));
		lblSemana.setForeground(new Color(55, 65, 81));

		btnSemanaSiguiente = new JButton("Semana Siguiente ->");
		btnSemanaSiguiente.setName("btnSemanaSiguiente");
		btnSemanaSiguiente.setBackground(COLOR_CABECERA);
		btnSemanaSiguiente.setForeground(Color.WHITE);
		btnSemanaSiguiente.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnSemanaSiguiente.setFocusPainted(false);

		panelNav.add(btnSemanaAnterior,  BorderLayout.WEST);
		panelNav.add(lblSemana,          BorderLayout.CENTER);
		panelNav.add(btnSemanaSiguiente, BorderLayout.EAST);
		panelCentral.add(panelNav);
		panelCentral.add(Box.createVerticalStrut(8));

		// Tabla-calendario
		tabGrid = new JTable();
		tabGrid.setName("tabGrid");
		tabGrid.setRowHeight(36);
		tabGrid.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tabGrid.setDefaultEditor(Object.class, null); // readonly
		tabGrid.setShowGrid(true);
		tabGrid.setGridColor(new Color(229, 231, 235));
		tabGrid.setDefaultRenderer(Object.class, new ReservaCellRenderer());

		// Cabecera con fondo azul
		tabGrid.getTableHeader().setBackground(COLOR_CABECERA);
		tabGrid.getTableHeader().setForeground(COLOR_HEADER_FG);
		tabGrid.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
		tabGrid.getTableHeader().setPreferredSize(new Dimension(0, 52));
		tabGrid.getTableHeader().setDefaultRenderer(new CabeceraRenderer());

		scrollGrid = new JScrollPane(tabGrid);
		scrollGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelCentral.add(scrollGrid);

		frame.getContentPane().add(panelCentral, BorderLayout.CENTER);
	}

	/** Construye el panel de leyenda con los tres tipos de celda */
	private JPanel buildPanelLeyenda() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 4));
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(229, 231, 235)),
				BorderFactory.createEmptyBorder(6, 12, 6, 12)));

		panel.add(buildItemLeyenda(COLOR_LIBRE,     COLOR_LIBRE_BRD,     "",   "Libre"));
		panel.add(buildItemLeyenda(COLOR_SOCIO,     COLOR_SOCIO_BRD,     "S",  "Reserva de Socio"));
		panel.add(buildItemLeyenda(COLOR_ACTIVIDAD, COLOR_ACT_BRD,       "A",  "Actividad del Centro"));
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
		cuadro.setPreferredSize(new Dimension(28, 28));

		JLabel etiq = new JLabel(texto);
		etiq.setFont(new Font("SansSerif", Font.BOLD, 12));
		etiq.setForeground(new Color(55, 65, 81));

		item.add(cuadro);
		item.add(etiq);
		return item;
	}

	// ── Renderer para las celdas del grid ───────────────────────────────────

	/**
	 * Renderer que colorea cada celda segun su tipo (libre/socio/actividad).
	 * Tambien configura el tooltip mostrado al pasar el raton.
	 */
	static class ReservaCellRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {

			JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			lbl.setHorizontalAlignment(SwingConstants.CENTER);
			lbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
			lbl.setBorder(BorderFactory.createLineBorder(new Color(209, 213, 219), 1));

			if (column == 0) {
				// Columna de horas: fondo blanco, texto en negrita
				lbl.setText(value != null ? value.toString() : "");
				lbl.setBackground(Color.WHITE);
				lbl.setForeground(new Color(55, 65, 81));
				lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
				lbl.setToolTipText(null);
				return lbl;
			}

			if (value instanceof ReservaCeldaDTO) {
				ReservaCeldaDTO celda = (ReservaCeldaDTO) value;
				switch (celda.getTipo()) {
					case "socio":
						lbl.setBackground(COLOR_SOCIO);
						lbl.setForeground(Color.WHITE);
						lbl.setBorder(BorderFactory.createLineBorder(COLOR_SOCIO_BRD, 2));
						lbl.setText("[S] " + abreviar(celda.getNombre(), 14));
						lbl.setToolTipText("<html><b>Reserva de Socio</b><br/>"
								+ celda.getNombre() + "<br/><i>"
								+ celda.getHora() + " | " + celda.getFecha() + "</i></html>");
						break;
					case "actividad":
						lbl.setBackground(COLOR_ACTIVIDAD);
						lbl.setForeground(Color.WHITE);
						lbl.setBorder(BorderFactory.createLineBorder(COLOR_ACT_BRD, 2));
						lbl.setText("[A] " + abreviar(celda.getNombre(), 14));
						lbl.setToolTipText("<html><b>Actividad del Centro</b><br/>"
								+ celda.getNombre() + "<br/><i>"
								+ celda.getHora() + " | " + celda.getFecha() + "</i></html>");
						break;
					default: // libre
						lbl.setBackground(COLOR_LIBRE);
						lbl.setForeground(new Color(22, 163, 74));
						lbl.setBorder(BorderFactory.createLineBorder(COLOR_LIBRE_BRD, 1));
						lbl.setText("");
						lbl.setToolTipText("Libre");
						break;
				}
			} else {
				lbl.setBackground(Color.WHITE);
				lbl.setForeground(Color.BLACK);
				lbl.setToolTipText(null);
			}
			return lbl;
		}

		private String abreviar(String texto, int max) {
			if (texto == null) return "";
			return texto.length() <= max ? texto : texto.substring(0, max) + "...";
		}
	}

	// ── Renderer para la cabecera: dia de semana + numero de dia + mes ──────

	static class CabeceraRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {

			JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			lbl.setHorizontalAlignment(SwingConstants.CENTER);
			lbl.setBackground(COLOR_CABECERA);
			lbl.setForeground(COLOR_HEADER_FG);
			lbl.setOpaque(true);
			lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(59, 130, 246)));

			if (column == 0) {
				lbl.setText("<html><b>Hora</b></html>");
				lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
			} else if (value instanceof Date) {
				Date fecha = (Date) value;
				SimpleDateFormat fmtDia  = new SimpleDateFormat("d",       new Locale("es", "ES"));
				SimpleDateFormat fmtSem  = new SimpleDateFormat("EEE",     new Locale("es", "ES"));
				SimpleDateFormat fmtMes  = new SimpleDateFormat("MMM",     new Locale("es", "ES"));
				lbl.setText("<html><center><small>" + fmtSem.format(fecha).toUpperCase()
						+ "</small><br/><b style='font-size:14px'>" + fmtDia.format(fecha) + "</b>"
						+ "<br/><small>" + fmtMes.format(fecha) + "</small></center></html>");
			} else {
				lbl.setText(value != null ? value.toString() : "");
			}
			return lbl;
		}
	}

	// ── Getters y Setters para el Controlador ────────────────────────────────

	public JFrame getFrame()                            { return this.frame; }
	public JComboBox<String> getCmbInstalacion()        { return this.cmbInstalacion; }
	public JButton getBtnSemanaAnterior()               { return this.btnSemanaAnterior; }
	public JButton getBtnSemanaSiguiente()              { return this.btnSemanaSiguiente; }
	public JLabel getLblSemana()                        { return this.lblSemana; }
	public JTable getTabGrid()                          { return this.tabGrid; }
	public JScrollPane getScrollGrid()                  { return this.scrollGrid; }

	/** Rellena el combo con los nombres de las instalaciones */
	public void setInstalaciones(List<InstalacionEntity> instalaciones) {
		cmbInstalacion.removeAllItems();
		for (InstalacionEntity inst : instalaciones)
			cmbInstalacion.addItem(inst.getNombre());
	}

	/** Devuelve el nombre de la instalacion actualmente seleccionada en el combo */
	public String getInstalacionSeleccionada() {
		Object sel = cmbInstalacion.getSelectedItem();
		return sel != null ? sel.toString() : "";
	}

	/** Actualiza el texto del indicador de semana */
	public void setSemanaLabel(int semanaActual, int totalSemanas) {
		lblSemana.setText("Semana " + semanaActual + " de " + totalSemanas);
	}

	/** Activa o desactiva los botones de navegacion segun la posicion actual */
	public void setNavegacionHabilitada(boolean anteriorHabilitado, boolean siguienteHabilitado) {
		btnSemanaAnterior.setEnabled(anteriorHabilitado);
		btnSemanaSiguiente.setEnabled(siguienteHabilitado);
		btnSemanaAnterior.setBackground(anteriorHabilitado  ? COLOR_CABECERA : new Color(156, 163, 175));
		btnSemanaSiguiente.setBackground(siguienteHabilitado ? COLOR_CABECERA : new Color(156, 163, 175));
	}

	/**
	 * Actualiza la tabla del calendario con los datos del grid y las fechas de la semana.
	 *
	 * @param grid      matriz [hora][dia] de ReservaCeldaDTO
	 * @param fechas    lista de fechas (Date) correspondientes a las columnas
	 * @param horas     array de strings con las etiquetas de hora ("08:00", etc.)
	 */
	public void setGrid(ReservaCeldaDTO[][] grid, List<java.util.Date> fechas, String[] horas) {
		int numDias = fechas.size();

		// Construimos el DefaultTableModel con las fechas (Date) como nombre de columna
		// El renderer de cabecera sabra formatearlas
		Object[] columnNames = new Object[numDias + 1];
		columnNames[0] = "Hora";
		for (int d = 0; d < numDias; d++)
			columnNames[d + 1] = fechas.get(d);

		Object[][] data = new Object[horas.length][numDias + 1];
		for (int h = 0; h < horas.length; h++) {
			data[h][0] = horas[h]; // columna de hora como String
			for (int d = 0; d < numDias; d++)
				data[h][d + 1] = grid[h][d]; // ReservaCeldaDTO
		}

		DefaultTableModel model = new DefaultTableModel(data, columnNames) {
			@Override public boolean isCellEditable(int row, int column) { return false; }
		};
		tabGrid.setModel(model);

		// Ajustar anchos de columna
		tabGrid.getColumnModel().getColumn(0).setPreferredWidth(60);
		for (int d = 1; d <= numDias; d++)
			tabGrid.getColumnModel().getColumn(d).setPreferredWidth(110);

		// Forzar uso del renderer de cabecera personalizado en todas las columnas
		for (int i = 0; i < tabGrid.getColumnModel().getColumnCount(); i++)
			tabGrid.getColumnModel().getColumn(i).setHeaderRenderer(new CabeceraRenderer());
	}
}
