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

    
    public boolean insertar(int idSocio,
                            int idInstalacion,
                            LocalDateTime inicio,
                            int duracion,
                            double costo,
                            String estado) {

        String sql = """
            INSERT INTO Reservas 
            (id_socio, id_instalacion, fecha_hora_inicio, duracion, costo, pagado, estado)
            VALUES (?, ?, ?, ?, ?, false, ?)
        """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idSocio);
            ps.setInt(2, idInstalacion);
            ps.setTimestamp(3, Timestamp.valueOf(inicio));
            ps.setInt(4, duracion);
            ps.setDouble(5, costo);
            ps.setString(6, estado);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    public boolean existeSolapamiento(int idInstalacion,
                                      LocalDateTime inicioNueva,
                                      int duracionNueva) {

        LocalDateTime finNueva = inicioNueva.plusMinutes(duracionNueva);

        String sql = """
            SELECT COUNT(*) 
            FROM Reservas
            WHERE id_instalacion = ?
            AND estado = 'activa'
            AND fecha_hora_inicio < ?
            AND DATE_ADD(fecha_hora_inicio, INTERVAL duracion MINUTE) > ?
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

    
    public ResultSet obtenerPorInstalacion(int idInstalacion) {

        String sql = """
            SELECT * FROM Reservas
            WHERE id_instalacion = ?
            ORDER BY fecha_hora_inicio
        """;

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idInstalacion);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
