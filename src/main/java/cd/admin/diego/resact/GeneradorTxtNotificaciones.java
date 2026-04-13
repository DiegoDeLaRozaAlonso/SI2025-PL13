package cd.admin.diego.resact;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class GeneradorTxtNotificaciones {

	private static final String CARPETA_SALIDA = "notificaciones_cancelacion";

	public int generarTxtsCancelacion(List<NotificacionReservaCanceladaDto> notificaciones) throws IOException {
		Path carpeta = Paths.get(CARPETA_SALIDA);
		Files.createDirectories(carpeta);

		int generados = 0;

		for (NotificacionReservaCanceladaDto dto : notificaciones) {
			String nombreFichero =
					"notificacion_socio_" + dto.getIdSocio()
					+ "_reserva_" + dto.getIdReserva()
					+ ".txt";

			Path ruta = carpeta.resolve(nombreFichero);

			String contenido =
					"Estimado/a " + dto.getNombreSocio() + ":\n\n"
					+ "Le informamos de que su reserva de la instalación "
					+ dto.getInstalacion() + ", prevista para el día "
					+ dto.getFecha() + " a las " + dto.getHora()
					+ ", ha sido cancelada.\n\n"
					+ "Motivo:\n"
					+ "La reserva coincide con la actividad \""
					+ dto.getActividadNueva()
					+ "\", que tiene prioridad en la planificación del centro.\n\n";

			if (dto.isPagada()) {
				contenido +=
						"Información sobre el pago:\n"
						+ "El importe de la reserva (" + String.format("%.2f", dto.getImporte()) + " €) "
						+ "será descontado en su factura del próximo mes.\n\n";
			}

			contenido +=
					"Disculpe las molestias.\n"
					+ "Centro Deportivo\n";

			Files.write(
					ruta,
					contenido.getBytes(StandardCharsets.UTF_8),
					StandardOpenOption.CREATE,
					StandardOpenOption.TRUNCATE_EXISTING
			);

			generados++;
		}

		return generados;
	}
}