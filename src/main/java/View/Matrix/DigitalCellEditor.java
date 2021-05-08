package View.Matrix;

import javax.swing.*;
import java.awt.*;

public class DigitalCellEditor extends DefaultCellEditor {
    private final InputVerifier verifier;

    public DigitalCellEditor() {
        super(new JTextField());
        verifier = new DigitVerifier();
    }

    @Override
    public boolean stopCellEditing() {
        return verifier.verify(editorComponent) && super.stopCellEditing();
    }

    private static class DigitVerifier extends InputVerifier {
        @Override
        public boolean verify(JComponent input) {
            try{
                Double.parseDouble(((JTextField) input).getText());
                input.setBackground(Color.WHITE);
            } catch (NumberFormatException ex) {
                input.setBackground(Color.RED);
                return false;
            }

            return true;
        }
    }
}
