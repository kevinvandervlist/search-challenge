package com.ing.sea.pdeng.graph.search

import scalax.collection.edge.WDiHyperEdge

import scala.runtime.ScalaRunTime

abstract class HyperEdgeInfo[V, DV <: V : Manifest, FV <: V : Manifest] {
  def edge: WDiHyperEdge[V]

  def in: IndexedSeq[DV] = Vector.empty

  def f: FV = null.asInstanceOf[FV]

  def out: DV = null.asInstanceOf[DV]
}

object HyperEdgeInfo {
  def apply[V, DV <: V : Manifest, FV <: V : Manifest](edge: WDiHyperEdge[V]): HyperEdgeInfo[V, DV, FV] = {
    val nodes: IndexedSeq[V] = edge.toVector
    new HyperEdgeInfoImpl[V, DV, FV](
      edge,
      nodes.slice(0, nodes.length - 2).asInstanceOf[IndexedSeq[DV]],
      nodes(nodes.length - 2).asInstanceOf[FV],
      nodes(nodes.length - 1).asInstanceOf[DV],
    )
  }
}

private class HyperEdgeInfoImpl[V, DV <: V : Manifest, FV <: V : Manifest](override val edge: WDiHyperEdge[V], override val in: IndexedSeq[DV] = Vector.empty, override val f: FV = null.asInstanceOf[FV], override val out: DV = null.asInstanceOf[DV]) extends HyperEdgeInfo[V, DV, FV] {
  private lazy val cachedHashCode = ScalaRunTime._hashCode(edge)

  def allVerticesInEdge: Set[V] = edge.toSet

  override def hashCode(): Int = cachedHashCode

  override def equals(obj: Any): Boolean = obj match {
    case e: HyperEdgeInfo[V, DV, FV] => e.edge.equals(edge)
    case _ => false
  }

  override def toString: String = s"def ${f.toString}(${in.map(_.toString).mkString(",")}): ${out.toString}"
}
