package cd.admin.luismi.cancelReser;

import giis.demo.util.ApplicationException;
import giis.demo.util.Database;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CancelReservaModelo {

    private final Database db = new Database();
    private final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // Lista socios
    public List<String> getNombresSocios() {
        List<String> lista = new ArrayList<>();

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT nombre FROM Socios ORDER BY nombre")) {

            ResultSet rs = ps.executeQuery();

            while (rs.next())
                lista.add(rs.getString(1));

            return lista;

        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    // Reservas activas por socio
    public List<Object[]> getReservasSocio(String nombre) {

        String sql = """
            SELECT r.id_reserva, i.nombre, r.fecha_hora_inicio, r.duracion
            FROM Reservas r
            JOIN Socios s ON s.id_socio = r.id_socio
            JOIN Instalaciones i ON i.id_instalacion = r.id_instalacion
            WHERE s.nombre=? AND r.estado='activa'
            ORDER BY r.fecha_hora_inicio
            """;

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);

            ResultSet rs = ps.executeQuery();
            List<Object[]> lista = new ArrayList<>();

            while (rs.next()) {
                lista.add(new Object[]{
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4)
                });
            }

            return lista;

        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    // ✅ corregido: normaliza fecha ANTES de parsear
    private String normalizarFecha(String fechaBD) {
        // formato con segundos → cortar
        if (fechaBD.length() > 16) {
            return fechaBD.substring(0, 16);
        }
        return fechaBD;
    }

    public String calcularFin(String inicio, int duracion) {

        inicio = normalizarFecha(inicio);

        LocalDateTime ini = LocalDateTime.parse(inicio, FMT);
        return ini.plusMinutes(duracion)
                  .toLocalTime()
                  .withSecond(0)
                  .withNano(0)
                  .toString();
    }

    // Cancelar reserva (versión final)
    public void cancelarReserva(int idReserva, String motivo) {

        try (Connection conn = db.getConnection()) {

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT id_socio, fecha_hora_inicio, costo " +
                    "FROM Reservas WHERE id_reserva=? AND estado='activa'"
            );
            ps.setInt(1, idReserva);
            ResultSet rs = ps.executeQuery();

            if (!rs.next())
                throw new ApplicationException("Reserva no encontrada o ya cancelada.");

            int idSocio = rs.getInt(1);
            String fechaBD = rs.getString(2);

            // ✅ normalizar fecha (evita errores de parseo y errores de comparación)
            fechaBD = normalizarFecha(fechaBD);

            LocalDateTime inicio = LocalDateTime.parse(fechaBD, FMT)
                    .withSecond(0).withNano(0);

            LocalDateTime ahora = LocalDateTime.now()
                    .withSecond(0).withNano(0);

            if (!inicio.isAfter(ahora))
                throw new ApplicationException("La reserva ya ha comenzado, no se puede cancelar.");

            // ✅ Cancelar
            PreparedStatement ps2 = conn.prepareStatement(
                    "UPDATE Reservas SET estado='cancelada', motivo_cancelacion=? WHERE id_reserva=?"
            );
            ps2.setString(1, motivo);
            ps2.setInt(2, idReserva);
            ps2.executeUpdate();

            // ✅ Devolver horas
            PreparedStatement ps3 = conn.prepareStatement(
                    "UPDATE Socios SET debe_dinero=0 WHERE id_socio=?"
            );
            ps3.setInt(1, idSocio);
            ps3.executeUpdate();

            // ✅ PDF cancelación
            new PDFCancelacion().generar(idReserva, idSocio, motivo, rs.getDouble(3));

        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }
}