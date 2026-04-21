package cd.socio.pablo.listaEspera;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.Button;
import java.awt.ScrollPane;
import javax.swing.JScrollPane;

public class ListaEsperaView {

	private JFrame frame;
	private JLabel lNombreActividad;
	private JButton bCerrarListaEspera;
	private JTable tableListaEspera;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ListaEsperaView window = new ListaEsperaView();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ListaEsperaView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 504);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Lista de Espera");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 21));
		lblNewLabel.setBounds(32, 23, 161, 19);
		frame.getContentPane().add(lblNewLabel);
		
		lNombreActividad = new JLabel("");
		lNombreActividad.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lNombreActividad.setBounds(203, 26, 192, 14);
		frame.getContentPane().add(lNombreActividad);
		
		bCerrarListaEspera = new JButton("Cerrar");
		bCerrarListaEspera.setBounds(165, 418, 89, 23);
		frame.getContentPane().add(bCerrarListaEspera);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 54, 414, 353);
		frame.getContentPane().add(scrollPane);
		
		tableListaEspera = new JTable();
		scrollPane.setViewportView(tableListaEspera);
	}
	public JFrame getFrame() {return frame;}
	public JTable getTablaListaEspera() { return tableListaEspera;}
	public JLabel getLabelNombre() {return lNombreActividad;}
	public JButton getBotonCerrar() {return bCerrarListaEspera;}
}
