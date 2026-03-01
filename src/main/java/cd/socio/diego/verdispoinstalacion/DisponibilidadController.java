package cd.socio.diego.verdispoinstalacion;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.TableModel;

import giis.demo.util.SwingUtil;

public class DisponibilidadController {

	private final DisponibilidadModel model;
	private final DisponibilidadView view;

	public DisponibilidadController(DisponibilidadModel m, DisponibilidadView v) {
		this.model = m;
		this.view = v;
		initView();
	}

	public void initController() {

		view.getCmbInstalacion().addActionListener(
				e -> SwingUtil.exceptionWrapper(this::refrescarTabla));

		view.getDateChooser().addPropertyChangeListener("date",
				evt -> SwingUtil.exceptionWrapper(this::refrescarTabla));

		view.getBtnCerrar().addActionListener(e -> view.getFrame().dispose());
	}

	private void initView() {
		cargarInstalaciones();

		Date hoy = new Date();
		view.getDateChooser().setDate(hoy);

		LocalDate ldHoy = LocalDate.now();
		LocalDate ldMax = ldHoy.plusDays(30);

		Date min = Date.from(ldHoy.atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date max = Date.from(ldMax.atStartOfDay(ZoneId.systemDefault()).toInstant());

		view.getDateChooser().setMinSelectableDate(min);
		view.getDateChooser().setMaxSelectableDate(max);

		refrescarTabla();
		view.getFrame().setVisible(true);
	}

	private void cargarInstalaciones() {
		List<InstalacionDTO> instalaciones = model.getInstalacionesEnUso();

		DefaultComboBoxModel<InstalacionDTO> cbm = new DefaultComboBoxModel<>();
		for (InstalacionDTO i : instalaciones)
			cbm.addElement(i);

		view.getCmbInstalacion().setModel((ComboBoxModel<InstalacionDTO>) cbm);

		if (cbm.getSize() > 0)
			view.getCmbInstalacion().setSelectedIndex(0);
	}

	private void refrescarTabla() {
		InstalacionDTO inst = (InstalacionDTO) view.getCmbInstalacion().getSelectedItem();
		if (inst == null) {
			view.getTabla().setModel(model.getEmptyTableModel());
			return;
		}

		Date d = view.getDateChooser().getDate();
		if (d == null) {
			view.getTabla().setModel(model.getEmptyTableModel());
			return;
		}

		LocalDate fecha = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		TableModel tm = model.getDisponibilidadTableModel(inst.getIdInstalacion(), fecha);
		view.getTabla().setModel(tm);

		// ✅ NO llamar a autoAdjustColumns aquí (te rompe el ancho "hasta la derecha")
		// SwingUtil.autoAdjustColumns(view.getTabla());
	}
}