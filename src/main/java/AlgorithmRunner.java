import java.util.*;
import java.util.concurrent.Callable;


public class AlgorithmRunner<T> {
    public void runAlgorithm(Callable<ArrayList<ArrayList<Integer>>> func){

    }

    public Graph<T> dfs(Node<T> startVertex, Graph<T> graph) {
        var resultGraph = new Graph<T>();
        Stack<Node<T>> stack = new Stack<>();
        stack.push(startVertex);
        var seenNodes = new HashSet<T>();
        seenNodes.add(startVertex.getValue());
        while (!stack.isEmpty()){
            var currentVertex = stack.peek();
            resultGraph.addNode(currentVertex);
            var allInSeen = true;
            for (T vertex : currentVertex.getAdjacency()) {
                if (!seenNodes.contains(vertex)) {
                    seenNodes.add(vertex);
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
}
