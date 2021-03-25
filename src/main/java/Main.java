import Controller.DrawerController;
import Models.AlgorithmRunner;
import Models.Graph;
import Models.Node;
import View.GraphDrawer;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        var graph = new Graph<String>(new HashMap<>(){
            {
                put("a", (new Node<>("a", new ArrayList<>(Arrays.asList("b", "c", "d", "e")))));
                put("b", (new Node<>("b", new ArrayList<>(Arrays.asList("a", "c", "d", "e")))));
                put("c", (new Node<>("c", new ArrayList<>(Arrays.asList("b", "a", "d", "e")))));
                put("d", (new Node<>("d", new ArrayList<>(Arrays.asList("b", "c", "a", "e")))));
                put("e", (new Node<>("e", new ArrayList<>(Arrays.asList("b", "c", "d", "a")))));
//                put(1, (new Node<>(1, new ArrayList<>(Arrays.asList(2, 3, 4)))));
//                put(2, (new Node<>(2, new ArrayList<>(Arrays.asList(3, 4, 5)))));
//                put(3, (new Node<>(3, new ArrayList<>(Arrays.asList(1, 2)))));
//                put(4, (new Node<>(4, new ArrayList<>(Arrays.asList(1, 2)))));
//                put(5, (new Node<>(5, new ArrayList<>(Collections.singletonList(2)))));
//                put(6, (new Node<>(6, new ArrayList<>(Arrays.asList(1, 2)))));
//                put(7, (new Node<>(7, new ArrayList<>(Arrays.asList(1, 2)))));
//                put(8, (new Node<>(8, new ArrayList<>(Arrays.asList(1, 2)))));
//                put(9, (new Node<>(9, new ArrayList<>(Arrays.asList(1, 2)))));
//                put(10, (new Node<>(10, new ArrayList<>(Arrays.asList(1, 2)))));
//                put(11, (new Node<>(11, new ArrayList<>(Arrays.asList(1, 2)))));
//                put(12, (new Node<>(12, new ArrayList<>(Arrays.asList(1, 2)))));
            }
        });
//        var res1 = AlgorithmRunner.dfs(graph.getBaseMap().get(1), graph);
//        Graph<Integer> res2 = AlgorithmRunner.bfs(graph.getBaseMap().get(1), graph);
        DrawerController controller = new DrawerController(graph);
        GraphDrawer drawer = new GraphDrawer(controller);
        drawer.setVisible(true);
    }
}
