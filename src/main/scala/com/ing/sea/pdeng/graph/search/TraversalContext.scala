package com.ing.sea.pdeng.graph.search

import scalax.collection.Graph
import scalax.collection.GraphPredef.DiHyperEdgeLikeIn

/* Captures the *static* context of a traversal */
trait TraversalContext[V, DV <: V, FV <: V, E[+X] <: DiHyperEdgeLikeIn[X]] {
  def graph: Graph[V, E]
  def target: DV
  def given: Set[DV]
}
