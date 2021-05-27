package View.Matrix;

import javax.swing.*;
import javax.swing.table.TableModel;

public class VertexCellEditor extends DefaultCellEditor {
    private final TableModel model;
    private final int currentRow, currentColumn;

    public VertexCellEditor(int row, int column, TableModel model) {
        super(new JTextField());
        this.model = model;
        currentRow = row;
        currentColumn = column;
    }

    @Override
    public boolean stopCellEditing() {
        String text = ((JTextField) editorComponent).getText();
        if (!model.getValueAt(currentColumn, currentRow).equals(text))
            model.setValueAt(text, currentColumn, currentRow);

        return super.stopCellEditing();
    }
}
