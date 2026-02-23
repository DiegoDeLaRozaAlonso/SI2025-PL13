package cd.socio.diego.verdispoinstalacion;

import javax.swing.JFrame;
import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;

import java.awt.Dimension;

import com.toedter.calendar.JDateChooser;

public class DisponibilidadView {

	private JFrame frame;
	private JComboBox<InstalacionDTO> cmbInstalacion;

	private JDateChooser dateChooser;

	private JTable tabla;
	private JButton btnCerrar;

	public DisponibilidadView() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Disponibilidad Socio");
		frame.setName("DisponibilidadSocio");
		frame.setBounds(0, 0, 820, 520);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		// 3 columnas: etiqueta / campo fecha con ancho / resto
		frame.getContentPane().setLayout(new MigLayout("", "[120!][220!][grow]", "[][][grow][]"));

		JLabel lblInst = new JLabel("Instalación:");
		frame.getContentPane().add(lblInst, "cell 0 0,alignx left");

		cmbInstalacion = new JComboBox<>();
		cmbInstalacion.setName("cmbInstalacion");
		frame.getContentPane().add(cmbInstalacion, "cell 1 0 2 1,growx");

		JLabel lblFecha = new JLabel("Fecha a consultar:");
		frame.getContentPane().add(lblFecha, "cell 0 1,alignx left");

		dateChooser = new JDateChooser();
		dateChooser.setName("dateChooserFecha");
		dateChooser.setDateFormatString("yyyy-MM-dd");
		dateChooser.setPreferredSize(new Dimension(220, 24));
		dateChooser.getCalendarButton().setPreferredSize(new Dimension(28, 24));
		frame.getContentPane().add(dateChooser, "cell 1 1,alignx left");

		tabla = new JTable();
		tabla.setName("tabDisponibilidad");
		tabla.setFillsViewportHeight(true);

		JScrollPane sp = new JScrollPane(tabla);
		sp.setPreferredSize(new Dimension(760, 340));
		frame.getContentPane().add(sp, "cell 0 2 3 1,grow");

		btnCerrar = new JButton("Cerrar");
		btnCerrar.setName("btnCerrar");
		frame.getContentPane().add(btnCerrar, "cell 2 3,alignx right");
	}

	public JFrame getFrame() {
		return frame;
	}

	public JComboBox<InstalacionDTO> getCmbInstalacion() {
		return cmbInstalacion;
	}

	public JDateChooser getDateChooser() {
		return dateChooser;
	}

	public JTable getTabla() {
		return tabla;
	}

	public JButton getBtnCerrar() {
		return btnCerrar;
	}
}