package Models;

import java.util.*;
import java.util.concurrent.Callable;


public class AlgorithmRunner {
    private AlgorithmRunner() { }
    public void runAlgorithm(Callable<ArrayList<ArrayList<Integer>>> func){

    }

    public static <T> Graph<T> dfs(Node<T> startVertex, Graph<T> graph) {
        var resultGraph = new Graph<T>();
        Stack<Node<T>> stack = new Stack<>();
        stack.push(startVertex);
        var seenVertices = new HashSet<T>();
        seenVertices.add(startVertex.getValue());
        while (!stack.isEmpty()){
            var currentVertex = stack.peek();
            resultGraph.addNode(currentVertex);
            var allInSeen = true;
            for (T vertex : currentVertex.getAdjacency()) {
                if (!seenVertices.contains(vertex)) {
                    seenVertices.add(vertex);
                    stack.push(graph.getBaseMap().get(vertex));
                    resultGraph.addNode(currentVertex, Collections.singletonList(vertex));
                    allInSeen = false;
                    break;
                }
            }

            if (allInSeen) stack.pop();
        }

        return resultGraph;
    }

    public static <T> Graph<T> bfs(Node<T> startVertex, Graph<T> graph) {
        var resultGraph = new Graph<T>();
        ArrayDeque<Node<T>> queue = new ArrayDeque<>();
        queue.add(startVertex);
        var seenVertices = new HashSet<T>();
        seenVertices.add(startVertex.getValue());
        while (!queue.isEmpty()){
            var currentVertex = queue.peek();
            resultGraph.addNode(currentVertex);
            for (T vertex : currentVertex.getAdjacency()) {
                if (!seenVertices.contains(vertex)) {
                    seenVertices.add(vertex);
                    queue.add(graph.getBaseMap().get(vertex));
                    resultGraph.addNode(currentVertex, Collections.singletonList(vertex));
                }
            }

            queue.poll();
        }

        return resultGraph;
    }
}