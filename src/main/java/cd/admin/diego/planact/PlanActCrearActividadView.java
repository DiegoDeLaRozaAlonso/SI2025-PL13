package cd.admin.diego.planact;

import java.awt.Dimension;
import java.util.Date;

import javax.swing.*;
import javax.swing.table.TableModel;

import net.miginfocom.swing.MigLayout;
import com.toedter.calendar.JDateChooser;

public class PlanActCrearActividadView {

	private JFrame frame;

	private JTextField txtNombreActividad;
	private JTextField txtTipoActividad;
	private JComboBox<Object> cbInstalacion;

	private JSpinner spAforo;
	private JSpinner spPrecioSocio;
	private JSpinner spPrecioNoSocio;

	private JComboBox<PeriodoInscripcionDTO> cbPeriodoInscripcion;
	private JButton btnInfoPeriodo;

	// Calendarios
	private JDateChooser dcFechaInicio;
	private JDateChooser dcFechaFin;

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
		frame.setBounds(100, 100, 820, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new MigLayout("", "[grow]", "[][][][][][grow][][]"));

		JLabel lblTitulo = new JLabel("Crear actividad");
		frame.getContentPane().add(lblTitulo, "cell 0 0");

		JPanel pnlForm = new JPanel(new MigLayout("", "[][grow]", "[][][][][][][][]"));
		frame.getContentPane().add(pnlForm, "cell 0 1,growx");

		// Nombre
		pnlForm.add(new JLabel("Nombre de la actividad:"), "cell 0 0,alignx right");
		txtNombreActividad = new JTextField();
		pnlForm.add(txtNombreActividad, "cell 1 0,growx");

		// Tipo
		pnlForm.add(new JLabel("Tipo actividad:"), "cell 0 1,alignx right");
		txtTipoActividad = new JTextField();
		pnlForm.add(txtTipoActividad, "cell 1 1,growx");

		// Instalación
		pnlForm.add(new JLabel("Instalación a ocupar:"), "cell 0 2,alignx right");
		cbInstalacion = new JComboBox<>();
		pnlForm.add(cbInstalacion, "cell 1 2,growx");

		// Aforo
		pnlForm.add(new JLabel("Aforo máximo:"), "cell 0 3,alignx right");
		spAforo = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
		pnlForm.add(spAforo, "cell 1 3");

		// Precio socio
		pnlForm.add(new JLabel("Precio socio (€):"), "cell 0 4,alignx right");
		spPrecioSocio = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 9999.0, 0.5));
		pnlForm.add(spPrecioSocio, "cell 1 4,split 2");
		pnlForm.add(new JLabel("€"), "cell 1 4");

		// Precio no socio
		pnlForm.add(new JLabel("Precio no socio (€):"), "cell 0 5,alignx right");
		spPrecioNoSocio = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 9999.0, 0.5));
		pnlForm.add(spPrecioNoSocio, "cell 1 5,split 2");
		pnlForm.add(new JLabel("€"), "cell 1 5");

		// Periodo
		pnlForm.add(new JLabel("Periodo de inscripción:"), "cell 0 6,alignx right");
		cbPeriodoInscripcion = new JComboBox<>();
		pnlForm.add(cbPeriodoInscripcion, "cell 1 6,growx");

		// Botón Info Periodo alineado derecha
		btnInfoPeriodo = new JButton("Info Periodo");
		JPanel pnlInfo = new JPanel(new MigLayout("", "[grow][]", "[]"));
		pnlInfo.add(new JLabel(""), "cell 0 0,growx");
		pnlInfo.add(btnInfoPeriodo, "cell 1 0,alignx right");
		pnlForm.add(pnlInfo, "cell 1 7,growx");

		// Fechas (SIMÉTRICAS)
		pnlForm.add(new JLabel("Fecha de inicio:"), "cell 0 8,alignx right");

		JPanel pnlFechas = new JPanel(new MigLayout("", "[grow,fill][][][grow,fill]", "[]"));

		dcFechaInicio = new JDateChooser();
		dcFechaInicio.setName("dcFechaInicio");
		dcFechaInicio.setDateFormatString("yyyy-MM-dd");

		dcFechaFin = new JDateChooser();
		dcFechaFin.setName("dcFechaFin");
		dcFechaFin.setDateFormatString("yyyy-MM-dd");

		// Inicio ocupa todo el espacio disponible
		pnlFechas.add(dcFechaInicio, "cell 0 0,growx,pushx");

		// Label fecha fin
		pnlFechas.add(new JLabel("Fecha de fin:"), "cell 2 0");

		// Fin ocupa el mismo comportamiento
		pnlFechas.add(dcFechaFin, "cell 3 0,growx,pushx");

		pnlForm.add(pnlFechas, "cell 1 8,growx");

		// Horario
		JLabel lblHorario = new JLabel("Horario semanal de la actividad:");
		frame.getContentPane().add(lblHorario, "cell 0 2");

		tabHorario = new JTable();
		tabHorario.setRowSelectionAllowed(false);

		JScrollPane sp = new JScrollPane(tabHorario);
		sp.setPreferredSize(new Dimension(750, 350));
		frame.getContentPane().add(sp, "cell 0 3,grow");

		// Botones inferiores
		JPanel pnlBtns = new JPanel(new MigLayout("", "[][grow][][grow][]", "[]"));

		btnCrear = new JButton("Crear actividad");
		btnBorrarTodo = new JButton("Borrar todo");
		btnCerrar = new JButton("Cerrar");

		pnlBtns.add(btnCrear, "cell 0 0");
		pnlBtns.add(new JLabel(""), "cell 1 0,growx");
		pnlBtns.add(btnBorrarTodo, "cell 2 0");
		pnlBtns.add(new JLabel(""), "cell 3 0,growx");
		pnlBtns.add(btnCerrar, "cell 4 0");

		frame.getContentPane().add(pnlBtns, "cell 0 4,growx");
	}

	// Getters / setters

	public JFrame getFrame() { return frame; }

	public String getNombreActividad() { return txtNombreActividad.getText(); }
	public String getTipoActividad() { return txtTipoActividad.getText(); }

	public JComboBox<Object> getCbInstalacion() { return cbInstalacion; }
	public JComboBox<PeriodoInscripcionDTO> getCbPeriodoInscripcion() { return cbPeriodoInscripcion; }

	public int getAforo() { return (Integer) spAforo.getValue(); }
	public double getPrecioSocio() { return ((Number) spPrecioSocio.getValue()).doubleValue(); }
	public double getPrecioNoSocio() { return ((Number) spPrecioNoSocio.getValue()).doubleValue(); }

	public Date getFechaInicioDate() { return dcFechaInicio.getDate(); }
	public Date getFechaFinDate() { return dcFechaFin.getDate(); }
	
	// SETTERS que usa el Controller

	public void setNombreActividad(String v) {
		txtNombreActividad.setText(v);
	}
	public void setTipoActividad(String v) {
		txtTipoActividad.setText(v);
	}
	public void setAforo(int v) {
		spAforo.setValue(v);
	}
	public void setPrecioSocio(double v) {
		spPrecioSocio.setValue(v);
	}
	public void setPrecioNoSocio(double v) {
		spPrecioNoSocio.setValue(v);
	}
	// ✅ ESTO ES LO QUE FALTABA (para que compile con tu Controller)
	public void setFechaInicioDate(Date d) { dcFechaInicio.setDate(d); }
	public void setFechaFinDate(Date d) { dcFechaFin.setDate(d); }

	public JTable getTablaHorario() { return tabHorario; }
	public void setHorarioModel(TableModel model) { tabHorario.setModel(model); }

	public JButton getBtnCrear() { return btnCrear; }
	public JButton getBtnBorrarTodo() { return btnBorrarTodo; }
	public JButton getBtnCerrar() { return btnCerrar; }
	public JButton getBtnInfoPeriodo() { return btnInfoPeriodo; }
}