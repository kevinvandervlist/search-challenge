package com.ing.sea.pdeng.graph.search.cachingcats.prune

import com.ing.sea.pdeng.graph.search.HyperEdgeInfo
import scalax.collection.edge.WDiHyperEdge

import scala.collection.mutable

private class PruneState[V, DV <: V : Manifest, FV <: V : Manifest] {
  sealed trait UsefulGroup {
    def maybeUseful: UsefulGroup = this
  }

  case object NotUseful extends UsefulGroup {
    override def maybeUseful: UsefulGroup = MaybeUseful
  }

  case object MaybeUseful extends UsefulGroup

  private val _useful: mutable.Map[FV, UsefulGroup] = mutable.Map()

  sealed trait ProducibleGroup {
    def maybeProducible: ProducibleGroup = this
  }

  case object NotProducible extends ProducibleGroup {
    override def maybeProducible: ProducibleGroup = MaybeProducible
  }

  case object MaybeProducible extends ProducibleGroup

  private val _producible: mutable.Map[DV, ProducibleGroup] = mutable.Map()

  /** When pruning the graph, should we keep this edge? */
  def keep(e: WDiHyperEdge[V]): Boolean = {
    val ei = HyperEdgeInfo.apply[V, DV, FV](e)
    if (isNotProducible(ei.out) && isNotUseful(ei.f)) {
      false
    } else {
      true
    }
  }

  def size: Int = _producible.size + _useful.size

  @inline
  def isNotProducible(v: DV): Boolean =
    _producible.getOrElse(v, NotProducible) == NotProducible

  @inline
  def maybeProducible(v: DV): Unit = {
    val initial = _producible.getOrElse(v, NotProducible)
    val updated = initial.maybeProducible
    if(initial != updated) {
      _producible.update(v, updated)
    }
  }

  @inline
  def isNotUseful(f: FV): Boolean =
    _useful.getOrElse(f, NotUseful) == NotUseful

  @inline
  def isMaybeUseful(f: FV): Boolean =
    ! isNotUseful(f)

  // This is how it works conceptually, except we can optimize by only writing iff different
  @inline
  def maybeUseful(f: FV): Unit = {
    val initial = _useful.getOrElse(f, NotUseful)
    val updated = initial.maybeUseful
    if(initial != updated) {
      _useful.update(f, updated)
    }
  }

  @inline
  def notUseful(f: FV): Unit = ()
}