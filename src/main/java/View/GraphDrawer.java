package View;

import Controller.DrawerController;
import Controller.MainController;
import Models.Graph;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.List;

public class GraphDrawer extends JFrame {
    private final List<String> nodes;
    private final List<Pair<String, String>> edges;
    private static final int divider = 10;
    private final ActionListener onExitButtonClick;
    private final ActionListener onStartButtonClick;
    private final HashMap<String, Pair<Integer, Integer>> coordinates = new HashMap<>();

    private class GraphPanel extends JPanel {
        private GraphPanel(){
            setDoubleBuffered(true);
            setSize(400, 400);
        }

        public void drawNode(Graphics g, int centerX, int centerY, int width, int height, String text){
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

        private void drawNodes(Graphics g, List<String> nodes) {
            int radius = Math.min(getHeight()/4, getWidth()/4);
            int x;
            int y;
            double phi = 2 * Math.PI / nodes.size();
            for (int i = 0; i < nodes.size(); i++){
                x = (int)(radius * Math.cos(phi * i)) + getWidth()/2;
                y = (int)(radius * Math.sin(phi * i)) + getHeight()/2;
                drawNode(g, x, y, getWidth()/divider, getHeight()/divider, nodes.get(i));
                coordinates.put(nodes.get(i), Pair.of(x, y));
            }
        }

        private void drawEdges(Graphics g, List<Pair<String, String>> edges, int width, int height) {
            for (Pair<String, String> edge : edges){
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
            drawNodes(g, nodes);
            drawEdges(g, edges, getWidth()/divider, getHeight()/divider);
            drawNodes(g, nodes);
        }
    }

    private class ButtonsPanel extends JPanel {
        private ButtonsPanel(){
            setLayout(new FlowLayout(FlowLayout.LEADING));
            setVisible(true);
            createAlgorithmsPart();
            createServicePart();
            setBackground(Color.getHSBColor(83.5F, 67, 88));
            setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        }

        private void createAlgorithmsPart() {
            add(new JLabel("Выбор алгоритма для запуска: "));
            JComboBox<String> algorithms = new JComboBox<>(new String[]{"BFS", "DFS", "dfsadasd dsad"});
            add(algorithms);
            JButton startButton = new JButton("Запустить алгоритм");
            startButton.addActionListener(onStartButtonClick);
            add(startButton);
        }

        private void createServicePart() {
            JButton toMenu = new JButton("В главное меню");
            toMenu.addActionListener(onExitButtonClick);
            add(toMenu, BorderLayout.EAST); // Почему-то не получается добавить кнопку к правому краю контейнера
        }
    }

    public GraphDrawer(ActionListener onExit, ActionListener onStart,
                       List<String> nodes, List<Pair<String, String>> pairs) {
        this.nodes = nodes;
        this.edges = pairs;
        onExitButtonClick = onExit;
        onStartButtonClick =onStart;
        setLayout(null);
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        setVisible(true);

        var graphPanel = new GraphPanel();
        graphPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        add(graphPanel, BorderLayout.CENTER);
        add(new ButtonsPanel(), BorderLayout.NORTH);
    }
}