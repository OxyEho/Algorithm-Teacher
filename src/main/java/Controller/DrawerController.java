package Controller;

import Models.Graph;
import View.GraphDrawer;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;


public class DrawerController {
    private final Graph<Integer> graph;
    public DrawerController(Graph<Integer> graph){
        this.graph = graph;
    }

    public Integer[] getNodes(){
        return graph.getBaseMap().keySet().toArray(new Integer[0]);
    }

    public ArrayList<Pair<Integer, Integer>> getEdges(){
        return graph.getEdges();
    }
}
