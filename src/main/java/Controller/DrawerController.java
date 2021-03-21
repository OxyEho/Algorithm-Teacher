package Controller;

import Models.Graph;
import View.GraphDrawer;

import java.util.ArrayList;


public class DrawerController {
    private final Graph<Integer> graph;
    private final GraphDrawer graphDrawer;
    public DrawerController(Graph<Integer> graph, GraphDrawer graphDrawer){
        this.graph = graph;
        this.graphDrawer = graphDrawer;
    }

    public void transGraph(){
        Integer[] nodes = graph.getBaseMap().keySet().toArray(new Integer[0]);
        ArrayList<Integer[]> edges = new ArrayList<>();
        for (var node : graph.getNodeList()){
            for (var neighbour : node.getAdjacency()){
                edges.add(new Integer[]{node.getValue(), neighbour});
            }
        }

        graphDrawer.drawGraph(nodes, edges);
    }
}
