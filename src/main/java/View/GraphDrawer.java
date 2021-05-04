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
    private JComboBox<String> startNodeChoice; // (но мне пока не хочется этого делать)
    private final JComboBox<String> algorithmChoice;
    private List<Circle> circles = new ArrayList<>();
    private JTable table; // Либо храним GraphCreator здесь, либо как-то перегружаем конструктор
    private final GraphPanel graphPanel;

    private class GraphCreator extends JPanel {
        private static final int CELL_SIZE = 60;

        private GraphCreator(List<String> nodes, List<Pair<String, String>> pairs,
                             ActionListener sizeListener, ActionListener matrixListener) {
            super();
            setVisible(true);
            setLayout(null);
            setPreferredSize(new Dimension(GraphDrawer.this.getWidth() / 2, GraphDrawer.this.getHeight()));
            setDoubleBuffered(true);
            setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.GRAY, Color.GRAY));
            addTableWithInfrastructure(nodes.size() + 1, nodes, pairs, sizeListener, matrixListener);
        }

        private void addTableWithInfrastructure(int size, List<String> nodes, List<Pair<String, String>> pairs,
                                                ActionListener sizeListener, ActionListener matrixListener) {
            JPanel container = new JPanel();
            GroupLayout layout = new GroupLayout(container);
            layout.setAutoCreateGaps(true);
            layout.setAutoCreateContainerGaps(true);
            container.setLayout(layout);
            container.setBounds(
                    80, 85, GraphDrawer.this.getWidth() / 2 - 80, GraphDrawer.this.getHeight() / 2 - 85
            );
            table = new JTable(size, size);
            JLabel sizeLabel = new JLabel("Размер: ");
            JTextField sizeField = new JTextField();
            sizeField.addActionListener(sizeListener);
            JButton button = new JButton("Показать граф");
            button.addActionListener(matrixListener);

            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            table.setSize(new Dimension(CELL_SIZE * size, CELL_SIZE * size));
            setTableSize(table);
            setGraph(table, size, nodes, pairs);
            table.setRowSelectionAllowed(false);
            table.setFont(new Font("Microsoft JhengHei", Font.BOLD, 26));

            layout.setHorizontalGroup(
                    layout.createParallelGroup().addGroup(
                            layout.createSequentialGroup().addComponent(sizeLabel).addComponent(sizeField).addComponent(button)
                    ).addComponent(table)
            );
            layout.linkSize(sizeField, sizeLabel);
            layout.setVerticalGroup(
                    layout.createSequentialGroup().addGroup(
                            layout.createParallelGroup().addComponent(sizeLabel).addComponent(sizeField).addComponent(button)
                    ).addComponent(table)
            );

            add(container);
        }

        private void setTableSize(JTable table) {
            table.setRowHeight(CELL_SIZE);
            for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
                TableColumn column = table.getColumnModel().getColumn(i);
                column.setMaxWidth(CELL_SIZE);
            }
        }

        private void setGraph(JTable table, int size, List<String> nodes, List<Pair<String, String>> pairs) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
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
        private HashMap<String, Pair<Integer, Integer>> coordinates;
        private List<Pair<String, String>> edges;
        private List<String> nodes;

        private GraphPanel(List<String> nodes, List<Pair<String, String>> edges){
            super(new Dimension(GraphDrawer.this.getWidth() / 2, GraphDrawer.this.getHeight()));
            preferredWidth = GraphDrawer.this.getWidth() / 2;
            preferredHeight = GraphDrawer.this.getHeight();
            coordinates = new HashMap<>();
            this.nodes = nodes;
            this.edges = edges;
            addNodes();
        }

        private void addNodes() {
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

        public void setNodesAndEdges(List<String> nodes, List<Pair<String, String>> edges) {
            this.nodes = nodes;
            this.edges = edges;
            circles = new ArrayList<>();
            coordinates = new HashMap<>();
            startNodeChoice.removeAllItems();
            for (var node: nodes) {
                startNodeChoice.addItem(node);
                System.out.println(node);
            }
            removeAll();
            addNodes();
            repaint();
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
            constraints.fill = GridBagConstraints.NONE;
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
            add(toMenu, constraints);
        }
    }

    public GraphDrawer(ActionListener onExit, ActionListener onStart,
                       ActionListener sizeListener, ActionListener matrixListener,
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

        graphPanel = new GraphPanel(nodes, pairs);
        GraphCreator graphCreator = new GraphCreator(nodes, pairs, sizeListener, matrixListener);
        add(graphPanel, BorderLayout.WEST);
        add(new ButtonsPanel(onStart, onExit), BorderLayout.NORTH);
        add(graphCreator, BorderLayout.EAST);
    }

    public String getStartNodeChoice() {
        return (String) startNodeChoice.getSelectedItem();
    }

    public String getAlgorithm() { return (String) algorithmChoice.getSelectedItem(); }

    public String[][] getTable() {
        String[][] result = new String[table.getRowCount()][table.getRowCount()];
        Object current;
        for (int i = 0; i < table.getRowCount(); i++){
            for (int j = 0; j < table.getRowCount(); j++){
                current = table.getModel().getValueAt(i, j);
                if (current != null)
                    result[i][j] = current.toString();
                else
                    result[i][j] = "";
            }
        }

        return result;
    }

    public void setTableSize(int size) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == 0 || j ==0)
                    model.setValueAt("name" + (i + j), i, j);
                else
                    model.setValueAt(0, i, j);
            }
        }
    }

    public void setNodesAndEdges(List<String> nodes, List<Pair<String, String>> edges) {
        graphPanel.setNodesAndEdges(nodes, edges);
    }

    public void illuminateNodes(List<String> paintingSequence) { // А может можно это занести в GraphPanel?
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