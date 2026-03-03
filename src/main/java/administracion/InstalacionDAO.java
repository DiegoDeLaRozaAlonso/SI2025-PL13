package administracion;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class InstalacionDAO {

    private Connection conexion;

    public InstalacionDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public int obtenerIdPorNombre(String nombreInstalacion) {

        String sql = "SELECT id_instalacion FROM Instalaciones WHERE nombreInstalacion = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, nombreInstalacion);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("id_instalacion");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    // ==============================
    // OBTENER HORARIO
    // ==============================

    public LocalTime[] obtenerHorario(int idInstalacion, String tipoHorario) {

        String sql = """
                SELECT hora_apertura, hora_cierre
                FROM HorariosInstalacion
                WHERE id_instalacion = ?
                AND tipo_horario = ?
                """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idInstalacion);
            ps.setString(2, tipoHorario);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                LocalTime apertura = LocalTime.parse(rs.getString("hora_apertura"));
                LocalTime cierre = LocalTime.parse(rs.getString("hora_cierre"));

                return new LocalTime[]{apertura, cierre};
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // ==============================
    // SOLAPE RESERVAS
    // ==============================

    public boolean haySolapeReserva(int idInstalacion,
                                    String nuevaInicio,
                                    String nuevaFin) {

        String sql = """
                SELECT COUNT(*)
                FROM Reservas
                WHERE id_instalacion = ?
                AND estado != 'cancelada'
                AND datetime(fecha_hora_inicio) < datetime(?)
                AND datetime(fecha_hora_inicio, '+' || duracion || ' minutes') > datetime(?)
                """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idInstalacion);
            ps.setString(2, nuevaFin);
            ps.setString(3, nuevaInicio);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true; // si falla, mejor bloquear
    }

    // ==============================
    // SOLAPE SESIONES
    // ==============================

    public boolean haySolapeSesion(int idInstalacion,
                                   String fecha,
                                   String horaInicio,
                                   String horaFin) {

        String sql = """
                SELECT COUNT(*)
                FROM SesionesActividad
                WHERE id_instalacion = ?
                AND fecha = ?
                AND hora_inicio < ?
                AND hora_fin > ?
                """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idInstalacion);
            ps.setString(2, fecha);
            ps.setString(3, horaFin);
            ps.setString(4, horaInicio);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    // ==============================
    // SOLAPE PLANIFICACION
    // ==============================

    public boolean haySolapePlanificacion(int idInstalacion,
                                          String fecha,
                                          String horaInicio,
                                          String horaFin) {

        String sql = """
                SELECT COUNT(*)
                FROM PlanificacionActividades
                WHERE id_instalacion = ?
                AND fecha = ?
                AND hora_inicio < ?
                AND hora_fin > ?
                """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idInstalacion);
            ps.setString(2, fecha);
            ps.setString(3, horaFin);
            ps.setString(4, horaInicio);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }
}