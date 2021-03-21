package View;

import javax.swing.*;
import java.awt.*;

public class Circle extends JComponent {
    private final String text;
    public Circle(String text){
        this.text = text;
    }

    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillOval(0, 0, 90, 90);
        g.setColor(Color.WHITE);
        g.fillOval(10, 10, 70, 70);
        g.setColor(Color.BLACK);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 40));
        g.drawString(text, 35, 55);
    }

}
