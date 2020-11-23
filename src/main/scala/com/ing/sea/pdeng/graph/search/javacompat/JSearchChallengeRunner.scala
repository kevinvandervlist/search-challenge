package com.ing.sea.pdeng.graph.search.javacompat

import java.util.function.Supplier

import com.ing.sea.pdeng.graph.{CallableUnit, Type, Vertex}
import com.ing.sea.pdeng.graph.search.{HyperEdgeInfo, SearchChallenge, TraversalContext}
import scalax.collection.Graph
import scalax.collection.edge.WDiHyperEdge

import scala.jdk.CollectionConverters._
import scala.jdk.StreamConverters._

class JSearchChallengeRunner(search: JSearchChallenge) extends SearchChallenge {
  def predecessor(t: Type, g: Graph[Vertex, WDiHyperEdge]): java.lang.Iterable[HyperEdgeInfo[Vertex, Type, CallableUnit]] =
    predecessorEdgesContaining(t, g).map(HyperEdgeInfo.apply[Vertex, Type, CallableUnit]).asJava

  def successor(t: Type, g: Graph[Vertex, WDiHyperEdge]): java.lang.Iterable[HyperEdgeInfo[Vertex, Type, CallableUnit]] =
    successorEdgesContaining(t, g).map(HyperEdgeInfo.apply[Vertex, Type, CallableUnit]).asJava

  def allEdges(g: Graph[Vertex, WDiHyperEdge]): Supplier[java.lang.Iterable[HyperEdgeInfo[Vertex, Type, CallableUnit]]] =
    () => g
      .edges
      .map(_.toOuter)
      .map(HyperEdgeInfo.apply[Vertex, Type, CallableUnit])
      .asJava

  override def traversals(ctx: TraversalContext[V, DV, FV, WDiHyperEdge]): LazyList[List[WDiHyperEdge[V]]] = {
    val javaResult = search.traversals_java(
      new JContext(ctx.target, ctx.`given`.asJava),
      predecessor(_, ctx.graph),
      successor(_, ctx.graph),
      allEdges(ctx.graph)
    )
    javaResult.toScala(LazyList).map(_.asScala.toList)
  }
}
