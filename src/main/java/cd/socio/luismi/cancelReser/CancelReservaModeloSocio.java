package cd.socio.luismi.cancelReser;

import giis.demo.util.ApplicationException;
import giis.demo.util.Database;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CancelReservaModeloSocio {

    private final Database db = new Database();
    private final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // ==========================================================
    // OBTENER RESERVAS ACTIVAS DEL SOCIO LOGUEADO
    // ==========================================================
    public List<Object[]> getReservasSocio(int idSocio) {

        String sql = """
            SELECT r.id_reserva, i.nombre, r.fecha_hora_inicio, r.duracion 
            FROM Reservas r
            JOIN Instalaciones i ON i.id_instalacion = r.id_instalacion
            WHERE r.id_socio=? AND r.estado='activa'
            ORDER BY r.fecha_hora_inicio
            """;

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idSocio);
            ResultSet rs = ps.executeQuery();

            List<Object[]> lista = new ArrayList<>();

            while (rs.next()) {
                lista.add(new Object[]{
                        rs.getInt("id_reserva"),
                        rs.getString("nombre"),
                        rs.getString("fecha_hora_inicio"),
                        rs.getInt("duracion")
                });
            }

            return lista;

        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    // Normalizar fecha SQLite → sin segundos
    private String normalizar(String f) {
        if (f.length() >= 16)
            return f.substring(0, 16);
        return f;
    }

    // ==========================================================
    // CANCELAR RESERVA (VERSIÓN SOCIO)
    // ==========================================================
    public void cancelarReserva(int idReserva, int idSocio, String motivo) {

        try (Connection conn = db.getConnection()) {

            PreparedStatement ps = conn.prepareStatement(
                "SELECT fecha_hora_inicio, costo FROM Reservas WHERE id_reserva=? AND id_socio=? AND estado='activa'"
            );
            ps.setInt(1, idReserva);
            ps.setInt(2, idSocio);

            ResultSet rs = ps.executeQuery();

            if (!rs.next())
                throw new ApplicationException("Reserva no encontrada o ya cancelada.");

            String fechaBD = normalizar(rs.getString("fecha_hora_inicio"));
            double costo = rs.getDouble("costo");

            LocalDateTime inicio = LocalDateTime.parse(fechaBD, FMT);
            LocalDateTime ahora = LocalDateTime.now().withSecond(0).withNano(0);

            // ✅ BLOQUEO ABSOLUTO: si inicio <= ahora → NO cancelar
            if (!inicio.isAfter(ahora)) {
                throw new ApplicationException(
                    "No se puede cancelar una reserva que ya ha comenzado o pertenece a una fecha/hora pasada."
                );
            }

            // ✅ Cancelar: solo estado + motivo
            PreparedStatement ps2 = conn.prepareStatement(
                "UPDATE Reservas SET estado='cancelada', motivo_cancelacion=? WHERE id_reserva=?"
            );
            ps2.setString(1, motivo);
            ps2.setInt(2, idReserva);
            ps2.executeUpdate();

            // ✅ PDF igual que en admin
            new PDFCancelacion().generar(idReserva, idSocio, motivo, costo);

        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }
}