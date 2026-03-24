package administracion;

import java.sql.*;

public class UsuarioDAO {

    private Connection conexion;

    public UsuarioDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public int obtenerIdPorNombre(String nombreUsuario) {

        String sql = "SELECT id_socio FROM Socios WHERE nombre = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, nombreUsuario);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("id_socio");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }
}