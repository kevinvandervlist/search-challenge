package com.ing.sea.pdeng.graph

import com.ing.sea.pdeng.graph.search.{HyperEdgeInfo, TraversalContext}
import scalax.collection.Graph
import scalax.collection.GraphPredef.DiHyperEdgeLikeIn
import scalax.collection.GraphTraversal.{Direction, Predecessors, Successors}
import scalax.collection.edge.WDiHyperEdge

trait TraversalTools[V, DV <: V, FV <: V, EE[+X] <: DiHyperEdgeLikeIn[X], CC <: TraversalContext[V, DV, FV, EE]] {
  type C = CC
  type E = WDiHyperEdge[V]
  type EdgeInfo = HyperEdgeInfo[V, DV, FV]

  private def edgesContaining(node: V, direction: Direction, graph: Graph[V, EE]): Iterable[EE[V]] = graph.find(node) match {
    case None =>
      throw new IllegalStateException(s"Vertex '$node' does not exist in the graph -- cannot find edges containing that node")
    case Some(start) => start
      .outerEdgeTraverser
      .withDirection(direction)
      .withMaxDepth(1)
  }

  /** Within a graph, find all the successor edges containing a given node */
  protected def successorEdgesContaining(node: V, graph: Graph[V, EE]): Iterable[EE[V]] =
    edgesContaining(node, Successors, graph)
      // A successor is just that iff one of the input nodes is the given node
      .filter(_.dropRight(2).exists(_ == node))

  /** Within a graph, find all the predecessor edges containing a given node */
  protected def predecessorEdgesContaining(node: V, graph: Graph[V, EE]): Iterable[EE[V]] =
    edgesContaining(node, Predecessors, graph)
      // A predecessor is just that iff the output of an edge is the given node
      .filter(_.last == node)

  //  protected def predecessorEdgesContainingRaw(node: V, graph: Graph[V, EE]): Iterable[EE[V]] =
  //    edgesContaining(node, Predecessors, graph)
}
