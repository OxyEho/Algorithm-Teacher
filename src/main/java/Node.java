import java.util.ArrayList;
import java.util.Objects;

public class Node<T> {
    private T value;
    private T father;
    private ArrayList<T> adjacency;

    public Node(T value, ArrayList<T> adjacencyList) {
        this.value = value;
        adjacency = adjacencyList;
        father = null;
    }

    public void setFather(T father) { this.father = father; }

    public T getFather() { return father; }

    public ArrayList<T> getAdjacency(){ return adjacency; }

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
    public int hashCode() {
        return value.hashCode();
    }
}
