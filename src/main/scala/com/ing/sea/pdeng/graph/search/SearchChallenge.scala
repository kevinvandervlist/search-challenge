package com.ing.sea.pdeng.graph.search

import java.{lang, util}

import com.ing.sea.pdeng.graph.{CallableUnit, Type, Vertex}
import scalax.collection.edge.WDiHyperEdge

abstract class SearchChallenge extends TypeGraphSearch[Vertex, Type, CallableUnit, WDiHyperEdge, TraversalContext[Vertex, Type, CallableUnit, WDiHyperEdge]] {
  type V = Vertex
  type DV = Type
  type FV = CallableUnit

  override def traversals(ctx: TraversalContext[Vertex, Type, CallableUnit, WDiHyperEdge]): lang.Iterable[util.List[WDiHyperEdge[Vertex]]]
}