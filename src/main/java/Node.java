import java.util.ArrayList;
import java.util.Objects;

public class Node<T> {
    private T value;
    private ArrayList<T> adjacency;

    public Node(T value, ArrayList<T> adjacencyList) {
        this.value = value;
        adjacency = adjacencyList;
    }

    public ArrayList<T> getAdjacency(){ return adjacency; }

    public void addAdjacency(T neighbour){ adjacency.add(neighbour); }

    public T getValue() { return value; }

    @Override
    public String toString() { return value.toString(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node<?> node = (Node<?>) o;
        return value.equals(node.value);
    }

    @Override
    public int hashCode() { return value.hashCode(); }
}
