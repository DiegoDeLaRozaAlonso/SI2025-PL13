package cd.admi.Alejandro.Visualizacion;

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

/**
 * Vista de la pantalla de reserva de instalacion para actividades (administracion).
 * Contiene un formulario con selectores de actividad, instalacion, fecha y horario,
 * un panel lateral de resumen en tiempo real y un panel de conflictos que aparece
 * cuando el horario solicitado choca con una reserva existente.
 *
 * <br/>Sigue el patron MVC: no incluye logica de negocio ni manejadores de eventos.
 * Al final se incluyen los metodos de acceso necesarios para el controlador.
 */
public class ReservarActividadView {

	// Colores
	private static final Color COLOR_PURPLE      = new Color(124,  58, 237);
	private static final Color COLOR_PURPLE_DARK = new Color( 91,  33, 182);
	private static final Color COLOR_RED_BG      = new Color(254, 242, 242);
	private static final Color COLOR_RED_BRD     = new Color(252, 165, 165);
	private static final Color COLOR_RED_TXT     = new Color(153,  27,  27);
	private static final Color COLOR_GREEN_BG    = new Color(240, 253, 244);
	private static final Color COLOR_GREEN_BRD   = new Color(134, 239, 172);
	private static final Color COLOR_GREEN_TXT   = new Color( 22, 101,  52);
	private static final Color COLOR_BLUE_BG     = new Color(239, 246, 255);
	private static final Color COLOR_BLUE_TXT    = new Color( 30,  64, 175);
	private static final Color COLOR_GRAY_DISABLED = new Color(209, 213, 219);

	private JFrame         frame;

	// ── Formulario ──────────────────────────────────────────────────────────
	private JComboBox<String> cmbActividad;
	private JComboBox<String> cmbInstalacion;
	private JTextField        txtFecha;
	private JComboBox<String> cmbHoraInicio;
	private JComboBox<String> cmbHoraFin;
	private JButton           btnConfirmar;

	// ── Panel lateral ───────────────────────────────────────────────────────
	private JLabel   lblResumenActividad;
	private JLabel   lblResumenInstalacion;
	private JLabel   lblResumenFecha;
	private JLabel   lblResumenHorario;
	private JLabel   lblResumenDuracion;

	// Panel de conflictos
	private JPanel   panelConflictos;
	private JLabel   lblNumConflictos;
	private JTable   tabConflictos;

	// Panel de disponible (verde)
	private JPanel   panelDisponible;

	// Panel de exito
	private JPanel   panelExito;
	private JLabel   lblExitoMsg;

	public ReservarActividadView() {
		initialize();
	}

	private void initialize() {
		// ── Frame ────────────────────────────────────────────────────────────
		frame = new JFrame("Reservar Instalacion para Actividad");
		frame.setName("ReservarActividad");
		frame.setBounds(0, 0, 900, 620);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		frame.getContentPane().setBackground(new Color(249, 250, 251));

		// ── Cabecera morada ──────────────────────────────────────────────────
		JPanel panelHeader = new JPanel(new BorderLayout());
		panelHeader.setBackground(COLOR_PURPLE_DARK);
		panelHeader.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

		JLabel lblTitulo = new JLabel("Reservar Instalacion para Actividad");
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
		lblTitulo.setForeground(Color.WHITE);

		JLabel lblSubtitulo = new JLabel("Panel de Administracion");
		lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblSubtitulo.setForeground(new Color(221, 214, 254));

		JPanel headerTexto = new JPanel();
		headerTexto.setOpaque(false);
		headerTexto.setLayout(new BoxLayout(headerTexto, BoxLayout.Y_AXIS));
		headerTexto.add(lblTitulo);
		headerTexto.add(Box.createVerticalStrut(4));
		headerTexto.add(lblSubtitulo);
		panelHeader.add(headerTexto, BorderLayout.CENTER);
		frame.getContentPane().add(panelHeader, BorderLayout.NORTH);

		// ── Contenido principal: formulario (izq) + panel lateral (der) ──────
		JPanel panelContenido = new JPanel(new BorderLayout(12, 0));
		panelContenido.setBackground(new Color(249, 250, 251));
		panelContenido.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

		// Formulario (2/3 del ancho)
		JPanel panelFormulario = buildPanelFormulario();
		panelContenido.add(panelFormulario, BorderLayout.CENTER);

		// Panel lateral (1/3 del ancho)
		JPanel panelLateral = buildPanelLateral();
		panelLateral.setPreferredSize(new Dimension(270, 0));
		panelContenido.add(panelLateral, BorderLayout.EAST);

		frame.getContentPane().add(panelContenido, BorderLayout.CENTER);
	}

	// ── Construccion del formulario ──────────────────────────────────────────

	private JPanel buildPanelFormulario() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(229, 231, 235)),
				BorderFactory.createEmptyBorder(20, 20, 20, 20)));

		// Actividad
		panel.add(buildLabel("Actividad *", COLOR_PURPLE));
		panel.add(Box.createVerticalStrut(4));
		cmbActividad = new JComboBox<>();
		cmbActividad.setName("cmbActividad");
		cmbActividad.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
		cmbActividad.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(cmbActividad);
		panel.add(Box.createVerticalStrut(14));

		// Instalacion
		panel.add(buildLabel("Instalacion *", null));
		panel.add(Box.createVerticalStrut(4));
		cmbInstalacion = new JComboBox<>();
		cmbInstalacion.setName("cmbInstalacion");
		cmbInstalacion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
		cmbInstalacion.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(cmbInstalacion);
		panel.add(Box.createVerticalStrut(14));

		// Fecha
		panel.add(buildLabel("Fecha * (formato ISO: yyyy-MM-dd)", null));
		panel.add(Box.createVerticalStrut(4));
		txtFecha = new JTextField();
		txtFecha.setName("txtFecha");
		txtFecha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
		txtFecha.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(txtFecha);
		panel.add(Box.createVerticalStrut(14));

		// Hora inicio / Hora fin en la misma fila
		JPanel panelHoras = new JPanel(new GridLayout(1, 2, 12, 0));
		panelHoras.setBackground(Color.WHITE);
		panelHoras.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelHoras.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

		JPanel subInicio = new JPanel();
		subInicio.setLayout(new BoxLayout(subInicio, BoxLayout.Y_AXIS));
		subInicio.setBackground(Color.WHITE);
		subInicio.add(buildLabel("Hora Inicio *", null));
		subInicio.add(Box.createVerticalStrut(4));
		cmbHoraInicio = new JComboBox<>();
		cmbHoraInicio.setName("cmbHoraInicio");
		subInicio.add(cmbHoraInicio);

		JPanel subFin = new JPanel();
		subFin.setLayout(new BoxLayout(subFin, BoxLayout.Y_AXIS));
		subFin.setBackground(Color.WHITE);
		subFin.add(buildLabel("Hora Fin *", null));
		subFin.add(Box.createVerticalStrut(4));
		cmbHoraFin = new JComboBox<>();
		cmbHoraFin.setName("cmbHoraFin");
		subFin.add(cmbHoraFin);

		panelHoras.add(subInicio);
		panelHoras.add(subFin);
		panel.add(panelHoras);
		panel.add(Box.createVerticalStrut(16));

		// Bloque informativo azul
		JPanel panelInfo = new JPanel();
		panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
		panelInfo.setBackground(COLOR_BLUE_BG);
		panelInfo.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(191, 219, 254), 2),
				BorderFactory.createEmptyBorder(10, 12, 10, 12)));
		panelInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelInfo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

		JLabel lblInfoTit = new JLabel("Informacion importante:");
		lblInfoTit.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblInfoTit.setForeground(COLOR_BLUE_TXT);
		panelInfo.add(lblInfoTit);
		panelInfo.add(Box.createVerticalStrut(4));
		String[] lineas = {
			"La instalacion quedara bloqueada para esta actividad en el horario seleccionado",
			"Los socios no podran hacer reservas individuales en este horario",
			"El sistema detecta automaticamente conflictos con otras reservas existentes"
		};
		for (String linea : lineas) {
			JLabel l = new JLabel("• " + linea);
			l.setFont(new Font("SansSerif", Font.PLAIN, 11));
			l.setForeground(COLOR_BLUE_TXT);
			panelInfo.add(l);
		}
		panel.add(panelInfo);
		panel.add(Box.createVerticalStrut(16));

		// Boton confirmar
		btnConfirmar = new JButton("Confirmar Reserva de Instalacion");
		btnConfirmar.setName("btnConfirmar");
		btnConfirmar.setFont(new Font("SansSerif", Font.BOLD, 14));
		btnConfirmar.setBackground(COLOR_PURPLE);
		btnConfirmar.setForeground(Color.WHITE);
		btnConfirmar.setFocusPainted(false);
		btnConfirmar.setEnabled(false); // empieza deshabilitado hasta que no haya conflictos
		btnConfirmar.setAlignmentX(Component.LEFT_ALIGNMENT);
		btnConfirmar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
		panel.add(btnConfirmar);

		// Panel de exito (oculto por defecto)
		panelExito = new JPanel(new BorderLayout());
		panelExito.setBackground(new Color(34, 197, 94));
		panelExito.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
		panelExito.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelExito.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
		panelExito.setVisible(false);
		lblExitoMsg = new JLabel("Instalacion reservada correctamente", SwingConstants.CENTER);
		lblExitoMsg.setFont(new Font("SansSerif", Font.BOLD, 14));
		lblExitoMsg.setForeground(Color.WHITE);
		panelExito.add(lblExitoMsg, BorderLayout.CENTER);
		panel.add(Box.createVerticalStrut(10));
		panel.add(panelExito);

		return panel;
	}

	// ── Construccion del panel lateral ──────────────────────────────────────

	private JPanel buildPanelLateral() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(new Color(249, 250, 251));

		// --- Resumen ---
		JPanel panelResumen = new JPanel();
		panelResumen.setLayout(new BoxLayout(panelResumen, BoxLayout.Y_AXIS));
		panelResumen.setBackground(Color.WHITE);
		panelResumen.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(229, 231, 235)),
				BorderFactory.createEmptyBorder(14, 14, 14, 14)));
		panelResumen.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel lblResumenTit = new JLabel("Resumen de la Reserva");
		lblResumenTit.setFont(new Font("SansSerif", Font.BOLD, 14));
		lblResumenTit.setForeground(new Color(31, 41, 55));
		panelResumen.add(lblResumenTit);
		panelResumen.add(Box.createVerticalStrut(10));

		lblResumenActividad   = buildResumenFila(panelResumen, "Actividad:",   COLOR_PURPLE);
		lblResumenInstalacion = buildResumenFila(panelResumen, "Instalacion:", null);
		lblResumenFecha       = buildResumenFila(panelResumen, "Fecha:",       null);
		lblResumenHorario     = buildResumenFila(panelResumen, "Horario:",     null);
		lblResumenDuracion    = buildResumenFila(panelResumen, "Duracion:",    null);

		panel.add(panelResumen);
		panel.add(Box.createVerticalStrut(10));

		// --- Panel de conflictos (rojo, oculto por defecto) ---
		panelConflictos = new JPanel();
		panelConflictos.setLayout(new BoxLayout(panelConflictos, BoxLayout.Y_AXIS));
		panelConflictos.setBackground(COLOR_RED_BG);
		panelConflictos.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(COLOR_RED_BRD, 2),
				BorderFactory.createEmptyBorder(12, 12, 12, 12)));
		panelConflictos.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelConflictos.setVisible(false);

		JLabel lblConflTit = new JLabel("Conflictos Detectados");
		lblConflTit.setFont(new Font("SansSerif", Font.BOLD, 14));
		lblConflTit.setForeground(COLOR_RED_TXT);
		panelConflictos.add(lblConflTit);
		panelConflictos.add(Box.createVerticalStrut(6));

		lblNumConflictos = new JLabel();
		lblNumConflictos.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblNumConflictos.setForeground(COLOR_RED_TXT);
		panelConflictos.add(lblNumConflictos);
		panelConflictos.add(Box.createVerticalStrut(8));

		tabConflictos = new JTable();
		tabConflictos.setName("tabConflictos");
		tabConflictos.setDefaultEditor(Object.class, null);
		tabConflictos.setRowHeight(28);
		tabConflictos.setFont(new Font("SansSerif", Font.PLAIN, 11));
		tabConflictos.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
		tabConflictos.setDefaultRenderer(Object.class, new ConflictoCellRenderer());
		JScrollPane scrollConflictos = new JScrollPane(tabConflictos);
		scrollConflictos.setPreferredSize(new Dimension(240, 120));
		scrollConflictos.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
		scrollConflictos.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelConflictos.add(scrollConflictos);
		panelConflictos.add(Box.createVerticalStrut(8));

		JLabel lblBloqueo = new JLabel("<html><b>No se puede crear la reserva.</b><br/>Cambie el horario, fecha o instalacion.</html>");
		lblBloqueo.setFont(new Font("SansSerif", Font.PLAIN, 11));
		lblBloqueo.setForeground(COLOR_RED_TXT);
		panelConflictos.add(lblBloqueo);
		panel.add(panelConflictos);

		// --- Panel disponible (verde, oculto por defecto) ---
		panelDisponible = new JPanel(new BorderLayout());
		panelDisponible.setBackground(COLOR_GREEN_BG);
		panelDisponible.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(COLOR_GREEN_BRD, 2),
				BorderFactory.createEmptyBorder(12, 12, 12, 12)));
		panelDisponible.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelDisponible.setVisible(false);

		JLabel lblDisponibleTit = new JLabel("Disponible");
		lblDisponibleTit.setFont(new Font("SansSerif", Font.BOLD, 14));
		lblDisponibleTit.setForeground(COLOR_GREEN_TXT);
		JLabel lblDisponibleMsg = new JLabel("<html>No hay conflictos.<br/>La instalacion esta libre en este horario.</html>");
		lblDisponibleMsg.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblDisponibleMsg.setForeground(COLOR_GREEN_TXT);

		JPanel subDisp = new JPanel();
		subDisp.setLayout(new BoxLayout(subDisp, BoxLayout.Y_AXIS));
		subDisp.setOpaque(false);
		subDisp.add(lblDisponibleTit);
		subDisp.add(Box.createVerticalStrut(4));
		subDisp.add(lblDisponibleMsg);
		panelDisponible.add(subDisp, BorderLayout.CENTER);
		panel.add(Box.createVerticalStrut(10));
		panel.add(panelDisponible);

		return panel;
	}

	// ── Helpers de construccion ──────────────────────────────────────────────

	private JLabel buildLabel(String texto, Color color) {
		JLabel lbl = new JLabel(texto);
		lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
		lbl.setForeground(color != null ? color : new Color(55, 65, 81));
		lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
		return lbl;
	}

	/** Crea una fila etiqueta + valor en el panel de resumen y devuelve el JLabel del valor */
	private JLabel buildResumenFila(JPanel parent, String etiqueta, Color colorValor) {
		JLabel lblEtiq = new JLabel(etiqueta);
		lblEtiq.setFont(new Font("SansSerif", Font.BOLD, 11));
		lblEtiq.setForeground(new Color(107, 114, 128));
		parent.add(lblEtiq);

		JLabel lblValor = new JLabel("-");
		lblValor.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblValor.setForeground(colorValor != null ? colorValor : new Color(31, 41, 55));
		parent.add(lblValor);
		parent.add(Box.createVerticalStrut(6));
		return lblValor;
	}

	// ── Renderer para las celdas de conflictos ───────────────────────────────

	static class ConflictoCellRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			setFont(new Font("SansSerif", Font.PLAIN, 11));
			setBackground(isSelected ? new Color(254, 202, 202) : Color.WHITE);
			setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(252, 165, 165)));
			// Columna "tipo": muestra badge de color
			if (column == 0 && value != null) {
				String tipo = value.toString();
				if ("actividad".equalsIgnoreCase(tipo)) {
					setBackground(new Color(237, 233, 254));
					setForeground(new Color(109,  40, 217));
					setFont(new Font("SansSerif", Font.BOLD, 10));
					setText("ACTIVIDAD");
				} else {
					setBackground(new Color(219, 234, 254));
					setForeground(new Color( 29,  78, 216));
					setFont(new Font("SansSerif", Font.BOLD, 10));
					setText("SOCIO");
				}
			} else {
				setForeground(new Color(31, 41, 55));
			}
			return this;
		}
	}

	// ── Getters para el Controlador ──────────────────────────────────────────

	public JFrame          getFrame()          { return this.frame; }
	public JComboBox<String> getCmbActividad()   { return this.cmbActividad; }
	public JComboBox<String> getCmbInstalacion() { return this.cmbInstalacion; }
	public JTextField      getTxtFecha()        { return this.txtFecha; }
	public JComboBox<String> getCmbHoraInicio()  { return this.cmbHoraInicio; }
	public JComboBox<String> getCmbHoraFin()     { return this.cmbHoraFin; }
	public JButton         getBtnConfirmar()    { return this.btnConfirmar; }

	public String getActividadSeleccionada()   {
		Object s = cmbActividad.getSelectedItem(); return s != null ? s.toString() : "";
	}
	public String getInstalacionSeleccionada() {
		Object s = cmbInstalacion.getSelectedItem(); return s != null ? s.toString() : "";
	}
	public String getFecha()       { return txtFecha.getText().trim(); }
	public String getHoraInicio()  {
		Object s = cmbHoraInicio.getSelectedItem(); return s != null ? s.toString() : "";
	}
	public String getHoraFin()     {
		Object s = cmbHoraFin.getSelectedItem(); return s != null ? s.toString() : "";
	}

	/** Rellena el combo de actividades con los nombres */
	public void setActividades(List<ActividadEntity> actividades) {
		cmbActividad.removeAllItems();
		for (ActividadEntity a : actividades)
			cmbActividad.addItem(a.getNombre());
	}

	/** Rellena el combo de instalaciones con los nombres */
	public void setInstalaciones(List<InstalacionEntity> instalaciones) {
		cmbInstalacion.removeAllItems();
		for (InstalacionEntity i : instalaciones)
			cmbInstalacion.addItem(i.getNombre());
	}

	/** Rellena ambos combos de hora con las opciones generadas por el modelo */
	public void setOpcionesHora(String[] opciones) {
		cmbHoraInicio.removeAllItems();
		cmbHoraFin.removeAllItems();
		for (String h : opciones) {
			cmbHoraInicio.addItem(h);
			cmbHoraFin.addItem(h);
		}
		// Valores por defecto: 10:00 - 11:00
		cmbHoraInicio.setSelectedItem("10:00");
		cmbHoraFin.setSelectedItem("11:00");
	}

	/** Actualiza el panel de resumen lateral con los valores actuales del formulario */
	public void actualizarResumen(String actividad, String instalacion,
			String fecha, String horaInicio, String horaFin, double duracion) {
		lblResumenActividad.setText("<html><b>" + actividad + "</b></html>");
		lblResumenInstalacion.setText(instalacion);
		lblResumenFecha.setText(fecha.isEmpty() ? "-" : fecha);
		lblResumenHorario.setText(horaInicio + " - " + horaFin);
		lblResumenDuracion.setText(duracion > 0 ? String.format("%.1f hora(s)", duracion) : "-");
	}

	/**
	 * Actualiza el estado del panel lateral de conflictos/disponible
	 * y habilita o deshabilita el boton de confirmar.
	 *
	 * @param conflictos lista de conflictos (vacia = sin conflictos)
	 * @param camposCompletos true si todos los campos obligatorios tienen valor
	 */
	public void actualizarEstadoConflictos(List<ConflictoDTO> conflictos, boolean camposCompletos) {
		boolean hayConflictos = !conflictos.isEmpty();
		boolean puedeConfirmar = camposCompletos && !hayConflictos;

		// Panel rojo
		panelConflictos.setVisible(hayConflictos);
		// Panel verde
		panelDisponible.setVisible(camposCompletos && !hayConflictos);

		// Tabla de conflictos
		if (hayConflictos) {
			lblNumConflictos.setText("Ya existen " + conflictos.size() + " reserva(s) en este horario:");
			@SuppressWarnings("serial")
			DefaultTableModel tm = new DefaultTableModel(
					new String[]{"Tipo", "Nombre", "Inicio", "Fin"}, 0) {
				@Override public boolean isCellEditable(int r, int c) { return false; }
			};
			for (ConflictoDTO c : conflictos)
				tm.addRow(new Object[]{c.getTipo(), c.getNombre(), c.getHoraInicio(), c.getHoraFin()});
			tabConflictos.setModel(tm);
		}

		// Boton
		btnConfirmar.setEnabled(puedeConfirmar);
		btnConfirmar.setBackground(puedeConfirmar ? COLOR_PURPLE : COLOR_GRAY_DISABLED);
		btnConfirmar.setForeground(puedeConfirmar ? Color.WHITE : new Color(107, 114, 128));
	}

	/** Muestra o oculta el panel de exito tras confirmar la reserva */
	public void mostrarExito(boolean visible, String nombreActividad) {
		panelExito.setVisible(visible);
		if (visible)
			lblExitoMsg.setText("Instalacion reservada correctamente para: " + nombreActividad);
	}
}
