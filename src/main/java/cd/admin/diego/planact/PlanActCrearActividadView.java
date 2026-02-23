package cd.admin.diego.planact;

import java.awt.Dimension;

import javax.swing.*;
import javax.swing.table.TableModel;

import net.miginfocom.swing.MigLayout;

public class PlanActCrearActividadView {

	private JFrame frame;

	private JTextField txtNombreActividad;
	private JTextField txtTipoActividad;
	private JComboBox<Object> cbInstalacion;

	private JSpinner spAforo;
	private JSpinner spPrecioSocio;
	private JSpinner spPrecioNoSocio;

	private JComboBox<PeriodoInscripcionDTO> cbPeriodoInscripcion;

	// NUEVO: botón info periodo
	private JButton btnInfoPeriodo;

	private JTextField txtFechaInicio;
	private JTextField txtFechaFin;

	private JTable tabHorario;
	private JButton btnCrear;
	private JButton btnBorrarTodo;
	private JButton btnCerrar;

	public PlanActCrearActividadView() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Crear actividad");
		frame.setName("CrearActividad");
		frame.setBounds(0, 0, 760, 640);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new MigLayout("", "[grow]", "[][][][][][grow][][]"));

		JLabel lblTitulo = new JLabel("Crear actividad");
		frame.getContentPane().add(lblTitulo, "cell 0 0");

		JPanel pnlForm = new JPanel(new MigLayout("", "[][grow]", "[][][][][][][][]"));
		frame.getContentPane().add(pnlForm, "cell 0 1,growx");

		pnlForm.add(new JLabel("Nombre de la actividad:"), "cell 0 0,alignx right");
		txtNombreActividad = new JTextField();
		txtNombreActividad.setName("txtNombreActividad");
		pnlForm.add(txtNombreActividad, "cell 1 0,growx");

		pnlForm.add(new JLabel("Tipo actividad:"), "cell 0 1,alignx right");
		txtTipoActividad = new JTextField();
		txtTipoActividad.setName("txtTipoActividad");
		pnlForm.add(txtTipoActividad, "cell 1 1,growx");

		pnlForm.add(new JLabel("Instalación a ocupar:"), "cell 0 2,alignx right");
		cbInstalacion = new JComboBox<>();
		cbInstalacion.setName("cbInstalacion");
		pnlForm.add(cbInstalacion, "cell 1 2,growx");

		pnlForm.add(new JLabel("Aforo máximo:"), "cell 0 3,alignx right");
		spAforo = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
		spAforo.setName("spAforo");
		pnlForm.add(spAforo, "cell 1 3");

		pnlForm.add(new JLabel("Precio socio (€):"), "cell 0 4,alignx right");
		spPrecioSocio = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 9999.0, 0.5));
		spPrecioSocio.setName("spPrecioSocio");
		pnlForm.add(spPrecioSocio, "cell 1 4,split 2");
		pnlForm.add(new JLabel("€"), "cell 1 4");

		pnlForm.add(new JLabel("Precio no socio (€):"), "cell 0 5,alignx right");
		spPrecioNoSocio = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 9999.0, 0.5));
		spPrecioNoSocio.setName("spPrecioNoSocio");
		pnlForm.add(spPrecioNoSocio, "cell 1 5,split 2");
		pnlForm.add(new JLabel("€"), "cell 1 5");

		pnlForm.add(new JLabel("Periodo de inscripción:"), "cell 0 6,alignx right");
		cbPeriodoInscripcion = new JComboBox<>();
		cbPeriodoInscripcion.setName("cbPeriodoInscripcion");
		pnlForm.add(cbPeriodoInscripcion, "cell 1 6,growx");

		// NUEVO: botón debajo del combo, alineado a la derecha
		btnInfoPeriodo = new JButton("Info Periodo");
		btnInfoPeriodo.setName("btnInfoPeriodo");
		JPanel pnlInfoPeriodo = new JPanel(new MigLayout("", "[grow][]", "[]"));
		pnlInfoPeriodo.add(new JLabel(""), "cell 0 0,growx");
		pnlInfoPeriodo.add(btnInfoPeriodo, "cell 1 0,alignx right");
		pnlForm.add(pnlInfoPeriodo, "cell 1 7,growx");

		// Fechas (ISO)
		pnlForm.add(new JLabel("Fecha inicio (ISO):"), "cell 0 8,alignx right");
		JPanel pnlFechas = new JPanel(new MigLayout("", "[grow][][][grow]", "[]"));
		txtFechaInicio = new JTextField();
		txtFechaInicio.setName("txtFechaInicio");
		txtFechaFin = new JTextField();
		txtFechaFin.setName("txtFechaFin");
		pnlFechas.add(txtFechaInicio, "cell 0 0,growx");
		pnlFechas.add(new JLabel("Fecha fin (ISO):"), "cell 1 0");
		pnlFechas.add(txtFechaFin, "cell 3 0,growx");
		pnlForm.add(pnlFechas, "cell 1 8,growx");

		JLabel lblHorario = new JLabel("Horario semanal de la actividad:");
		frame.getContentPane().add(lblHorario, "cell 0 2");

		tabHorario = new JTable();
		tabHorario.setName("tabHorario");
		tabHorario.setRowSelectionAllowed(false);

		JScrollPane sp = new JScrollPane(tabHorario);
		sp.setPreferredSize(new Dimension(650, 320));
		frame.getContentPane().add(sp, "cell 0 3,grow");

		JPanel pnlBtns = new JPanel(new MigLayout("", "[][grow][][grow][]", "[]"));
		btnCrear = new JButton("Crear actividad");
		btnCrear.setName("btnCrearActividad");
		btnBorrarTodo = new JButton("Borrar todo");
		btnBorrarTodo.setName("btnBorrarTodo");
		btnCerrar = new JButton("Cerrar");
		btnCerrar.setName("btnCerrar");

		pnlBtns.add(btnCrear, "cell 0 0");
		pnlBtns.add(new JLabel(""), "cell 1 0,growx");
		pnlBtns.add(btnBorrarTodo, "cell 2 0");
		pnlBtns.add(new JLabel(""), "cell 3 0,growx");
		pnlBtns.add(btnCerrar, "cell 4 0");

		frame.getContentPane().add(pnlBtns, "cell 0 4,growx");
	}

	public JFrame getFrame() { return frame; }

	public String getNombreActividad() { return txtNombreActividad.getText(); }
	public void setNombreActividad(String v) { txtNombreActividad.setText(v); }

	public String getTipoActividad() { return txtTipoActividad.getText(); }
	public void setTipoActividad(String v) { txtTipoActividad.setText(v); }

	public JComboBox<Object> getCbInstalacion() { return cbInstalacion; }

	public int getAforo() { return (Integer) spAforo.getValue(); }
	public void setAforo(int v) { spAforo.setValue(v); }

	public double getPrecioSocio() { return ((Number) spPrecioSocio.getValue()).doubleValue(); }
	public void setPrecioSocio(double v) { spPrecioSocio.setValue(v); }

	public double getPrecioNoSocio() { return ((Number) spPrecioNoSocio.getValue()).doubleValue(); }
	public void setPrecioNoSocio(double v) { spPrecioNoSocio.setValue(v); }

	public JComboBox<PeriodoInscripcionDTO> getCbPeriodoInscripcion() { return cbPeriodoInscripcion; }

	// NUEVO: getter botón
	public JButton getBtnInfoPeriodo() { return btnInfoPeriodo; }

	public String getFechaInicio() { return txtFechaInicio.getText(); }
	public void setFechaInicio(String v) { txtFechaInicio.setText(v); }

	public String getFechaFin() { return txtFechaFin.getText(); }
	public void setFechaFin(String v) { txtFechaFin.setText(v); }

	public JTable getTablaHorario() { return tabHorario; }
	public void setHorarioModel(TableModel model) { tabHorario.setModel(model); }

	public JButton getBtnCrear() { return btnCrear; }
	public JButton getBtnBorrarTodo() { return btnBorrarTodo; }
	public JButton getBtnCerrar() { return btnCerrar; }
}