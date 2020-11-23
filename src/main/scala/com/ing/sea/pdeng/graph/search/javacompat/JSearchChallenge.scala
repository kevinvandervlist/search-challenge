package com.ing.sea.pdeng.graph.search.javacompat

import java.util

import com.ing.sea.pdeng.graph.{Type, Vertex}
import scalax.collection.edge.WDiHyperEdge
import java.util.function.{Function, Supplier}

abstract class JSearchChallenge {
  def traversals_java(ctx: JContext,
                      predecessor: Function[Type, java.lang.Iterable[JHyperEdgeInfo]],
                      successor: Function[Type, java.lang.Iterable[JHyperEdgeInfo]],
                      allEdges: Supplier[java.lang.Iterable[JHyperEdgeInfo]]
                     ): java.util.stream.Stream[util.List[WDiHyperEdge[Vertex]]]
}
