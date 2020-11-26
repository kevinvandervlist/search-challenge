package com.ing.sea.pdeng.graph.search.cachingcats.prune

import com.ing.sea.pdeng.graph.TraversalTools
import com.ing.sea.pdeng.graph.search.TraversalContext
import scalax.collection.GraphPredef.DiHyperEdgeLikeIn

trait Prune[V, DV <: V, FV <: V, EE[+X] <: DiHyperEdgeLikeIn[X], CC <: TraversalContext[V, DV, FV, EE]] extends TraversalTools[V, DV, FV, EE, CC] {
  def prune(ctx: C): C
}