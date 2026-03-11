package cd.socio.AlejandroVisualizacionReservas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Vista de la pantalla de pagos pendientes de un socio.
 * Muestra la cabecera con el nombre e identificador del socio,
 * un panel de alerta con el numero de cargos pendientes,
 * una tabla con el detalle de cada cargo (tipo, descripcion, fecha, importe)
 * y un panel inferior con el total pendiente de pago.
 *
 * Sigue el patron MVC: no incluye logica de negocio ni manejadores de eventos.
 */
public class PagosPendientesView {

	// Colores
	private static final Color COLOR_NAVY       = new Color( 26,  41,  64);
	private static final Color COLOR_GOLD       = new Color(200, 168,  75);
	private static final Color COLOR_GOLD_LIGHT = new Color(255, 248, 230);
	private static final Color COLOR_RED        = new Color(192,  23,  75);
	private static final Color COLOR_BLUE_BADGE = new Color(232, 240, 254);
	private static final Color COLOR_BLUE_TXT   = new Color( 26,  86, 219);
	private static final Color COLOR_BG         = new Color(244, 241, 236);

	private JFrame frame;
	private JTable tabCargos;
	private JLabel lblNombreSocio;
	private JLabel lblIdSocio;
	private JLabel lblAlerta;
	private JLabel lblTotalImporte;

	public PagosPendientesView() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame("Pagos Pendientes");
		frame.setName("PagosPendientes");
		frame.setBounds(0, 0, 700, 480);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		frame.getContentPane().setBackground(COLOR_BG);

		frame.getContentPane().add(buildPanelHeader(), BorderLayout.NORTH);
		frame.getContentPane().add(buildPanelCentro(), BorderLayout.CENTER);
		frame.getContentPane().add(buildPanelTotal(),  BorderLayout.SOUTH);
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

		lblIdSocio = new JLabel("Socio #----");
		lblIdSocio.setFont(new Font("Monospaced", Font.PLAIN, 11));
		lblIdSocio.setForeground(COLOR_GOLD);

		lblNombreSocio = new JLabel("Nombre del Socio");
		lblNombreSocio.setFont(new Font("SansSerif", Font.BOLD, 22));
		lblNombreSocio.setForeground(Color.WHITE);

		izq.add(lblIdSocio);
		izq.add(Box.createVerticalStrut(4));
		izq.add(lblNombreSocio);

		panel.add(izq, BorderLayout.CENTER);
		return panel;
	}

	// ── Centro: alerta + tabla ────────────────────────────────────────────────

	private JPanel buildPanelCentro() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(COLOR_BG);
		panel.setBorder(BorderFactory.createEmptyBorder(16, 24, 8, 24));

		// Panel de alerta
		JPanel panelAlerta = new JPanel(new BorderLayout());
		panelAlerta.setBackground(COLOR_GOLD_LIGHT);
		panelAlerta.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(COLOR_GOLD),
				BorderFactory.createEmptyBorder(10, 14, 10, 14)));
		panelAlerta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
		panelAlerta.setAlignmentX(Component.LEFT_ALIGNMENT);

		lblAlerta = new JLabel("⚠  Tienes 0 cargos pendientes de pago.");
		lblAlerta.setFont(new Font("Monospaced", Font.PLAIN, 13));
		lblAlerta.setForeground(new Color(122, 92, 0));
		panelAlerta.add(lblAlerta, BorderLayout.CENTER);

		panel.add(panelAlerta);
		panel.add(Box.createVerticalStrut(16));

		// Tabla
		tabCargos = new JTable();
		tabCargos.setName("tabCargos");
		tabCargos.setFont(new Font("SansSerif", Font.PLAIN, 13));
		tabCargos.setRowHeight(38);
		tabCargos.setGridColor(new Color(238, 238, 238));
		tabCargos.setShowVerticalLines(false);
		tabCargos.setSelectionBackground(new Color(252, 232, 240));
		tabCargos.getTableHeader().setBackground(COLOR_NAVY);
		tabCargos.getTableHeader().setForeground(COLOR_GOLD);
		tabCargos.getTableHeader().setFont(new Font("Monospaced", Font.PLAIN, 11));
		tabCargos.getTableHeader().setReorderingAllowed(false);
		tabCargos.setDefaultRenderer(Object.class, new CargoCellRenderer());

		JScrollPane scroll = new JScrollPane(tabCargos);
		scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
		scroll.setBorder(BorderFactory.createLineBorder(new Color(221, 221, 221)));
		panel.add(scroll);

		return panel;
	}

	// ── Panel inferior con el total ───────────────────────────────────────────

	private JPanel buildPanelTotal() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(COLOR_BG);
		panel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(2, 0, 0, 0, COLOR_NAVY),
				BorderFactory.createEmptyBorder(12, 24, 14, 24)));

		JLabel lblTexto = new JLabel("TOTAL PENDIENTE DE PAGO");
		lblTexto.setFont(new Font("Monospaced", Font.BOLD, 13));
		lblTexto.setForeground(COLOR_NAVY);

		lblTotalImporte = new JLabel("0,00 €");
		lblTotalImporte.setFont(new Font("Monospaced", Font.BOLD, 20));
		lblTotalImporte.setForeground(COLOR_RED);
		lblTotalImporte.setHorizontalAlignment(SwingConstants.RIGHT);

		panel.add(lblTexto,        BorderLayout.WEST);
		panel.add(lblTotalImporte, BorderLayout.EAST);
		return panel;
	}

	// ── Renderer de celdas ────────────────────────────────────────────────────

	static class CargoCellRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			setFont(new Font("SansSerif", Font.PLAIN, 13));
			setBackground(isSelected ? new Color(252, 232, 240)
					: (row % 2 == 0 ? new Color(250, 250, 248) : Color.WHITE));
			setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(238, 238, 238)));
			setForeground(new Color(51, 51, 51));
			setHorizontalAlignment(SwingConstants.LEFT);

			// Columna Tipo: badge de color
			if (column == 0 && value != null) {
				String tipo = value.toString();
				if ("actividad".equalsIgnoreCase(tipo)) {
					setBackground(COLOR_BLUE_BADGE);
					setForeground(COLOR_BLUE_TXT);
					setFont(new Font("Monospaced", Font.BOLD, 10));
					setText("ACTIVIDAD");
				} else if ("reserva".equalsIgnoreCase(tipo)) {
					setBackground(new Color(252, 232, 240));
					setForeground(new Color(192, 23, 75));
					setFont(new Font("Monospaced", Font.BOLD, 10));
					setText("RESERVA");
				}
			}

			// Columna Importe: rojo monoespaciado alineado a la derecha
			if (column == 3) {
				setFont(new Font("Monospaced", Font.BOLD, 14));
				setForeground(new Color(192, 23, 75));
				setHorizontalAlignment(SwingConstants.RIGHT);
			}

			return this;
		}
	}

	// ── Getters para el controlador ───────────────────────────────────────────

	public JFrame getFrame() { return this.frame; }

	/** Actualiza la cabecera con los datos del socio */
	public void setSocio(int idSocio, String nombre) {
		lblIdSocio.setText("Socio #" + String.format("%04d", idSocio));
		lblNombreSocio.setText(nombre);
	}

	/** Rellena la tabla con la lista de cargos y actualiza alerta y total */
	public void setCargos(List<CargoPendienteDTO> cargos) {
		DefaultTableModel model = new DefaultTableModel(
				new String[]{"Tipo", "Descripción", "Fecha", "Importe"}, 0) {
			@Override public boolean isCellEditable(int r, int c) { return false; }
		};

		double total = 0;
		for (CargoPendienteDTO c : cargos) {
			model.addRow(new Object[]{c.getTipo(), c.getDescripcion(),
					c.getFecha(), c.getImporteFormateado()});
			total += c.getImporte();
		}
		tabCargos.setModel(model);

		// Ajuste de anchos de columna
		tabCargos.getColumnModel().getColumn(0).setPreferredWidth(90);
		tabCargos.getColumnModel().getColumn(0).setMaxWidth(90);
		tabCargos.getColumnModel().getColumn(2).setPreferredWidth(110);
		tabCargos.getColumnModel().getColumn(2).setMaxWidth(110);
		tabCargos.getColumnModel().getColumn(3).setPreferredWidth(100);
		tabCargos.getColumnModel().getColumn(3).setMaxWidth(100);

		// Actualizar alerta
		int n = cargos.size();
		lblAlerta.setText("⚠  Tienes " + n + " cargo" + (n == 1 ? "" : "s")
				+ " pendiente" + (n == 1 ? "" : "s") + " de pago."
				+ (n > 0 ? " Regulariza tu situacion para seguir disfrutando de las actividades." : ""));

		// Actualizar total
		lblTotalImporte.setText(String.format("%.2f €", total).replace(".", ","));
	}
}
