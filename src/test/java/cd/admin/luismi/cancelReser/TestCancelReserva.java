package cd.admin.luismi.cancelReser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.Before;
import org.junit.Test;

import giis.demo.util.ApplicationException;
import giis.demo.util.Database;

public class TestCancelReserva {
    
    private Database db;
    private CancelReservaModelo modelo;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Before
    public void setUp() {
        db = new Database();
        db.createDatabase(false);
        loadCleanDatabase();
        modelo = new CancelReservaModelo();
    }

    private void loadCleanDatabase() {
        // Necesitamos Socios, Instalaciones, Reservas
        db.executeBatch(new String[] {
            "DELETE FROM Reservas",
            "DELETE FROM Instalaciones",
            "DELETE FROM Socios",
            "INSERT INTO Socios(id_socio, nombre, email, contrasena, fecha_registro) VALUES (1, 'Socio 1', 's1@a.com', '123', '2026-01-01')",
            "INSERT INTO Instalaciones(id_instalacion, nombre, tipo, precioInstalacion) VALUES (1, 'Pista 1', 'tenis', 10.0)"
        });
    }

    private String fechaFutura() {
        return LocalDateTime.now().plusDays(2).withMinute(0).withSecond(0).withNano(0).format(FMT);
    }
    
    private String fechaPasada() {
        return LocalDateTime.now().minusDays(2).withMinute(0).withSecond(0).withNano(0).format(FMT);
    }

    @Test
    public void testCancelarReservaValida() throws Exception {
        // CE1: Reserva existe, está activa y fecha_inicio es futura
        db.executeBatch(new String[] {
            "INSERT INTO Reservas(id_reserva, id_socio, id_instalacion, fecha_hora_inicio, duracion, costo, estado) " +
            "VALUES (100, 1, 1, '" + fechaFutura() + "', 60, 10.0, 'activa')"
        });
        
        modelo.cancelarReserva(100, "Motivo test");
        
        // Verificar estado 'cancelada'
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT estado, motivo_cancelacion FROM Reservas WHERE id_reserva=100")) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                assertEquals("cancelada", rs.getString("estado"));
                assertEquals("Motivo test", rs.getString("motivo_cancelacion"));
            }
        }
    }

    @Test
    public void testCancelarReservaInexistenteOCancelada() {
        // CE2: Reserva no existe
        ApplicationException e1 = assertThrows(ApplicationException.class, () -> modelo.cancelarReserva(999, "No existe"));
        assertEquals("Reserva no encontrada o ya cancelada.", e1.getMessage());

        // CE2: Reserva existe pero ya está cancelada
        db.executeBatch(new String[] {
            "INSERT INTO Reservas(id_reserva, id_socio, id_instalacion, fecha_hora_inicio, duracion, costo, estado) " +
            "VALUES (101, 1, 1, '" + fechaFutura() + "', 60, 10.0, 'cancelada')"
        });
        ApplicationException e2 = assertThrows(ApplicationException.class, () -> modelo.cancelarReserva(101, "Ya cancelada"));
        assertEquals("Reserva no encontrada o ya cancelada.", e2.getMessage());
    }

    @Test
    public void testCancelarReservaPasadaOComenzada() {
        // CE3: Reserva existe y activa pero en el pasado
        db.executeBatch(new String[] {
            "INSERT INTO Reservas(id_reserva, id_socio, id_instalacion, fecha_hora_inicio, duracion, costo, estado) " +
            "VALUES (102, 1, 1, '" + fechaPasada() + "', 60, 10.0, 'activa')"
        });

        ApplicationException e = assertThrows(ApplicationException.class, () -> modelo.cancelarReserva(102, "Pasada"));
        assertEquals("No se puede cancelar una reserva que ya ha comenzado o pertenece a una fecha/hora pasada.", e.getMessage());
    }
}
