package cd.admin.diego.planact;

import java.awt.Dimension;

import javax.swing.*;
import javax.swing.table.TableModel;

import net.miginfocom.swing.MigLayout;

/**
 * Vista: Pantalla "Crear actividad" (similar al boceto).
 * MVC: No incluye listeners; solo componentes + getters/setters.
 */
public class PlanActCrearActividadView {

	private JFrame frame;

	private JTextField txtNombreActividad;
	private JTextField txtTipoActividad;
	private JComboBox<Object> cbInstalacion;

	private JSpinner spAforo;
	private JSpinner spPrecioSocio;
	private JSpinner spPrecioNoSocio;

	private JTextField txtInscripcionInicio;
	private JTextField txtInscripcionFin;

	private JTextField txtFechaInicio;
	private JSpinner spNumSemanas;

	private JTable tabHorario;
	private JButton btnCrear;
	private JButton btnBorrarTodo;
	private JButton btnAtras;

	public PlanActCrearActividadView() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Crear actividad");
		frame.setName("CrearActividad");
		frame.setBounds(0, 0, 760, 640);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new MigLayout("", "[grow]", "[][][][][][][][][][grow][][]"));

		JLabel lblTitulo = new JLabel("Crear actividad");
		frame.getContentPane().add(lblTitulo, "cell 0 0");

		// --- Formulario superior ---
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
		pnlForm.add(spAforo, "cell 1 3,split 2");
		pnlForm.add(new JLabel(" "), "cell 1 3");

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

		pnlForm.add(new JLabel("Periodo de inscripción (ISO):"), "cell 0 6,alignx right");
		JPanel pnlIns = new JPanel(new MigLayout("", "[][grow][][grow]", "[]"));
		txtInscripcionInicio = new JTextField();
		txtInscripcionInicio.setName("txtInscripcionInicio");
		txtInscripcionFin = new JTextField();
		txtInscripcionFin.setName("txtInscripcionFin");
		pnlIns.add(new JLabel("Inicio"), "cell 0 0");
		pnlIns.add(txtInscripcionInicio, "cell 1 0,growx");
		pnlIns.add(new JLabel("Fin"), "cell 2 0");
		pnlIns.add(txtInscripcionFin, "cell 3 0,growx");
		pnlForm.add(pnlIns, "cell 1 6,growx");

		pnlForm.add(new JLabel("Fecha inicio (ISO):"), "cell 0 7,alignx right");
		JPanel pnlInicio = new JPanel(new MigLayout("", "[][grow][]", "[]"));
		txtFechaInicio = new JTextField();
		txtFechaInicio.setName("txtFechaInicio");
		spNumSemanas = new JSpinner(new SpinnerNumberModel(1, 1, 52, 1));
		spNumSemanas.setName("spNumSemanas");
		pnlInicio.add(txtFechaInicio, "cell 0 0,growx");
		pnlInicio.add(new JLabel("Nº semanas duración:"), "cell 1 0");
		pnlInicio.add(spNumSemanas, "cell 2 0");
		pnlForm.add(pnlInicio, "cell 1 7,growx");

		// --- Horario semanal ---
		JLabel lblHorario = new JLabel("Horario semanal de la actividad:");
		frame.getContentPane().add(lblHorario, "cell 0 2");

		tabHorario = new JTable();
		tabHorario.setName("tabHorario");
		tabHorario.setRowSelectionAllowed(false);

		// Scroll para la tabla (como en tkrun)
		JScrollPane sp = new JScrollPane(tabHorario);
		sp.setPreferredSize(new Dimension(650, 280));
		frame.getContentPane().add(sp, "cell 0 3,grow");

		// --- Botonera inferior ---
		JPanel pnlBtns = new JPanel(new MigLayout("", "[][grow][][grow][]", "[]"));
		btnCrear = new JButton("Crear actividad");
		btnCrear.setName("btnCrearActividad");
		btnBorrarTodo = new JButton("Borrar todo");
		btnBorrarTodo.setName("btnBorrarTodo");
		btnAtras = new JButton("Atrás");
		btnAtras.setName("btnAtras");

		pnlBtns.add(btnCrear, "cell 0 0");
		pnlBtns.add(new JLabel(""), "cell 1 0,growx");
		pnlBtns.add(btnBorrarTodo, "cell 2 0");
		pnlBtns.add(new JLabel(""), "cell 3 0,growx");
		pnlBtns.add(btnAtras, "cell 4 0");

		frame.getContentPane().add(pnlBtns, "cell 0 4,growx");
	}

	// --- Accesores para el Controller ---
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

	public String getInscripcionInicio() { return txtInscripcionInicio.getText(); }
	public void setInscripcionInicio(String v) { txtInscripcionInicio.setText(v); }

	public String getInscripcionFin() { return txtInscripcionFin.getText(); }
	public void setInscripcionFin(String v) { txtInscripcionFin.setText(v); }

	public String getFechaInicio() { return txtFechaInicio.getText(); }
	public void setFechaInicio(String v) { txtFechaInicio.setText(v); }

	public int getNumSemanas() { return (Integer) spNumSemanas.getValue(); }
	public void setNumSemanas(int v) { spNumSemanas.setValue(v); }

	public JTable getTablaHorario() { return tabHorario; }
	public void setHorarioModel(TableModel model) { tabHorario.setModel(model); }

	public JButton getBtnCrear() { return btnCrear; }
	public JButton getBtnBorrarTodo() { return btnBorrarTodo; }
	public JButton getBtnAtras() { return btnAtras; }
}