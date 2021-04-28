package Models;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class Graph<T>{
    private final HashMap<T, Node<T>> baseMap;
    private ArrayList<Pair<T, T>> edges;

    public Graph(HashMap<T, Node<T>> baseMap){
        this.baseMap = baseMap;
        this.edges = makeEdges();
    }

    public Graph(){ baseMap = new HashMap<>(); }


    public void addNode(Node<T> node, Collection<T> neighbours){
        if (!baseMap.containsKey(node.getValue())) {
            baseMap.put(node.getValue(), new Node<>(node.getValue(), new ArrayList<>(neighbours)));
        }
        else baseMap.get(node.getValue()).addAdjacency(neighbours);
    }

    public void addNode(Node<T> node){
        if (!baseMap.containsKey(node.getValue())) {
            baseMap.put(node.getValue(), new Node<>(node.getValue(), new ArrayList<>()));
        }
    }

    public Node<T> getNode(T value){ return baseMap.get(value); }

    public HashMap<T, Node<T>> getBaseMap() {
        return baseMap;
    }

    public Collection<Node<T>> getNodeList(){ return baseMap.values(); }

    public ArrayList<Pair<T, T>> getEdges(){
        return edges;
    }

    public ArrayList<Pair<T, T>> makeEdges(){
        ArrayList<Pair<T, T>> edges = new ArrayList<>();
        for (var node : getNodeList()){
            for (var neighbour : node.getAdjacency()){
                edges.add(Pair.of(node.getValue(), neighbour));
            }
        }
        return edges;
    }
}
