package Controller;

import Models.AlgorithmRunner;
import Models.Graph;
import Models.Node;
import View.GraphDrawer;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.stream.Collectors;


public class DrawerController {
    private Graph<String> graph;
    private final ActionListener toMenuAction;
    private final GraphDrawer graphDrawer;
    public DrawerController(ActionListener toMenu, Graph<String> graph){
        this.graph = graph;
        toMenuAction = toMenu;
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

    private class GraphSizeFieldListener implements ActionListener  {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                JTextField field = (JTextField) e.getSource();
                int size = Integer.parseInt(field.getText());
                graphDrawer.setTableSize(size);
            } catch (NumberFormatException | ClassCastException numberFormatException) {
                numberFormatException.printStackTrace();
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
                    graph = new Graph<>(parseGraph(table));
                    graphDrawer.setNodesAndEdges(getNodes(), getEdges());
                }
            } catch (NumberFormatException numberFormatException) {
                numberFormatException.printStackTrace();
            }
        }

        private HashMap<String, Node<String>> parseGraph(String[][] table){
            HashMap<String, ArrayList<String>> nodesWithAdjacency = new HashMap<>();
            for (int i = 1; i < table.length; i++)
                if (!nodesWithAdjacency.containsKey(table[0][i]))
                    nodesWithAdjacency.put(table[0][i], new ArrayList<>());

            for (int i = 0; i < table.length; i++){
                for (int j = 0; j < table.length; j++){
                    if (i == 0 || j == 0) continue;
                    if (Integer.parseInt(table[i][j]) != 0){
                        if (nodesWithAdjacency.containsKey(table[i][0]))
                            nodesWithAdjacency.get(table[i][0]).add(table[0][j]);
                    }
                }
            }

            HashMap<String, Node<String>> result = new HashMap<>();
            for (String key : nodesWithAdjacency.keySet()){
                result.put(key, new Node<>(key, nodesWithAdjacency.get(key)));
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
