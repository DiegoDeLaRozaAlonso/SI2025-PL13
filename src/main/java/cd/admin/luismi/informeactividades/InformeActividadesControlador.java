package cd.admin.luismi.informeactividades;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class InformeActividadesControlador {

    private InformeActividadesVista vista;
    private InformeActividadesModelo modelo;

    public InformeActividadesControlador(InformeActividadesVista vista, InformeActividadesModelo modelo) {
        this.vista = vista;
        this.modelo = modelo;

        this.vista.getBtnGenerarInforme().addActionListener(e -> generarInforme());
        this.vista.getBtnExportar().addActionListener(e -> exportarInforme());
    }

    private void generarInforme() {
        try {
            String filtro = vista.getTxtFiltroActividad().getText();
            List<ActividadReporteDTO> reporte = modelo.obtenerInformeActividades(filtro);
            
            DefaultTableModel tableModel = vista.getTableModel();
            tableModel.setRowCount(0); // Limpiar tabla

            for (ActividadReporteDTO r : reporte) {
                tableModel.addRow(new Object[]{
                        r.getNombre(),
                        r.getInstalacion(),
                        r.getFechaInicio(),
                        r.getFechaFin(),
                        r.getNumeroPlazas(),
                        r.getNumeroReservas(),
                        String.format("%.2f %%", r.getPorcentajeOcupacion()),
                        r.getListaEspera(),
                        r.getEdiciones()
                });
            }

            if (reporte.isEmpty()) {
                JOptionPane.showMessageDialog(vista.getFrame(), "No se encontraron actividades con ese filtro.");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista.getFrame(), "Error al generar informe: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportarInforme() {
        if (vista.getTable().getRowCount() == 0) {
            JOptionPane.showMessageDialog(vista.getFrame(), "La tabla está vacía. Genere el informe primero.");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exportar Informe");
        fileChooser.setSelectedFile(new File("informe_actividades.csv"));

        int userSelection = fileChooser.showSaveDialog(vista.getFrame());

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileToSave))) {
                
                // Cabeceras
                DefaultTableModel tableModel = vista.getTableModel();
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    bw.write(tableModel.getColumnName(i));
                    if (i < tableModel.getColumnCount() - 1) {
                        bw.write(",");
                    }
                }
                bw.newLine();

                // Datos
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    for (int j = 0; j < tableModel.getColumnCount(); j++) {
                        Object val = tableModel.getValueAt(i, j);
                        bw.write(val != null ? val.toString() : "");
                        if (j < tableModel.getColumnCount() - 1) {
                            bw.write(",");
                        }
                    }
                    bw.newLine();
                }

                JOptionPane.showMessageDialog(vista.getFrame(), "Informe exportado exitosamente.");

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(vista.getFrame(), "Error al exportar archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
