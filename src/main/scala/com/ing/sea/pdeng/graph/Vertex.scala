package com.ing.sea.pdeng.graph

import scalax.collection.GraphEdge
import scalax.collection.edge.WDiHyperEdge

object Vertex {
  def e(vertices: Vertex*): WDiHyperEdge[Vertex] =
    WDiHyperEdge(vertices.toList)(1)(GraphEdge.Sequence)

  def t(name: String*): Type = Type(name.mkString("."))

  def cu(name: String*): CallableUnit = CallableUnit(name.mkString("."))
}

sealed trait Vertex {
  def name: String
}
case class Type(name: String) extends Vertex
case class CallableUnit(name: String) extends Vertex
