package View.AbstractPanels;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class AbstractButtonsPanel extends JPanel {
    protected final GridBagConstraints constraints;

    public AbstractButtonsPanel(Dimension preferredSize) {
        setPreferredSize(preferredSize);
        setLayout(new GridBagLayout());
        setBackground(Color.getHSBColor(83.5F, 67, 88));
        setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.5;
        constraints.gridy = 0  ;
        setVisible(true);
    }
}
