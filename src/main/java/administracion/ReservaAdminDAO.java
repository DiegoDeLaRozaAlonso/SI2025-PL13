package administracion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ReservaAdminDAO {

	private Connection conexion;

    public ReservaAdminDAO(Connection conexion) {
        this.conexion = conexion;
    }

    
 // ---------------------------------------------------
    // 1️⃣ Calcular coste según precio de Instalacion
    // ---------------------------------------------------
    public double calcularCosto(int idInstalacion, int duracionMinutos) {

        String sql = "SELECT precioInstalacion FROM Instalaciones WHERE id_instalacion = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idInstalacion);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double precioHora = rs.getDouble("precioInstalacion");
                double horas = duracionMinutos / 60.0;
                return precioHora * horas;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // ---------------------------------------------------
    // 2️⃣ Comprobar solapamiento con RESERVAS
    // ---------------------------------------------------
    private boolean existeSolapamientoReservas(int idInstalacion,
                                               LocalDateTime inicioNueva,
                                               int duracionNueva) {

        LocalDateTime finNueva = inicioNueva.plusMinutes(duracionNueva);

        String sql = """
            SELECT COUNT(*) 
            FROM Reservas
            WHERE id_instalacion = ?
            AND estado = 'activa'
            AND fecha_hora_inicio < ?
            AND datetime(fecha_hora_inicio, '+' || duracion || ' minutes') > ?
        """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idInstalacion);
            ps.setTimestamp(2, Timestamp.valueOf(finNueva));
            ps.setTimestamp(3, Timestamp.valueOf(inicioNueva));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ---------------------------------------------------
    // 3️⃣ Comprobar solapamiento con SESIONES ACTIVIDAD
    // ---------------------------------------------------
    private boolean existeSolapamientoSesiones(int idInstalacion,
                                               LocalDateTime inicioNueva,
                                               int duracionNueva) {

        LocalDateTime finNueva = inicioNueva.plusMinutes(duracionNueva);

        String sql = """
            SELECT COUNT(*) 
            FROM SesionesActividad
            WHERE id_instalacion = ?
            AND datetime(fecha || ' ' || hora_inicio) < ?
            AND datetime(fecha || ' ' || hora_fin) > ?
        """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idInstalacion);
            ps.setTimestamp(2, Timestamp.valueOf(finNueva));
            ps.setTimestamp(3, Timestamp.valueOf(inicioNueva));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ---------------------------------------------------
    // 4️⃣ Comprobar solapamiento con PLANIFICACION
    // ---------------------------------------------------
    private boolean existeSolapamientoPlanificacion(int idInstalacion,
                                                    LocalDateTime inicioNueva,
                                                    int duracionNueva) {

        LocalDateTime finNueva = inicioNueva.plusMinutes(duracionNueva);

        String sql = """
            SELECT COUNT(*) 
            FROM PlanificacionActividades
            WHERE id_instalacion = ?
            AND datetime(fecha || ' ' || hora_inicio) < ?
            AND datetime(fecha || ' ' || hora_fin) > ?
        """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idInstalacion);
            ps.setTimestamp(2, Timestamp.valueOf(finNueva));
            ps.setTimestamp(3, Timestamp.valueOf(inicioNueva));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ---------------------------------------------------
    // 5️⃣ Método público que lo comprueba TODO
    // ---------------------------------------------------
    public boolean existeSolapamiento(int idInstalacion,
                                      LocalDateTime inicioNueva,
                                      int duracionNueva) {

        return existeSolapamientoReservas(idInstalacion, inicioNueva, duracionNueva)
                || existeSolapamientoSesiones(idInstalacion, inicioNueva, duracionNueva)
                || existeSolapamientoPlanificacion(idInstalacion, inicioNueva, duracionNueva);
    }

    // ---------------------------------------------------
    // 6️⃣ Insertar reserva
    // ---------------------------------------------------
    public boolean insertar(int idSocio,
                            int idInstalacion,
                            LocalDateTime inicio,
                            int duracionMinutos) {

        double costo = calcularCosto(idInstalacion, duracionMinutos);

        String sql = """
            INSERT INTO Reservas
            (id_socio, id_instalacion, fecha_hora_inicio,
             duracion, costo, pagado, estado)
            VALUES (?, ?, ?, ?, ?, 0, 'activa')
        """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idSocio);
            ps.setInt(2, idInstalacion);
            ps.setTimestamp(3, Timestamp.valueOf(inicio));
            ps.setInt(4, duracionMinutos);
            ps.setDouble(5, costo);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
