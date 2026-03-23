package cd.admin.luismi.cancelReser;

import giis.demo.util.ApplicationException;
import giis.demo.util.Database;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CancelReservaModelo {

    private final Database db = new Database();

    // SQLite devuelve "yyyy-MM-dd HH:mm:ss", normalizamos a "yyyy-MM-dd HH:mm"
    private final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // ==========================================================
    // LISTA DE SOCIOS
    // ==========================================================
    public List<String> getNombresSocios() {
        List<String> lista = new ArrayList<>();

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT nombre FROM Socios ORDER BY nombre")) {

            ResultSet rs = ps.executeQuery();
            while (rs.next())
                lista.add(rs.getString("nombre"));

            return lista;

        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    // ==========================================================
    // RESERVAS ACTIVAS DE UN SOCIO
    // ==========================================================
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
                        rs.getInt("id_reserva"),
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

    // ==========================================================
    // NORMALIZAR FECHA: QUITAR SEGUNDOS
    // ==========================================================
    private String normalizarFecha(String fecha) {
        if (fecha.length() >= 16)
            return fecha.substring(0, 16);
        return fecha;
    }

    // ==========================================================
    // CALCULAR FIN DE RESERVA
    // ==========================================================
    public String calcularFin(String inicio, int duracionMin) {

        inicio = normalizarFecha(inicio);

        LocalDateTime ini = LocalDateTime.parse(inicio, FMT);
        return ini.plusMinutes(duracionMin)
                  .toLocalTime()
                  .withSecond(0)
                  .withNano(0)
                  .toString();
    }

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

            int idSocio = rs.getInt("id_socio");
            String fechaBD = rs.getString("fecha_hora_inicio");
            double costo = rs.getDouble("costo");

            // Normalizar: quitar segundos si los hay
            if (fechaBD.length() > 16)
                fechaBD = fechaBD.substring(0, 16);

            LocalDateTime inicio = LocalDateTime.parse(fechaBD, FMT);
            LocalDateTime ahora  = LocalDateTime.now().withSecond(0).withNano(0);

            // ✅ Primero descartamos pasado y presente
            if (!inicio.isAfter(ahora)) {
                throw new ApplicationException(
                    "No se puede cancelar una reserva que ya ha comenzado o pertenece a una fecha/hora pasada."
                );
            }

            // Cancelar
            PreparedStatement ps2 = conn.prepareStatement(
                    "UPDATE Reservas SET estado='cancelada', motivo_cancelacion=? WHERE id_reserva=?"
            );
            ps2.setString(1, motivo);
            ps2.setInt(2, idReserva);
            ps2.executeUpdate();

            // PDF
            new PDFCancelacion().generar(idReserva, idSocio, motivo, costo);

        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }
}