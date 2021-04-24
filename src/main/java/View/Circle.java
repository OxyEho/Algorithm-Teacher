package View;

import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Formatter;

public class Circle extends JComponent {
    private final String text;
    private int centerX, centerY;
    private final int width, height;
    private Color color;

    public Circle(int centerX, int centerY, int width, int height, Color color, String text){
        //setVisible(true);
        setBounds(centerX, centerY, width, height);
        this.text = text;
        this.centerX = centerX;
        this.centerY = centerY;
        this.width = width;
        this.height = height;
        this.color = color;
        System.out.print(Arrays.toString(new int[]{centerX, centerY, width, height}));
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.fillOval(30, 30, 50, 50); // Если сделать centerX, centerY = 30, 30, то заработает
//        g.setColor(Color.BLACK);
//        g.fillOval(centerX, centerY, width, height);
//        g.setColor(color);
//        g.fillOval(centerX + 10,centerY + 10, width - 20, height - 20);
//        g.setColor(Color.BLACK);
//        Font font = new Font("TimesRoman", Font.PLAIN, height/3);
//        g.setFont(font);
//        Rectangle2D r = g.getFontMetrics().getStringBounds(text, g);
//        g.drawString(text, centerX + width / 2 - (int)r.getWidth() / 2,
//                centerY + height / 2 + (int)r.getHeight() / 4);
    }
}