package View;

import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GraphDrawer extends JFrame {
    private static final int DIVIDER = 10;
    private final HashMap<String, JButton> buttons;
    private final JComboBox<String> startNodeChoice;
    private final JComboBox<String> algorithmChoice;
    private final List<Pair<String, String>> edges;
    private final ActionListener onExitButtonClick;
    private final ActionListener onStartButtonClick;
    private final HashMap<String, Pair<Integer, Integer>> coordinates = new HashMap<>();
    private final List<Circle> circles = new ArrayList<>();

 //   private final HashMap<String, Color> colors = new HashMap<>();

    private class GraphPanel extends JPanel {
        private GraphPanel(List<String> nodes){
            setVisible(true);
            setLayout(null);
            setPreferredSize(
                    new Dimension(GraphDrawer.this.getWidth() / 2, GraphDrawer.this.getHeight())
            );
            setDoubleBuffered(true);
            setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.GRAY, Color.GRAY));
            addNodes(nodes);
//            addMouseMotionListener(new MouseMotionListener() {
//                @Override
//                public void mouseDragged(MouseEvent e) {
//
//                }
//
//                @Override
//                public void mouseMoved(MouseEvent event) {
//                    System.out.println(getBounds());
//                    if (event.getX() == getX()) {
//                        setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
//                    }
//                }
//            });
        }

        private void addNodes(List<String> nodes) {
            for (String nodeName : nodes){
                Circle circle = new Circle(0, 0, Color.WHITE, nodeName);
                add(circle);
                circles.add(circle);
            }
        }

        private void recalculateNodesCoordinates() {
            int width = GraphDrawer.this.getWidth() / 2;
            int height = GraphDrawer.this.getHeight();
            int radius = Math.min(height/4, width/4);
            int x;
            int y;
            double phi = 2 * Math.PI / circles.size();
            for (int i = 0; i < circles.size(); i++){
                x = (int)(radius * Math.cos(phi * i)) + width/2;
                y = (int)(radius * Math.sin(phi * i)) + height/2;
                circles.get(i).setHeight(height/DIVIDER + 10);
                circles.get(i).setWidth(width/DIVIDER + 10);
                circles.get(i).setBounds(x, y, width/DIVIDER + 10, height/DIVIDER + 10);
                coordinates.put(circles.get(i).getText(), Pair.of(x, y));
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
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            recalculateNodesCoordinates();

            drawEdges(g, edges, getWidth()/DIVIDER, getHeight()/DIVIDER);
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
            add(new JLabel("Выбор алгоритма для запуска:"));
            add(algorithmChoice);
            JButton startButton = new JButton("Запустить алгоритм");
            startButton.addActionListener(onStartButtonClick);
            buttons.put(startButton.getText(), startButton);
            add(startButton);
            add(new JLabel("Выбор начальной вершины:"));
            add(startNodeChoice);
        }

        private void createServicePart() {
            JButton toMenu = new JButton("В главное меню");
            toMenu.addActionListener(onExitButtonClick);
            buttons.put(toMenu.getText(), toMenu);
            add(toMenu, BorderLayout.EAST); // Почему-то не получается добавить кнопку к правому краю контейнера
        }
    }

    public GraphDrawer(ActionListener onExit, ActionListener onStart,
                       List<String> nodes, List<Pair<String, String>> pairs) {
        this.edges = pairs;
        onExitButtonClick = onExit;
        onStartButtonClick = onStart;
        startNodeChoice = new JComboBox<>(nodes.toArray(String[]::new));
        algorithmChoice = new JComboBox<>(new String[]{"BFS", "DFS"});
        buttons = new HashMap<>();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setVisible(true);

        GraphPanel graphPanel = new GraphPanel(nodes);
        add(graphPanel, BorderLayout.WEST);
        add(new ButtonsPanel(), BorderLayout.NORTH);
    }

    public String getStartNodeChoice() {
        return (String) startNodeChoice.getSelectedItem();
    }

    public String getAlgorithm() {
        return (String) algorithmChoice.getSelectedItem();
    }

    public void illuminateNodes(List<String> paintingSequence) {
        for (Circle circle : circles){
            circle.setColor(Color.WHITE);
        }
        System.out.println("Invoked!");
        System.out.println(String.join(" ", paintingSequence));
        AtomicInteger lastIndex = new AtomicInteger();
        final Timer timer = new Timer(1500, null);
        timer.addActionListener(tick -> {
            if (lastIndex.get() < paintingSequence.size()){
                for (Circle circle : circles) {
                    if (circle.getText().equals(paintingSequence.get(lastIndex.get()))) {
                        circle.setColor(Color.ORANGE);
                        break;
                    }
                }
                lastIndex.getAndIncrement();
                repaint();
            } else {
                timer.stop();
                buttons.get("Запустить алгоритм").setEnabled(true);
                System.out.println("Stopped!");
            }
        });
        timer.start();
    }
}