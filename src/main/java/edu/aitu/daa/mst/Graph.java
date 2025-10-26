package edu.aitu.daa.mst;

import java.util.*;

public class Graph {
    public final List<String> labels;
    public final List<Edge> edges;
    public final Map<String, Integer> index_of;
    public final int n;
    public Graph(List<String> labels, List<Edge> edges) {
        this.labels = new ArrayList<>(labels);
        this.edges = new ArrayList<>(edges);
        this.index_of = new HashMap<>();
        for (int i = 0; i < labels.size(); i++) {
            index_of.put(labels.get(i), i);
        }
        this.n = labels.size();
    }
    public List<List<Edge>> toAdj() {
        List<List<Edge>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (Edge e : edges) {
            adj.get(e.u).add(e);
            adj.get(e.v).add(new Edge(e.v, e.u, e.w));
        }
        return adj;
    }
}
