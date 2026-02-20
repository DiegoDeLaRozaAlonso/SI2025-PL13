package cd.admin.diego.planact;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * TableModel para seleccionar horario semanal con checkboxes por hora.
 *  - Col 0: Hora (String)
 *  - Col 1..5: L..V (Boolean)
 */
public class WeeklyScheduleTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;

	private static final String[] COLS = { "Hora", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes" };
	private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm");

	private final List<LocalTime> hours = new ArrayList<>();
	private final boolean[][] selected; // [row][dayIndex 0..4]

	public WeeklyScheduleTableModel(LocalTime start, LocalTime endExclusive) {
		LocalTime t = start;
		while (t.isBefore(endExclusive)) {
			hours.add(t);
			t = t.plusHours(1);
		}
		selected = new boolean[hours.size()][5];
	}

	public WeeklyScheduleTableModel() {
		// por defecto 08:00..22:00 (endExclusive)
		this(LocalTime.of(8, 0), LocalTime.of(22, 0));
	}

	@Override public int getRowCount() { return hours.size(); }
	@Override public int getColumnCount() { return COLS.length; }
	@Override public String getColumnName(int column) { return COLS[column]; }

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == 0) return String.class;
		return Boolean.class;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex != 0;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0) return hours.get(rowIndex).format(FMT);
		return selected[rowIndex][columnIndex - 1];
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (columnIndex == 0) return;
		selected[rowIndex][columnIndex - 1] = (Boolean) aValue;
		fireTableCellUpdated(rowIndex, columnIndex);
	}

	public void clearAll() {
		for (int r = 0; r < selected.length; r++)
			for (int d = 0; d < 5; d++)
				selected[r][d] = false;
		fireTableDataChanged();
	}

	/**
	 * Devuelve una lista de slots seleccionados: (dayIndex 0..4, hh:mm)
	 */
	public List<Slot> getSelectedSlots() {
		List<Slot> out = new ArrayList<>();
		for (int r = 0; r < hours.size(); r++) {
			for (int d = 0; d < 5; d++) {
				if (selected[r][d]) out.add(new Slot(d, hours.get(r)));
			}
		}
		return out;
	}

	public static class Slot {
		public final int dayIndex0Mon; // 0..4
		public final LocalTime start;

		public Slot(int dayIndex0Mon, LocalTime start) {
			this.dayIndex0Mon = dayIndex0Mon;
			this.start = start;
		}

		public LocalTime endPlus1h() {
			return start.plusHours(1);
		}
	}
}