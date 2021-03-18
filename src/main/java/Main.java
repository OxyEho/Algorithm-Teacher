import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
//        System.out.println("a");
        var algorithmRunner = new AlgorithmRunner<Integer>();
        var graph = new Graph<Integer>(new HashMap<>(){
            {
                put(1, (new Node<>(1, new ArrayList<>(Arrays.asList(2, 3, 4)))));
                put(2, (new Node<>(2, new ArrayList<>(Arrays.asList(3, 4)))));
                put(3, (new Node<>(3, new ArrayList<>(Arrays.asList(1, 2)))));
                put(4, (new Node<>(4, new ArrayList<>(Arrays.asList(1, 2)))));
            }
        });
        var res = algorithmRunner.dfs(graph.getBaseMap().get(1), graph);
    }
}
