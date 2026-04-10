package cd.admin.Alejandro.InformeOcupacion;

/**
 * Entidad ligera de Actividad para poblar el combo de filtros del informe.
 * Solo contiene id y nombre; el resto de campos no son necesarios en este contexto.
 *
 * IMPORTANTE: todos los campos son String porque Apache Commons DbUtils requiere
 * que el tipo Java coincida con lo que devuelve SQLite al usar CAST a TEXT.
 */
public class ActividadEntity {

	private String id;
	private String nombre;

	public String getId()     { return id;     }
	public String getNombre() { return nombre; }

	public void setId(String value)     { this.id     = value; }
	public void setNombre(String value) { this.nombre = value; }
}
