package com.ing.sea.pdeng.graph.search

import com.ing.sea.pdeng.graph.{CallableUnit, Type, Vertex}
import com.ing.sea.pdeng.graph.Vertex.t
import com.ing.sea.pdeng.graph.csv.CSVReader
import com.ing.sea.pdeng.graph.search.javacompat.JSearchChallengeRunner
import com.ing.sea.pdeng.graph.search.rensink.RensinkSearchStrategyJava
import com.ing.sea.pdeng.graph.search.testcases.SearchTestCases
import org.scalatest.matchers.should.Matchers
import org.scalatest.tags.Slow
import org.scalatest.wordspec.AnyWordSpec
import scalax.collection.Graph
import scalax.collection.edge.WDiHyperEdge

import scala.io.Source

@Slow
class TypeGraphSearchFilesSpec extends AnyWordSpec with Matchers with SearchTestCases {
  val strategies: List[SearchChallenge] = List(
    new JSearchChallengeRunner(new RensinkSearchStrategyJava()),
    // new JSearchChallengeRunner(new EfficientSearchStrategyJava),
    // new EfficientSearchStrategyScala,
    // Note that this fails, because it's really naive :)
    // new NaiveTailRec
  )

  val tests: Seq[(String, Type, Set[Type], Option[Int])] = List(
    ("large.csv", t("RTyLrWLwQv"), Set(
      t("wqiaiyuUwj"),
    ), Some(1)),
    ("large2.csv", t("wZNHUjQJZm"), Set(
      t("LemWqhRXIa"),
      t("nurXcDYLrM")
    ), None)
  )

  private val graphs = tests.map(t => (CSVReader.fromCSV(Source.fromResource(t._1).getLines()), t._2, t._3, t._4))

  for(s <- strategies) {
    s"Strategy ${s.name}" should {
      // Run all the registered tests -- they all should find solutions
      for ((g, t, a, expectedSize) <- graphs) {
        s"searching for ${t.name} in ${g.size}" in {
          val tst = asTraversalTest(g, t, a)
          val traversals = tst.traversals(s.asInstanceOf[TypeGraphSearch[Vertex, Type, CallableUnit, WDiHyperEdge, DepthContext[Vertex, Type, CallableUnit, WDiHyperEdge]]])
          for(traversal <- traversals) {
            assert(ValidateSolution.isValid(a, traversal), "Invalid trace found")
          }
          expectedSize match {
            case Some(expected) => traversals.size shouldBe expected
            case None => // I don't know what the expected size is
          }
        }
      }
    }
  }

  def asTraversalTest(g: Graph[Vertex, WDiHyperEdge], t: Type, a: Set[Type]): TraversalTest = new TraversalTest {
    override val target: Type = t
    override val available: Set[Type] = a
    override val graph: Graph[Vertex, WDiHyperEdge] = g
    override val maxDepth: Int = Integer.MAX_VALUE
    override val expectedResults: List[List[WDiHyperEdge[Vertex]]] = List.empty
  }
}
