package cd.PA_pruebas.UO282638;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import cd.admin.diego.planact.WeeklyScheduleTableModel;

public class WeeklyScheduleTableModelTest {

    @Test
    public void testGetSelectedSlotsSinSeleccionDevuelveListaVacia() {
        WeeklyScheduleTableModel model =
                new WeeklyScheduleTableModel(LocalTime.of(8, 0), LocalTime.of(11, 0));

        List<WeeklyScheduleTableModel.Slot> slots = model.getSelectedSlots();

        assertTrue(slots.isEmpty());
    }

    @Test
    public void testGetSelectedSlotsConUnaSeleccionDevuelveUnSlot() {
        WeeklyScheduleTableModel model =
                new WeeklyScheduleTableModel(LocalTime.of(8, 0), LocalTime.of(11, 0));

        // fila 0 = 08:00, columna 1 = Lunes
        model.setValueAt(true, 0, 1);

        List<WeeklyScheduleTableModel.Slot> slots = model.getSelectedSlots();

        assertEquals(1, slots.size());
        assertEquals(0, slots.get(0).dayIndex0Mon);
        assertEquals(LocalTime.of(8, 0), slots.get(0).start);
    }

    @Test
    public void testGetSelectedSlotsConVariasSeleccionesDevuelveTodosLosSlots() {
        WeeklyScheduleTableModel model =
                new WeeklyScheduleTableModel(LocalTime.of(8, 0), LocalTime.of(11, 0));

        // 08:00 lunes
        model.setValueAt(true, 0, 1);
        // 09:00 miércoles
        model.setValueAt(true, 1, 3);
        // 10:00 viernes
        model.setValueAt(true, 2, 5);

        List<WeeklyScheduleTableModel.Slot> slots = model.getSelectedSlots();

        assertEquals(3, slots.size());

        assertEquals(0, slots.get(0).dayIndex0Mon);
        assertEquals(LocalTime.of(8, 0), slots.get(0).start);

        assertEquals(2, slots.get(1).dayIndex0Mon);
        assertEquals(LocalTime.of(9, 0), slots.get(1).start);

        assertEquals(4, slots.get(2).dayIndex0Mon);
        assertEquals(LocalTime.of(10, 0), slots.get(2).start);
    }

    @Test
    public void testClearAllEliminaTodasLasSelecciones() {
        WeeklyScheduleTableModel model =
                new WeeklyScheduleTableModel(LocalTime.of(8, 0), LocalTime.of(11, 0));

        model.setValueAt(true, 0, 1);
        model.setValueAt(true, 1, 3);

        model.clearAll();

        List<WeeklyScheduleTableModel.Slot> slots = model.getSelectedSlots();

        assertTrue(slots.isEmpty());
    }

    @Test
    public void testEndPlus1hCalculaCorrectamenteHoraFin() {
        WeeklyScheduleTableModel.Slot slot =
                new WeeklyScheduleTableModel.Slot(0, LocalTime.of(10, 0));

        assertEquals(LocalTime.of(11, 0), slot.endPlus1h());
    }
}