package Models;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class Graph<T>{
    private final HashMap<T, Node<T>> baseMap;
    private HashMap<Pair<T, T>, Double> edges;
    private final boolean isDirected;
    private final boolean isWeighted;

    public Graph(HashMap<T, Node<T>> baseMap, boolean isDirected, boolean isWeighted){
        this.baseMap = baseMap;
        this.edges = makeEdges();
        this.isDirected = isDirected;
        this.isWeighted = isWeighted;
    }

    public Graph(boolean isDirected, boolean isWeighted) {
        baseMap = new HashMap<>();
        this.isDirected = isDirected;
        this.isWeighted = isWeighted;
    }

    public boolean isWeighted() { return isWeighted; }

    public boolean isDirected() { return isDirected; }

    public void addNode(Node<T> node, Map<T, Double> neighbours) {
        if (!baseMap.containsKey(node.getValue())) {
            baseMap.put(node.getValue(), new Node<>(node.getValue(), new HashMap<>(neighbours)));
        }
        else baseMap.get(node.getValue()).addAdjacency(neighbours);
    }

    public void addNode(Node<T> node) {
        if (!baseMap.containsKey(node.getValue())) {
            baseMap.put(node.getValue(), new Node<>(node.getValue(), new HashMap<>()));
        }
    }

    public Node<T> getNode(T value){ return baseMap.get(value); }

    public HashMap<T, Node<T>> getBaseMap() { return baseMap; }

    public Collection<Node<T>> getNodeList() { return baseMap.values(); }

    public HashMap<Pair<T, T>, Double> getEdges() { return edges; }

    public HashMap<Pair<T, T>, Double> makeEdges() {
        HashMap<Pair<T, T>, Double> edges = new HashMap<>();
        for (var node : getNodeList()) {
            for (var neighbour : node.getAdjacency().keySet()) {
                // Важно понять, не запутались ли мы в весах
                edges.put(Pair.of(node.getValue(), neighbour), node.getAdjacency().get(neighbour));
            }
        }
        return edges;
    }
}
