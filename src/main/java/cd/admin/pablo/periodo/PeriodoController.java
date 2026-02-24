package cd.admin.pablo.periodo;

import giis.demo.util.SwingUtil;

public class PeriodoController {
	
	private PeriodoModel modelo;
	private PeriodoView vista;
	private PeriodoDTO periodo;
	
	public PeriodoController(PeriodoModel modelo, PeriodoView vista) {
		this.modelo = modelo;
		this.vista = vista;
		this.iniciarVista();
	}
	
	private void iniciarVista() {
		vista.getFrame().setVisible(true);
	}
	
	public void iniciarControlador() {
		vista.getBotonCrear().addActionListener(e -> SwingUtil.exceptionWrapper(() -> crearPeriodo()));
		vista.getBotonCancelar().addActionListener(e -> SwingUtil.exceptionWrapper(() -> cerrar()));
	}
	
	private void crearPeriodo() {
		periodo = new PeriodoDTO(
				vista.getNombre().getText(), vista.getDescripcion().getText(), 
				vista.getFechaInicio(), vista.getFechaFinSocio(), vista.getFechaFinNoSocio());
		
		modelo.insertarPeriodo(periodo);
		
		javax.swing.JOptionPane.showMessageDialog(
				vista.getFrame(), "Periodo " + vista.getNombre().getText() + " creado con exito");
		
		vista.reinicio();
	}
	
	private void cerrar() {
		this.vista.getFrame().dispose();
	}
	
	
}
