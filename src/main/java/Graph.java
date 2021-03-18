import java.util.*;

public class Graph<T>{
    private HashMap<T, Node<T>> baseMap;
    public Graph(HashMap<T, Node<T>> baseMap){
        this.baseMap = baseMap;
    }
    public Graph(){ baseMap = new HashMap<>(); }

//    непонятно насколько нужен этот метод, у Node есть такой же
//    public ArrayList<T> getAdjacencyList(T value){
//        for (Node<T> node : baseMap.values()){
//            if (node.equals(value))
//                return node.getAdjacency();
//        }
//
//        return null;
//    }

    public void addNode(Node<T> node){
        if (!baseMap.containsKey(node.getValue())) {
            baseMap.put(node.getValue(), new Node<>(node.getValue(), new ArrayList<>()));
        }
    }

    public Node<T> getNode(T value){ return baseMap.get(value); }

    public HashMap<T, Node<T>> getBaseMap() {
        return baseMap;
    }

    public ArrayList<Node<T>> getNodeList(){ return (ArrayList<Node<T>>) baseMap.values(); }
}
