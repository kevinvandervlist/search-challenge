package com.ing.sea.pdeng.graph.search;

import com.ing.sea.pdeng.graph.CallableUnit;
import com.ing.sea.pdeng.graph.Type;
import com.ing.sea.pdeng.graph.Vertex;
import com.ing.sea.pdeng.graph.search.javacompat.JContext;
import com.ing.sea.pdeng.graph.search.javacompat.JSearchChallenge;
import scala.Function1;
import scalax.collection.edge.WDiHyperEdge;

import java.util.ArrayList;
import java.util.List;

public class EfficientSearchStrategyJava extends JSearchChallenge {
    @Override
    public Iterable<List<WDiHyperEdge<Vertex>>> traversals_java(JContext ctx,
                                                                Function1<Type, Iterable<HyperEdgeInfo<Vertex, Type, CallableUnit>>> predecessor,
                                                                Function1<Type, Iterable<HyperEdgeInfo<Vertex, Type, CallableUnit>>> successor) {
        // A set with the 'given' nodes known at the start of a search: ctx.given;
        // A 'target' node we want to reach: ctx.target
        // For example, to give all edges producing 'target', one can query:
        // predecessor.apply(ctx.target));
        return new ArrayList<>();
    }
}
