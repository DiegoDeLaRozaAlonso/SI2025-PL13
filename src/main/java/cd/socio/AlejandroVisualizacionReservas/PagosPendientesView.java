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

	private static final Color COLOR_HEADER_BG = new Color(220, 220, 220);
	private static final Color COLOR_BG        = new Color(240, 240, 240);

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
		panel.setBackground(COLOR_HEADER_BG);
		panel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY),
				BorderFactory.createEmptyBorder(14, 20, 14, 20)));

		JPanel izq = new JPanel();
		izq.setOpaque(false);
		izq.setLayout(new BoxLayout(izq, BoxLayout.Y_AXIS));

		lblIdSocio = new JLabel("Socio #----");
		lblIdSocio.setFont(new Font("SansSerif", Font.PLAIN, 11));
		lblIdSocio.setForeground(Color.DARK_GRAY);

		lblNombreSocio = new JLabel("Nombre del Socio");
		lblNombreSocio.setFont(new Font("SansSerif", Font.BOLD, 20));
		lblNombreSocio.setForeground(Color.BLACK);

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
		panel.setBorder(BorderFactory.createEmptyBorder(14, 20, 8, 20));

		// Panel de alerta
		JPanel panelAlerta = new JPanel(new BorderLayout());
		panelAlerta.setBackground(new Color(255, 255, 220));
		panelAlerta.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.GRAY),
				BorderFactory.createEmptyBorder(8, 12, 8, 12)));
		panelAlerta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
		panelAlerta.setAlignmentX(Component.LEFT_ALIGNMENT);

		lblAlerta = new JLabel("⚠  Tienes 0 cargos pendientes de pago.");
		lblAlerta.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblAlerta.setForeground(Color.DARK_GRAY);
		panelAlerta.add(lblAlerta, BorderLayout.CENTER);

		panel.add(panelAlerta);
		panel.add(Box.createVerticalStrut(12));

		// Tabla estilo Swing por defecto
		tabCargos = new JTable();
		tabCargos.setName("tabCargos");
		tabCargos.setRowHeight(24);

		JScrollPane scroll = new JScrollPane(tabCargos);
		scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(scroll);

		return panel;
	}

	// ── Panel inferior con el total ───────────────────────────────────────────

	private JPanel buildPanelTotal() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(COLOR_HEADER_BG);
		panel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY),
				BorderFactory.createEmptyBorder(10, 20, 12, 20)));

		JLabel lblTexto = new JLabel("TOTAL PENDIENTE DE PAGO");
		lblTexto.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblTexto.setForeground(Color.DARK_GRAY);

		lblTotalImporte = new JLabel("0,00 €");
		lblTotalImporte.setFont(new Font("SansSerif", Font.BOLD, 18));
		lblTotalImporte.setForeground(Color.BLACK);
		lblTotalImporte.setHorizontalAlignment(SwingConstants.RIGHT);

		panel.add(lblTexto,        BorderLayout.WEST);
		panel.add(lblTotalImporte, BorderLayout.EAST);
		return panel;
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