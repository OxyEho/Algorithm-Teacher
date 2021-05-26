package View;

import View.AbstractPanels.AbstractButtonsPanel;
import View.AbstractPanels.AbstractGraphPanel;
import View.Matrix.MatrixWithInfrastructure;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.Timer;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class GraphDrawer extends JFrame {
    private final HashMap<String, JButton> buttons; // Из этого всего можно сделать мапу и объявить её в абстрактном классе
    private final JComboBox<String> startNodeChoice; // (но мне пока не хочется этого делать)
    private final JComboBox<String> algorithmChoice;
    private final JComboBox<String> graphNames;
    private List<Circle> circles = new ArrayList<>();
    private final GraphPanel graphPanel;
    private final GraphCreator graphCreator;
    private final ButtonsPanel buttonsPanel;

    private class GraphCreator extends JPanel {
        private final MatrixWithInfrastructure matrixPanel;

        private GraphCreator(List<String> nodes, List<Pair<String, String>> pairs,
                             DocumentListener sizeListener, ActionListener matrixListener) {
            super();
            setVisible(true);
            setLayout(null);
            setPreferredSize(new Dimension(GraphDrawer.this.getWidth() / 2, GraphDrawer.this.getHeight()));
            setDoubleBuffered(true);
            setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.GRAY, Color.GRAY));
            matrixPanel = new MatrixWithInfrastructure(
                    GraphDrawer.this.getWidth() / 2, GraphDrawer.this.getHeight() / 2,
                    nodes.size() + 1, sizeListener, matrixListener, nodes, pairs
            );
            add(matrixPanel);
        }

        public JTable getTable() { return matrixPanel.getTable(); }

        public JScrollPane getScrollPane() { return matrixPanel.getScrollPane(); }

        private JList<String> getRowHeaders(List<String> nodes) { return matrixPanel.getRowHeaders(nodes); }

        private void rebuildMatrix(int size, List<String> names, List<Pair<String, String>> pairs) {
            matrixPanel.setCellSizes();
            matrixPanel.setColumnHeaders(names);
            matrixPanel.setTableValues(size, names, pairs);
        }
    }

    private class GraphPanel extends AbstractGraphPanel {
        private static final int DIVIDER = 10;
        private static final int ARROW_SIZE = 10;
        private final int preferredWidth, preferredHeight;
        private HashMap<String, Pair<Integer, Integer>> coordinates;
        private List<Pair<String, String>> edges;
        private List<String> nodes;
        private boolean isDirected, isWeighted;

        private GraphPanel(List<String> nodes, List<Pair<String, String>> edges,
                           boolean isDirected, boolean isWeighted){
            super(new Dimension(GraphDrawer.this.getWidth() / 2, GraphDrawer.this.getHeight()));
            preferredWidth = GraphDrawer.this.getWidth() / 2;
            preferredHeight = GraphDrawer.this.getHeight();
            coordinates = new HashMap<>();
            this.nodes = nodes;
            this.edges = edges;
            this.isWeighted = isWeighted;
            this.isDirected = isDirected;
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
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(6));
            if (isDirected) {
                drawArrowEdges(g2, edges, width, height);
            } else {
                drawLineEdges(g2, edges, width, height);
            }
        }

        private void drawLineEdges(Graphics2D g2, List<Pair<String, String>> edges, int width, int height) {
            for (Pair<String, String> edge : edges) {
                Pair<Integer, Integer> from = coordinates.get(edge.getLeft());
                Pair<Integer, Integer> to = coordinates.get(edge.getRight());
                g2.drawLine(from.getLeft() + width/2, from.getRight() + height/2,
                        to.getLeft() + width/2, to.getRight() + height/2);
            }
        }

        private void drawArrowEdges(Graphics2D g2, List<Pair<String, String>> edges, int width, int height) {
            for (Pair<String, String> edge : edges) {
                Pair<Integer, Integer> from = coordinates.get(edge.getLeft());
                Pair<Integer, Integer> to = coordinates.get(edge.getRight());
                drawArrow(g2, from.getLeft() + width/2, from.getRight() + height/2,
                        to.getLeft() + width/2, to.getRight() + height/2);
            }
        }

        private void drawArrow(Graphics2D g2, int x1, int y1, int x2, int y2) { // не работает
            int radius = 0;
            g2.drawLine(x1, y1, x2, y2);
            double dx = x2 - x1;
            double angle = Math.atan((y2 - y1) / dx) + (x1 < x2 ? Math.PI : 0);
            if (dx == 0.0d) {
                angle = (y1 > y2 ? Math.PI/2 : -Math.PI/2);
            }


            double arrowAngle = Math.PI / 12.0d;
            int arrowSize = 80;
            double arrowX1 = arrowSize * Math.cos(angle - arrowAngle) - radius;
            double arrowY1 = arrowSize * Math.sin(angle - arrowAngle) - radius;
            double arrowX2 = arrowSize * Math.cos(angle + arrowAngle) - radius;
            double arrowY2 = arrowSize * Math.sin(angle + arrowAngle) - radius;

            GeneralPath polygon = new GeneralPath();
            polygon.moveTo(x2, y2);
            polygon.lineTo(x2 + arrowX1, y2 + arrowY1);
            polygon.lineTo(x2 + arrowX2, y2 + arrowY2);
            polygon.closePath();
            g2.fill(polygon);
            g2.drawLine(x1, y1, x2, y2);
//            g2.drawLine(x1, y1, (int) (x2 + cx), (int) (y2 + cy));
//            g2.drawLine(x1, y1, (int) (x2 * (angle / 2)), (int)(y2 * (angle/2)));
//            AffineTransform tx = new AffineTransform();
//            Polygon arrowHead = new Polygon();
//            arrowHead.addPoint( 0,5);
//            arrowHead.addPoint( -5, -5);
//            arrowHead.addPoint( 5,-5);
//            tx.setToIdentity();
//            double angle = Math.atan2(y2-y1, x2-x1);
//            tx.translate(x2, y2);
//            tx.rotate((angle-Math.PI/2d));
//
//            Graphics2D g = (Graphics2D) g2.create();
//            g.setTransform(tx);
//            g.fill(arrowHead);
//
//            g.dispose();
//            double dx = x2 - x1, dy = y2 - y1;
//            double angle = Math.atan2(dy, dx);
//            int len = (int) Math.sqrt(dx*dx + dy*dy);
//            AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
//            at.concatenate(AffineTransform.getRotateInstance(angle));
//            g2.transform(at);
//
//            // Draw horizontal arrow starting in (0, 0)
//            g2.drawLine(x1, y1, len, y2);
//            g2.fillPolygon(new int[] { len, len - ARROW_SIZE, len - ARROW_SIZE, len },
//                    new int[] { 0, -ARROW_SIZE, ARROW_SIZE, 0 }, 4);
        }

        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            recalculateNodesCoordinates();
            drawEdges(g, edges, getWidth()/DIVIDER, getHeight()/DIVIDER);
        }

        private void illuminateNodes(List<String> paintingSequence) { // А может можно это занести в GraphPanel?
            for (Circle circle : circles)
                circle.setColor(Color.WHITE);

            AtomicInteger lastIndex = new AtomicInteger();
            final Timer timer = new Timer(1500, null);
            timer.addActionListener(tick -> onTimerTick(timer, paintingSequence, lastIndex));
            timer.start();
        }

        public void setNodesAndEdges(List<String> nodes, List<Pair<String, String>> edges) {
            nodes.sort(String::compareTo); // (x, y) -> x.compateTo(y)
            nodes.sort(Comparator.comparingInt(String::length));
            this.nodes = nodes;
            this.edges = edges;
            circles = new ArrayList<>();
            coordinates = new HashMap<>();
            startNodeChoice.removeAllItems();
            for (String node: this.nodes) {
                startNodeChoice.addItem(node);
            }
            removeAll();
            addNodes();
            repaint();
        }
    }

    private class ButtonsPanel extends AbstractButtonsPanel {
        private final JTextField graphName = new JTextField("Graph");
        private ButtonsPanel(ActionListener onStartButtonClick, ActionListener onExitButtonClick,
                             ActionListener saveAction){
            super(new Dimension(GraphDrawer.this.getWidth(), GraphDrawer.this.getHeight() / 20));
            createAlgorithmsPart(onStartButtonClick);
            createServicePart(onExitButtonClick, saveAction);
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

        private void createServicePart(ActionListener onExitButtonClick, ActionListener saveAction) {
            constraints.gridx++;
            add(new JLabel("Введите имя графа:"), constraints);
            constraints.gridx++;
            graphName.setPreferredSize(new Dimension(100, 25));
            add(graphName, constraints);
            JButton saveButton = new JButton("Сохранить");
            buttons.put(saveButton.getText(), saveButton);
            saveButton.addActionListener(saveAction);
            constraints.gridx++;
            add(saveButton, constraints);
            constraints.gridx++;
            add(graphNames, constraints);
            JButton toMenu = new JButton("В главное меню");
            toMenu.addActionListener(onExitButtonClick);
            buttons.put(toMenu.getText(), toMenu);
            constraints.gridx++;
            add(toMenu, constraints);

        }

        public String getGraphName() {return graphName.getText(); }
    }

    public GraphDrawer(ActionListener onExit, ActionListener onStart, ActionListener saveAction,
                       DocumentListener sizeListener, ActionListener matrixListener,
                       List<String> nodes, List<Pair<String, String>> pairs,
                       boolean isDirected, boolean isWeighted) {

        startNodeChoice = new JComboBox<>(nodes.toArray(String[]::new));
        algorithmChoice = new JComboBox<>(new String[]{"BFS", "DFS"});
        graphNames = new JComboBox<>();
        buttons = new HashMap<>();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setVisible(true);
        // Если создавать панели раньше чем делать все сеттеры, то панели не будут отображаться
        graphPanel = new GraphPanel(nodes, pairs, isDirected, isWeighted);
        graphCreator = new GraphCreator(nodes, pairs, sizeListener, matrixListener);
        buttonsPanel = new ButtonsPanel(onStart, onExit, saveAction);
        add(graphPanel, BorderLayout.WEST);
        add(graphCreator, BorderLayout.EAST);
        add(buttonsPanel, BorderLayout.NORTH);

    }

    public String getStartNodeChoice() {
        return (String) startNodeChoice.getSelectedItem();
    }

    public String getAlgorithm() { return (String) algorithmChoice.getSelectedItem(); }

    public String getGraphName() { return buttonsPanel.getGraphName(); }

    public String[][] getTable() {
        JTable table = graphCreator.getTable();
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

    private void addColumns(int size) {
        DefaultTableModel model = (DefaultTableModel) graphCreator.getTable().getModel();
        model.setRowCount(size);
        model.setColumnCount(size);
    }

    public void setTableSize(int size) {
        JTable table = graphCreator.getTable();
        List<String> names = new ArrayList<>();
        for (int i = 1; i < size + 1; i++)
            names.add("v" + i);
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (int i = model.getColumnCount() - 1; i >= 0 ; i--) {
            TableColumn c = table.getColumnModel().getColumn(i);
            table.removeColumn(c);
        }
        addColumns(size + 1);
        graphCreator.rebuildMatrix(size + 1, names, Collections.emptyList());
        JList<String> headers = graphCreator.getRowHeaders(names);
        headers.setFixedCellHeight(60);
        graphCreator.getScrollPane().setRowHeaderView(headers);
    }

    public void setNodesAndEdges(List<String> nodes, List<Pair<String, String>> edges) {
        graphPanel.setNodesAndEdges(nodes, edges);
    }

    public void illuminateNodes(List<String> paintingSequence) { graphPanel.illuminateNodes(paintingSequence); }

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

    public JButton getButton(String name) {
        return buttons.getOrDefault(name, null);
    }

    public JTextField getSizeField() { return graphCreator.matrixPanel.getSizeField(); }
}