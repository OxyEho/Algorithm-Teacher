import java.util.*;

public class Graph<T>{
    private ArrayList<Node<T>> baseList;
    public Graph(ArrayList<Node<T>> baseMap){
        this.baseList = baseMap;
    }

    public ArrayList<T> getAdjacencyList(T value){
        for (Node<T> node : baseList){
            if (node.equals(value))
                return node.getAdjacency();
        }

        return null;
    }

    public ArrayList<Node<T>> getNodeList(){ return baseList; }
}
