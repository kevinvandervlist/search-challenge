package com.ing.sea.pdeng.graph.search.javacompat

import java.util

import com.ing.sea.pdeng.graph.{CallableUnit, Type, Vertex}
import com.ing.sea.pdeng.graph.search.HyperEdgeInfo
import scalax.collection.edge.WDiHyperEdge
import java.util.function.{Function, Supplier}

abstract class JSearchChallenge {
  def traversals_java(ctx: JContext,
                      predecessor: Function[Type, java.lang.Iterable[HyperEdgeInfo[Vertex, Type, CallableUnit]]],
                      successor: Function[Type, java.lang.Iterable[HyperEdgeInfo[Vertex, Type, CallableUnit]]],
                      allEdges: Supplier[java.lang.Iterable[HyperEdgeInfo[Vertex, Type, CallableUnit]]]
                     ): java.util.stream.Stream[util.List[WDiHyperEdge[Vertex]]]
}
