package com.ing.sea.pdeng.graph.search.cachingcats

import cats.Eval
import cats.implicits.toTraverseOps
import com.ing.sea.pdeng.graph.{CallableUnit, Type, Vertex}
import com.ing.techrd.minoa.CartesianProduct
import scalax.collection.Graph
import scalax.collection.edge.WDiHyperEdge
import com.ing.sea.pdeng.graph.search.cachingcats.prune.{MaybeUsefulProducible, OverApproximate}
import com.ing.sea.pdeng.graph.search.{DepthContext, HyperEdgeInfo, SearchChallenge, TraversalContext, TypeGraphSearch}
import com.ing.sea.pdeng.graph.search.cachingcats.IndexedSeqTypeclass._

import scala.collection.immutable.Queue
import scala.collection.{View, mutable}

class CachingCats extends SearchChallenge {
  override def traversals(ctx: C): LazyList[List[WDiHyperEdge[V]]] =
    new CachingCatsImpl[Vertex, Type, CallableUnit]().traversals(
      DepthContext[Vertex, Type, CallableUnit, WDiHyperEdge](ctx.graph, ctx.target, ctx.`given`)
    )
}

private class CachingCatsImpl[V, DV <: V : Manifest, FV <: V : Manifest] extends TypeGraphSearch[V, DV, FV, WDiHyperEdge, DepthContext[V, DV, FV, WDiHyperEdge]] {
  override def traversals(ctx: C): LazyList[List[WDiHyperEdge[V]]] = {
    val pruneStrategy = new MaybeUsefulProducible[V, DV, FV]().prune(_)
    val pruneStrategy2 = new OverApproximate[V, DV, FV]().prune(_)
    val prunedCtx = pruneStrategy2(pruneStrategy(ctx))
    LazyList.from(
      new Traverser(prunedCtx, predecessorsContaining(_, prunedCtx.graph)).run()
    ).map(_.toList)
  }

  private def predecessorsContaining(node: DV, graph: Graph[V, WDiHyperEdge]): Iterable[EdgeInfo] =
    predecessorEdgesContaining(node, graph).map(HyperEdgeInfo.apply[V, DV, FV])

  private class Traverser(initialCtx: DepthContext[V, DV, FV, WDiHyperEdge], pred: DV => Iterable[HyperEdgeInfo[V, DV, FV]]) {
    type EdgeInfo = HyperEdgeInfo[V, DV, FV]

    private val state: mutable.Map[EdgeInfo, Solutions] = mutable.Map()

    def run(): Iterable[IndexedSeq[WDiHyperEdge[V]]] = pred(initialCtx.target)
      .flatMap(e => traverse(initialCtx.copy(trace = Queue(e), seen = Set(e.out))).value
        .map(_.map(_.edge).distinct)
        .filter(_.nonEmpty)
      )

    @inline
    private def storeSolutions(result: Solutions)(implicit edge: EdgeInfo): Solutions = {
      // Only take the result _after_ the current edge. Because we can only reuse what happens *after* we have reach
      // the current edge in the new traversal in case of a reusable result
      val toStore = result.map(_.dropWhile(e => !optimizedEdgeEquals(e))).map(_.distinct)
      state.put(edge, toStore)
      result
    }

    @inline
    private def retrieve(implicit edge: EdgeInfo): Option[Solutions] =
      state.get(edge)

    @inline
    private def optimizedEdgeEquals(other: EdgeInfo)(implicit thisEdge: EdgeInfo): Boolean =
      other.hashCode() == thisEdge.hashCode() && other == thisEdge

    @inline
    private def combineStoredResult(previousResult: Solutions, ctx: C): Eval[Solutions] = previousResult match {
      case p if p.isEmpty => Eval.now(IndexedSeq.empty)
      case p => Eval.now(p.map(_.prependedAll(ctx.trace)))
    }

    @inline
    private def allInElementsAreGoals(ctx: C)(implicit thisEdge: EdgeInfo): Boolean =
      thisEdge.in forall ctx.given.contains

    @inline
    private def isEdgeCycle(ctx: C)(implicit thisEdge: EdgeInfo): Boolean =
      ctx.trace.count(optimizedEdgeEquals) >= 2

    @inline
    private def everythingIsSeen(ctx: C)(implicit thisEdge: EdgeInfo): Boolean =
      thisEdge.in forall ctx.seen.contains

    @inline
    private def groupByOut(ctx: C)(implicit thisEdge: EdgeInfo): View[(DV, Vector[HyperEdgeInfo[V, DV, FV]])] =
      thisEdge.in
        .view
        // In case there are duplicate types
        .distinct
        // Only retain vertices that are not a goal (as we already 'resolved' them)
        .filterNot(ctx.`given`.contains)
        // And retrieve the predicates,
        // but also make sure we don't pick them if the out is already seen
        .map(in_t => in_t -> pred(in_t).filterNot(e => ctx.seen.contains(e.out)).toVector)

    @inline
    private def unreachableDependencies(groupedByOut: Iterable[(DV, IndexedSeq[EdgeInfo])]): Boolean =
      groupedByOut.exists(t => t._2.isEmpty)

    private def traverse(ctx: DepthContext[V, DV, FV, WDiHyperEdge]): Eval[Solutions] = {
      implicit val thisEdge: EdgeInfo = ctx.trace.last

      // If we have a previous result, reuse that.
      // Include the current traversal, as it might be different dependencies coming together
      // (e.g. 'branch then merge all')
      retrieve match {
        case Some(previousResult) if previousResult.isEmpty =>
          return Eval.now(IndexedSeq.empty)
        case Some(previousResult) =>
          return combineStoredResult(previousResult, ctx)
        case None =>
      }

      // All our 'in' elements are in our goals
      if (allInElementsAreGoals(ctx)) {
        return Eval.now(storeSolutions(IndexedSeq(IndexedSeq(ctx.trace.last))))
      }

      // If we reached max depth, but did not find any results yet we return an empty trace
      if (ctx.depth >= ctx.maxDepth) {
        return Eval.now(storeSolutions(IndexedSeq.empty))
      }

      // A cycle based on edges, so this path won't yield a solution
      if (isEdgeCycle(ctx)) {
        return Eval.now(storeSolutions(IndexedSeq.empty))
      }
      // Another cycle, but based on the fact that all types are seen
      // Don't store this as a cycle can be caused by any number of traces, so no need
      // to disable this opportunity for other traversals
      if (everythingIsSeen(ctx)) {
        return Eval.now(IndexedSeq.empty)
      }

      val groupedByOut: View[(DV, Vector[EdgeInfo])] = groupByOut(ctx)

      // Some dependencies cannot be produced -- bail
      if (unreachableDependencies(groupedByOut)) {
        return Eval.now(storeSolutions(IndexedSeq.empty[Solution]))
      }

      val options: IndexedSeq[IndexedSeq[EdgeInfo]] = CartesianProduct.product(groupByOut(ctx).map(_._2).toIndexedSeq)

      recursiveTraverseTrampolining(ctx, options)
        .map(_.filter(_.nonEmpty))
        .map(storeSolutions)
    }

    // SplitMerge will explore all opportunities (based an the cartesian product) and yield path(s) if they exist
    @inline
    private def recursiveTraverseTrampolining(ctx: DepthContext[V, DV, FV, WDiHyperEdge], allOptions: IndexedSeq[IndexedSeq[EdgeInfo]]): Eval[Solutions] = {
      val f = exploreOpportunity(ctx, _)
      val optionalPaths: IndexedSeq[Eval[IndexedSeq[IndexedSeq[EdgeInfo]]]] = allOptions.map(f)
      optionalPaths
        .sequence
        .map(_.flatten.distinct)
    }

    // Explore each of the opportunities, making sure a viable path is found
    @inline
    private def exploreOpportunity(ctx: DepthContext[V, DV, FV, WDiHyperEdge], opportunity: IndexedSeq[EdgeInfo]): Eval[Solutions] = {
      val unmergedResult: IndexedSeq[Eval[IndexedSeq[IndexedSeq[EdgeInfo]]]] =
        opportunity.map(pe => Eval.defer(traverse(ctx.copy(
          seen = ctx.seen + pe.out,
          depth = ctx.depth + 1,
          trace = ctx.trace.appended(pe)
        ))))
      expensive(unmergedResult).map(_.map(solution => solution.prepended(ctx.trace.last)))
    }

    @inline
    private def expensive(unmergedResult: IndexedSeq[Eval[Solutions]]): Eval[Solutions] =
      unmergedResult.sequence.map {
        case x if x.isEmpty => Vector.empty
        case x if x.exists(_.isEmpty) => Vector.empty
        case x => combinePartialSolutions(x)
      }

    // Note that edges is assumed to contain only distinct elements
    @inline
    def containsDuplicateProductionEdges(edges: Seq[EdgeInfo]): Boolean = {
      // the code below is slightly faster than the group by / exists combo
      // because I can use the invariant above
      // edges.groupBy(_.out).exists(_._2.size > 1)
      edges.size != edges.map(_.out).distinct.size
    }

    @inline
    private def combinePartialSolutions(partials: IndexedSeq[Solutions]): Solutions = {
      // This piece of code is basically this, except more performant
      /**
        * if(partials.exists(_.isEmpty)) {
        * IndexedSeq.empty
        * } else if (partials.forall(_.size == 1)) {
        * IndexedSeq(partials.flatten.flatten)
        * } else {
        * [ ... ]
        * }
        */
      var idx = 0
      var noOnes = 0
      while (idx < partials.length) {
        if (partials(idx).isEmpty) {
          return IndexedSeq.empty
        } else if (partials(idx).size == 1) {
          noOnes += 1
        }
        idx += 1
      }
      if (noOnes == idx) {
        return IndexedSeq(partials.flatten.flatten)
      }
      // [...] else starts here
      combinePartialSolutionsComplexCase(partials)
    }

    // This case is special.  We need to accommodate for situations where there is more than
    // one alternative to achieve our desired result. We therefore make a distinction between
    // sizes <= 1 (which are allways all required) and > 1 (which we need all combinations of)
    @inline
    private def combinePartialSolutionsComplexCase(partials: IndexedSeq[Solutions]): Solutions = {
      val partitioned = partials.groupBy(_.size > 1)

      // We always need all partials that are <= 1
      val ones: Seq[EdgeInfo] = partitioned.getOrElse(false, Set.empty).flatten.flatten.toIndexedSeq

      // But for the partials we require a combination of all options.
      val alternatives: IndexedSeq[IndexedSeq[IndexedSeq[EdgeInfo]]] = CartesianProduct.product(partitioned(true))
      // We then flatten each alternatives so they form a coherent joined solution
      val coherentPartial: IndexedSeq[IndexedSeq[EdgeInfo]] = alternatives.flatten
      // Finally, we make sure that each coherent partial solution is joined with the partials of 1
      val allOptions = coherentPartial.map(_ ++ ones).map(_.distinct)
      allOptions.filterNot(containsDuplicateProductionEdges)
    }
  }
}