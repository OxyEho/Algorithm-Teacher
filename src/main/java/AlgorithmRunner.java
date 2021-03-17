import java.util.*;
import java.util.concurrent.Callable;


public class AlgorithmRunner {
    private static Graph<Integer> exampleGraph;
    private AlgorithmRunner(){
        HashMap<Node<Integer>, ArrayList<Integer>> originMap = new HashMap<>(){
            {
                put(1, new ArrayList<>(Arrays.asList(2, 3, 4)));
                put(new Node<>(2), new ArrayList<>(Collections.singletonList(1)));
                put(new Node<>(3), new ArrayList<>(Arrays.asList(1, 4)));
                put(new Node<>(4), new ArrayList<>(Arrays.asList(1, 3, 5)));
                put(new Node<>(5), new ArrayList<>(Collections.singletonList(4)));
            }
        };
        exampleGraph = new Graph<>(originMap);
    }
    public static void runAlgorithm(Callable<ArrayList<ArrayList<Integer>>> func){

    }

    private void dfs(Node<Integer> startVertex) {
        Stack<Node<Integer>> stack = new Stack<>();
        stack.push(startVertex);
        Node<Integer> currentVertex = stack.peek();
        while (!stack.isEmpty()){

        }
    }
}
