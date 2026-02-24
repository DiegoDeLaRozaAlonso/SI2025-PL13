package cd.admin.pablo.periodo;

import java.util.Date;

import giis.demo.util.ApplicationException;
import giis.demo.util.Database;

public class PeriodoModel {
	
	private Database db = new Database();
	
	private java.sql.Date fechaSocio;
	private java.sql.Date fechaFinSocio;
	private java.sql.Date fechaNoSocio;
	
	public void insertarPeriodo(PeriodoDTO periodo) {
		
		validaParametros(
				periodo.getNombre(), 
				periodo.getDescripcion(),
				periodo.getFechaInicio(), 
				periodo.getFechaFinSocio(), 
				periodo.getFechaFinNoSocio()
		);
		
		convierteFecha(periodo);
		
		String sql = "INSERT INTO PeriodosInscripcion"
				+ "(nombre, descripcion, fecha_inicio_socio, fecha_fin_socio, fecha_fin_noSocio) "
				+ "VALUES (?,?,?,?,?);";
		
		db.executeUpdate(sql, 
				periodo.getNombre(), 
				periodo.getDescripcion(),
				fechaSocio,
				fechaFinSocio,
				fechaNoSocio
				/*new java.sql.Date(periodo.getFechaInicio().getYear(), periodo.getFechaInicio().getMonth(), periodo.getFechaInicio().getDay()),
				new java.sql.Date(periodo.getFechaFinSocio()),
				new java.sql.Date(periodo.getFechaFinNoSocio())*/
		);
	}
	
	private void convierteFecha(PeriodoDTO periodo){
		fechaSocio = new java.sql.Date(periodo.getFechaInicio().getYear(), periodo.getFechaInicio().getMonth(), periodo.getFechaInicio().getDay());
		fechaNoSocio = new java.sql.Date(periodo.getFechaFinNoSocio().getYear(), periodo.getFechaFinNoSocio().getMonth(), periodo.getFechaFinNoSocio().getDay());
		fechaFinSocio = new java.sql.Date(periodo.getFechaFinSocio().getYear(), periodo.getFechaFinSocio().getMonth(), periodo.getFechaFinSocio().getDay());
		
	}
	
	private void validaParametros(String nombre, String desc, Date inicio, Date finSocio, Date finNoSocio) {
		
		if (nombre == null || nombre.trim().isEmpty()) 
			throw new ApplicationException("El campo nombre no puede estar vacio");

		if (desc == null || desc.trim().isEmpty()) 
			throw new ApplicationException("El campo descripcion no puede estar vacio");
		
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
