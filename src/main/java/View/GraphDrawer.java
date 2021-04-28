package View;

import View.AbstractPanels.AbstractButtonsPanel;
import View.AbstractPanels.AbstractGraphPanel;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
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

    private class GraphCreator extends JPanel {
        private GraphCreator(List<String> nodes, List<Pair<String, String>> pairs) {
            super();
            setVisible(true);
            setLayout(null);
            setPreferredSize(new Dimension(GraphDrawer.this.getWidth() / 2, GraphDrawer.this.getHeight()));
            setDoubleBuffered(true);
            setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.GRAY, Color.GRAY));
            addTable(nodes.size() + 1, nodes, pairs);
        }

        private void addTable(int size, List<String> nodes, List<Pair<String, String>> pairs) {
            JTable table = new JTable(size, size);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            table.setSize(new Dimension(30 * size, 30 * size));
            setTableSize(table);
            setGraph(table, size, nodes, pairs);
            table.setBounds(30, 30, table.getWidth(), table.getWidth());
            table.setRowSelectionAllowed(false);
            add(table);
        }

        private void setTableSize(JTable table) {
            table.setRowHeight(30);
            for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
                TableColumn column = table.getColumnModel().getColumn(i);
                column.setMaxWidth(30);
            }
        }

        private void setGraph(JTable table, int size, List<String> nodes, List<Pair<String, String>> pairs) {
            DefaultTableModel model = (DefaultTableModel)table.getModel();
            for (int i = 0; i < size - 1; i++) {
                model.setValueAt(nodes.get(i), 0, i+1);
                model.setValueAt(nodes.get(i), i+1, 0);
            }
            for (int i = 1; i < size; i++) {
                for (int j = 1; j < size; j++) {
                    model.setValueAt(0, i, j);
                }
            }

            for (Pair<String, String> pair: pairs) {
                int x = nodes.indexOf(pair.getLeft());
                int y = nodes.indexOf(pair.getRight());
                model.setValueAt(1, x + 1, y + 1);
            }
        }
    }

    private class GraphPanel extends AbstractGraphPanel {
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
        GraphCreator graphCreator = new GraphCreator(nodes, pairs);
        add(graphPanel, BorderLayout.WEST);
        add(new ButtonsPanel(onStart, onExit), BorderLayout.NORTH);
        add(graphCreator, BorderLayout.EAST);
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