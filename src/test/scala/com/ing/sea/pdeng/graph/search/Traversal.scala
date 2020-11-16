package com.ing.sea.pdeng.graph.search

import com.ing.sea.pdeng.graph.Vertex
import scalax.collection.edge.WDiHyperEdge

import scala.language.implicitConversions

object Traversal {
  implicit def toTraversal(trace: Seq[WDiHyperEdge[Vertex]]): Traversal =
    Traversal(trace)
}

/*
  Wrapper around *ordered* List[WDiHyperEdge[Vertex]] to allow for equality checking
  that's based on *unordered* Set[WDiHyperEdge[Vertex]]
 */
case class Traversal(trace: Seq[WDiHyperEdge[Vertex]]) {
  val asSet = trace.toSet // order doesn't matter
  
  override def equals(other: Any): Boolean = other match {
    case that: Traversal => asSet == that.asSet
    case _ => false
  }

  override def hashCode(): Int = asSet.hashCode()
}                                                