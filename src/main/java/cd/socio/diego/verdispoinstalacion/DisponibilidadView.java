package cd.socio.diego.verdispoinstalacion;

import javax.swing.JFrame;
import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;

import java.awt.Dimension;

import javax.swing.table.TableColumn;

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

		// Importante: fill + que la fila de la tabla haga push
		frame.getContentPane().setLayout(new MigLayout("fill", "[120!][220!][grow]", "[][][grow, push][]"));

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

		// ===== TABLA =====
		tabla = new JTable();
		tabla.setName("tabDisponibilidad");
		tabla.setFillsViewportHeight(true);

		// ✅ Esto hace que la tabla “rellene” el viewport ajustando columnas
		tabla.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

		JScrollPane sp = new JScrollPane(tabla);
		frame.getContentPane().add(sp, "cell 0 2 3 1,grow,push");

		btnCerrar = new JButton("Cerrar");
		btnCerrar.setName("btnCerrar");
		frame.getContentPane().add(btnCerrar, "cell 2 3,alignx right");

		// Ajuste inicial de columnas (cuando aún no hay modelo, no hace nada)
		configurarColumnasFijas();
	}

	/**
	 * Fija anchos razonables para Hora/Estado, y deja Detalle elástica.
	 * OJO: esto se debe llamar también tras setModel (cuando ya hay columnas).
	 */
	public void configurarColumnasFijas() {
		if (tabla.getColumnModel().getColumnCount() < 3) return;

		TableColumn c0 = tabla.getColumnModel().getColumn(0); // Hora
		TableColumn c1 = tabla.getColumnModel().getColumn(1); // Estado
		TableColumn c2 = tabla.getColumnModel().getColumn(2); // Detalle

		c0.setMinWidth(70);
		c0.setPreferredWidth(80);
		c0.setMaxWidth(100);

		c1.setMinWidth(90);
		c1.setPreferredWidth(110);
		c1.setMaxWidth(140);

		// Detalle: que se coma todo el resto
		c2.setMinWidth(200);
		c2.setPreferredWidth(600);
	}

	public JFrame getFrame() { return frame; }
	public JComboBox<InstalacionDTO> getCmbInstalacion() { return cmbInstalacion; }
	public JDateChooser getDateChooser() { return dateChooser; }
	public JTable getTabla() { return tabla; }
	public JButton getBtnCerrar() { return btnCerrar; }
}