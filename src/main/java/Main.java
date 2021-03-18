import Models.AlgorithmRunner;
import Models.Graph;
import Models.Node;
import View.GraphDrawer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        var graph = new Graph<Integer>(new HashMap<>(){
            {
                put(1, (new Node<>(1, new ArrayList<>(Arrays.asList(2, 3, 4)))));
                put(2, (new Node<>(2, new ArrayList<>(Arrays.asList(3, 4, 5)))));
                put(3, (new Node<>(3, new ArrayList<>(Arrays.asList(1, 2)))));
                put(4, (new Node<>(4, new ArrayList<>(Arrays.asList(1, 2)))));
                put(5, (new Node<>(5, new ArrayList<>(Collections.singletonList(2)))));
            }
        });
//        var res1 = AlgorithmRunner.dfs(graph.getBaseMap().get(1), graph);
//        Graph<Integer> res2 = AlgorithmRunner.bfs(graph.getBaseMap().get(1), graph);
        GraphDrawer drawer = new GraphDrawer();
    }
}
