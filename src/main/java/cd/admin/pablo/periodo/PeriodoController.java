package cd.admin.pablo.periodo;

import giis.demo.util.SwingUtil;

public class PeriodoController {
	
	private PeriodoModel modelo;
	private PeriodoView vista;
	
	public PeriodoController(PeriodoModel modelo, PeriodoView vista) {
		this.modelo = modelo;
		this.vista = vista;
		this.iniciarVista();
	}
	
	private void iniciarVista() {
		vista.getFrame().setVisible(true);
	}
	
	private void iniciarControlador() {
		vista.getBotonCrear().addActionListener(e -> SwingUtil.exceptionWrapper(() -> crearPeriodo()));
		restablecer();
	}
	
	private void restablecer() {
		vista.getNombre().setText("");
		vista.getDescripcion().setText("");
	}
	
	private crearPeriodo() {
		
	}
	
	
}
