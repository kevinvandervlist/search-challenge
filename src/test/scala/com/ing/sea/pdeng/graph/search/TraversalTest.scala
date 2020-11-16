package com.ing.sea.pdeng.graph.search

import com.ing.sea.pdeng.graph.{CallableUnit, Type, Vertex}
import scalax.collection.Graph
import scalax.collection.edge.WDiHyperEdge
import scalax.collection.io.dot.{DotEdgeStmt, DotRootGraph, HyperEdgeTransformer, Id, NodeId}
import scalax.collection.io.dot._
import scala.jdk.CollectionConverters._

object TraversalTest {
  def random(g: Graph[Vertex, WDiHyperEdge], expected: List[List[WDiHyperEdge[Vertex]]]): TraversalTest = {
    val rand = new java.util.Random()

    val nodes = g.nodes.map(_.toOuter)
      .filter(_.isInstanceOf[Type])
      .toList.asInstanceOf[List[Type]]

    val randomTarget = nodes(rand.nextInt(nodes.length))

    val randomAvailable = (0 to rand.nextInt(10))
      .map(_ => nodes(rand.nextInt(nodes.length)))
      .toSet

    new TraversalTest {
      override val target: Type = randomTarget
      override val available: Set[Type] = randomAvailable
      override val graph: Graph[Vertex, WDiHyperEdge] = g
      override val expectedResults: List[List[WDiHyperEdge[Vertex]]] = expected
      override val maxDepth: Int = Integer.MAX_VALUE
    }
  }
}

trait TraversalTest {
  val target: Type
  val available: Set[Type]
  val graph: Graph[Vertex, WDiHyperEdge]
  val expectedResults: List[List[WDiHyperEdge[Vertex]]]
  val maxDepth: Int
  def traversals(s: TypeGraphSearch[Vertex, Type, CallableUnit, WDiHyperEdge, DepthContext[Vertex, Type, CallableUnit, WDiHyperEdge]]): Iterable[Seq[WDiHyperEdge[Vertex]]] =
    s.traversals(DepthContext[Vertex, Type, CallableUnit, WDiHyperEdge](graph, target, available, maxDepth)).asScala.map(_.asScala.toSeq)

  def graphDot: String = {
    graph.toDot(
      root,
      edgeTransformer = _ => None,
      hyperEdgeTransformer,
      connectedNodeTransformer,
      isolatedNodeTransformer
    )
  }

  def expectedDots: List[String] = expectedResults.map(p => {
    val g: Graph[Vertex, WDiHyperEdge] = Graph(p: _*)
    g.toDot(
      root,
      edgeTransformer = _ => None,
      hyperEdgeTransformer,
      connectedNodeTransformer,
      isolatedNodeTransformer
    )
  })

  def actualDots(found: Iterable[Seq[WDiHyperEdge[Vertex]]]): Iterable[String] = found.map(p => {
    val g: Graph[Vertex, WDiHyperEdge] = Graph(p: _*)
    g.toDot(
      root,
      edgeTransformer = _ => None,
      hyperEdgeTransformer,
      connectedNodeTransformer,
      isolatedNodeTransformer
    )
  })

  // Everything below is related to creating dot files

  private def root = DotRootGraph(
    graph.isDirected,
    Some(Id("TypeGraph"))
  )

  private def id(vertex: Vertex): NodeId = vertex match {
    case Type(name) => NodeId(name)
    case CallableUnit(name) => NodeId(name)
  }


  private def hyperEdgeTransformer: Option[HyperEdgeTransformer[Vertex, WDiHyperEdge]] = Some(h => {
    if(h.size == 2) {
      // It is a HyperEdge type, but only has one source and one target
      val sources = h.edge.sources.map(_.value).toList
      val targets = h.edge.targets.map(_.value).toList
      val source = id(sources.head)
      val target = id(targets.head)
      List(root -> DotEdgeStmt(source, target))
    } else {
      // This is an actual hyperedge. We need to create several edges for dot here:
      // - each argument -> f
      // - f -> ret
      val info: HyperEdgeInfo[Vertex, Type, CallableUnit] = HyperEdgeInfo(h.toOuter)
      info.in.map(source => {
        root -> DotEdgeStmt(id(source), id(info.f))
      }) :+ root -> DotEdgeStmt(id(info.f), id(info.out))
    }
  })

  private def connectedNodeTransformer: Option[NodeTransformer[Vertex, WDiHyperEdge]] = {
    Some(n => {
      Some(root -> DotNodeStmt(id(n.value), List.empty))
    })
  }

  private def isolatedNodeTransformer: Option[NodeTransformer[Vertex, WDiHyperEdge]] = None
}