package cd.admin.diego.planact;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import java.awt.Window;

/**
 * Dialog con pestañas para mostrar la info del periodo.
 * No modifica nada: solo lectura.
 */
public class PlanActPeriodoInfoDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	public PlanActPeriodoInfoDialog(Window owner, PeriodoInscripcionDTO p) {
		super(owner, "Info Periodo", ModalityType.APPLICATION_MODAL);
		setLayout(new MigLayout("", "[grow]", "[grow][]"));
		setSize(420, 220);
		setLocationRelativeTo(owner);

		JTabbedPane tabs = new JTabbedPane();

		// Socios
		JPanel socios = new JPanel(new MigLayout("", "[][grow]", "[][]"));
		socios.add(new JLabel("Inicio (socios):"), "cell 0 0,alignx right");
		socios.add(new JLabel(nz(p.getFechaInicioSocio())), "cell 1 0");
		socios.add(new JLabel("Fin (socios):"), "cell 0 1,alignx right");
		socios.add(new JLabel(nz(p.getFechaFinSocio())), "cell 1 1");
		tabs.addTab("Socios", socios);

		// No socios: inicio = fin socios, fin = fin no socio
		JPanel noSocios = new JPanel(new MigLayout("", "[][grow]", "[][]"));
		noSocios.add(new JLabel("Inicio (no socios):"), "cell 0 0,alignx right");
		noSocios.add(new JLabel(nz(p.getFechaFinSocio())), "cell 1 0");
		noSocios.add(new JLabel("Fin (no socios):"), "cell 0 1,alignx right");
		noSocios.add(new JLabel(nz(p.getFechaFinNoSocio())), "cell 1 1");
		tabs.addTab("No socios", noSocios);

		add(tabs, "cell 0 0,grow");

		JButton cerrar = new JButton("Cerrar");
		cerrar.addActionListener(e -> dispose());
		add(cerrar, "cell 0 1,alignx right");
	}

	private String nz(String s) {
		return (s == null || s.isBlank()) ? "-" : s;
	}
}