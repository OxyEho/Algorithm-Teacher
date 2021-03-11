import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Graph<T> {
    private HashMap<T, ArrayList<T>> baseMap;
    public Graph(HashMap<T, ArrayList<T>> baseMap){
        this.baseMap = baseMap;
    }

    public ArrayList<T> getAdjacencyList(T value){
        return baseMap.get(value);
    }

    public Set<T> getNodeList(){
        return baseMap.keySet();
    }
}
