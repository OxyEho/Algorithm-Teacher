package Controller;

import Models.AlgorithmRunner;
import Models.Graph;
import Models.Node;
import View.GraphDrawer;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class DrawerController {
    private final Graph<String> graph;
    private final ActionListener toMenuAction;
    private final GraphDrawer graphDrawer;
    public DrawerController(ActionListener toMenu, Graph<String> graph){
        this.graph = graph;
        toMenuAction = toMenu;
        graphDrawer = new GraphDrawer(toMenu, new RunButtonListener(), getNodes(), getEdges());
    }

    private class RunButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton origin = (JButton) e.getSource();
            if (origin.getText().equals("Запустить алгоритм")){
                AlgorithmRunner.bfs(graph.getNode("a"), graph);
            }
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
                .map(Node::getValue)
                .collect(Collectors.toList());
    }
}
