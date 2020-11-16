package com.ing.sea.pdeng.graph.search

import scala.annotation.tailrec

object CartesianProduct {
  // Slow, but always works regardless of size of `in`
  def apply[T](in: IndexedSeq[IndexedSeq[T]]): IndexedSeq[IndexedSeq[T]] = {
    @tailrec
    def loop[T](acc: List[List[T]], rest: List[List[T]]): List[List[T]] = rest match {
      case Nil => acc
      case head :: tail =>
        val newAcc: List[List[T]] = for {
          i <- head
          a <- acc
        } yield i :: a
        loop(newAcc, tail)
    }

    loop(List(List.empty), in.toList.map(_.toList)).map(_.toVector).toVector
  }
}
