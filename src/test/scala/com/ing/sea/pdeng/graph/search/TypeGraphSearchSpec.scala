package com.ing.sea.pdeng.graph.search

import java.io.File
import java.nio.file.Files

import com.ing.sea.pdeng.graph.{CallableUnit, Type, Vertex}
import com.ing.sea.pdeng.graph.dot.ImageWriter
import com.ing.sea.pdeng.graph.search.javacompat.JSearchChallengeRunner
import com.ing.sea.pdeng.graph.search.testcases.SearchTestCases
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import scalax.collection.edge.WDiHyperEdge

class TypeGraphSearchSpec extends AnyWordSpec with Matchers with SearchTestCases {
  val strategies: List[SearchChallenge] = List(
    // new JSearchChallengeRunner(new EfficientSearchStrategyJava),
    // new EfficientSearchStrategyScala,
    new NaiveTailRec,
  )
  type Search = TypeGraphSearch[Vertex, Type, CallableUnit, WDiHyperEdge, DepthContext[Vertex, Type, CallableUnit, WDiHyperEdge]]

  val tests = Map(
    "not find a path when there is none available" -> noPathAvailable,
    "not find a path when a vertex dependency cannot be satisfied" -> noPathAvailableBecauseUnsatisfiableDependency,
    "find a path when there is direct one available" -> linearPathAvailable,
    "find a path when there is a direct one available with joins" -> composedPathAvailable,
    "find a path when there is a direct one available with joins but also with spurious paths" -> composedPathsAvailableWithSuperfluousPaths,
    "find a path when there are two direct paths available" -> composedPathsAvailableWithTwoOptions,
    "find a path when there is a cycle halfway through" -> linearPathWithCycle,
    "deal with an edge level cycle" -> edgeCycle,
    "find a path when there is an indirect dependency" -> indirectDependency,
    "find a path when there are 2 indirect dependencies" -> twoIndirectDependencies,
    "find a path when there are 2 indirect dependencies and a potential loop" -> twoIndirectDependenciesWithLoop,
    "branch then merge all" -> branchThenMergeAll,
    "two alternatives providing input" -> twoAlternativesProvidingInput,
    "dilemma of choices" -> dilemmaOfChoices,
    "complex case, simple result where target is input of another edge" -> complexCaseSimpleResultWhereTargetIsInputOfAnotherEdge,
    "regression: one of the inputs is a given but can also be produced" -> regressionOneOfInputsIsGivenButCanAlsoBeProduced,
    "regression: cycle via an edge" -> regressionCycleViaEdge,
    "bug: multiple options: combinatorics" -> multipleOptions,
    "bug: multiple options: combinatorics minimized" -> multipleOptionsMinimized,
    "bug: multiple options: combinatorics minimized with a prefixed trace" -> multipleOptionsMinimizedPrefixedTrace,
    "bug: multiple options: combinatorics minimized with a choice" -> multipleOptionsMinimizedPrefixedChoices,
    "bug: multiple options with back references: combinatorics" -> multipleOptionsWithBackReferences,
    "bug: don't conflate alternatives at choice" -> dontConflateAlternativesAtChoice,
    "bug: unresolved dependencies" -> unresolvedDependencies,
    "complete hello world model" -> completeHelloWorldModel,
    "don't overflow the stack" -> noStackOverflowError,
  )

  for(s <- strategies) {
    s"Strategy ${s.name}" should {
      // Run all the registered tests
      for ((name, t) <- tests) {
        name in {
          evaluate(t, s.asInstanceOf[TypeGraphSearch[Vertex, Type, CallableUnit, WDiHyperEdge, DepthContext[Vertex, Type, CallableUnit, WDiHyperEdge]]])
        }
      }
    }
  }

  private def evaluate(test: TraversalTest, strategy: TypeGraphSearch[Vertex, Type, CallableUnit, WDiHyperEdge, DepthContext[Vertex, Type, CallableUnit, WDiHyperEdge]]): Unit = {
    val foundTraversals = test.traversals(strategy)
    for(traversal <- foundTraversals) {
      assert(ValidateSolution.isValid(test.available, traversal), "Invalid trace found")
    }
    val found = foundTraversals.map(Traversal.apply)
    val foundSet = found.toSet
    val expected = test.expectedResults.map(Traversal.apply)
    val expectedSet = expected.toSet
    
    try {
      withClue("duplicate found cases") {
        foundSet should have size found.size
      }
      withClue("duplicate expected cases") {
        expectedSet should have size expected.size
      }
      withClue("more found that are not expected") {
        (foundSet diff expectedSet) shouldBe empty
      }
      withClue("more expected that were not found") {
        (expectedSet diff foundSet) shouldBe empty
      }
      /*withClue(s"# of found traces doesn't meet # expected traces: ") {
        foundSet should have size expectedSet.size
      }
      withClue("not same elements: ") {
        foundSet should contain theSameElementsAs expectedSet
      } */
    } catch {
      case e: Exception =>
        val failureDirectory = Files.createTempDirectory(strategy.name)

        ImageWriter.dumpToFile(new File(failureDirectory.toFile, "complete"), test.graphDot)
        ImageWriter.dumpToFile(new File(failureDirectory.toFile, "expected"), test.expectedDots)
        ImageWriter.dumpToFile(new File(failureDirectory.toFile, "actual"), test.actualDots(found.map(_.trace)))

        println(s"Wrote graphs to: \n$failureDirectory")
        // TODO: HACK
        try {
          import sys.process._
          val toSVG = "bash -c 'for dot in *.dot; do dot -Tsvg $dot -o ${dot%.dot}.svg; done'"
          println(Process(toSVG, failureDirectory.toFile).!!)
        } catch {
          case e: Exception =>
            println(e)
        }

        // And rethrow the original exception
        throw e
    }
  }
}
