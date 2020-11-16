package com.ing.sea.pdeng.graph.search.javacompat

import java.util

import com.ing.sea.pdeng.graph.{CallableUnit, Type, Vertex}
import com.ing.sea.pdeng.graph.search.HyperEdgeInfo
import scalax.collection.edge.WDiHyperEdge

abstract class JSearchChallenge {
  def traversals_java(ctx: JContext,
                      predecessor: Type => java.lang.Iterable[HyperEdgeInfo[Vertex, Type, CallableUnit]],
                      successor: Type => java.lang.Iterable[HyperEdgeInfo[Vertex, Type, CallableUnit]]): java.lang.Iterable[util.List[WDiHyperEdge[Vertex]]]
}
