public class Main {
    public static void main(String[] args) {
//        System.out.println("a");
        var algorithmRunner = new AlgorithmRunner();
        var res = algorithmRunner.dfs(algorithmRunner.exampleGraph.getBaseMap().get(1));
    }
}
