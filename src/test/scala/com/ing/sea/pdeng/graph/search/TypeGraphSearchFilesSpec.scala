package com.ing.sea.pdeng.graph.search

import com.ing.sea.pdeng.graph.{CallableUnit, Type, Vertex}
import com.ing.sea.pdeng.graph.Vertex.{cu, e, t}
import com.ing.sea.pdeng.graph.csv.CSVReader
import com.ing.sea.pdeng.graph.search.cachingcats.CachingCats
import com.ing.sea.pdeng.graph.search.javacompat.JSearchChallengeRunner
import com.ing.sea.pdeng.graph.search.rensink.RensinkSearchStrategyJava
import com.ing.sea.pdeng.graph.search.testcases.SearchTestCases
import org.scalatest.concurrent.TimeLimitedTests
import org.scalatest.matchers.should.Matchers
import org.scalatest.tags.Slow
import org.scalatest.time.{Minute, Seconds, Span}
import org.scalatest.wordspec.AnyWordSpec
import scalax.collection.Graph
import scalax.collection.edge.WDiHyperEdge

import scala.io.Source

@Slow
class TypeGraphSearchFilesSpec extends AnyWordSpec with Matchers with SearchTestCases with TimeLimitedTests {
  val timeLimit: Span = Span(1, Minute)

  val strategies: List[SearchChallenge] = List(
    new JSearchChallengeRunner(new RensinkSearchStrategyJava()),

    // Disabled because it fails the tests.
    // new CachingCats,

    // Enable your own strategy in Java
    // new JSearchChallengeRunner(new EfficientSearchStrategyJava),

    // Enable your own strategy in Scala
    // new EfficientSearchStrategyScala,

    // Note that this fails, because it's really naive :)
    // new NaiveTailRec
  )

  val tests: Seq[(String, Type, Set[Type], Option[Seq[Seq[WDiHyperEdge[Vertex]]]])] = List(
    ("large.csv", t("RTyLrWLwQv"), Set(
      t("wqiaiyuUwj"),
    ), Some(List(
      List(
        e(t("wqiaiyuUwj"), t("OcxaVFUHzO"), cu("hAariiCoGS"), t("RTyLrWLwQv")),
        e(t("LybrYItEwm"), t("iJdMdVxaay"), t("ylVuJBSVgq"), cu("zTnrkygQnO"), t("OcxaVFUHzO")),
        e(t("POPBgcHRNk"), cu("aVVSpwmOHh"), t("LybrYItEwm")),
        e(t("xCkleBYfCf"), cu("OKAZUSamjH"), t("POPBgcHRNk")),
        e(t("rWqSRrExjd"), cu("HxQhhYFwAt"), t("xCkleBYfCf")),
        e(t("wqiaiyuUwj"), cu("NCBqyEVgec"), t("rWqSRrExjd")),
        e(t("POPBgcHRNk"), cu("ARyFRHOwAu"), t("iJdMdVxaay")),
        e(t("POPBgcHRNk"), cu("juKlVquZpT"), t("ylVuJBSVgq")),
      ),
    ))),
    ("large2.csv", t("wZNHUjQJZm"), Set(
      t("LemWqhRXIa"),
      t("nurXcDYLrM")
    ), None)
  )

  private val graphs = tests.map(t => (CSVReader.fromCSV(Source.fromResource(t._1).getLines()), t._2, t._3, t._4))

  for(s <- strategies) {
    s"Strategy ${s.name}" should {
      // Run all the registered tests -- they all should find solutions
      for ((g, t, a, expectedResults) <- graphs) {
        s"searching for ${t.name} in ${g.size}" in {
          val tst = asTraversalTest(g, t, a)
          val traversals = tst.traversals(s.asInstanceOf[TypeGraphSearch[Vertex, Type, CallableUnit, WDiHyperEdge, DepthContext[Vertex, Type, CallableUnit, WDiHyperEdge]]])
          for(traversal <- traversals) {
            assert(ValidateSolution.isValid(a, traversal), "Invalid trace found")
          }
          expectedResults match {
            case Some(expected) =>
              traversals.size shouldBe expected.size
              for(gt <- expected) {
                // Each expected outcome should be there
                var exists = false
                val gtSet = gt.toSet
                for(at <- traversals) {
                  if(gtSet == at.toSet) {
                    exists = true
                  }
                }
                if(! exists) {
                  fail(s"Test trace $gt is not found in the actual traversals")
                }
              }
            case None => // I don't know what the expected result is
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
