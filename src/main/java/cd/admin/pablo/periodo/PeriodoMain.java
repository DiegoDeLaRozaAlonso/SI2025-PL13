package cd.admin.pablo.periodo;

public class PeriodoMain {
	
	public static void main(String[] args) {
		PeriodoModel modelo = new PeriodoModel();
		
		PeriodoView vista = new PeriodoView();
		
		new PeriodoController(modelo, vista);
	}
}
