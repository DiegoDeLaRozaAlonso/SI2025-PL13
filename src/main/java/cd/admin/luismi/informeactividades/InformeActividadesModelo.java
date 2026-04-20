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

    public List<ActividadReporteDTO> obtenerInformeActividades(String filtroActividad) {
        String sql = """
            SELECT 
                a.nombre AS nombre,
                i.nombre AS instalacion,
                a.fecha_inicio AS fechaInicio,
                a.fecha_fin AS fechaFin,
                a.aforo AS numeroPlazas,
                (SELECT COUNT(*) FROM Inscripciones ins WHERE ins.id_actividad = a.id_actividad AND ins.estado='admitido') AS numeroReservas,
                (SELECT COUNT(*) FROM Inscripciones ins WHERE ins.id_actividad = a.id_actividad AND ins.estado='lista_espera') AS listaEspera,
                (SELECT COUNT(*) FROM Actividades a2 WHERE a2.nombre = a.nombre) AS ediciones
            FROM Actividades a
            JOIN Instalaciones i ON a.id_instalacion = i.id_instalacion
        """;

        if (filtroActividad != null && !filtroActividad.trim().isEmpty()) {
            sql += " WHERE LOWER(a.nombre) LIKE LOWER(?)";
        }

        try (Connection conn = db.getConnection()) {
            QueryRunner qr = new QueryRunner();
            if (filtroActividad != null && !filtroActividad.trim().isEmpty()) {
                return qr.query(conn, sql, new BeanListHandler<>(ActividadReporteDTO.class), "%" + filtroActividad.trim() + "%");
            } else {
                return qr.query(conn, sql, new BeanListHandler<>(ActividadReporteDTO.class));
            }
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }
}
