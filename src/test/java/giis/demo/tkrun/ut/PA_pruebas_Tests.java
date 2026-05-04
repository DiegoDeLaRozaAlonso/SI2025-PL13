package giis.demo.tkrun.ut;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cd.admin.Alejandro.Reserva.ReservarActividadModel;
import cd.admin.Alejandro.Reserva.ConflictoDTO;
import cd.admin.Alejandro.InformeOcupacion.OcupacionFilaDTO;
import giis.demo.util.ApplicationException;
import giis.demo.util.Database;

import java.util.List;

/**
 * Pruebas unitarias de los procesos de negocio de Alejandro Fernández Peña.
 *
 * Proceso 1 - Reserva de instalación para una actividad (HU 33733)
 *   Métodos: horaAMinutos, calcularDuracionHoras, detectarConflictos, crearSesionActividad
 *
 * Proceso 2 - Cálculo de ocupación de instalaciones (HU 34391)
 *   Métodos: getPoWrcentajeActividad, getPorcentajeSocio, getPlazasLibres
 */
public class PA_pruebas_Tests {

    private ReservarActividadModel reservaModel;

    @BeforeEach
    public void setUp() {
        Database db = new Database();
        db.createDatabase(true);
        reservaModel = new ReservarActividadModel();

        // Datos necesarios para los tests de conflictos (CE5, CE6, CE7, CE8g)
        db.executeUpdate("INSERT OR IGNORE INTO Actividades "
                + "(id_actividad, nombre, descripcion, id_instalacion, aforo, "
                + "costo_socio, costo_no_socio, fecha_inicio, fecha_fin, id_periodo) "
                + "VALUES (101, 'Prueba1resact', 'Actividad prueba', "
                + "7, 18, 12.00, 20.00, '2026-03-10', '2026-03-20', 4)");
        db.executeUpdate("INSERT OR IGNORE INTO Actividades "
                + "(id_actividad, nombre, descripcion, id_instalacion, aforo, "
                + "costo_socio, costo_no_socio, fecha_inicio, fecha_fin, id_periodo) "
                + "VALUES (102, 'Prueba2resact', 'Actividad prueba 2', "
                + "7, 18, 12.00, 20.00, '2026-03-01', '2026-03-15', 4)");
        db.executeUpdate("INSERT OR IGNORE INTO SesionesActividad "
                + "(id_sesion, id_actividad, fecha, hora_inicio, hora_fin, id_instalacion) "
                + "VALUES (101, 101, '2026-03-12', '18:00', '19:00', 7)");
        db.executeUpdate("DELETE FROM SesionesActividad WHERE id_sesion IN (102, 103)");
        db.executeUpdate("INSERT INTO SesionesActividad "
                + "(id_sesion, id_actividad, fecha, hora_inicio, hora_fin, id_instalacion) "
                + "VALUES (102, 101, '2026-06-01', '16:00', '18:00', 7)");
        db.executeUpdate("INSERT INTO SesionesActividad "
                + "(id_sesion, id_actividad, fecha, hora_inicio, hora_fin, id_instalacion) "
                + "VALUES (103, 101, '2026-06-01', '18:00', '20:00', 7)");
    }

    //
    // PROCESO 1 - RESERVA DE INSTALACIÓN PARA ACTIVIDAD (Historia Usuario 33733)
    //

    // ── horaAMinutos ──────────────────────────────────────────────────────────

    /** CE1: */
    @Test
    public void testHoraAMinutos_HoraEnPunto() {
        assertEquals(480, ReservarActividadModel.horaAMinutos("08:00"));
    }

    /** CE2: */
    @Test
    public void testHoraAMinutos_Medianoche() {
        assertEquals(0, ReservarActividadModel.horaAMinutos("00:00"));
    }

    /** CE3*/
    @Test
    public void testCalcularDuracion_FinPosterior() {
        assertEquals(2, ReservarActividadModel.calcularDuracionHoras("08:00", "10:00"));
    }

    /** CE4:  */
    @Test
    public void testCalcularDuracion_FinIgualOAnterior() {
        assertEquals(0, ReservarActividadModel.calcularDuracionHoras("10:00", "10:00"));
        assertEquals(0, ReservarActividadModel.calcularDuracionHoras("12:00", "10:00"));
    }


    /** CE5:  */
    @Test
    public void testDetectarConflictos_SinConflictos() {
        List<ConflictoDTO> conflictos =
                reservaModel.detectarConflictos(7, "2026-03-12", "20:00", "21:00");
        assertTrue(conflictos.isEmpty(), "Sin conflictos: debe devolver lista vacía");
    }

    /** CE6: */
    @Test
    public void testDetectarConflictos_SoloConActividad() {
        List<ConflictoDTO> conflictos =
                reservaModel.detectarConflictos(7, "2026-03-12", "18:00", "19:00");
        assertEquals(1, conflictos.size(), "Debe haber 1 conflicto con actividad");
        assertEquals("actividad", conflictos.get(0).getTipo());
    }

    /** CE7: */
    @Test
    public void testDetectarConflictos_MultiplesConflictos() {
        List<ConflictoDTO> conflictos =
                reservaModel.detectarConflictos(7, "2026-06-01", "17:00", "19:00");
        long numActividad = conflictos.stream().filter(c -> "actividad".equals(c.getTipo())).count();
        assertTrue(numActividad >= 2, "Deben detectarse al menos 2 conflictos");
    }

    /** CE8: */
    @Test
    public void testDetectarConflictos_SlotAdyacente_SinSolapamiento() {
        List<ConflictoDTO> conflictos =
                reservaModel.detectarConflictos(7, "2026-03-12", "19:00", "21:00");
        assertTrue(conflictos.isEmpty(), "Slot adyacente no debe generar conflicto");
    }

    // ── crearSesionActividad ──────────────────────────────────────────────────

    /** CE9:  */
    @Test
    public void testCrearSesion_SlotLibre_CreaCorrectamente() {
        assertDoesNotThrow(() ->
                reservaModel.crearSesionActividad(1, 1, "2026-03-20", "10:00", "11:00"));
        Database db2 = new Database();
        List<?> resultado = db2.executeQueryPojo(
                cd.admin.Alejandro.Reserva.ActividadEntity.class,
                "SELECT id_actividad AS id FROM SesionesActividad"
                + " WHERE id_actividad=1 AND fecha='2026-03-20'"
                + " AND hora_inicio='10:00' AND hora_fin='11:00'");
        assertFalse(resultado.isEmpty(), "La sesion debe haberse insertado en la BD");
    }

    /** CE10: */
    @Test
    public void testCrearSesion_ConConflicto_LanzaExcepcion() {
        assertThrows(ApplicationException.class, () ->
                reservaModel.crearSesionActividad(101, 7, "2026-03-12", "18:00", "19:00"));
    }

    // PROCESO 2 - CÁLCULO DE OCUPACIÓN DE INSTALACIONES (HU 34391)

    /** CE11: */
    @Test
    public void testPorcentajeActividad_OcupacionParcial() {
        assertEquals(50, crearDto("10", "20", "0", "30").getPorcentajeActividad());
    }

    /** CE12:  */
    @Test
    public void testPorcentajeActividad_SuperaAforo_Limitado100() {
        assertEquals(100, crearDto("25", "20", "0", "30").getPorcentajeActividad());
    }

    /** CE13: */
    @Test
    public void testPorcentajeActividad_AforoCero() {
        assertEquals(0, crearDto("5", "0", "0", "30").getPorcentajeActividad());
    }

    /** CE14:  */
    @Test
    public void testPorcentajeSocio_OcupacionParcial() {
        assertEquals(10, crearDto("0", "20", "3", "30").getPorcentajeSocio());
    }

    /** CE15:  */
    @Test
    public void testPorcentajeSocio_SuperaCapacidad_Limitado100() {
        assertEquals(100, crearDto("0", "20", "35", "30").getPorcentajeSocio());
    }

    /** CE16:  */
    @Test
    public void testPorcentajeSocio_CapacidadCero() {
        assertEquals(0, crearDto("0", "20", "3", "0").getPorcentajeSocio());
    }

    /** CE17: */
    @Test
    public void testPlazasLibres_HayPlazas() {
        assertEquals(10, crearDto("10", "20", "0", "30").getPlazasLibres());
    }

    /** CE18: */
    @Test
    public void testPlazasLibres_InscritosSuperiores_NuncaNegativo() {
        assertEquals(0, crearDto("25", "20", "0", "30").getPlazasLibres());
    }

    /** CE19:  */
    @Test
    public void testPlazasLibres_AforoCero() {
        assertEquals(0, crearDto("0", "0", "0", "30").getPlazasLibres());
    }

    // ── Utilidad ──────────────────────────────────────────────────────────────

    private OcupacionFilaDTO crearDto(String inscritos, String aforo,
                                      String reservas, String capacidad) {
        OcupacionFilaDTO dto = new OcupacionFilaDTO();
        dto.setInscritosActividad(inscritos);
        dto.setAforoActividad(aforo);
        dto.setReservasActivas(reservas);
        dto.setCapacidadInstalacion(capacidad);
        return dto;
    }
}