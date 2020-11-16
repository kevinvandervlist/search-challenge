package com.ing.sea.pdeng.graph.search

import com.ing.sea.pdeng.graph.{CallableUnit, Type, Vertex}
import scalax.collection.edge.WDiHyperEdge

object ValidateSolution {
  def isValid(given: Set[Type], solution: Iterable[WDiHyperEdge[Vertex]]): Boolean = {
    val infos = solution.map(HyperEdgeInfo.apply[Vertex, Type, CallableUnit])
    val available = infos.map(_.out).toSet ++ `given`
    val notProducible = infos.flatMap(_.in).filterNot(available.contains)
    if (notProducible.nonEmpty) {
      println(notProducible)
    }
    notProducible.isEmpty
  }
}