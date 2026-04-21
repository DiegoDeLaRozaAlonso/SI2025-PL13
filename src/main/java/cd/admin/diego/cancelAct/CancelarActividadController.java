package cd.admin.diego.cancelAct;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;

import giis.demo.util.SwingUtil;

public class CancelarActividadController {

	private CancelarActividadModel model;
	private CancelarActividadView view;

	public CancelarActividadController(CancelarActividadModel model, CancelarActividadView view) {
		this.model = model;
		this.view = view;
		this.initController();
	}

	private void initController() {
		cargarComboActividades();

		view.getBtnFiltrar().addActionListener(e ->
			SwingUtil.exceptionWrapper(() -> cargarActividades())
		);

		view.getRbActivas().addActionListener(e ->
			SwingUtil.exceptionWrapper(() -> cargarActividades())
		);

		view.getRbFuturas().addActionListener(e ->
			SwingUtil.exceptionWrapper(() -> cargarActividades())
		);

		view.getBtnVolver().addActionListener(e ->
			view.getFrame().dispose()
		);

		view.getTablaActividades().addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				if (e.getClickCount() == 2) {
					SwingUtil.exceptionWrapper(() -> abrirConfirmacionActividadSeleccionada());
				}
			}
		});

		cargarActividades();
		view.getFrame().setVisible(true);
	}

	private void cargarComboActividades() {
		view.getCbActividad().removeAllItems();
		view.getCbActividad().addItem("Todas");

		List<String> actividades = model.obtenerNombresActividades();
		for (String nombre : actividades) {
			view.getCbActividad().addItem(nombre);
		}
	}

	private void cargarActividades() {
		String filtroNombre = (String) view.getCbActividad().getSelectedItem();
		String tipoFiltro = view.getRbActivas().isSelected() ? "ACTIVAS" : "FUTURAS";

		List<ActividadCancelDTO> actividades = model.obtenerActividades(filtroNombre, tipoFiltro);
		rellenarTablaActividades(actividades);
	}

	private void rellenarTablaActividades(List<ActividadCancelDTO> actividades) {
		DefaultTableModel tm = view.getModeloTabla();
		tm.setRowCount(0);

		for (ActividadCancelDTO a : actividades) {
			tm.addRow(new Object[] {
				a.getIdActividad(),
				a.getNombre(),
				a.getInstalacion(),
				formatearFecha(a.getFechaInicio()),
				formatearFecha(a.getFechaFin()),
				a.getInscritos()
			});
		}
	}

	private void abrirConfirmacionActividadSeleccionada() {
		int fila = view.getTablaActividades().getSelectedRow();
		if (fila < 0) {
			JOptionPane.showMessageDialog(
				view.getFrame(),
				"Seleccione una actividad.",
				"Información",
				JOptionPane.INFORMATION_MESSAGE
			);
			return;
		}

		int idActividad = (int) view.getModeloTabla().getValueAt(fila, 0);
		ActividadCancelDTO actividad = model.obtenerActividadPorId(idActividad);

		if (actividad == null) {
			JOptionPane.showMessageDialog(
				view.getFrame(),
				"No se ha encontrado la actividad seleccionada.",
				"Error",
				JOptionPane.ERROR_MESSAGE
			);
			return;
		}

		ConfirmarCancelacionView confirmView = new ConfirmarCancelacionView();
		cargarDatosConfirmacion(confirmView, actividad);
		initConfirmacionController(confirmView, actividad);
		confirmView.getFrame().setVisible(true);
	}

	private void cargarDatosConfirmacion(ConfirmarCancelacionView confirmView, ActividadCancelDTO actividad) {
		confirmView.getLblNombreValor().setText(actividad.getNombre());
		confirmView.getLblInstalacionValor().setText(actividad.getInstalacion());
		confirmView.getLblFechaInicioValor().setText(formatearFecha(actividad.getFechaInicio()));
		confirmView.getLblFechaFinValor().setText(formatearFecha(actividad.getFechaFin()));
		confirmView.getLblInscritosValor().setText(String.valueOf(actividad.getInscritos()));

		List<AfectadoActividadDTO> afectados = model.obtenerAfectados(actividad.getIdActividad());

		DefaultTableModel tm = confirmView.getModeloTablaAfectados();
		tm.setRowCount(0);

		for (AfectadoActividadDTO a : afectados) {
			tm.addRow(new Object[] {
				a.getNombre(),
				"socio".equals(a.getTipo()) ? "Socio" : "No socio",
				a.getEmail() == null ? "" : a.getEmail(),
				a.getDni() == null ? "" : a.getDni(),
				a.getPagado() == 1 ? "Sí" : "No"
			});
		}
	}

	private void initConfirmacionController(ConfirmarCancelacionView confirmView, ActividadCancelDTO actividad) {
		confirmView.getBtnCancelar().addActionListener(e ->
			confirmView.getFrame().dispose()
		);

		confirmView.getBtnContinuar().addActionListener(e ->
			SwingUtil.exceptionWrapper(() -> continuarCancelacion(confirmView, actividad))
		);
	}

	private void continuarCancelacion(ConfirmarCancelacionView confirmView, ActividadCancelDTO actividad) {
		String motivo = confirmView.getTxtMotivo().getText().trim();

		if (motivo.isEmpty()) {
			JOptionPane.showMessageDialog(
				confirmView.getFrame(),
				"Debe indicar un motivo para la cancelación.",
				"Validación",
				JOptionPane.WARNING_MESSAGE
			);
			return;
		}

		List<AfectadoActividadDTO> afectados = model.obtenerAfectados(actividad.getIdActividad());

		int opcion = JOptionPane.showConfirmDialog(
			confirmView.getFrame(),
			"Se va a cancelar la actividad \"" + actividad.getNombre() + "\".\n¿Desea continuar?",
			"Confirmación",
			JOptionPane.YES_NO_OPTION,
			JOptionPane.QUESTION_MESSAGE
		);

		if (opcion != JOptionPane.YES_OPTION) {
			return;
		}

		model.ejecutarCancelacionCompleta(actividad.getIdActividad(), motivo);

		try {
			generarTxtNotificacion(actividad, afectados, motivo);
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(
				confirmView.getFrame(),
				"La actividad se canceló, pero hubo un error al generar el archivo .txt.",
				"Error",
				JOptionPane.ERROR_MESSAGE
			);
			ex.printStackTrace();
		}

		JOptionPane.showMessageDialog(
			confirmView.getFrame(),
			"La actividad se ha cancelado correctamente.",
			"Operación completada",
			JOptionPane.INFORMATION_MESSAGE
		);

		confirmView.getFrame().dispose();
		cargarComboActividades();
		cargarActividades();
	}

	private void generarTxtNotificacion(ActividadCancelDTO actividad, List<AfectadoActividadDTO> afectados, String motivo)
			throws IOException {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Guardar notificación de cancelación");
		chooser.setSelectedFile(new File("cancelacion_actividad_" + actividad.getIdActividad() + ".txt"));

		int resultado = chooser.showSaveDialog(view.getFrame());
		if (resultado != JFileChooser.APPROVE_OPTION) {
			return;
		}

		File fichero = chooser.getSelectedFile();
		if (!fichero.getName().toLowerCase().endsWith(".txt")) {
			fichero = new File(fichero.getAbsolutePath() + ".txt");
		}

		try (FileWriter fw = new FileWriter(fichero)) {
			fw.write("NOTIFICACIÓN DE CANCELACIÓN DE ACTIVIDAD\n");
			fw.write("=======================================\n\n");

			fw.write("Actividad: " + actividad.getNombre() + "\n");
			fw.write("Instalación: " + actividad.getInstalacion() + "\n");
			fw.write("Fecha inicio: " + formatearFecha(actividad.getFechaInicio()) + "\n");
			fw.write("Fecha fin: " + formatearFecha(actividad.getFechaFin()) + "\n");
			fw.write("Motivo: " + motivo + "\n\n");

			fw.write("AFECTADOS\n");
			fw.write("---------\n");

			for (AfectadoActividadDTO a : afectados) {
				if (!"socio".equals(a.getTipo())) {
					continue;
				}

				fw.write("Nombre: " + valorSeguro(a.getNombre()) + "\n");
				fw.write("Tipo: " + ("socio".equals(a.getTipo()) ? "Socio" : "No socio") + "\n");

				if (a.getEmail() != null && !a.getEmail().isEmpty()) {
					fw.write("Email: " + a.getEmail() + "\n");
				}
				if (a.getDni() != null && !a.getDni().isEmpty()) {
					fw.write("DNI: " + a.getDni() + "\n");
				}

				if (a.getPagado() == 1) {
					fw.write("Estado pago: Pagado\n");
					fw.write("Se aplicará un descuento de " + String.format("%.2f", a.getMontoDescuento())
						+ " € en el próximo cobro.\n");
				} else {
					fw.write("Estado pago: No pagado\n");
					fw.write("No procede devolución.\n");
				}

				fw.write("---------------------------------------\n");
			}
		}
	}

	private String formatearFecha(String fecha) {
		try {
			LocalDate d = LocalDate.parse(fecha);
			return d.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		} catch (Exception e) {
			return fecha;
		}
	}

	private String valorSeguro(String s) {
		return s == null ? "" : s;
	}
}