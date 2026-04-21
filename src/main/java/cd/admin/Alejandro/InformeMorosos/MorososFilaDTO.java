package cd.admin.Alejandro.InformeMorosos;

/**
 * DTO generico de un pago pendiente: puede ser un recibo mensual o una
 * inscripcion a una actividad que no ha sido abonada.
 *
 * Todos los campos son String porque Apache Commons DbUtils requiere que el
 * tipo Java coincida con lo que devuelve SQLite al usar CAST a TEXT.
 *
 * El campo 'concepto' se calcula directamente en la query SQL:
 *   - Recibo     -> "Cuota Enero 2026"
 *   - Inscripcion -> "Inscripcion: Aquagym"
 */
public class MorososFilaDTO {

	/** ID del socio */
	private String idSocio;

	/** Nombre del socio */
	private String nombreSocio;

	/** Descripcion del pago: cuota mensual o nombre de la actividad */
	private String concepto;

	/** Fecha de emision o de inscripcion (yyyy-MM-dd) */
	private String fechaEmision;

	/** Fecha de vencimiento del recibo o fecha fin de la actividad (yyyy-MM-dd) */
	private String fechaVencimiento;

	/** Importe pendiente */
	private String total;

	/** "recibo" o "inscripcion" — para el TXT */
	private String tipo;

	public MorososFilaDTO() {}

	// ── Getters ──────────────────────────────────────────────────────────────
	public String getIdSocio()          { return idSocio;          }
	public String getNombreSocio()      { return nombreSocio;      }
	public String getConcepto()         { return concepto;         }
	public String getFechaEmision()     { return fechaEmision;     }
	public String getFechaVencimiento() { return fechaVencimiento; }
	public String getTotal()            { return total;            }
	public String getTipo()             { return tipo;             }

	// ── Setters ──────────────────────────────────────────────────────────────
	public void setIdSocio(String v)          { this.idSocio          = v; }
	public void setNombreSocio(String v)      { this.nombreSocio      = v; }
	public void setConcepto(String v)         { this.concepto         = v; }
	public void setFechaEmision(String v)     { this.fechaEmision     = v; }
	public void setFechaVencimiento(String v) { this.fechaVencimiento = v; }
	public void setTotal(String v)            { this.total            = v; }
	public void setTipo(String v)             { this.tipo             = v; }

	// ── Helpers ───────────────────────────────────────────────────────────────
	public double getTotalDouble() {
		try { return Double.parseDouble(total); }
		catch (Exception e) { return 0.0; }
	}
}
