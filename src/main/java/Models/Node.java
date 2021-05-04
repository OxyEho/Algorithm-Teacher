package Models;

import java.util.ArrayList;
import java.util.Collection;

public class Node<T> {
    private final T value;
    private final ArrayList<T> adjacency;
    private int sequenceNumber;

    public Node(T value, ArrayList<T> adjacencyList) {
        this.value = value;
        adjacency = adjacencyList;
    }

    public ArrayList<T> getAdjacency(){ return adjacency; }

    public void addAdjacency(Collection<T> neighbours) {
        adjacency.addAll(neighbours);
    }

    public T getValue() { return value; }

    public int getSequenceNumber() { return sequenceNumber; }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

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
