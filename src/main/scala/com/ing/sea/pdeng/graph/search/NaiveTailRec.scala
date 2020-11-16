package com.ing.sea.pdeng.graph.search

import java.util

import scalax.collection.Graph
import scalax.collection.edge.WDiHyperEdge

import scala.collection.immutable.Queue
import scala.collection.mutable
import scala.util.control.TailCalls.{TailRec, done, tailcall}
import scala.jdk.CollectionConverters._

class NaiveTailRec extends SearchChallenge {
  override def traversals(ctx: C): java.lang.Iterable[util.List[WDiHyperEdge[V]]] =
    new NaiveTraverser(ctx, predecessorsContaining(_, ctx.graph))
      .run()
      .map(_.asJava)
      .asJava

  private def predecessorsContaining(node: DV, graph: Graph[V, WDiHyperEdge]): Set[EdgeInfo] =
    predecessorEdgesContaining(node, graph).map(HyperEdgeInfo.apply[V, DV, FV]).toSet

  private class NaiveTraverser(initialCtx: DepthContext[V, DV, FV, WDiHyperEdge], pred: DV => Set[HyperEdgeInfo[V, DV, FV]]) {
    type EdgeInfo = HyperEdgeInfo[V, DV, FV]

    def run(): Set[IndexedSeq[WDiHyperEdge[V]]] = pred(initialCtx.target)
      .flatMap(e => traverse(initialCtx.copy(trace = Queue(e), seen = Set(e.out))).result
        .map { sols =>
          assert(isValidSolution(sols, initialCtx), "Invalid solution detected!")
          sols
        }
        .map(_.map(_.edge).distinct)
        .filter(_.nonEmpty)
      )

    def traverse(ctx: DepthContext[V, DV, FV, WDiHyperEdge]): TailRec[IndexedSeq[IndexedSeq[EdgeInfo]]] = {
      implicit val thisEdge: EdgeInfo = ctx.trace.last
      // All our 'in' elements are in our goals
      if (thisEdge.in forall ctx.given.contains) {
        return done(IndexedSeq(ctx.trace.toVector))
      }

      // If we reached max depth, but did not find any results yet we return an empty trace
      if (ctx.depth >= ctx.maxDepth) {
        return done(IndexedSeq.empty[IndexedSeq[EdgeInfo]])
      }

      // A cycle based on edges, so this path won't yield a solution
      if(ctx.trace.count(_ == thisEdge) >= 2) {
        return done(IndexedSeq.empty)
      }
      // Another cycle, but based on the fact that all types are seen
      // Don't store this as a cycle can be caused by any number of traces, so no need
      // to disable this opportunity for other traversals
      if (thisEdge.in forall ctx.seen.contains) {
        return done(IndexedSeq.empty)
      }

      val groupedByOut: Map[DV, Vector[EdgeInfo]] = thisEdge.in
        // Only retain vertices that are not a goal (as we already 'resolved' them)
        .filterNot(ctx.`given`.contains)
        .map(in_t =>
          in_t -> pred(in_t).toVector
        ).toMap

      val options: IndexedSeq[IndexedSeq[EdgeInfo]] = CartesianProduct(groupedByOut.values.toVector)
      val newCtx = ctx.copy(depth = ctx.depth + 1)

      recursiveTraverseTrampolining(newCtx, options)
        .map(_.filter(_.nonEmpty))
    }

    // SplitMerge will explore all opportunities (based an the cartesian product) and yield path(s) if they exist
    private def recursiveTraverseTrampolining(ctx: DepthContext[V, DV, FV, WDiHyperEdge], allOptions: IndexedSeq[IndexedSeq[EdgeInfo]]): TailRec[IndexedSeq[IndexedSeq[EdgeInfo]]] = {
      val f = exploreOpportunity(ctx, _)
      val optionalPaths: IndexedSeq[TailRec[IndexedSeq[IndexedSeq[EdgeInfo]]]] = allOptions.map(f)
      sequence(optionalPaths).map(_.flatten)
    }

    // Explore each of the opportunities, making sure a viable path is found
    private def exploreOpportunity(ctx: DepthContext[V, DV, FV, WDiHyperEdge], opportunity: IndexedSeq[EdgeInfo]): TailRec[IndexedSeq[IndexedSeq[EdgeInfo]]] = {
      val unmergedResult: IndexedSeq[TailRec[IndexedSeq[IndexedSeq[EdgeInfo]]]] =
        opportunity.map(pe => tailcall(traverse(ctx.copy(
          seen = ctx.seen + pe.out,
          trace = ctx.trace.appended(pe)
        ))))
      mergeOpportunity(unmergedResult)
    }

    private def mergeOpportunity(unmergedResult: IndexedSeq[TailRec[IndexedSeq[IndexedSeq[EdgeInfo]]]]): TailRec[IndexedSeq[IndexedSeq[EdgeInfo]]] =
      sequence(unmergedResult).map {
        case x if x.exists(_.isEmpty) => Vector.empty
        case x => combinePartialSolutions(x)
      }

    def containsDuplicateProductionEdges(edges: Seq[EdgeInfo]): Boolean =
      edges.groupBy(_.out).exists(_._2.distinct.size > 1)

    private def combinePartialSolutions(partials: IndexedSeq[IndexedSeq[IndexedSeq[EdgeInfo]]]): IndexedSeq[IndexedSeq[EdgeInfo]] = {
      if(partials.exists(_.isEmpty)) {
        // If there are empty partials, we don't have a valid solution
        IndexedSeq.empty
      } else if (partials.forall(_.size == 1)) {
        // If there are no alternatives for any partial, squash together in a single solution
        IndexedSeq(partials.flatten.flatten)
      } else {
        // This case is special.  We need to accommodate for situations where there is more than
        // one alternative to achieve our desired result. We therefore make a distinction between
        // sizes <= 1 (which are allways all required) and > 1 (which we need all combinations of)
        val partitioned = partials.groupBy(_.size > 1)

        // We always need all partials that are <= 1
        val ones: Seq[EdgeInfo] = partitioned.getOrElse(false, Set.empty).flatten.flatten.toIndexedSeq

        // But for the partials we require a combination of all options.
        val alternatives: IndexedSeq[IndexedSeq[IndexedSeq[EdgeInfo]]] = CartesianProduct(partitioned(true))
        // We then flatten each alternatives so they form a coherent joined solution
        val coherentPartial: IndexedSeq[IndexedSeq[EdgeInfo]] = alternatives.flatten
        // Finally, we make sure that each coherent partial solution is joined with the partials of 1
        val allOptions = coherentPartial.map(_ ++ ones)
        allOptions.filterNot(containsDuplicateProductionEdges).map(_.distinct)
      }
    }

    // This is basically sequence on a Traverse type class
    private def sequence[A](elems: IndexedSeq[TailRec[A]]): TailRec[IndexedSeq[A]] = {
      // Doing it this way is a small optimisation
      def inner(in: Seq[TailRec[A]]): TailRec[mutable.Buffer[A]] = elems match {
        case _ if in.isEmpty => done(new mutable.ListBuffer[A]())
        case _ => in.head.flatMap(result => tailcall(inner(in.tail)).map(_ += result))
      }

      inner(elems).map(_.toIndexedSeq)
    }
  }
}