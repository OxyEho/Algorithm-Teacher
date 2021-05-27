package Models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Node<T> {
    private final T value;
    private final HashMap<T, Double> adjacency;
    private int sequenceNumber;

    public Node() {
        super();
        value = null;
        adjacency = null;
    }

    public Node(T value, HashMap<T, Double> adjacencyList) {
        this.value = value;
        adjacency = adjacencyList;
    }

    public HashMap<T, Double> getAdjacency(){ return adjacency; }

    public void addAdjacency(Map<T, Double> neighbours) {
        for (T neighbour: neighbours.keySet()) {
            adjacency.put(neighbour, neighbours.get(neighbour));
        }
    }

    public T getValue() { return value; }

    public int getSequenceNumber() { return sequenceNumber; }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public static <T> void nodeSerialize(Node<T> node, String filename){
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            mapper.writeValue(new File(filename), node);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Node nodeDeserialize(String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(fileName), Node.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
