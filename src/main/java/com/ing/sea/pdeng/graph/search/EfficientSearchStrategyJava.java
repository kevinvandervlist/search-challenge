package com.ing.sea.pdeng.graph.search;

import com.ing.sea.pdeng.graph.CallableUnit;
import com.ing.sea.pdeng.graph.Type;
import com.ing.sea.pdeng.graph.Vertex;
import com.ing.sea.pdeng.graph.search.javacompat.JContext;
import com.ing.sea.pdeng.graph.search.javacompat.JSearchChallenge;
import scalax.collection.edge.WDiHyperEdge;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class EfficientSearchStrategyJava extends JSearchChallenge {
    @Override
    public Stream<List<WDiHyperEdge<Vertex>>> traversals_java(JContext ctx,
                                                              Function<Type, Iterable<HyperEdgeInfo<Vertex, Type, CallableUnit>>> predecessor,
                                                              Function<Type, Iterable<HyperEdgeInfo<Vertex, Type, CallableUnit>>> successor,
                                                              Supplier<Iterable<HyperEdgeInfo<Vertex, Type, CallableUnit>>> allEdges) {
        // A set with the 'given' nodes known at the start of a search: ctx.given;
        // A 'target' node we want to reach: ctx.target
        // For example, to give all edges producing 'target', one can query:
        // predecessor.apply(ctx.target));
        // If you want to have all edges of the complet graph, invoke `allEdges`
        return Stream.empty();
    }
}
