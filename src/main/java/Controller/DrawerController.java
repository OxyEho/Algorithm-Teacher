package Controller;

import Models.Graph;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class DrawerController {
    private final Graph<Integer> graph;
    public DrawerController(Graph<Integer> graph){
        this.graph = graph;
    }

    public List<String> getNodes(){
        return graph.getBaseMap().keySet().stream().map(Object::toString).collect(Collectors.toList());
    }

    public List<Pair<String, String>> getEdges(){
        return graph
                .getEdges()
                .stream()
                .map(pair -> Pair.of(pair.getLeft().toString(), pair.getRight().toString()))
                .collect(Collectors.toList());
    }
}
