package cd.admin.pablo.periodo;

import java.util.Date;

import giis.demo.util.ApplicationException;
import giis.demo.util.Database;

public class PeriodoModel {
	
	private Database db = new Database();
	
	public void insertarPeriodo(PeriodoDTO periodo) {
		String sql = "INSERT INTO PeriodosInscripcion "
				+ "(id_periodo, id_actividad, nombre, descripcion, tipo, fecha_inicio, fecha_fin) "
				+ "VALUES (?,?,?,?,?,?,?,?,?)";
	}
	
	private void validaNotEmpty() {
		
	}
	
	
	private void validaFechaPeriodo(Date inicio, Date finSocio, Date finNoSocio) {
		validaNotNull(inicio, "La fecha de inicio del periodo no puede ser nula");
		validaNotNull(finSocio, "La fecha de fin del periodo de SOCIOS no puede ser nula");
		validaNotNull(finNoSocio, "La fecha de fin del periodo de NO SOCIOS no puede ser nula");
		validaFecha(inicio.compareTo(finSocio) <= 0, "La fecha de inicio no puede ser posterior a la de fin de SOCIO");
		validaFecha(finSocio.compareTo(finNoSocio) <= 0, "La fecha de fin SOCIO no puede ser posterior a la fin de NO SOCIO");
	}
	
	/* De uso general para validacion de objetos */
	private void validaNotNull(Object obj, String message) {
		if (obj == null)
			throw new ApplicationException(message);
	}

	private void validaFecha(boolean condition, String message) {
		if (!condition)
			throw new ApplicationException(message);
	}
}
