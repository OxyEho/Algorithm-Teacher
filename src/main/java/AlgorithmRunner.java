import java.util.*;
import java.util.concurrent.Callable;


public class AlgorithmRunner {
    private static Graph<Integer> exampleGraph;
    private AlgorithmRunner(){
        HashMap<Integer, ArrayList<Integer>> originMap = new HashMap<>(){
            {
                put(1, new ArrayList<>(Arrays.asList(2, 3, 4)));
                put(2, new ArrayList<>(Collections.singletonList(1)));
                put(3, new ArrayList<>(Arrays.asList(1, 4)));
                put(4, new ArrayList<>(Arrays.asList(1, 3, 5)));
                put(5, new ArrayList<>(Collections.singletonList(4)));
            }
        };
        exampleGraph = new Graph<>(originMap);
    }
    public static void runAlgorithm(Callable<ArrayList<ArrayList<Integer>>> func){

    }
}
