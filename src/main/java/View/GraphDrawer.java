package View;

import View.AbstractPanels.AbstractButtonsPanel;
import View.AbstractPanels.AbstractGraphDrawer;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GraphDrawer extends JFrame {
    private static final int DIVIDER = 10;
    private final HashMap<String, JButton> buttons; // Из этого всего можно сделать мапу и объявить её в абстрактном классе
    private final JComboBox<String> startNodeChoice; // (но мне пока не хочется этого делать)
    private final JComboBox<String> algorithmChoice;
    private final List<Circle> circles = new ArrayList<>();

    private class GraphPanel extends AbstractGraphDrawer {
        private final int preferredWidth, preferredHeight;
        private final HashMap<String, Pair<Integer, Integer>> coordinates;
        private final List<Pair<String, String>> edges;

        private GraphPanel(List<String> nodes, List<Pair<String, String>> edges){
            super(new Dimension(GraphDrawer.this.getWidth() / 2, GraphDrawer.this.getHeight()));
            preferredWidth = GraphDrawer.this.getWidth() / 2;
            preferredHeight = GraphDrawer.this.getHeight();
            coordinates = new HashMap<>();
            this.edges = edges;
            addNodes(nodes);
        }

        private void addNodes(List<String> nodes) {
            for (String nodeName : nodes){
                Circle circle = new Circle(0, 0, Color.WHITE, nodeName);
                add(circle);
                circles.add(circle);
            }
        }

        private void recalculateNodesCoordinates() {
            int radius = Math.min(preferredHeight/4, preferredWidth/4);
            int x, y;
            double phi = 2 * Math.PI / circles.size();
            for (int i = 0; i < circles.size(); i++){
                x = (int)(radius * Math.cos(phi * i)) + preferredWidth/2;
                y = (int)(radius * Math.sin(phi * i)) + preferredHeight/2;
                circles.get(i).setHeight(preferredHeight/DIVIDER + 10);
                circles.get(i).setWidth(preferredWidth/DIVIDER + 10);
                circles.get(i).setBounds(x, y, preferredWidth/DIVIDER + 10, preferredHeight/DIVIDER + 10);
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

    private class ButtonsPanel extends AbstractButtonsPanel {

        private ButtonsPanel(ActionListener onStartButtonClick, ActionListener onExitButtonClick){
            super(new Dimension(GraphDrawer.this.getWidth(), GraphDrawer.this.getHeight() / 20));
            createAlgorithmsPart(onStartButtonClick);
            createServicePart(onExitButtonClick);

        }

        private void createAlgorithmsPart(ActionListener onStartButtonClick) {
            constraints.gridx = 0;
            add(new JLabel("Выбор алгоритма для запуска:"), constraints);
            constraints.gridx++;
            add(algorithmChoice, constraints);
            JButton startButton = new JButton("Запустить алгоритм");
            startButton.addActionListener(onStartButtonClick);
            buttons.put(startButton.getText(), startButton);
            constraints.gridx++;
            add(startButton, constraints);
            constraints.gridx++;
            add(new JLabel("Выбор начальной вершины:"), constraints);
            constraints.gridx++;
            add(startNodeChoice, constraints);
        }

        private void createServicePart(ActionListener onExitButtonClick) {
            JButton toMenu = new JButton("В главное меню");
            toMenu.addActionListener(onExitButtonClick);
            buttons.put(toMenu.getText(), toMenu);
            constraints.gridx++;
            add(toMenu, constraints); // Почему-то не получается добавить кнопку к правому краю контейнера
        }
    }

    public GraphDrawer(ActionListener onExit, ActionListener onStart,
                       List<String> nodes, List<Pair<String, String>> pairs) {
        startNodeChoice = new JComboBox<>(nodes.toArray(String[]::new));
        algorithmChoice = new JComboBox<>(new String[]{"BFS", "DFS"});
        buttons = new HashMap<>();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setVisible(true);

        GraphPanel graphPanel = new GraphPanel(nodes, pairs);
        add(graphPanel, BorderLayout.WEST);
        add(new ButtonsPanel(onStart, onExit), BorderLayout.NORTH);
    }

    public String getStartNodeChoice() {
        return (String) startNodeChoice.getSelectedItem();
    }

    public String getAlgorithm() { return (String) algorithmChoice.getSelectedItem(); }

    public void illuminateNodes(List<String> paintingSequence) {
        for (Circle circle : circles)
            circle.setColor(Color.WHITE);

        AtomicInteger lastIndex = new AtomicInteger();
        final Timer timer = new Timer(1500, null);
        timer.addActionListener(tick -> onTimerTick(timer, paintingSequence, lastIndex));
        timer.start();
    }

    private void onTimerTick(Timer timer, List<String> paintingSequence, AtomicInteger index) {
        if (index.get() < paintingSequence.size()){
            for (Circle circle : circles) {
                if (circle.getText().equals(paintingSequence.get(index.get()))) {
                    circle.setColor(Color.ORANGE);
                    break;
                }
            }
            index.getAndIncrement();
            repaint();
        } else {
            timer.stop();
            buttons.get("Запустить алгоритм").setEnabled(true);
            System.out.println("Stopped!");
        }
    }
}