package com.ing.sea.pdeng.graph.search.rensink;

import com.ing.sea.pdeng.graph.CallableUnit;
import com.ing.sea.pdeng.graph.Type;
import com.ing.sea.pdeng.graph.Vertex;
import com.ing.sea.pdeng.graph.search.javacompat.JContext;
import com.ing.sea.pdeng.graph.search.javacompat.JHyperEdgeInfo;
import com.ing.sea.pdeng.graph.search.javacompat.JSearchChallenge;
import scalax.collection.edge.WDiHyperEdge;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class RensinkSearchStrategyJava extends JSearchChallenge {
    @Override
    public Stream<List<WDiHyperEdge<Vertex>>> traversals_java(JContext ctx,
                                                              Function<Type, Iterable<JHyperEdgeInfo>> predecessor,
                                                              Function<Type, Iterable<JHyperEdgeInfo>> successor,
                                                              Supplier<Iterable<JHyperEdgeInfo>> allEdges) {
        Graph g = new Graph("SolveMe");
        // Create the graph for this search domain
        allEdges.get().forEach(e -> {
            g.addEdge(e);
            e.in.forEach(g::addNode);
            g.addNode(e.f);
            g.addNode(e.out);
        });

        // ... but make sure to add all givens as 0-arg'd callable units
        for(Type given : ctx.given) {
            g.addEdge(new JHyperEdgeInfo(
                    null,
                    Collections.emptyList(),
                    new CallableUnit(String.format("given-%s", given.name())),
                    given
            ));
        }

        MySearch ms = new MySearch(g);
        Iterator<Solution> solutionIterator = ms.search(ctx.target);
        Iterable<Solution> solutionIterable = () -> solutionIterator;
        return StreamSupport.stream(solutionIterable.spliterator(), false)
                .map(solution -> solution
                        .stream()
                        .filter(ei -> ei.edge != null) // Filter those, because they represent the 'given' types
                        .map(e -> e.edge)
                        .collect(Collectors.toList())
                );
    }

}
