package com.ing.sea.pdeng.graph.search.javacompat;

import com.ing.sea.pdeng.graph.CallableUnit;
import com.ing.sea.pdeng.graph.Type;
import com.ing.sea.pdeng.graph.Vertex;
import scalax.collection.edge.WDiHyperEdge;

import java.util.List;

public class JHyperEdgeInfo {
    /** A leaky abstraction; here we *do* leak a Scala type */
    public final WDiHyperEdge<Vertex> edge;
    public final List<Type> in;
    public final CallableUnit f;
    public final Type out;

    public JHyperEdgeInfo(WDiHyperEdge<Vertex> edge, List<Type> in, CallableUnit f, Type out) {
        this.edge = edge;
        this.in = in;
        this.f = f;
        this.out = out;
    }
}
