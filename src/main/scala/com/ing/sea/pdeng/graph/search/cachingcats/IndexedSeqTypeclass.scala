package com.ing.sea.pdeng.graph.search.cachingcats

import cats.{Applicative, Eval, Traverse}
import cats.data.Chain
import cats.kernel.instances.StaticMethods.wrapMutableIndexedSeq

object IndexedSeqTypeclass {
  /**
    * Note that an indexed seq does technically does not obey the identity law.
    * Items can be mutated in place. However, in my case that's fine.
    */
  implicit val indexedSeqTraversable: Traverse[IndexedSeq] = new Traverse[IndexedSeq] {
    override def traverse[G[_], A, B](fa: IndexedSeq[A])(f: A => G[B])(implicit G: Applicative[G]): G[IndexedSeq[B]] =
      if(fa.isEmpty) {
        G.pure(IndexedSeq.empty)
      } else {
        G.map(Chain.traverseViaChain {
          val as = collection.mutable.ArrayBuffer[A]()
          as ++= fa
          wrapMutableIndexedSeq(as)
        }(f))(_.toVector)
      }

    override def foldLeft[A, B](fa: IndexedSeq[A], b: B)(f: (B, A) => B): B = ???

    override def foldRight[A, B](fa: IndexedSeq[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] = ???
  }
}
