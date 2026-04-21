package cd.admin.diego.informeactividad;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import giis.demo.util.SwingUtil;

public class InformeActividadController {

	private InformeActividadModel model;
	private InformeActividadView view;

	public InformeActividadController(InformeActividadModel model, InformeActividadView view) {
		this.model = model;
		this.view = view;
	}

	public void initController() {
		cargarCombos();
		actualizarEstadoFiltros();

		view.getRbDosFechas().addActionListener(e ->
			SwingUtil.exceptionWrapper(() -> {
				actualizarEstadoFiltros();
				cargarInforme();
			})
		);

		view.getRbAnho().addActionListener(e ->
			SwingUtil.exceptionWrapper(() -> {
				actualizarEstadoFiltros();
				cargarInforme();
			})
		);

		view.getRbPeriodo().addActionListener(e ->
			SwingUtil.exceptionWrapper(() -> {
				actualizarEstadoFiltros();
				cargarInforme();
			})
		);

		view.getDcFechaInicio().getDateEditor().addPropertyChangeListener(evt ->
			SwingUtil.exceptionWrapper(() -> {
				if (view.getRbDosFechas().isSelected()) {
					cargarInforme();
				}
			})
		);

		view.getDcFechaFin().getDateEditor().addPropertyChangeListener(evt ->
			SwingUtil.exceptionWrapper(() -> {
				if (view.getRbDosFechas().isSelected()) {
					cargarInforme();
				}
			})
		);

		view.getCbAnhos().addActionListener(e ->
			SwingUtil.exceptionWrapper(() -> {
				if (view.getRbAnho().isSelected()) {
					cargarInforme();
				}
			})
		);

		view.getCbPeriodos().addActionListener(e ->
			SwingUtil.exceptionWrapper(() -> {
				if (view.getRbPeriodo().isSelected()) {
					cargarInforme();
				}
			})
		);

		view.getCbActividades().addActionListener(e ->
			SwingUtil.exceptionWrapper(this::cargarInforme)
		);

		view.getBtnCancelar().addActionListener(e -> view.getFrame().dispose());

		view.getBtnGenerarInformeTxt().addActionListener(e -> {
			try {
				generarTXT();
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(
					view.getFrame(),
					"Error al generar el archivo TXT.",
					"Error",
					JOptionPane.ERROR_MESSAGE
				);
			}
		});

		cargarInforme();
		view.getFrame().setVisible(true);
	}

	private void cargarCombos() {
		view.getCbAnhos().removeAllItems();
		for (Integer anho : model.obtenerAniosDisponibles()) {
			view.getCbAnhos().addItem(anho);
		}

		view.getCbPeriodos().removeAllItems();
		for (PeriodoDTO periodo : model.obtenerPeriodos()) {
			view.getCbPeriodos().addItem(periodo);
		}

		view.getCbActividades().removeAllItems();

		ActividadDTO todas = new ActividadDTO();
		todas.setIdActividad(0);
		todas.setNombre("Todas");
		view.getCbActividades().addItem(todas);

		for (ActividadDTO actividad : model.obtenerActividades()) {
			view.getCbActividades().addItem(actividad);
		}
	}

	private void actualizarEstadoFiltros() {
		boolean dosFechas = view.getRbDosFechas().isSelected();
		boolean anho = view.getRbAnho().isSelected();
		boolean periodo = view.getRbPeriodo().isSelected();

		view.getDcFechaInicio().setEnabled(dosFechas);
		view.getDcFechaFin().setEnabled(dosFechas);

		view.getCbAnhos().setEnabled(anho);
		view.getCbPeriodos().setEnabled(periodo);
	}

	private void cargarInforme() {
		List<InformeActividadDTO> actividades;
		Integer idActividad = getIdActividadSeleccionada();

		if (view.getRbDosFechas().isSelected()) {
			String fechaInicio = getFechaSqlDesdeChooser(view.getDcFechaInicio());
			String fechaFin = getFechaSqlDesdeChooser(view.getDcFechaFin());

			if (fechaInicio != null && fechaFin != null) {
				try {
					LocalDate fi = LocalDate.parse(fechaInicio);
					LocalDate ff = LocalDate.parse(fechaFin);

					if (fi.isAfter(ff)) {
						JOptionPane.showMessageDialog(
							view.getFrame(),
							"La fecha de inicio no puede ser posterior a la fecha de fin.",
							"Filtro incorrecto",
							JOptionPane.WARNING_MESSAGE
						);
						return;
					}
				} catch (Exception e) {
					return;
				}
			}

			actividades = model.obtenerInformePorFechas(fechaInicio, fechaFin, idActividad);

		} else if (view.getRbAnho().isSelected()) {
			Integer anho = (Integer) view.getCbAnhos().getSelectedItem();
			if (anho == null) {
				return;
			}
			actividades = model.obtenerInformePorAnho(anho, idActividad);

		} else {
			PeriodoDTO periodo = (PeriodoDTO) view.getCbPeriodos().getSelectedItem();
			if (periodo == null) {
				return;
			}
			actividades = model.obtenerInformePorPeriodo(periodo.getIdPeriodo(), idActividad);
		}

		rellenarTabla(actividades);
	}

	private Integer getIdActividadSeleccionada() {
		ActividadDTO actividadSeleccionada = (ActividadDTO) view.getCbActividades().getSelectedItem();

		if (actividadSeleccionada == null || actividadSeleccionada.getIdActividad() == 0) {
			return null;
		}

		return actividadSeleccionada.getIdActividad();
	}

	private void rellenarTabla(List<InformeActividadDTO> actividades) {
		DefaultTableModel tm = view.getModeloTabla();
		tm.setRowCount(0);

		for (InformeActividadDTO a : actividades) {
			tm.addRow(new Object[] {
				a.getNombre(),
				a.getEdicion(),
				a.getPlazas(),
				a.getInscritos(),
				String.format("%.2f%%", a.getPorcentajeOcupacion()),
				a.getEnListaEspera()
			});
		}
	}

	private void generarTXT() throws IOException {
		if (view.getModeloTabla().getRowCount() == 0) {
			JOptionPane.showMessageDialog(
				view.getFrame(),
				"No hay actividades para exportar.",
				"Información",
				JOptionPane.INFORMATION_MESSAGE
			);
			return;
		}

		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Guardar informe de actividades");
		chooser.setSelectedFile(new File("informe_actividades.txt"));

		int resultado = chooser.showSaveDialog(view.getFrame());
		if (resultado != JFileChooser.APPROVE_OPTION) {
			return;
		}

		File fichero = chooser.getSelectedFile();

		if (!fichero.getName().toLowerCase().endsWith(".txt")) {
			fichero = new File(fichero.getAbsolutePath() + ".txt");
		}

		try (FileWriter fw = new FileWriter(fichero)) {
			DefaultTableModel tm = view.getModeloTabla();

			fw.write("INFORME DE ACTIVIDADES\n");
			fw.write("======================\n\n");
			fw.write("Filtro aplicado: " + obtenerDescripcionFiltro() + "\n");
			fw.write("Actividad: " + obtenerDescripcionActividad() + "\n\n");

			for (int c = 0; c < tm.getColumnCount(); c++) {
				fw.write(tm.getColumnName(c));
				if (c < tm.getColumnCount() - 1) {
					fw.write(" | ");
				}
			}
			fw.write("\n");

			for (int f = 0; f < tm.getRowCount(); f++) {
				for (int c = 0; c < tm.getColumnCount(); c++) {
					Object valor = tm.getValueAt(f, c);
					fw.write(valor == null ? "" : valor.toString());
					if (c < tm.getColumnCount() - 1) {
						fw.write(" | ");
					}
				}
				fw.write("\n");
			}
		}

		JOptionPane.showMessageDialog(
			view.getFrame(),
			"Informe generado correctamente en formato .txt.",
			"Informe completado",
			JOptionPane.INFORMATION_MESSAGE
		);
	}

	private String obtenerDescripcionFiltro() {
		if (view.getRbDosFechas().isSelected()) {
			String fechaInicio = getFechaSqlDesdeChooser(view.getDcFechaInicio());
			String fechaFin = getFechaSqlDesdeChooser(view.getDcFechaFin());

			return "2 fechas [" + (fechaInicio == null ? "" : fechaInicio)
					+ " - " + (fechaFin == null ? "" : fechaFin) + "]";
		}

		if (view.getRbAnho().isSelected()) {
			return "Año [" + view.getCbAnhos().getSelectedItem() + "]";
		}

		PeriodoDTO periodo = (PeriodoDTO) view.getCbPeriodos().getSelectedItem();
		return "Periodo [" + (periodo == null ? "" : periodo.getNombre()) + "]";
	}

	private String obtenerDescripcionActividad() {
		ActividadDTO actividad = (ActividadDTO) view.getCbActividades().getSelectedItem();
		return actividad == null ? "Todas" : actividad.getNombre();
	}

	private String getFechaSqlDesdeChooser(com.toedter.calendar.JDateChooser chooser) {
		if (chooser.getDate() != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.format(chooser.getDate());
		}

		try {
			Object editor = chooser.getDateEditor().getUiComponent();
			if (editor instanceof JTextField) {
				String texto = ((JTextField) editor).getText().trim();

				if (!texto.isEmpty()) {
					SimpleDateFormat entrada = new SimpleDateFormat("dd/MM/yyyy");
					entrada.setLenient(false);
					java.util.Date fecha = entrada.parse(texto);

					SimpleDateFormat salida = new SimpleDateFormat("yyyy-MM-dd");
					return salida.format(fecha);
				}
			}
		} catch (Exception e) {
			return null;
		}

		return null;
	}
}