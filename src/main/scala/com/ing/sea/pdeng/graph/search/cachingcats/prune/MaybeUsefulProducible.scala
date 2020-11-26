package com.ing.sea.pdeng.graph.search.cachingcats.prune

import com.ing.sea.pdeng.graph.search.{DepthContext, HyperEdgeInfo}
import scalax.collection.Graph
import scalax.collection.edge.WDiHyperEdge

import scala.collection.mutable

/**
  * A really simple method to prune a graph:
  * 1) everything is assumed to be not useful/not producible
  * 2) Traverse and according to criteria you can see in the code,
  *   a node is upgraded to maybe(producible|useful). A 'demote' is not possible; you cannot
  *   go back in 'status'.
  * 3) Finally all edges with nodes that are maybe useful/producible are kept and
  *   all others are removed. This drastically reduces the search space.
  */
class MaybeUsefulProducible[V, DV <: V : Manifest, FV <: V : Manifest] extends Prune[V, DV, FV, WDiHyperEdge, DepthContext[V, DV, FV, WDiHyperEdge]] {

  override def prune(ctx: C): C =
    removeObsoleteEdges(ctx, buildState(ctx))

  private def buildState(ctx: C): PruneState[V, DV, FV] = {
    val g = ctx.graph
    val state = new PruneState[V, DV, FV]()
    val seen: mutable.Set[WDiHyperEdge[V]] = new mutable.HashSet()
    // Check if an edge is seen. If not, automatically mark is as such.
    @inline
    def notSeen(edge: WDiHyperEdge[V]): Boolean = {
      if(seen.contains(edge)) {
        false
      } else {
        seen.addOne(edge)
        true
      }
    }
    // First collect all edges that start with 'given'.
    val edges = ctx.`given`.flatMap(successorEdgesContaining(_, g))
    val stack: mutable.Stack[WDiHyperEdge[V]] = mutable.Stack()
    stack.addAll(edges)
    seen.addAll(edges)

    // mark initial nodes as useful
    ctx.`given` foreach state.maybeProducible
    state.maybeProducible(ctx.target)

    while(stack.nonEmpty) {
      val edge = HyperEdgeInfo.apply[V, DV, FV](stack.pop())
      if (edge.in.exists(state.isNotProducible)) {
        state.notUseful(edge.f)
      } else {
        state.maybeUseful(edge.f)
        state.maybeProducible(edge.out)
      }
      stack.addAll(successorEdgesContaining(edge.out, g).view.filter(notSeen))
    }

    state
  }

  private def removeObsoleteEdges(ctx: C, state: PruneState[V, DV, FV]): C =
    ctx.copy(graph = Graph(ctx.graph
      .edges
      .view
      .map(_.toOuter)
      .filter(state.keep)
      .toIndexedSeq: _*)
    )
}