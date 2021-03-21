package View;

import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class GraphDrawer extends JFrame {
    public GraphDrawer() {
        setLayout(null);
        setBounds(600, 200, 800, 800);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public void drawGraph(Integer[] nodes, ArrayList<Pair<Integer, Integer>> edges){
        Pair<Integer, Integer> size = Pair.of(100, 100);
        HashMap<Integer, Pair<Integer, Integer>> bounds = new HashMap<>();
        drawNodes(nodes, size, bounds);
        drawEdges(edges, bounds);
        repaint();
    }

    private void drawNodes(Integer[] nodes, Pair<Integer, Integer> size, HashMap<Integer, Pair<Integer, Integer>> bounds) {
        int radius = 200;
        int x;
        int y;
        double phi = 2 * Math.PI / nodes.length;
        for (int i = 0; i < nodes.length; i++){
            Circle circle = new Circle(nodes[i].toString());
            x = (int)(radius * Math.cos(phi * i)) + 400;
            y = (int)(radius * Math.sin(phi * i)) + 400;
            circle.setBounds(x, y, size.getLeft(), size.getRight());
            add(circle);
            bounds.put(nodes[i], Pair.of(x, y));
        }
    }

    private void drawEdges(ArrayList<Pair<Integer, Integer>> edges, HashMap<Integer, Pair<Integer, Integer>> bounds) {
        for (Pair<Integer, Integer> edge : edges){
            Pair<Integer, Integer> from = bounds.get(edge.getLeft());
            Pair<Integer, Integer> to = bounds.get(edge.getRight());
            JComponent line = new JComponent(){
                @Override
                public void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D)g;
                    g2.setColor(Color.BLACK);
                    g2.setStroke(new BasicStroke(6));
                    g2.drawLine(from.getLeft() + 50, from.getRight() + 50,
                            to.getLeft() + 50, to.getRight() + 50);
                }};
            line.setBounds(0, 0, 800, 800);
            add(line);
        }
    }

}
