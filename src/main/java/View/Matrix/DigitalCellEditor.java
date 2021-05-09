package View.Matrix;

import javax.swing.*;

public class DigitalCellEditor extends DefaultCellEditor {
    private final InputVerifier verifier;

    public DigitalCellEditor() {
        super(new JTextField());
        verifier = new DigitVerifier();
    }

    @Override
    public boolean stopCellEditing() {
        if (!verifier.verify(editorComponent)) {
            JOptionPane.showMessageDialog(
                    null,
                    "В этой клетке должно быть действительное число (дробная часть через точку)",
                    "Error Message",
                    JOptionPane.ERROR_MESSAGE);
            cancelCellEditing();
        }

        return super.stopCellEditing();
    }

    private static class DigitVerifier extends InputVerifier {
        @Override
        public boolean verify(JComponent input) {
            JTextField textField = (JTextField) input;
            try{
                try {
                    textField.setText(String.valueOf(Integer.parseInt(textField.getText())));
                } catch (NumberFormatException ex) {
                    textField.setText(String.valueOf(Double.parseDouble(textField.getText())));
                }
            } catch (NumberFormatException ex) {
                return false;
            }

            return true;
        }
    }
}
