package cd.Administracion.Alejandro.Contabilidad;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import giis.demo.util.ApplicationException;
import giis.demo.util.SwingUtil;

/**
 * Controlador para la pantalla de contabilidad mensual (administracion).
 * Es el punto de entrada de esta pantalla; se invoca:
 *  - instanciando el controlador con la vista y el modelo
 *  - ejecutando initController() para instalar los manejadores de eventos
 *
 * Gestiona la generacion de la tabla de socios con sus importes pendientes
 * y la descarga del informe en formato CSV o TXT.
 */
public class ContabilidadMensualController {

	private ContabilidadMensualModel model;
	private ContabilidadMensualView  view;

	public ContabilidadMensualController(ContabilidadMensualModel m, ContabilidadMensualView v) {
		this.model = m;
		this.view  = v;
		this.initView();
	}

	/** Instala los manejadores de eventos */
	public void initController() {
		// Toggle CSV / TXT
		view.getBtnCSV().addActionListener(
				e -> SwingUtil.exceptionWrapper(() -> view.setFormato("CSV")));

		view.getBtnTXT().addActionListener(
				e -> SwingUtil.exceptionWrapper(() -> view.setFormato("TXT")));

		// Boton generar informe
		view.getBtnGenerar().addActionListener(
				e -> SwingUtil.exceptionWrapper(() -> generarInforme()));

		// Boton descargar
		view.getBtnDescargar().addActionListener(
				e -> SwingUtil.exceptionWrapper(() -> descargarInforme()));
	}

	/** Muestra la ventana */
	public void initView() {
		view.getFrame().setVisible(true);
	}

	// ── Manejadores ───────────────────────────────────────────────────────────

	/**
	 * Carga los datos del mes seleccionado y los muestra en la tabla.
	 */
	private void generarInforme() {
		int mes    = view.getMesSeleccionado();
		String mesTexto = view.getMesTexto();
		List<ContabilidadMensualDTO> datos =
				model.getContabilidadMensual(mes, ContabilidadMensualModel.ANHO_BASE);
		view.setDatos(datos, mesTexto);
	}

	/**
	 * Genera el fichero en el formato seleccionado (CSV o TXT) y lo guarda
	 * en el directorio de trabajo con el nombre contabilidad_<mes>.csv/txt
	 */
	private void descargarInforme() {
		int mes = view.getMesSeleccionado();
		String mesTexto = view.getMesTexto();
		List<ContabilidadMensualDTO> datos =
				model.getContabilidadMensual(mes, ContabilidadMensualModel.ANHO_BASE);

		String formato   = view.getFormato();
		String nombreFichero = "contabilidad_" + mesTexto.replace(" ", "_")
				+ "." + formato.toLowerCase();

		try (FileWriter fw = new FileWriter(nombreFichero)) {
			if ("CSV".equals(formato)) {
				fw.write(generarCSV(datos, mesTexto));
			} else {
				fw.write(generarTXT(datos, mesTexto));
			}
			javax.swing.JOptionPane.showMessageDialog(
					view.getFrame(),
					"Fichero guardado: " + nombreFichero,
					"Descarga completada",
					javax.swing.JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException ex) {
			throw new ApplicationException("Error al guardar el fichero: " + ex.getMessage());
		}
	}

	// ── Generadores de contenido ──────────────────────────────────────────────

	private String generarCSV(List<ContabilidadMensualDTO> datos, String mesTexto) {
		StringBuilder sb = new StringBuilder();
		sb.append("ID Socio;Nombre;Actividades Pendientes (EUR);Reservas Pendientes (EUR);Total (EUR)\n");
		for (ContabilidadMensualDTO d : datos) {
			sb.append(d.getIdSocio()).append(";")
			  .append(d.getNombre()).append(";")
			  .append(String.format("%.2f", d.getActividades())).append(";")
			  .append(String.format("%.2f", d.getReservas())).append(";")
			  .append(String.format("%.2f", d.getTotal())).append("\n");
		}
		return sb.toString();
	}

	private String generarTXT(List<ContabilidadMensualDTO> datos, String mesTexto) {
		String sep = "=".repeat(60);
		StringBuilder sb = new StringBuilder();
		sb.append("CONTABILIDAD MENSUAL -- ").append(mesTexto).append("\n")
		  .append(sep).append("\n\n");

		double totalGeneral = 0;
		for (ContabilidadMensualDTO d : datos) {
			sb.append("Socio ").append(d.getIdSocio())
			  .append(" -- ").append(d.getNombre()).append("\n")
			  .append("  Actividades pendientes: ")
			  .append(String.format("%.2f", d.getActividades())).append(" EUR\n")
			  .append("  Reservas pendientes:    ")
			  .append(String.format("%.2f", d.getReservas())).append(" EUR\n")
			  .append("  TOTAL:                  ")
			  .append(String.format("%.2f", d.getTotal())).append(" EUR\n\n");
			totalGeneral += d.getTotal();
		}
		sb.append(sep).append("\n")
		  .append("TOTAL GENERAL: ").append(String.format("%.2f", totalGeneral)).append(" EUR");

		return sb.toString();
	}
}
