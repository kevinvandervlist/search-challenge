package com.ing.sea.pdeng.graph.search

import scalax.collection.edge.WDiHyperEdge

class EfficientSearchStrategyScala extends SearchChallenge {
  override def traversals(ctx: TraversalContext[V, DV, FV, WDiHyperEdge]): LazyList[List[WDiHyperEdge[V]]] = {
    // A set with the 'given' nodes known at the start of a search: ctx.given;
    // A 'target' node we want to reach: ctx.target
    // For example, to give all edges producing 'target', one can query:
    // predecessorEdgesContaining(ctx.target, ctx.graph)
    LazyList.empty
  }
}
