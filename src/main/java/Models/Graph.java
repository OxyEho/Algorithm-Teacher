package Models;

import java.util.*;

public class Graph<T>{
    private final HashMap<T, Node<T>> baseMap;
    public Graph(HashMap<T, Node<T>> baseMap){
        this.baseMap = baseMap;
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
}
