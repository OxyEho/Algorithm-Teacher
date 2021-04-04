package View;

import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import java.awt.*;

public class Circle extends JComponent {
    private final String text;
    private final Pair<Integer, Integer> size;
    public Circle(String text, Pair<Integer, Integer> size){
        this.text = text;
        this.size = size;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillOval(0, 0, this.size.getLeft() - 10, this.size.getRight() - 10);
        g.setColor(Color.WHITE);
        g.fillOval(10, 10, this.size.getLeft() - 30, this.size.getRight() - 30);
        g.setColor(Color.BLACK);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 40));
        g.drawString(text, 35, 55);
    }
}