package Controller;

import Models.AlgorithmRunner;
import Models.Graph;
import Models.Node;
import View.GraphDrawer;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


public class DrawerController {
    private Graph<String> graph;
    private final GraphDrawer graphDrawer;
    public DrawerController(ActionListener toMenu, Graph<String> graph){
        this.graph = graph;
        graphDrawer = new GraphDrawer(toMenu, new RunButtonListener(), new GraphSizeFieldListener(),
                new ShowGraphButtonListener(), getNodes(), getEdges());
    }

    /**
     * -1 здесь есть значение по умолчанию.
     * Detour здесь есть обход.
     */
    private void setDefaultDetourOrder() {
        for (Node<String> node : graph.getNodeList())
            node.setSequenceNumber(-1);
    }

    private class RunButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton origin = (JButton) e.getSource();
            if (origin.getText().equals("Запустить алгоритм")){
                setDefaultDetourOrder();
                origin.setEnabled(false);
                String algorithm = graphDrawer.getAlgorithm();
                String startNode = graphDrawer.getStartNodeChoice();
                switch (algorithm) {
                    case "BFS" -> AlgorithmRunner.bfs(graph.getNode(startNode), graph);
                    case "DFS" -> AlgorithmRunner.dfs(graph.getNode(startNode), graph);
                }

                graphDrawer.illuminateNodes(getNameSequence());
            }
        }
    }

    private class GraphSizeFieldListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            JTextField field = graphDrawer.getSizeField();
            changeTable(field);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            JTextField field = graphDrawer.getSizeField();
            try {
                Integer.parseInt(field.getText());
                field.setBackground(Color.WHITE);
                changeTable(field);
            } catch (NumberFormatException ex) {
                field.setBackground(Color.RED);
            }
        }

        @Override
        public void changedUpdate(DocumentEvent e) { }

        private void changeTable(JTextField field) {
            try {
                int size = Integer.parseInt(field.getText());
                if (size > 0) {
                    field.setBackground(Color.WHITE);
                    graphDrawer.setTableSize(size);
                } else {
                    field.setBackground(Color.RED);
                    JOptionPane.showMessageDialog(
                            null,
                            "Размер графа должен быть больше 0", "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException numberFormatException){
                field.setBackground(Color.RED);
                JOptionPane.showMessageDialog(
                        null,
                        "Пожалуйста, введите натуральное число", "Error Message",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class ShowGraphButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                JButton origin = (JButton) e.getSource();
                if (origin.getText().equals("Показать граф")) {
                    String[][] table = graphDrawer.getTable(); // i - строки
                    if (!isValidSize(graphDrawer.getSizeField().getText())){
                        JOptionPane.showMessageDialog(
                                null,
                                "Пожалуйста, проверьте, что введенный размер есть натуральное число.",
                                "Error Message",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    graph = new Graph<>(parseGraph(table), false, false);
                    graphDrawer.setNodesAndEdges(getNodes(), getEdges());
                }
            } catch (NumberFormatException numberFormatException) {
                numberFormatException.printStackTrace();
            }
        }

        private boolean isValidSize(String sizeFieldText) {
            try {
                if (Integer.parseInt(sizeFieldText) <= 0) return false;
            } catch (NumberFormatException ex) {
                return false;
            }

            return true;
        }

        private HashMap<String, Node<String>> parseGraph(String[][] table) throws NumberFormatException {
            HashMap<String, HashMap<String, Double>> nodesWithAdjacency = new HashMap<>();
            for (int i = 1; i < table.length; i++)
                if (!nodesWithAdjacency.containsKey(table[0][i]))
                    nodesWithAdjacency.put(table[0][i], new HashMap<>());

            for (int i = 0; i < table.length; i++){
                for (int j = 0; j < table.length; j++){
                    if (i == 0 || j == 0) continue;
                    double number = Double.parseDouble(table[i][j]);
                    if (number >= 1e-10) { // != 0
                        if (nodesWithAdjacency.containsKey(table[i][0]))
                            nodesWithAdjacency.get(table[i][0]).put(table[0][j], number);
                    }
                }
            }

            HashMap<String, Node<String>> result = new HashMap<>();
            for (String nodeName : nodesWithAdjacency.keySet()){
                result.put(nodeName, new Node<>(nodeName, nodesWithAdjacency.get(nodeName)));
            }

            return result;
        }
    }

    public GraphDrawer getGraphDrawer() { return graphDrawer; }

    private List<String> getNodes(){
        return graph.getBaseMap().keySet().stream().map(Object::toString).collect(Collectors.toList());
    }

    private List<Pair<String, String>> getEdges(){
        return graph
                .getEdges()
                .keySet()
                .stream()
                .map(pair -> Pair.of(pair.getLeft(), pair.getRight()))
                .collect(Collectors.toList());
    }

    private List<String> getNameSequence() {
        return graph
                .getNodeList()
                .stream()
                .sorted(Comparator.comparingInt(Node::getSequenceNumber))
                .filter(node -> node.getSequenceNumber() != -1)
                .map(Node::getValue)
                .collect(Collectors.toList());
    }
}
