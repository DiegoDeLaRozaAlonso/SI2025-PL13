package cd.admin.pablo.listaPeriodo;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTable;

public class ListaPeriodoView {

	private JFrame frame;
	private JTable tablaActividades;
	private JButton bVolver;
	private JComboBox comboBox;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ListaPeriodoView window = new ListaPeriodoView();
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
	public ListaPeriodoView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 807, 559);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null); 
		
		JLabel lblNewLabel = new JLabel("Periodo");
		lblNewLabel.setFont(new Font("Calibri", Font.PLAIN, 22));
		lblNewLabel.setBounds(60, 40, 88, 30);
		frame.getContentPane().add(lblNewLabel);
		
		comboBox = new JComboBox();
		comboBox.setBounds(158, 40, 184, 30);
		frame.getContentPane().add(comboBox);
		
		bVolver = new JButton("Volver");
		bVolver.setFont(new Font("Tahoma", Font.PLAIN, 15));
		bVolver.setBounds(292, 472, 140, 37);
		frame.getContentPane().add(bVolver);
		
		tablaActividades = new JTable();
		tablaActividades.setBounds(10, 97, 773, 364);
		frame.getContentPane().add(tablaActividades);
	}
	
	public JFrame getFrame() {return this.frame;}
	public JTable getTable() {return this.tablaActividades;}
	public JComboBox getComboBox() {return this.comboBox;}
	public JButton getBotonVolver() {return this.bVolver ;}
	
}
