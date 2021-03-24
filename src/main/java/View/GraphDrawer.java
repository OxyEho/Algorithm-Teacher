package View;

import Controller.DrawerController;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class GraphDrawer extends JFrame {
    private static final int divider = 10;
    private final DrawerController controller;
    private final HashMap<Integer, Pair<Integer, Integer>> coordinates = new HashMap<>();
    private class GraphPanel extends JPanel{
        public GraphPanel(){
            setDoubleBuffered(true);
        }

        public void drawNode(Graphics g, int centerX, int centerY, int width, int height, String text){
            g.setColor(Color.BLACK);
            g.fillOval(centerX, centerY, width, height);
            g.setColor(Color.WHITE);
            g.fillOval(centerX + 10,centerY+ 10, width - 20, height - 20);
            g.setColor(Color.BLACK);
            g.setFont(new Font("TimesRoman", Font.PLAIN, height/4));
            g.drawString(text, centerX + width/2, centerY + height/2);
        }

        private void drawNodes(Graphics g, Integer[] nodes) {
            int radius = Math.min(getHeight()/4, getWidth()/4);
            int x;
            int y;
            double phi = 2 * Math.PI / nodes.length;
            for (int i = 0; i < nodes.length; i++){
                x = (int)(radius * Math.cos(phi * i)) + getWidth()/2;
                y = (int)(radius * Math.sin(phi * i)) + getHeight()/2;
                drawNode(g, x, y, getWidth()/divider, getHeight()/divider, nodes[i].toString());
                coordinates.put(nodes[i], Pair.of(x, y));
            }
        }

        private void drawEdges(Graphics g, ArrayList<Pair<Integer, Integer>> edges, int width, int height) {
            for (Pair<Integer, Integer> edge : edges){
                Pair<Integer, Integer> from = coordinates.get(edge.getLeft());
                Pair<Integer, Integer> to = coordinates.get(edge.getRight());
                Graphics2D g2 = (Graphics2D)g;
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(6));
                g2.drawLine(from.getLeft() + width/2, from.getRight() + height/2,
                        to.getLeft() + width/2, to.getRight() + height/2);
            }
        }

        @Override
        public void paint(Graphics g){
            drawNodes(g, controller.getNodes());
            drawEdges(g, controller.getEdges(), getWidth()/divider, getHeight()/divider);
            drawNodes(g, controller.getNodes());
        }
    }
    public GraphDrawer(DrawerController controller) {
        this.controller = controller;
        setLayout(null);
        setSize(800, 800);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);

        JPanel graphPanel = new GraphPanel();
        graphPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        contentPanel.add(graphPanel, BorderLayout.CENTER);
    }
}