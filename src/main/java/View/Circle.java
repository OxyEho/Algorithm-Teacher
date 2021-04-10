package View;

import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Circle extends JComponent {
    private final String text;
    private final int centerX, centerY;
    private final int width, height;

    public Circle(int centerX, int centerY, int width, int height, String text){
        this.text = text;
        this.centerX = centerX;
        this.centerY = centerY;
        this.width = width;
        this.height = height;
    }

    @Override
    public void paintComponent(Graphics g) {
//        g.setColor(Color.BLACK);
//        g.fillOval(0, 0, this.size.getLeft() - 10, this.size.getRight() - 10);
//        g.setColor(Color.WHITE);
//        g.fillOval(10, 10, this.size.getLeft() - 30, this.size.getRight() - 30);
//        g.setColor(Color.BLACK);
//        g.setFont(new Font("TimesRoman", Font.PLAIN, 40));
//        g.drawString(text, 35, 55);
        g.setColor(Color.BLACK);
        g.fillOval(centerX, centerY, width, height);
        g.setColor(Color.WHITE);
        g.fillOval(centerX + 10,centerY + 10, width - 20, height - 20);
        g.setColor(Color.BLACK);
        Font font = new Font("TimesRoman", Font.PLAIN, height/3);
        g.setFont(font);
        Rectangle2D r = g.getFontMetrics().getStringBounds(text, g);
        g.drawString(text, centerX + width / 2 - (int)r.getWidth() / 2,
                centerY + height / 2 + (int)r.getHeight() / 4);
    }
}