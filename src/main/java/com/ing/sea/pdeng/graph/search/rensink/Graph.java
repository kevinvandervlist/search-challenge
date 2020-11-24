package com.ing.sea.pdeng.graph.search.rensink;

import com.ing.sea.pdeng.graph.Vertex;
import com.ing.sea.pdeng.graph.search.javacompat.JHyperEdgeInfo;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Hypergraph
 * @author Arend Rensink
 *
 */
public class Graph {
    private final String name;
    private final Set<Vertex> nodes;
    private final Set<JHyperEdgeInfo> edges;

    public Graph(String name) {
        this.name = name;
        this.nodes = new LinkedHashSet<>();
        this.edges = new LinkedHashSet<>();
    }

    public String getName() {
        return this.name;
    }

    public Set<Vertex> getNodes() {
        return this.nodes;
    }

    public Set<JHyperEdgeInfo> getEdges() {
        return this.edges;
    }

    public boolean addNode(Vertex node) {
        return this.nodes.add(node);
    }

    public boolean addEdge(JHyperEdgeInfo edge) {
        return this.edges.add(edge);
    }

    @Override
    public String toString() {
        return String.format("Graph: (V: %s, E: %s)", nodes.size(), edges.size());
    }
}
