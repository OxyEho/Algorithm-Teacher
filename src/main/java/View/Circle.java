package View;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Circle extends JComponent {
    private final String text;
    private int width, height;
    private Color color;

    public Circle(int width, int height, Color color, String text){
        super();
        setVisible(true);
        this.text = text;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillOval(0, 0, width, height);
        g.setColor(color);
        g.fillOval(10,10, width - 20, height - 20);
        g.setColor(Color.BLACK);
        Font font = new Font("TimesRoman", Font.PLAIN, height/3);
        g.setFont(font);
        Rectangle2D rect = g.getFontMetrics().getStringBounds(text, g);
        g.drawString(text, width / 2 - (int)rect.getWidth() / 2,
                height / 2 + (int)rect.getHeight() / 4);
    }

    public void setHeight(int height) { this.height = height; }

    public void setWidth(int width) { this.width = width; }

    public void setColor(Color color) { this.color = color; }

    public String getText() { return text; }
}
