package cd.PA_pruebas.UO282638;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cd.admin.diego.planact.PlanActCrearActividadModel;
import cd.admin.diego.planact.WeeklyScheduleTableModel;
import giis.demo.util.ApplicationException;

public class PlanActCrearActividadModelTest {

    private PlanActCrearActividadModel model;

    @BeforeEach
    public void setUp() {
        model = new PlanActCrearActividadModel();
    }

    @Test
    public void testNombreObligatorio() {
        List<WeeklyScheduleTableModel.Slot> slots =
                List.of(new WeeklyScheduleTableModel.Slot(0, LocalTime.of(10, 0)));

        ApplicationException ex = assertThrows(ApplicationException.class, () ->
            model.crearActividadCompleta(
                "   ",
                "Yoga",
                1,
                10,
                20.0,
                30.0,
                LocalDate.of(2026, 2, 1),
                LocalDate.of(2026, 2, 28),
                slots,
                1
            )
        );

        assertEquals("El nombre de la actividad es obligatorio", ex.getMessage());
    }

    @Test
    public void testTipoObligatorio() {
        List<WeeklyScheduleTableModel.Slot> slots =
                List.of(new WeeklyScheduleTableModel.Slot(0, LocalTime.of(10, 0)));

        ApplicationException ex = assertThrows(ApplicationException.class, () ->
            model.crearActividadCompleta(
                "Yoga mañanas",
                "   ",
                1,
                10,
                20.0,
                30.0,
                LocalDate.of(2026, 2, 1),
                LocalDate.of(2026, 2, 28),
                slots,
                1
            )
        );

        assertEquals("El tipo (descripción) es obligatorio", ex.getMessage());
    }

    @Test
    public void testInstalacionObligatoria() {
        List<WeeklyScheduleTableModel.Slot> slots =
                List.of(new WeeklyScheduleTableModel.Slot(0, LocalTime.of(10, 0)));

        ApplicationException ex = assertThrows(ApplicationException.class, () ->
            model.crearActividadCompleta(
                "Yoga mañanas",
                "Yoga",
                0,
                10,
                20.0,
                30.0,
                LocalDate.of(2026, 2, 1),
                LocalDate.of(2026, 2, 28),
                slots,
                1
            )
        );

        assertEquals("Debes seleccionar una instalación", ex.getMessage());
    }

    @Test
    public void testDebeHaberAlMenosUnSlot() {
        ApplicationException ex = assertThrows(ApplicationException.class, () ->
            model.crearActividadCompleta(
                "Yoga mañanas",
                "Yoga",
                1,
                10,
                20.0,
                30.0,
                LocalDate.of(2026, 2, 1),
                LocalDate.of(2026, 2, 28),
                List.of(),
                1
            )
        );

        assertEquals("Debes seleccionar al menos un hueco en el horario semanal", ex.getMessage());
    }

    @Test
    public void testPrecioSocioNoPuedeSerNegativo() {
        List<WeeklyScheduleTableModel.Slot> slots =
                List.of(new WeeklyScheduleTableModel.Slot(0, LocalTime.of(10, 0)));

        ApplicationException ex = assertThrows(ApplicationException.class, () ->
            model.crearActividadCompleta(
                "Yoga mañanas",
                "Yoga",
                1,
                10,
                -1.0,
                30.0,
                LocalDate.of(2026, 2, 1),
                LocalDate.of(2026, 2, 28),
                slots,
                1
            )
        );

        assertEquals("Los precios no pueden ser negativos", ex.getMessage());
    }

    @Test
    public void testPrecioNoSocioNoPuedeSerNegativo() {
        List<WeeklyScheduleTableModel.Slot> slots =
                List.of(new WeeklyScheduleTableModel.Slot(0, LocalTime.of(10, 0)));

        ApplicationException ex = assertThrows(ApplicationException.class, () ->
            model.crearActividadCompleta(
                "Yoga mañanas",
                "Yoga",
                1,
                10,
                20.0,
                -5.0,
                LocalDate.of(2026, 2, 1),
                LocalDate.of(2026, 2, 28),
                slots,
                1
            )
        );

        assertEquals("Los precios no pueden ser negativos", ex.getMessage());
    }

    @Test
    public void testPeriodoObligatorio() {
        List<WeeklyScheduleTableModel.Slot> slots =
                List.of(new WeeklyScheduleTableModel.Slot(0, LocalTime.of(10, 0)));

        ApplicationException ex = assertThrows(ApplicationException.class, () ->
            model.crearActividadCompleta(
                "Yoga mañanas",
                "Yoga",
                1,
                10,
                20.0,
                30.0,
                LocalDate.of(2026, 2, 1),
                LocalDate.of(2026, 2, 28),
                slots,
                0
            )
        );

        assertEquals("Debes seleccionar un periodo de inscripción", ex.getMessage());
    }

    @Test
    public void testFechaFinNoPuedeSerAnteriorAFechaInicio() {
        List<WeeklyScheduleTableModel.Slot> slots =
                List.of(new WeeklyScheduleTableModel.Slot(0, LocalTime.of(10, 0)));

        ApplicationException ex = assertThrows(ApplicationException.class, () ->
            model.crearActividadCompleta(
                "Yoga mañanas",
                "Yoga",
                1,
                10,
                20.0,
                30.0,
                LocalDate.of(2026, 2, 28),
                LocalDate.of(2026, 2, 1),
                slots,
                1
            )
        );

        assertEquals("La fecha fin no puede ser anterior a la fecha inicio", ex.getMessage());
    }
}