package com.ing.sea.pdeng.graph.search

import scalax.collection.Graph
import scalax.collection.GraphPredef.DiHyperEdgeLikeIn

import scala.collection.immutable.Queue


/* Captures the dynamic and static context of a traversal */
case class DepthContext[V, DV <: V, FV <: V, E[+X] <: DiHyperEdgeLikeIn[X]]
  (
    /* always required */
    graph: Graph[V, E],
    target: DV,
    given: Set[DV],

    /* feel free to edit additional properties if you think that's required */
    maxDepth: Int = Integer.MAX_VALUE,
    seen: Set[DV] = Set.empty[DV],
    depth: Int = 0,
    trace: Queue[HyperEdgeInfo[V, DV, FV]] = Queue.empty,
  )
  extends TraversalContext[V, DV, FV, E]

