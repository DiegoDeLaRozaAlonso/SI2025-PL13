package cd.admin.Alejandro.InformeMorosos;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import giis.demo.util.ApplicationException;
import giis.demo.util.SwingUtil;

/**
 * Controlador del informe de socios morosos (administracion).
 * Punto de entrada: instanciar y llamar a initController().
 */
public class InformeMorososController {

	private static final String SEP_DOBLE  = repeatChar('=', 88);
	private static final String SEP_SIMPLE = repeatChar('-', 88);

	private InformeMorososModel model;
	private InformeMorososView  view;

	/** idSocio -> lista de pagos pendientes (recibos + inscripciones) */
	private Map<String, List<MorososFilaDTO>> agrupado = new LinkedHashMap<>();

	public InformeMorososController(InformeMorososModel m, InformeMorososView v) {
		this.model = m;
		this.view  = v;
		initView();
	}

	public void initController() {
		view.getBtnGenerar().addActionListener(
				e -> SwingUtil.exceptionWrapper(() -> generarInforme()));

		view.getBtnLimpiar().addActionListener(
				e -> SwingUtil.exceptionWrapper(() -> {
					agrupado.clear();
					view.limpiar();
				}));

		view.getBtnExportar().addActionListener(
				e -> SwingUtil.exceptionWrapper(() -> exportarTXT()));

		view.getTabResumen().getSelectionModel().addListSelectionListener(
				e -> SwingUtil.exceptionWrapper(() -> {
					if (!e.getValueIsAdjusting()) mostrarDetalle();
				}));
	}

	public void initView() {
		view.getFrame().setVisible(true);
	}

	// ── Generar informe ───────────────────────────────────────────────────────

	private void generarInforme() {
		String busqueda   = view.getBusqueda();
		double importeMin = parseDouble(view.getImporteMin(), 0.0);

		// Consulta UNION recibos + inscripciones de actividades finalizadas
		List<MorososFilaDTO> filas = model.getPagosPendientes(busqueda);

		// Agrupar por socio
		agrupado.clear();
		for (MorososFilaDTO f : filas)
			agrupado.computeIfAbsent(f.getIdSocio(), k -> new ArrayList<>()).add(f);

		// Filtro de importe minimo sobre el total por socio
		if (importeMin > 0)
			agrupado.entrySet().removeIf(e ->
				e.getValue().stream().mapToDouble(MorososFilaDTO::getTotalDouble).sum() < importeMin);

		// Filas para la tabla resumen
		List<String[]> filasResumen = new ArrayList<>();
		for (Map.Entry<String, List<MorososFilaDTO>> entry : agrupado.entrySet()) {
			List<MorososFilaDTO> pagos = entry.getValue();
			double total  = pagos.stream().mapToDouble(MorososFilaDTO::getTotalDouble).sum();
			filasResumen.add(new String[] {
				pagos.get(0).getIdSocio(),
				pagos.get(0).getNombreSocio(),
				String.valueOf(pagos.size()),
				String.format("%.2f €", total)
			});
		}

		// KPIs
		double totalGlobal = agrupado.values().stream()
				.flatMap(List::stream).mapToDouble(MorososFilaDTO::getTotalDouble).sum();
		int    totalPagos  = agrupado.values().stream().mapToInt(List::size).sum();
		double media       = agrupado.isEmpty() ? 0.0 : totalGlobal / agrupado.size();
		String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());

		view.setResumen(filasResumen);
		view.setKpis(agrupado.size(), totalPagos, totalGlobal, media);
		view.setFecha(fecha);
		view.getBtnExportar().setEnabled(!agrupado.isEmpty());
		view.getTabDetalle().setModel(new javax.swing.table.DefaultTableModel());
	}

	// ── Detalle al seleccionar socio ──────────────────────────────────────────

	private void mostrarDetalle() {
		int fila = view.getTabResumen().getSelectedRow();
		if (fila < 0) return;
		String idSocio = (String) view.getTabResumen().getValueAt(fila, 0);
		String nombre  = (String) view.getTabResumen().getValueAt(fila, 1);
		List<MorososFilaDTO> pagos = agrupado.get(idSocio);
		if (pagos == null) return;

		List<String[]> filasDetalle = new ArrayList<>();
		for (MorososFilaDTO p : pagos) {
			filasDetalle.add(new String[] {
				p.getConcepto(),
				formatFecha(p.getFechaEmision()),
				formatFecha(p.getFechaVencimiento()),
				String.format("%.2f €", p.getTotalDouble())
			});
		}
		view.setDetalle(nombre, filasDetalle);
	}

	// ── Exportar TXT ──────────────────────────────────────────────────────────

	private void exportarTXT() {
		if (agrupado.isEmpty())
			throw new ApplicationException("No hay datos. Pulse 'Generar informe' primero.");

		String ts     = new SimpleDateFormat("yyyy-MM-dd_HH-mm").format(new Date());
		String nombre = "informe_morosos_" + ts + ".txt";

		try (FileWriter fw = new FileWriter(nombre)) {
			fw.write(generarContenidoTXT());
		} catch (IOException e) {
			throw new ApplicationException("No se pudo guardar el fichero: " + e.getMessage());
		}

		javax.swing.JOptionPane.showMessageDialog(
				view.getFrame(),
				"Informe exportado correctamente:\n" + nombre,
				"Exportacion completada",
				javax.swing.JOptionPane.INFORMATION_MESSAGE);
	}

	String generarContenidoTXT() {
		StringBuilder sb    = new StringBuilder();
		String        ahora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());

		sb.append(SEP_DOBLE).append("\n");
		sb.append("  INFORME DE SOCIOS MOROSOS\n");
		sb.append("  Generado el : ").append(ahora).append("\n");
		sb.append("  Incluye recibos mensuales y cuotas de actividades finalizadas sin abonar.\n");
		sb.append(SEP_DOBLE).append("\n\n");

		double totalGlobal = agrupado.values().stream()
				.flatMap(List::stream).mapToDouble(MorososFilaDTO::getTotalDouble).sum();
		int    totalPagos  = agrupado.values().stream().mapToInt(List::size).sum();
		double media       = agrupado.isEmpty() ? 0.0 : totalGlobal / agrupado.size();

		sb.append("RESUMEN\n");
		sb.append(String.format("  Socios morosos              : %d%n",     agrupado.size()));
		sb.append(String.format("  Pagos pendientes (total)    : %d%n",     totalPagos));
		sb.append(String.format("  Importe total pendiente     : %.2f €%n", totalGlobal));
		sb.append(String.format("  Deuda media por socio       : %.2f €%n", media));
		sb.append("\n").append(SEP_DOBLE).append("\n\n");

		int idx = 1;
		for (Map.Entry<String, List<MorososFilaDTO>> entry : agrupado.entrySet()) {
			List<MorososFilaDTO> pagos    = entry.getValue();
			double               totalSoc = pagos.stream().mapToDouble(MorososFilaDTO::getTotalDouble).sum();

			sb.append(String.format("SOCIO %02d%n", idx++));
			sb.append(String.format("  Nombre             : %s%n", pagos.get(0).getNombreSocio()));
			sb.append(String.format("  Nº identificacion  : %s%n", pagos.get(0).getIdSocio()));
			sb.append(String.format("  Importe total      : %.2f €%n", totalSoc));
			sb.append("\n  PAGOS PENDIENTES\n");
			sb.append("  ").append(SEP_SIMPLE, 0, 78).append("\n");
			sb.append(String.format("  %-34s %-12s %-12s %12s%n",
					"CONCEPTO", "EMISION", "VENCIMIENTO", "IMPORTE"));
			sb.append("  ").append(SEP_SIMPLE, 0, 78).append("\n");

			for (MorososFilaDTO p : pagos) {
				sb.append(String.format("  %-34s %-12s %-12s %11.2f €%n",
						truncar(p.getConcepto(), 33),
						formatFecha(p.getFechaEmision()),
						formatFecha(p.getFechaVencimiento()),
						p.getTotalDouble()));
			}

			sb.append("  ").append(SEP_SIMPLE, 0, 78).append("\n");
			sb.append(String.format("  %-47s %11.2f €%n", "TOTAL PENDIENTE", totalSoc));
			sb.append("\n").append(SEP_SIMPLE).append("\n\n");
		}

		sb.append("Fin del informe.\n");
		return sb.toString();
	}

	// ── Utilidades ────────────────────────────────────────────────────────────

	private String formatFecha(String iso) {
		if (iso == null || iso.isBlank()) return "—";
		try {
			// Acepta "yyyy-MM-dd" y "yyyy-MM-dd HH:mm"
			String fecha = iso.trim().split("[ T]")[0];
			String[] p   = fecha.split("-");
			return p[2] + "/" + p[1] + "/" + p[0];
		} catch (Exception e) { return iso; }
	}

	private double parseDouble(String s, double defecto) {
		if (s == null || s.isBlank()) return defecto;
		try { return Double.parseDouble(s.replace(",", ".")); }
		catch (NumberFormatException e) { return defecto; }
	}

	private String truncar(String s, int max) {
		if (s == null) return "";
		return s.length() <= max ? s : s.substring(0, max - 1) + ".";
	}

	private static String repeatChar(char c, int n) {
		StringBuilder sb = new StringBuilder(n);
		for (int i = 0; i < n; i++) sb.append(c);
		return sb.toString();
	}
}
