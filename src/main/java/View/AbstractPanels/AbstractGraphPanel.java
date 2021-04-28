package View.AbstractPanels;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import java.awt.Color;
import java.awt.Dimension;

public class AbstractGraphPanel extends JPanel {
    public AbstractGraphPanel(Dimension preferredSize) {
        setVisible(true);
        setLayout(null);
        setPreferredSize(preferredSize);
        setDoubleBuffered(true);
        setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.GRAY, Color.GRAY));
    }
}
