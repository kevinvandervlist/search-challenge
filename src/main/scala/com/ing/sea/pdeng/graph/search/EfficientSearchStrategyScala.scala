package com.ing.sea.pdeng.graph.search
import java.{lang, util}
import scala.jdk.CollectionConverters._

import scalax.collection.edge.WDiHyperEdge

class EfficientSearchStrategyScala extends SearchChallenge {
  override def traversals(ctx: C): lang.Iterable[util.List[WDiHyperEdge[V]]] =
    List.empty.asJava
}
