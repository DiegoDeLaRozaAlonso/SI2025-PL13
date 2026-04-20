package cd.admin.luismi.informeactividades;

import giis.demo.util.Database;
import giis.demo.util.ApplicationException;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class InformeActividadesModelo {

    private final Database db = new Database();

    public List<ActividadReporteDTO> obtenerInformeActividades(String filtroActividad, String filtroEstado, String filtroEdicion, String filtroFechaInicio, String filtroFechaFin) {
        String sql = """
            SELECT 
                a.nombre AS nombre,
                i.nombre AS instalacion,
                a.estado AS estado,
                a.fecha_inicio AS fechaInicio,
                a.fecha_fin AS fechaFin,
                a.aforo AS numeroPlazas,
                (SELECT COUNT(*) FROM Inscripciones ins WHERE ins.id_actividad = a.id_actividad AND ins.estado='admitido') AS numeroReservas,
                (SELECT COUNT(*) FROM Inscripciones ins WHERE ins.id_actividad = a.id_actividad AND ins.estado='lista_espera') AS listaEspera,
                a.edicion AS ediciones
            FROM Actividades a
            JOIN Instalaciones i ON a.id_instalacion = i.id_instalacion
            WHERE 1=1
        """;

        List<Object> params = new java.util.ArrayList<>();

        if (filtroActividad != null && !filtroActividad.trim().isEmpty()) {
            sql += " AND LOWER(a.nombre) LIKE LOWER(?)";
            params.add("%" + filtroActividad.trim() + "%");
        }
        if (filtroEstado != null && !filtroEstado.trim().isEmpty()) {
            sql += " AND LOWER(a.estado) = LOWER(?)";
            params.add(filtroEstado.trim());
        }
        if (filtroFechaInicio != null && !filtroFechaInicio.trim().isEmpty()) {
            sql += " AND a.fecha_inicio >= ?";
            params.add(filtroFechaInicio.trim());
        }
        if (filtroFechaFin != null && !filtroFechaFin.trim().isEmpty()) {
            sql += " AND a.fecha_fin <= ?";
            params.add(filtroFechaFin.trim());
        }
        if (filtroEdicion != null && !filtroEdicion.trim().isEmpty()) {
            sql += " AND a.edicion = ?";
            try {
                params.add(Integer.parseInt(filtroEdicion.trim()));
            } catch (NumberFormatException e) {
                // If it's not a number, we add a condition that is always false
                sql += " AND 1=0";
            }
        }

        try (Connection conn = db.getConnection()) {
            QueryRunner qr = new QueryRunner();
            return qr.query(conn, sql, new BeanListHandler<>(ActividadReporteDTO.class), params.toArray());
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }
}