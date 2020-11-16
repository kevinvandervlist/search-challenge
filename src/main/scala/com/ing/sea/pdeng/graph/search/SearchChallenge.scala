package com.ing.sea.pdeng.graph.search

import com.ing.sea.pdeng.graph.{CallableUnit, Type, Vertex}
import scalax.collection.edge.WDiHyperEdge

trait SearchChallenge extends TypeGraphSearch[Vertex, Type, CallableUnit, WDiHyperEdge, DepthContext[Vertex, Type, CallableUnit, WDiHyperEdge]] {
  type V = Vertex
  type DV = Type
  type FV = CallableUnit
}