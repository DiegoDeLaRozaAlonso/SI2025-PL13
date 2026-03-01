package administracion;

import java.sql.*;

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
}