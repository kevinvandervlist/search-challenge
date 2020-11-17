package com.ing.sea.pdeng.graph.search

import com.ing.sea.pdeng.graph.TraversalTools
import scalax.collection.GraphPredef.DiHyperEdgeLikeIn

trait TypeGraphSearch[V, DV <: V, FV <: V, EE[+X] <: DiHyperEdgeLikeIn[X], CC <: TraversalContext[V, DV, FV, EE]] extends TraversalTools[V, DV, FV, EE, CC] {
  type Solution = IndexedSeq[EdgeInfo]
  type Solutions = IndexedSeq[Solution]
//  /** Yield an iterable of traversals of the given graph, based on the constraints provided here */
  def traversals(ctx: C): LazyList[List[EE[V]]]
  def name: String = getClass.getSimpleName

  @inline
  protected def isValidSolution(trace: Solution, ctx: C): Boolean = {
    val available = trace.map(_.out) ++ ctx.`given`
    val notProducible = trace.flatMap(_.in).filterNot(available.contains)
    if(notProducible.nonEmpty) {
      println(notProducible)
    }
    notProducible.isEmpty
  }
}
