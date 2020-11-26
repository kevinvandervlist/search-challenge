package com.ing.sea.pdeng.graph.search.cachingcats.prune

import com.ing.sea.pdeng.graph.search.{DepthContext, HyperEdgeInfo}
import scalax.collection.Graph
import scalax.collection.edge.WDiHyperEdge

import scala.collection.immutable.Queue
import scala.collection.mutable

class OverApproximate[V, DV <: V : Manifest, FV <: V : Manifest] extends Prune[V, DV, FV, WDiHyperEdge, DepthContext[V, DV, FV, WDiHyperEdge]] {
  override def prune(ctx: C): C =
    removeObsoleteEdges(ctx, buildState(ctx))

  case class Position(edge: WDiHyperEdge[V],
                      trace: Queue[HyperEdgeInfo[V, DV, FV]],
                      seen: Queue[DV])

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

    @inline
    def hasSeen(edge: WDiHyperEdge[V]): Boolean = {
      seen.contains(edge)
    }

    @inline
    def allInElementsAreGoals(thisEdge: EdgeInfo): Boolean =
      thisEdge.in forall ctx.given.contains

    @inline
    def optimizedEdgeEquals(other: EdgeInfo, thisEdge: EdgeInfo): Boolean =
      other.hashCode() == thisEdge.hashCode() && other == thisEdge

    @inline
    def isEdgeCycle(thisEdge: EdgeInfo): Boolean =
      ctx.trace.count(optimizedEdgeEquals(thisEdge, _)) >= 2

    @inline
    def everythingIsSeen(thisEdge: EdgeInfo, seen: Queue[DV]): Boolean =
      thisEdge.in forall seen.contains

    @inline
    def markTraceAsMaybe(updatedPos: Position): Unit = updatedPos.trace.foreach { e =>
      state.maybeProducible(e.out)
      state.maybeUseful(e.f)
    }

    // First collect all edges that start with 'target'.
    val stack: mutable.Stack[Position] = mutable.Stack()
    stack.addAll(predecessorEdgesContaining(ctx.target, g).map(Position.apply(_, Queue.empty, Queue.empty)))

    // mark initial nodes as useful
    ctx.`given` foreach state.maybeProducible
    state.maybeProducible(ctx.target)

    while(stack.nonEmpty) {
      val pos = stack.pop()
      val edge = HyperEdgeInfo.apply[V, DV, FV](pos.edge)

      val updatedPos = pos.copy(
        trace = pos.trace.appended(edge),
        seen = pos.seen.appended(edge.out)
      )

      if(state.isMaybeUseful(edge.f) || allInElementsAreGoals(edge)) {
        markTraceAsMaybe(updatedPos)
      } else {
        var stop = false
        // A cycle based on edges, so this path won't yield a solution
        if (isEdgeCycle(edge)) {
          stop = true
        }
        // Another cycle, but based on the fact that all types are seen
        // Don't store this as a cycle can be caused by any number of traces, so no need
        // to disable this opportunity for other traversals
        if (everythingIsSeen(edge, pos.seen)) {
          stop = true
        }
        if(! stop) {
          val preds = edge.in.flatMap(predecessorEdgesContaining(_, g))
          val seen = preds.filter(hasSeen)
          if(seen.nonEmpty) {
            // We encountered an edge we saw before, so mark the trace as maybe useful
            markTraceAsMaybe(updatedPos)
          }
          stack.addAll(preds
            .filter(notSeen)
            .map(e => updatedPos.copy(edge = e))
          )
        }
      }
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