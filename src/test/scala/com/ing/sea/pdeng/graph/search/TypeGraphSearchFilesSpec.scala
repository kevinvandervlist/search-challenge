package com.ing.sea.pdeng.graph.search

import com.ing.sea.pdeng.graph.{Type, Vertex}
import com.ing.sea.pdeng.graph.Vertex.t
import com.ing.sea.pdeng.graph.csv.CSVReader
import com.ing.sea.pdeng.graph.search.testcases.SearchTestCases
import org.scalatest.matchers.should.Matchers
import org.scalatest.tags.Slow
import org.scalatest.wordspec.AnyWordSpec
import scalax.collection.Graph
import scalax.collection.edge.WDiHyperEdge

import scala.io.Source

@Slow
class TypeGraphSearchFilesSpec extends AnyWordSpec with Matchers with SearchTestCases {
  val strategies: List[NaiveTailRec] = List(
    // new EfficientSearchStrategyJava,
    // new EfficientSearchStrategyScala,
    // Note that this fails, because it's really naive :)
    // new NaiveTailRec
  )

  val tests = List(
    ("large.csv", t("RTyLrWLwQv"), Set(
      t("wqiaiyuUwj"),
    ), 1)
  )

  private val graphs = tests.map(t => (CSVReader.fromCSV(Source.fromResource(t._1).getLines()), t._2, t._3, t._4))

  for(s <- strategies) {
    s"Strategy ${s.name}" should {
      // Run all the registered tests -- they all should find solutions
      for ((g, t, a, expectedSize) <- graphs) {
        s"searching for ${t.name} in ${g.size}" in {
          val tst = asTraversalTest(g, t, a)
          val traversals = tst.traversals(s)
          for(traversal <- traversals) {
            assert(ValidateSolution.isValid(a, traversal), "Invalid trace found")
          }
          traversals.size shouldBe expectedSize
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
