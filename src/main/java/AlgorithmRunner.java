import java.util.*;
import java.util.concurrent.Callable;


public class AlgorithmRunner {
    public Graph<Integer> exampleGraph;
    public AlgorithmRunner(){
        HashMap<Integer, Node<Integer>> originMap = new HashMap<>(){
            {
                put(1, (new Node<>(1, new ArrayList<>(Arrays.asList(2, 3, 4)))));
                put(2, (new Node<>(2, new ArrayList<>(Arrays.asList(3, 4)))));
                put(3, (new Node<>(3, new ArrayList<>(Arrays.asList(1, 2)))));
                put(4, (new Node<>(4, new ArrayList<>(Arrays.asList(1, 2)))));
            }
        };
        exampleGraph = new Graph<>(originMap);
    }

    public void runAlgorithm(Callable<ArrayList<ArrayList<Integer>>> func){

    }

    public Graph<Integer> dfs(Node<Integer> startVertex) {
        var resultGraph = new Graph<Integer>();
        Stack<Node<Integer>> stack = new Stack<>();
        stack.push(startVertex);
        var seenNodes = new HashSet<Integer>();
        seenNodes.add(startVertex.getValue());
        while (!stack.isEmpty()){
            var currentVertex = stack.peek();
            resultGraph.addNode(currentVertex);
            var allInSeen = true;
            for (Integer vertex : currentVertex.getAdjacency()) {
                if (!seenNodes.contains(vertex)) {
                    seenNodes.add(vertex);
                    stack.push(exampleGraph.getBaseMap().get(vertex));
                    resultGraph.getNode(currentVertex.getValue()).addAdjacency(vertex);
                    allInSeen = false;
                    break;
                }
            }

            if (allInSeen) stack.pop();
        }

        return resultGraph;
    }
}
