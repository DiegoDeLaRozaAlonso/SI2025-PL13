package cd.socio.luismi.desinscribirseactividad;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class DesinscribirseControlador {
    private DesinscribirseModelo modelo;
    private DesinscribirseVista vista;
    private int idSocioActual;
    private List<InscripcionActivaDTO> inscripcionesActuales;

    public DesinscribirseControlador(DesinscribirseModelo modelo, DesinscribirseVista vista, int idSocio) {
        this.modelo = modelo;
        this.vista = vista;
        this.idSocioActual = idSocio;

        initEventos();
        configurarTabla();
    }

    public void initController() {
        cargarInscripciones();
        vista.setVisible(true);
    }

    private void initEventos() {
        vista.getBtnActualizar().addActionListener(e -> cargarInscripciones());
    }

    private void cargarInscripciones() {
        inscripcionesActuales = modelo.getInscripcionesSocio(idSocioActual);
        vista.getTableModel().setRowCount(0); // Limpiar tabla

        for (InscripcionActivaDTO dto : inscripcionesActuales) {
            vista.getTableModel().addRow(new Object[]{
                dto.getNombreActividad(),
                dto.getEstado(),
                dto.getPrecio(),
                "Cancelar"
            });
        }
    }

    private void configurarTabla() {
        vista.getTablaActividades().getColumn("Cancelar").setCellRenderer(new ButtonRenderer());
        vista.getTablaActividades().getColumn("Cancelar").setCellEditor(new ButtonEditor(new JCheckBox()));
    }

    private void procesarCancelacion(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < inscripcionesActuales.size()) {
            InscripcionActivaDTO dto = inscripcionesActuales.get(rowIndex);
            
            int response = JOptionPane.showConfirmDialog(
                    vista,
                    "¿Estás seguro de que deseas cancelar tu inscripción a " + dto.getNombreActividad() + "?",
                    "Confirmar Cancelación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (response == JOptionPane.YES_OPTION) {
                try {
                    modelo.cancelarInscripcion(dto.getIdInscripcion(), idSocioActual);
                    JOptionPane.showMessageDialog(vista, "Inscripción cancelada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarInscripciones();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(vista, "Error al cancelar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    // --- Renderer y Editor para el botón en la JTable ---

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBackground(Color.RED);
            setForeground(Color.WHITE);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "Cancelar" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private int clickedRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.setBackground(Color.RED);
            button.setForeground(Color.WHITE);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            label = (value == null) ? "Cancelar" : value.toString();
            button.setText(label);
            isPushed = true;
            clickedRow = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                // Ejecutar la acción
                SwingUtilities.invokeLater(() -> {
                    procesarCancelacion(clickedRow);
                });
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
}
