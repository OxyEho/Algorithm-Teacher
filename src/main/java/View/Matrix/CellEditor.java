package View.Matrix;

import javax.swing.*;

public class CellEditor extends DefaultCellEditor {
    InputVerifier verifier = null;

    public CellEditor(InputVerifier verifier) {
        super(new JTextField());
        this.verifier = verifier;

    }

    @Override
    public boolean stopCellEditing() {
        return verifier.verify(editorComponent) && super.stopCellEditing();
    }
}
