package cd.socio.diego.misReservas;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import giis.demo.util.SwingUtil;

public class MisReservasController {

	private MisReservasModel model;
	private MisReservasView view;
	private int idSocio;

	public MisReservasController(MisReservasModel model, MisReservasView view, int idSocio) {
		this.model = model;
		this.view = view;
		this.idSocio = idSocio;
		this.initController();
	}

	public void initController() {
		view.getBtnFiltrar().addActionListener(e ->
			SwingUtil.exceptionWrapper(() -> cargarReservas())
		);

		view.getRbTodas().addActionListener(e ->
			SwingUtil.exceptionWrapper(() -> cargarReservas())
		);

		view.getRbPasadas().addActionListener(e ->
			SwingUtil.exceptionWrapper(() -> cargarReservas())
		);

		view.getRbActivas().addActionListener(e ->
			SwingUtil.exceptionWrapper(() -> cargarReservas())
		);

		view.getBtnVolver().addActionListener(e ->
			view.getFrame().dispose()
		);

		view.getBtnDescargar().addActionListener(e -> {
			try {
				descargarTXT();
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(
					view.getFrame(),
					"Error al descargar el archivo.",
					"Error",
					JOptionPane.ERROR_MESSAGE
				);
			}
		});

		cargarReservas();
		view.getFrame().setVisible(true);
	}

	private void cargarReservas() {
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

		String filtro = "TODAS";
		if (view.getRbPasadas().isSelected()) {
			filtro = "PASADAS";
		} else if (view.getRbActivas().isSelected()) {
			filtro = "ACTIVAS";
		}

		List<ReservaDTO> reservas = model.obtenerReservas(idSocio, fechaInicio, fechaFin, filtro);
		rellenarTabla(reservas);
	}

	private void rellenarTabla(List<ReservaDTO> reservas) {
		DefaultTableModel tm = view.getModeloTabla();
		tm.setRowCount(0);

		for (ReservaDTO r : reservas) {
			tm.addRow(new Object[] {
				formatearFecha(r.getFecha()),
				recortarSegundos(r.getHora()),
				r.getInstalacion(),
				r.getDuracion() + " min",
				String.format("%.2f", r.getPrecio()),
				r.getPagado() == 1 ? "Sí" : "No"
			});
		}
	}

	private void descargarTXT() throws IOException {
		if (view.getModeloTabla().getRowCount() == 0) {
			JOptionPane.showMessageDialog(
				view.getFrame(),
				"No hay reservas para descargar.",
				"Información",
				JOptionPane.INFORMATION_MESSAGE
			);
			return;
		}

		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Guardar reservas");
		chooser.setSelectedFile(new File("mis_reservas.txt"));

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
			"Reservas descargadas correctamente en formato .txt.",
			"Descarga completada",
			JOptionPane.INFORMATION_MESSAGE
		);
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

	private String recortarSegundos(String hora) {
		if (hora == null) {
			return "";
		}
		if (hora.length() >= 5) {
			return hora.substring(0, 5);
		}
		return hora;
	}

	private String formatearFecha(String fecha) {
		try {
			LocalDate d = LocalDate.parse(fecha);
			DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			return d.format(f);
		} catch (Exception e) {
			return fecha;
		}
	}
}