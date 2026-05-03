package cd.socio.luismi.reservainstalacion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.Before;
import org.junit.Test;

import giis.demo.util.ApplicationException;
import giis.demo.util.Database;

public class TestReservaInstalacion {

    private Database db;
    private ReservaClienteModelo modelo;

    @Before
    public void setUp() {
        db = new Database();
        db.createDatabase(false);
        loadCleanDatabase();
        modelo = new ReservaClienteModelo();
    }

    private void loadCleanDatabase() {
        db.executeBatch(new String[] {
            "DELETE FROM Reservas",
            "DELETE FROM SesionesActividad",
            "DELETE FROM PlanificacionActividades",
            "DELETE FROM Instalaciones",
            "DELETE FROM Socios",
            "INSERT INTO Socios(id_socio, nombre, email, contrasena, fecha_registro) VALUES (1, 'Socio 1', 's1@a.com', '123', '2026-01-01')",
            "INSERT INTO Instalaciones(id_instalacion, nombre, tipo, precioInstalacion) VALUES (1, 'Pista 1', 'tenis', 10.0)"
        });
    }

    @Test
    public void testReservaValida_CE1() {
        LocalDate fecha = LocalDate.now().plusDays(2);
        LocalTime hora = LocalTime.of(10, 0);
        
        boolean res = modelo.guardarReserva("Socio 1", "Pista 1", fecha, hora, 2);
        assertTrue(res);
        
        // Verificar en DB
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM Reservas WHERE id_socio=1")) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                assertEquals(1, rs.getInt(1));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testHorasInvalidas_CE2() {
        LocalDate fecha = LocalDate.now().plusDays(2);
        LocalTime hora = LocalTime.of(10, 0);

        ApplicationException e1 = assertThrows(ApplicationException.class, 
            () -> modelo.guardarReserva("Socio 1", "Pista 1", fecha, hora, 0));
        assertEquals("Solo se pueden reservar 1, 2 o 3 horas.", e1.getMessage());

        ApplicationException e2 = assertThrows(ApplicationException.class, 
            () -> modelo.guardarReserva("Socio 1", "Pista 1", fecha, hora, 4));
        assertEquals("Solo se pueden reservar 1, 2 o 3 horas.", e2.getMessage());
    }

    @Test
    public void testHoraNoEnPunto_CE3() {
        LocalDate fecha = LocalDate.now().plusDays(2);
        LocalTime hora = LocalTime.of(10, 30);

        ApplicationException e = assertThrows(ApplicationException.class, 
            () -> modelo.guardarReserva("Socio 1", "Pista 1", fecha, hora, 1));
        assertEquals("La hora debe ser en punto (ej. 17:00)", e.getMessage());
    }

    @Test
    public void testFueraHorarioComercial_CE4() {
        LocalDate fecha = LocalDate.now().plusDays(2);

        // Antes de apertura
        LocalTime hora1 = LocalTime.of(7, 0);
        ApplicationException e1 = assertThrows(ApplicationException.class, 
            () -> modelo.guardarReserva("Socio 1", "Pista 1", fecha, hora1, 1));
        assertEquals("Las reservas solo pueden comenzar entre 08:00 y 20:00.", e1.getMessage());

        // Después de cierre
        LocalTime hora2 = LocalTime.of(21, 0);
        ApplicationException e2 = assertThrows(ApplicationException.class, 
            () -> modelo.guardarReserva("Socio 1", "Pista 1", fecha, hora2, 1));
        assertEquals("Las reservas solo pueden comenzar entre 08:00 y 20:00.", e2.getMessage());

        // Termina después de cierre
        LocalTime hora3 = LocalTime.of(19, 0);
        ApplicationException e3 = assertThrows(ApplicationException.class, 
            () -> modelo.guardarReserva("Socio 1", "Pista 1", fecha, hora3, 3));
        assertEquals("La reserva no puede acabar después de las 20:00.", e3.getMessage());
    }

    @Test
    public void testAntelacionExcedida_CE5() {
        LocalDate fecha = LocalDate.now().plusDays(16);
        LocalTime hora = LocalTime.of(10, 0);

        ApplicationException e = assertThrows(ApplicationException.class, 
            () -> modelo.guardarReserva("Socio 1", "Pista 1", fecha, hora, 1));
        assertTrue(e.getMessage().startsWith("Solo se puede reservar dentro de los próximos 15 días"));
    }

    @Test
    public void testReservaEnPasado_CE6() {
        LocalDate fecha = LocalDate.now().minusDays(1);
        LocalTime hora = LocalTime.of(10, 0);

        ApplicationException e = assertThrows(ApplicationException.class, 
            () -> modelo.guardarReserva("Socio 1", "Pista 1", fecha, hora, 1));
        assertEquals("No se puede reservar en el pasado.", e.getMessage());
    }

    @Test
    public void testSocioNoExiste_CE7() {
        LocalDate fecha = LocalDate.now().plusDays(2);
        LocalTime hora = LocalTime.of(10, 0);

        ApplicationException e = assertThrows(ApplicationException.class, 
            () -> modelo.guardarReserva("Socio 99", "Pista 1", fecha, hora, 1));
        assertEquals("No existe un socio con ese nombre/email.", e.getMessage());
    }

    @Test
    public void testInstalacionNoExiste_CE8() {
        LocalDate fecha = LocalDate.now().plusDays(2);
        LocalTime hora = LocalTime.of(10, 0);

        ApplicationException e = assertThrows(ApplicationException.class, 
            () -> modelo.guardarReserva("Socio 1", "Pista 99", fecha, hora, 1));
        assertEquals("Instalación no encontrada.", e.getMessage());
    }

    @Test
    public void testTope4HorasDia_CE9() {
        LocalDate fecha = LocalDate.now().plusDays(2);
        
        // Reservar 3 horas a las 10:00
        modelo.guardarReserva("Socio 1", "Pista 1", fecha, LocalTime.of(10, 0), 3);

        // Intentar reservar 2 horas más el mismo día a las 15:00
        ApplicationException e = assertThrows(ApplicationException.class, 
            () -> modelo.guardarReserva("Socio 1", "Pista 1", fecha, LocalTime.of(15, 0), 2));
        assertEquals("No puedes reservar más de 4 horas en el mismo día.", e.getMessage());
    }

    @Test
    public void testTope3HorasSeguidas_CE10() {
        LocalDate fecha = LocalDate.now().plusDays(2);
        
        // Reservar 2 horas a las 10:00
        modelo.guardarReserva("Socio 1", "Pista 1", fecha, LocalTime.of(10, 0), 2);

        // Intentar reservar 2 horas contiguas a las 12:00
        ApplicationException e = assertThrows(ApplicationException.class, 
            () -> modelo.guardarReserva("Socio 1", "Pista 1", fecha, LocalTime.of(12, 0), 2));
        assertEquals("No se pueden reservar más de 3 horas seguidas.", e.getMessage());
    }

    @Test
    public void testTope6HorasTotal_CE11() {
        LocalDate fecha1 = LocalDate.now().plusDays(1);
        LocalDate fecha2 = LocalDate.now().plusDays(2);
        
        // Reservar 3h el día 1
        modelo.guardarReserva("Socio 1", "Pista 1", fecha1, LocalTime.of(10, 0), 3);
        // Reservar 2h el día 2
        modelo.guardarReserva("Socio 1", "Pista 1", fecha2, LocalTime.of(10, 0), 2);

        // Intentar reservar 2h (3+2+2 = 7) excedería 6h totales activas
        LocalDate fecha3 = LocalDate.now().plusDays(3);
        ApplicationException e = assertThrows(ApplicationException.class, 
            () -> modelo.guardarReserva("Socio 1", "Pista 1", fecha3, LocalTime.of(10, 0), 2));
        assertEquals("No puedes tener más de 6 horas activas en total.", e.getMessage());
    }

    @Test
    public void testConflictoHorario_CE12() {
        LocalDate fecha = LocalDate.now().plusDays(2);
        
        // Reservar 2 horas a las 10 12
        modelo.guardarReserva("Socio 1", "Pista 1", fecha, LocalTime.of(10, 0), 2);

        // Socio 2 intenta reservar a las 11
        db.executeBatch(new String[] {
            "INSERT INTO Socios(id_socio, nombre, email, contrasena, fecha_registro) VALUES (2, 'Socio 2', 's2@a.com', '123', '2026-01-01')"
        });

        ApplicationException e = assertThrows(ApplicationException.class, 
            () -> modelo.guardarReserva("Socio 2", "Pista 1", fecha, LocalTime.of(11, 0), 2));
        assertEquals("Conflicto de horario con otra reserva o actividad.", e.getMessage());
    }
}
