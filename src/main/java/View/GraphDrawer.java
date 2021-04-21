package View;

import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GraphDrawer extends JFrame {
    private static final int DIVIDER = 10;
    private final HashMap<String, JButton> buttons;
    private final JComboBox<String> startNodeChoice;
    private final JComboBox<String> algorithmChoice;
    private final List<String> nodes;
    private final List<Pair<String, String>> edges;
    private final ActionListener onExitButtonClick;
    private final ActionListener onStartButtonClick;
    private final HashMap<String, Pair<Integer, Integer>> coordinates = new HashMap<>();

    private final HashMap<String, Color> colors = new HashMap<>();

    private class GraphPanel extends JPanel {
        private GraphPanel(){
            setVisible(true);
            setSize(GraphDrawer.this.getWidth() / 2, GraphDrawer.this.getHeight());
            // setDoubleBuffered(true);
            setBackground(Color.RED);

//            addMouseListener(new MouseInputAdapter() {
//                @Override
//                public void mouseClicked(MouseEvent e) {
//                    super.mouseClicked(e);
//                }
//
//                @Override
//                public void mousePressed(MouseEvent e) {
//                    super.mousePressed(e);
//                }
//
//                @Override
//                public void mouseDragged(MouseEvent e) {
//                    super.mouseDragged(e);
//                }
//
//                @Override
//                public void mouseMoved(MouseEvent event) {
//                    super.mouseMoved(event);
//                    System.out.println(getX() + ' ' + event.getX());
//                    if (event.getX() == getX()) {
//                        setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
//                    }
//                }
//            });
            addMouseMotionListener(new MouseMotionListener() {
                @Override
                public void mouseDragged(MouseEvent e) {

                }

                @Override
                public void mouseMoved(MouseEvent event) {
                    System.out.println(getBounds());
                    if (event.getX() == getX()) {
                        setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
                    }
                }
            });
        }

        public void drawNode(Graphics g, int centerX, int centerY, int width, int height, String text){
            g.setColor(Color.BLACK);
            g.fillOval(centerX, centerY, width, height);
            g.setColor(colors.get(text));
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
                drawNode(g, x, y, getWidth()/DIVIDER, getHeight()/DIVIDER, nodes.get(i));
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
        public void paintComponent(Graphics g){
            drawNodes(g, nodes);
            drawEdges(g, edges, getWidth()/DIVIDER, getHeight()/DIVIDER);
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
        this.nodes = nodes;
        this.edges = pairs;
        onExitButtonClick = onExit;
        onStartButtonClick = onStart;
        startNodeChoice = new JComboBox<>(nodes.toArray(String[]::new));
        algorithmChoice = new JComboBox<>(new String[]{"BFS", "DFS"});
        buttons = new HashMap<>();

        //Тут начало костылей
        //Можно не наследовать Circle от JComponent а просто прокидывать графикс и хранить лист Circle
        for (String node : this.nodes){
            colors.put(node, Color.WHITE);
        }
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        setSize((int) width, (int) height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
        setVisible(true);

        GraphPanel graphPanel = new GraphPanel();
        //graphPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        add(graphPanel, BorderLayout.CENTER);
        add(new ButtonsPanel(), BorderLayout.NORTH);
    }

    public String getStartNodeChoice() {
        return (String) startNodeChoice.getSelectedItem();
    }

    public String getAlgorithm() {
        return (String) algorithmChoice.getSelectedItem();
    }

    public void illuminateNodes(List<String> paintingSequence) {
        for (String node : this.nodes){
            colors.put(node, Color.WHITE);
        }
        System.out.println("Invoked!");
        System.out.println(String.join(" ", paintingSequence));
        AtomicInteger lastIndex = new AtomicInteger();
        final Timer timer = new Timer(1500, null);
        timer.addActionListener(tick -> {
            if (lastIndex.get() < paintingSequence.size()){
                colors.put(paintingSequence.get(lastIndex.get()), Color.CYAN);
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