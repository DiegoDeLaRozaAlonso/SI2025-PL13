package cd.socio.luismi.desinscribirseactividad;

import giis.demo.util.Database;
import giis.demo.util.ApplicationException;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class DesinscribirseModelo {

    private final Database db = new Database();

    public List<InscripcionActivaDTO> getInscripcionesSocio(int idSocio) {
        String sql = """
            SELECT 
                i.id_inscripcion AS idInscripcion,
                a.id_actividad AS idActividad,
                a.nombre AS nombreActividad,
                i.estado AS estado,
                a.costo_socio AS precio
            FROM Inscripciones i
            JOIN Actividades a ON i.id_actividad = a.id_actividad
            WHERE i.id_socio = ? AND i.estado IN ('admitido', 'lista_espera')
        """;
        try (Connection conn = db.getConnection()) {
            QueryRunner qr = new QueryRunner();
            return qr.query(conn, sql, new BeanListHandler<>(InscripcionActivaDTO.class), idSocio);
        } catch (SQLException e) {
            throw new ApplicationException("Error obteniendo inscripciones del socio");
        }
    }

    public void cancelarInscripcion(int idInscripcion, int idSocio) {
        try (Connection conn = db.getConnection()) {
            conn.setAutoCommit(false);
            QueryRunner qr = new QueryRunner();

            // 1. Get info about the inscription and activity
            String sqlInfo = """
                SELECT i.estado AS estadoInscripcion, i.pagado, a.fecha_inicio, a.costo_socio, a.id_actividad
                FROM Inscripciones i
                JOIN Actividades a ON i.id_actividad = a.id_actividad
                WHERE i.id_inscripcion = ? AND i.id_socio = ?
            """;
            Map<String, Object> info = qr.query(conn, sqlInfo, new MapHandler(), idInscripcion, idSocio);
            if (info == null) {
                conn.rollback();
                throw new ApplicationException("No se encontró la inscripción o no pertenece al socio.");
            }

            String estadoInsc = (String) info.get("estadoInscripcion");
            boolean pagado = (int) info.get("pagado") == 1;
            String fechaInicioStr = (String) info.get("fecha_inicio");
            double costoSocio = (double) info.get("costo_socio");
            int idActividad = (int) info.get("id_actividad");

            LocalDate fechaInicio = LocalDate.parse(fechaInicioStr);
            LocalDate hoy = LocalDate.now();

            boolean haComenzado = !hoy.isBefore(fechaInicio);

            // 2. Change status to cancelada
            qr.update(conn, "UPDATE Inscripciones SET estado = 'cancelada' WHERE id_inscripcion = ?", idInscripcion);

            // 3. Logic based on whether it started or is in waitlist
            if (!haComenzado || "lista_espera".equals(estadoInsc)) {
                // Not charged or return money
                if (pagado) {
                    // Create reduction
                    String sqlRed = """
                        INSERT INTO Reduccion (id_socio, monto, fecha_generacion, descripcion)
                        VALUES (?, ?, ?, ?)
                    """;
                    qr.update(conn, sqlRed, idSocio, costoSocio, hoy.toString(), "Crédito por cancelación de actividad antes de inicio");
                }

                // 4. Free a spot: if there is someone in waitlist, promote the first one
                if ("admitido".equals(estadoInsc)) {
                    promoverListaEspera(conn, qr, idActividad);
                }
            } else {
                // Activity already started
                // Cost of this month is charged (already handled, or we just leave it and no future receipts will include it)
                // Spot is not freed, or yes? The prompt says "en caso de encontrarse en periodo de inscripcion se le devolvera... y liberara un hueco". 
                // So if it already started, we don't free a spot? The instructions imply spot is only freed if in inscription period.
            }

            conn.commit();
        } catch (SQLException e) {
            throw new ApplicationException("Error cancelando la inscripción");
        }
    }

    private void promoverListaEspera(Connection conn, QueryRunner qr, int idActividad) throws SQLException {
        // Find first person in waitlist
        String sqlWait = """
            SELECT id_inscripcion 
            FROM Inscripciones 
            WHERE id_actividad = ? AND estado = 'lista_espera' 
            ORDER BY fecha_inscripcion ASC 
            LIMIT 1
        """;
        Integer idPromover = qr.query(conn, sqlWait, new ScalarHandler<>(), idActividad);
        if (idPromover != null) {
            qr.update(conn, "UPDATE Inscripciones SET estado = 'admitido' WHERE id_inscripcion = ?", idPromover);
        }
    }
}
