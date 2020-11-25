package com.ing.sea.pdeng.graph.search.rensink;

import com.ing.sea.pdeng.graph.Vertex;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author Arend Rensink
 */
public class MySearch implements Search {
    private final GraphFacade gf;

    public MySearch(Graph graph) {
        this.gf = pruneGraph(new GraphFacade(graph));
    }

    public Graph getGraph() {
        return this.gf.getGraph();
    }

    @Override
    public Iterator<Solution> search(Vertex product) {
        return new MySearchInstance(this.gf, product);
    }

    private GraphFacade pruneGraph(GraphFacade gf) {
        Graph g = new Graph(gf.getName());
        gf.getEdges().stream().filter(e -> e.in.isEmpty()).forEach(g::addEdge);
        g.getEdges().stream().map(e -> e.out).forEach(g::addNode);
        LinkedList<Vertex> reachable = new LinkedList<>(g.getNodes());
        while (!reachable.isEmpty()) {
            Vertex next = reachable.remove();
            gf.getOutEdgeMap()
                    .get(next)
                    .stream()
                    .filter(e -> g.getNodes().containsAll(e.in))
                    .filter(e -> !e.in.contains(e.out))
                    .forEach(e -> {
                        if (g.getNodes().add(e.out)) {
                            reachable.add(e.out);
                        }
                        g.addEdge(e);
            });
        }
        GraphFacade result = new GraphFacade(g);
//        log("Result of pruning %s", gf.getName());
//        log("Original graph: %s nodes, %s edges", gf.getNodes().size(), gf.getEdges().size());
//        log("Pruned graph: %s nodes, %s edges", result.getNodes().size(), result.getEdges().size());
//        log("Max depth: %s", result.getNodeDepthMap().values().stream().reduce(0, Integer::max));
        return result;
    }
}
