package com.ing.sea.pdeng.graph.search.testcases

import com.ing.sea.pdeng.graph.{Type, Vertex}
import com.ing.sea.pdeng.graph.search.TraversalTest
import com.ing.sea.pdeng.graph.Vertex.{cu, e, t}
import com.ing.sea.pdeng.graph.search.testcases.CompleteHelloWorldModel
import scalax.collection.edge.WDiHyperEdge
import scalax.collection.Graph

trait SearchTestCases {
  /*
    e1: f1(a,b)  -> c
    e2: f2(c)    -> c
    e3: f3(e,f)  -> g

    source: {a,b}
    target: {g}

    -----

    e1: f1(a,b)  -> c {a,b,c}
    e2: f2(c)    -> c {a,b,c}

    NO REDUCTIONS POSSIBLE
   */
  protected val noPathAvailable: TraversalTest = new TraversalTest {
    val e1 = e(t("a"), t("b"), cu("f1"), t("c"))
    val e2 = e(t("c"), cu("f2"), t("c"))
    val e3 = e(t("e"), t("f"), cu("f3"), t("g"))
    override val target: Type = t("g")
    override val available: Set[Type] = Set(t("a"), t("b"))
    override val graph: Graph[Vertex, WDiHyperEdge] = Graph(e1, e2, e3)
    override val maxDepth: Int = Integer.MAX_VALUE
    override val expectedResults: List[List[WDiHyperEdge[Vertex]]] = List.empty
  }

  /*
      e1: f1(a)   -> b
      e2: f2(b,c) -> d
      e3: f3(d)   -> e

      source: {a}
      target: {e}

      -----

      e1: f1(a)   -> b {a,b}

      NO REDUCTIONS POSSIBLE
   */
  protected val noPathAvailableBecauseUnsatisfiableDependency: TraversalTest = new TraversalTest {
    val e1 = e(t("a"), cu("f1"), t("b"))
    val e2 = e(t("b"), t("c"), cu("f2"), t("d"))
    val e3 = e(t("d"), cu("f3"), t("e"))
    override val target: Type = t("e")
    override val available: Set[Type] = Set(t("a"))
    override val graph: Graph[Vertex, WDiHyperEdge] = Graph(e1, e2, e3)
    override val maxDepth: Int = Integer.MAX_VALUE
    override val expectedResults: List[List[WDiHyperEdge[Vertex]]] = List.empty
  }

  protected val maximumSearchDepthReached: TraversalTest = new TraversalTest {
    val e1 = e(t("a"), cu("f1"), t("b"))
    val e2 = e(t("b"), cu("f2"), t("c"))
    val e3 = e(t("c"), cu("f3"), t("d"))
    override val target: Type = t("d")
    override val available: Set[Type] = Set(t("a"))
    override val graph: Graph[Vertex, WDiHyperEdge] = Graph(e1, e2, e3)
    override val maxDepth: Int = 1
    override val expectedResults: List[List[WDiHyperEdge[Vertex]]] = List.empty
  }

  /*
    e1: f1(a,b) -> c
    e2: f2(c)   -> d
    e3: f3(d)   -> e

    source: {a,b}
    target: {e}

    -----

    e1: f1(a,b) -> c {a,b,c}
    e2: f2(c)   -> d {a,b,c,d}
    e3: f3(d)   -> e {a,b,c,d,e}

    TARGET REACHED
   */
  protected val linearPathAvailable: TraversalTest = new TraversalTest {
    val e1 = e(t("a"), t("b"), cu("f1"), t("c"))
    val e2 = e(t("c"), cu("f2"), t("d"))
    val e3 = e(t("d"), cu("f3"), t("e"))
    override val target: Type = t("e")
    override val available: Set[Type] = Set(t("a"), t("b"))
    override val graph: Graph[Vertex, WDiHyperEdge] = Graph(e1, e2, e3)
    override val maxDepth: Int = Integer.MAX_VALUE
    override val expectedResults: List[List[WDiHyperEdge[Vertex]]] = List(
      List(e1, e2, e3)
    )
  }

  /*
    e1: f1(a,b) -> c
    e2: f2(c)   -> d
    e3: f3(d)   -> e

    source: {a,b}
    target: {e}

    -----

    e1: f1(a,b) -> c {a,b,c}
    e2: f2(c)   -> d {a,b,c,d}
    e3: f3(d)   -> e {a,b,c,d,e}

    TARGET REACHED
   */

  protected val composedPathAvailable: TraversalTest = new TraversalTest {
    val e1 =  e(t("a"), t("b"), cu("f1"), t("c"))
    val e2 =  e(t("c"), cu("f2"), t("d"))
    val e3 = e(t("a"), t("d"), cu("f3"), t("e"))
    override val target: Type = t("e")
    override val available: Set[Type] = Set(t("a"), t("b"))
    override val graph: Graph[Vertex, WDiHyperEdge] = Graph(e1, e2, e3)
    override val maxDepth: Int = Integer.MAX_VALUE
    override val expectedResults: List[List[WDiHyperEdge[Vertex]]] = List(
      List(e1, e2, e3)
    )
  }

  /*

      e1a: f1(a,b)  -> c1
      e1b: f2(a,b)  -> c2
      e2a: f2(c1)   -> d1
      e2b: f2(c2)   -> d2
      e3a: f3(a,d1) -> e
      e3b: f3(a,x)  -> e

      source: {a,b}
      target: {e}

      e1a: f1(a,b)  -> c1 {a,b,c1}
      e2a: f2(c1)   -> d1 {a,b,c1,d1}
      e3a: f3(a,d1) -> e  {a,b,c1,d1,e}

      TARGET REACHED
   */

  protected val composedPathsAvailableWithSuperfluousPaths: TraversalTest = new TraversalTest {
    val e1a =  e(t("a"), t("b"), cu("f1"), t("c1"))
    val e1b =  e(t("a"), t("b"), cu("f2"), t("c2"))
    val e2a =  e(t("c1"), cu("f2"), t("d1"))
    val e2b =  e(t("c2"), cu("f2"), t("d2"))
    val e3a = e(t("a"), t("d1"), cu("f3"), t("e"))
    val e3b = e(t("a"), t("x"), cu("f3"), t("e"))
    override val target: Type = t("e")
    override val available: Set[Type] = Set(t("a"), t("b"))
    override val graph: Graph[Vertex, WDiHyperEdge] = Graph(e1a, e1b, e2a, e2b, e3a, e3b)
    override val maxDepth: Int = Integer.MAX_VALUE
    override val expectedResults: List[List[WDiHyperEdge[Vertex]]] = List(
      List(e1a, e2a, e3a)
    )
  }

  /*

      e1a: f1(a,b)  -> c1
      e1b: f2(a,b)  -> c2
      e2a: f2(c1)   -> d1
      e2b: f2(c2)   -> d2
      e3a: f3(a,d1) -> e
      e3b: f3(a,d2)  -> e

      source: {a,b}
      target: {e}

      e1a: f1(a,b)  -> c1 {a,b,c1}
      e2a: f2(c1)   -> d1 {a,b,c1,d1}
      e3a: f3(a,d1) -> e  {a,b,c1,d1,e}

      TARGET REACHED

      e1b: f1(a,b)  -> c1 {a,b,c2}
      e2b: f2(c2)   -> d2 {a,b,c2,d2}
      e3b: f3(a,d2) -> e  {a,b,c2,d2,e}

      TARGET REACHED

   */
  protected val composedPathsAvailableWithTwoOptions: TraversalTest = new TraversalTest {
    val e1a =  e(t("a"), t("b"), cu("f1"), t("c1"))
    val e1b =  e(t("a"), t("b"), cu("f2"), t("c2"))
    val e2a =  e(t("c1"), cu("f2"), t("d1"))
    val e2b =  e(t("c2"), cu("f2"), t("d2"))
    val e3a = e(t("a"), t("d1"), cu("f3"), t("e"))
    val e3b = e(t("a"), t("d2"), cu("f3"), t("e"))
    override val target: Type = t("e")
    override val available: Set[Type] = Set(t("a"), t("b"))
    override val graph: Graph[Vertex, WDiHyperEdge] = Graph(e1a, e1b, e2a, e2b, e3a, e3b)
    override val maxDepth: Int = Integer.MAX_VALUE
    override val expectedResults: List[List[WDiHyperEdge[Vertex]]] = List(
      List(e1a, e2a, e3a),
      List(e1b, e2b, e3b)
    )
  }

  /*
      e1: f1(a)    -> b
      e2: f2(a,b)  -> c
      e3: f3(c)    -> loop
      e4: f4(loop) -> c
      e5: f5(c)    -> d

      source: {a}
      target: {d}

      e1: f1(a)    -> b {a,b}
      e2: f2(a,b)  -> c {a,b,c}
      XXX: e3: f3(c)    -> loop {a,b,c,loop} // LOOP
      XXX: e4: f4(loop) -> c {a,b,c,loop} // LOOP
      e4: f5(c)    -> d {a,b,c,loop,d}

      TARGET REACHED
   */
  protected val linearPathWithCycle: TraversalTest = new TraversalTest {
    val e1 = e(t("a"), cu("f1"), t("b"))
    val e2 = e(t("a"), t("b"), cu("f2"), t("c"))
    val e3 = e(t("c"), cu("f3"), t("loop"))
    val e4 = e(t("loop"), cu("f4"), t("c"))
    val e5 = e(t("c"), cu("f5"), t("d"))
    override val target: Type = t("d")
    override val available: Set[Type] = Set(t("a"))
    override val graph: Graph[Vertex, WDiHyperEdge] = Graph(e1, e2, e3, e4, e5)
    override val maxDepth: Int = Integer.MAX_VALUE
    override val expectedResults: List[List[WDiHyperEdge[Vertex]]] = List(
      List(e1, e2, e5)
    )
  }

  protected val edgeCycle: TraversalTest = new TraversalTest {
    // These two edges are the issue; although we _have_ 0, we can't produce any of the nodes because we
    // always require at least one of them
    val e0a = e(t("0"), t("2"), cu("cu2"), t("1"))
    val e0b = e(t("0"), t("1"), cu("cu3"), t("2"))
    val e1 = e(t("1"), cu("cu1"), t("0"))
    val e2 = e(t("1"), t("21"), t("10"), cu("cu23"), t("22"))
    val e3 = e(t("x"), cu("cux"), t("21"))
    val e4 = e(t("y"), cu("cuy"), t("10"))
    override val target: Type = t("22")
    override val available: Set[Type] = Set(t("0"), t("x"), t("y"))
    override val graph: Graph[Vertex, WDiHyperEdge] = Graph(e0a, e0b, e1, e2, e3, e4)
    override val maxDepth: Int = Integer.MAX_VALUE
    override val expectedResults: List[List[WDiHyperEdge[Vertex]]] = List.empty
  }

  /*
     e1: f1(a)   -> b
     e2: f2(a,b) -> c
     e3: f3(a)   -> d
     e4: f4(c,d) -> e

     source: {a}
     target: {e}

     e1: f1(a)   -> b {a, b}
     e2: f2(a,b) -> c {a, b, c}
     e3: f3(a)   -> d {a, b, c, d}
     e4: f4(c,d) -> e {a, b, c, d, e}

     TARGET REACHED
   */

  protected val indirectDependency: TraversalTest = new TraversalTest {
    val e1 = e(t("a"), cu("f1"), t("b"))
    val e2 = e(t("a"), t("b"), cu("f2"), t("c"))
    val e3 = e(t("a"), cu("f3"), t("d"))
    val e4 = e(t("c"), t("d"), cu("f4"), t("e"))
    override val target: Type = t("e")
    override val available: Set[Type] = Set(t("a"))
    override val graph: Graph[Vertex, WDiHyperEdge] = Graph(e1, e2, e3, e4)
    override val maxDepth: Int = Integer.MAX_VALUE
    override val expectedResults: List[List[WDiHyperEdge[Vertex]]] = List(
      List(e1, e2, e3, e4)
    )
  }

  /*

      e1: f1(a)     -> b
      e2: f2(b)     -> c
      e3: f3(c)     -> d
      e3: f4(c,d)   -> e

      source: {a}
      target: {e}

      e1: f1(a)     -> b {a,b}
      e2: f2(b)     -> c {a,b,c}
      e3: f3(c)     -> d {a,b,c,d}
      e3: f4(c,d)   -> e {a,b,c,d,e}

      TARGET REACHED
   */


  protected val twoIndirectDependencies: TraversalTest = new TraversalTest {
    val e1 = e(t("a"), cu("f1"), t("b"))
    val e2 = e(t("b"), cu("f2"), t("c"))
    val e3 = e(t("c"), cu("f3"), t("d"))
    val e4 = e(t("c"), t("d"), cu("f4"), t("e"))
    override val target: Type = t("e")
    override val available: Set[Type] = Set(t("a"))
    override val graph: Graph[Vertex, WDiHyperEdge] = Graph(e1, e2, e3, e4)
    override val maxDepth: Int = Integer.MAX_VALUE
    override val expectedResults: List[List[WDiHyperEdge[Vertex]]] = List(
      List(e1, e2, e3, e4)
    )
  }

  /*

      e1: f1(a)     -> b
      e2: f2(b)     -> c
      e3: f3(c)     -> d
      e3: f4(d)     -> b (LOOP)
      e4: f5(c,d)   -> e

      source: {a}
      target: {e}

      e1: f1(a)     -> b {a,b}
      e2: f2(b)     -> c {a,b,c}
      e3: f3(c)     -> d {a,b,c,d}
      e4: f5(c,d)   -> e {a,b,c,d,e}

      TARGET REACHED
   */

  protected val twoIndirectDependenciesWithLoop: TraversalTest = new TraversalTest {
    val e1 = e(t("a"), cu("f1"), t("b"))
    val e2 = e(t("b"), cu("f2"), t("c"))
    val e3 = e(t("c"), cu("f3"), t("d"))
    val e4 = e(t("d"), cu("f4"), t("b"))
    val e5 = e(t("c"), t("d"), cu("f5"), t("e"))
    override val target: Type = t("e")
    override val available: Set[Type] = Set(t("a"))
    override val graph: Graph[Vertex, WDiHyperEdge] = Graph(e1, e2, e3, e4, e5)
    override val maxDepth: Int = Integer.MAX_VALUE
    override val expectedResults: List[List[WDiHyperEdge[Vertex]]] = List(
      List(e1, e2, e3, e5)
    )
  }


  /*

    e1: f1(a)      -> b
    e2: f2(b)      -> c
    e3: f3(b)      -> d
    e4: f4(a,b,c,d)-> e

    source: {a}
    target: {e}

    e1: f1(a)      -> b {a,b}
    e2: f2(b)      -> c {a,b,c}
    e3: f3(b)      -> d {a,b,c,d}
    e4: f5(a,b,c,d)-> e {a,b,c,d,e}

    TARGET REACHED
 */

  protected val branchThenMergeAll: TraversalTest = new TraversalTest {
    val e1 = e(t("a"), cu("f1"), t("b"))
    val e2 = e(t("b"), cu("f2"), t("c"))
    val e3 = e(t("b"), cu("f3"), t("d"))
    val e4 = e(t("a"), t("b"), t("c"), t("d"), cu("f4"), t("e"))
    override val target: Type = t("e")
    override val available: Set[Type] = Set(t("a"))
    override val graph: Graph[Vertex, WDiHyperEdge] = Graph(e1, e2, e3, e4)
    override val maxDepth: Int = Integer.MAX_VALUE
    override val expectedResults: List[List[WDiHyperEdge[Vertex]]] = List(
      List(e1, e2, e3, e4)
    )
  }
  /*

      e1: f1(a)      -> b
      e2: f2(a)      -> b
      e3: f3(b, b)   -> c

      source: {a}
      target: {c}


      e1: f1(a)      -> b {a,b}
      e3: f3(b,b)    -> c {a,b,c}

      TARGET REACHED (1)

      e2: f1(a)      -> b {a,b}
      e3: f3(b,b)    -> c {a,b,c}

      TARGET REACHED (2)
   */

  // Note: here we can't differentiate between 2 instances of 'b' so they are deemed to be equal.
  protected val twoAlternativesProvidingInput: TraversalTest = new TraversalTest {
    val e1 = e(t("a"), cu("f1"), t("b"))
    val e2 = e(t("a"), cu("f2"), t("b"))
    val e3 = e(t("b"), t("b"), cu("f3"), t("c"))
    override val target: Type = t("c")
    override val available: Set[Type] = Set(t("a"))
    override val graph: Graph[Vertex, WDiHyperEdge] = Graph(e1, e2, e3)
    override val maxDepth: Int = Integer.MAX_VALUE
    override val expectedResults: List[List[WDiHyperEdge[Vertex]]] = List(
      List(e1, e3),
      List(e2, e3),
    )
  }

  // which parameters for d will be chosen for f5 at runtime?
  protected val dilemmaOfChoices: TraversalTest = new TraversalTest {
    val e1 = e(t("a"), cu("f1"), t("b"))
    val e2 = e(t("a"), cu("f2"), t("c"))
    val e3 = e(t("c"), cu("f3"), t("d"))
    val e4 = e(t("b"), cu("f4"), t("d"))
    val e5 = e(t("b"), t("c"), t("d"), t("d"), cu("f5"), t("e"))

    override val target: Type = t("e")
    override val available: Set[Type] = Set(t("a"))
    override val graph: Graph[Vertex, WDiHyperEdge] = Graph(e1, e2, e3, e4, e5)
    override val maxDepth: Int = Integer.MAX_VALUE
    override val expectedResults: List[List[WDiHyperEdge[Vertex]]] = List(
      List(e1, e2, e3, e5),
      List(e1, e2, e4, e5)
    )
  }

  // Based on the case above case, but searching for an intermediate result:
  protected val complexCaseSimpleResultWhereTargetIsInputOfAnotherEdge: TraversalTest = new TraversalTest {
    val e1 = e(t("a"), cu("f1"), t("b"))
    val e2 = e(t("a"), cu("f2"), t("c"))
    val e3 = e(t("c"), cu("f3"), t("d"))
    val e4 = e(t("b"), cu("f4"), t("d"))
    val e5 = e(t("b"), t("c"), t("d"), t("d"), cu("f5"), t("e"))

    override val target: Type = t("c") // So a single edge should be enough!
    override val available: Set[Type] = Set(t("a"))
    override val graph: Graph[Vertex, WDiHyperEdge] = Graph(e1, e2, e3, e4, e5)
    override val maxDepth: Int = Integer.MAX_VALUE
    override val expectedResults: List[List[WDiHyperEdge[Vertex]]] = List(
      List(e2)
    )
  }

  protected val regressionOneOfInputsIsGivenButCanAlsoBeProduced: TraversalTest = new TraversalTest {
    val e1 = e(t("a"), cu("f1"), t("b"))
    val e2 = e(t("b"), cu("f2"), t("c"))
    val e3 = e(t("a"), t("c"), cu("f3"), t("d"))
    val e4 = e(t("x"), cu("f4"), t("a"))

    override val target: Type = t("d")
    override val available: Set[Type] = Set(t("a"))
    override val graph: Graph[Vertex, WDiHyperEdge] = Graph(e1, e2, e3, e4)
    override val maxDepth: Int = Integer.MAX_VALUE
    override val expectedResults: List[List[WDiHyperEdge[Vertex]]] = List(
      List(e1, e2, e3)
    )
  }

  protected val regressionCycleViaEdge: TraversalTest = new TraversalTest {
    val e1 = e(t("cid"), cu("gpas"), t("pal"))
    val e2 = e(t("pal"), cu("fmapidosa"), t("sadl"))
    val e3 = e(t("cid"), t("sadl"), cu("join"), t("smr"))
    val e4 = e(t("smr"), cu("extract"), t("sadl"))

    override val target: Type = t("smr")
    override val available: Set[Type] = Set(t("cid"))
    override val graph: Graph[Vertex, WDiHyperEdge] = Graph(e1, e2, e3, e4)
    override val maxDepth: Int = Integer.MAX_VALUE
    override val expectedResults: List[List[WDiHyperEdge[Vertex]]] = List(
      List(e1, e2, e3)
    )
  }

  protected val multipleOptions: TraversalTest = new TraversalTest {
    val e1 = e(t("a"), cu("f"), t("b"))
    val e2 = e(t("a"), cu("g"), t("b"))
    val e3 = e(t("a"), cu("h"), t("b"))

    val e4 = e(t("b"), cu("i"), t("c"))
    val e5 = e(t("b"), cu("j"), t("c"))
    val e6 = e(t("b"), cu("k"), t("c"))

    val e7 = e(t("b"), t("c"), cu("x"), t("d"))

    override val target: Type = t("d")
    override val available: Set[Type] = Set(t("a"))
    override val graph: Graph[Vertex, WDiHyperEdge] = Graph(e1, e2, e3, e4, e5, e6, e7)
    override val maxDepth: Int = Integer.MAX_VALUE
    override val expectedResults: List[List[WDiHyperEdge[Vertex]]] = List(
      List(e1, e4, e7),
      List(e1, e5, e7),
      List(e1, e6, e7),
      List(e2, e4, e7),
      List(e2, e5, e7),
      List(e2, e6, e7),
      List(e3, e4, e7),
      List(e3, e5, e7),
      List(e3, e6, e7),
    )
  }

  protected val multipleOptionsMinimized: TraversalTest = new TraversalTest {
    val e1 = e(t("a"), cu("f"), t("b"))
    val e2 = e(t("a"), cu("g"), t("b"))

    val e3 = e(t("b"), cu("h"), t("c"))

    val e4 = e(t("b"), t("c"), cu("x"), t("d"))

    override val target: Type = t("d")
    override val available: Set[Type] = Set(t("a"))
    override val graph: Graph[Vertex, WDiHyperEdge] = Graph(e1, e2, e3, e4)
    override val maxDepth: Int = Integer.MAX_VALUE
    override val expectedResults: List[List[WDiHyperEdge[Vertex]]] = List(
      List(e1, e3, e4),
      List(e2, e3, e4),
    )
  }

  protected val multipleOptionsMinimizedPrefixedTrace: TraversalTest = new TraversalTest {
    val e0 = e(t("α"), cu("β"), t("a"))
    val e1 = e(t("a"), cu("f"), t("b"))
    val e2 = e(t("a"), cu("g"), t("b"))

    val e3 = e(t("b"), cu("h"), t("c"))

    val e4 = e(t("b"), t("c"), cu("x"), t("d"))

    override val target: Type = t("d")
    override val available: Set[Type] = Set(t("α"))
    override val graph: Graph[Vertex, WDiHyperEdge] = Graph(e0, e1, e2, e3, e4)
    override val maxDepth: Int = Integer.MAX_VALUE
    override val expectedResults: List[List[WDiHyperEdge[Vertex]]] = List(
      List(e0, e1, e3, e4),
      List(e0, e2, e3, e4),
    )
  }

  protected val multipleOptionsMinimizedPrefixedChoices: TraversalTest = new TraversalTest {
    val e1 = e(t("a"), cu("f"), t("c"))
    val e2 = e(t("b"), cu("g"), t("c"))

    val e3 = e(t("c"), cu("h"), t("d"))
    val e4 = e(t("c"), cu("i"), t("d"))

    val e5 = e(t("c"), t("d"), cu("j"), t("e"))

    override val target: Type = t("e")
    override val available: Set[Type] = Set(t("a"), t("b"))
    override val graph: Graph[Vertex, WDiHyperEdge] = Graph(e1, e2, e3, e4, e5)
    override val maxDepth: Int = Integer.MAX_VALUE
    override val expectedResults: List[List[WDiHyperEdge[Vertex]]] = List(
      List(e1, e3, e5),
      List(e1, e4, e5),
      List(e2, e3, e5),
      List(e2, e4, e5)
    )
  }

  protected val multipleOptionsWithBackReferences: TraversalTest = new TraversalTest {
    val e1 = e(t("a"), cu("f"), t("b"))
    val e2 = e(t("a"), cu("g"), t("b"))
    val e3 = e(t("a"), cu("h"), t("b"))

    val e4 = e(t("b"), cu("i"), t("c"))
    val e5 = e(t("b"), cu("j"), t("c"))
    val e6 = e(t("b"), cu("k"), t("c"))

    val e7 = e(t("b"), cu("l"), t("a"))
    val e8 = e(t("c"), cu("l"), t("a"))

    val e9 = e(t("b"), t("c"), cu("x"), t("d"))

    override val target: Type = t("d")
    override val available: Set[Type] = Set(t("a"))
    override val graph: Graph[Vertex, WDiHyperEdge] = Graph(e1, e2, e3, e4, e5, e6, e7, e8, e9)
    override val maxDepth: Int = Integer.MAX_VALUE
    override val expectedResults: List[List[WDiHyperEdge[Vertex]]] = List(
      List(e1, e4, e9),
      List(e1, e5, e9),
      List(e1, e6, e9),
      List(e2, e4, e9),
      List(e2, e5, e9),
      List(e2, e6, e9),
      List(e3, e4, e9),
      List(e3, e5, e9),
      List(e3, e6, e9),
    )
  }

  protected val unresolvedDependencies: TraversalTest = new TraversalTest {
    val e1 = e(t("a"), cu("f"), t("c"))
    val e2 = e(t("b"), cu("g"), t("c"))
    val e3 = e(t("c"), cu("h"), t("d"))
    val e4 = e(t("a"), t("b"), t("d"), t("z"), cu("i"), t("e"))
    val e5 = e(t("a"), t("e"), t("b"), cu("j"), t("x"))

    override val target: Type = t("x")
    override val available: Set[Type] = Set(t("a"), t("b"))
    override val graph: Graph[Vertex, WDiHyperEdge] = Graph(e1, e2, e3, e4, e5)
    override val expectedResults: List[List[WDiHyperEdge[Vertex]]] = List.empty
    override val maxDepth: Int = Integer.MAX_VALUE
  }

  protected val helloWorldModel: TraversalTest = new TraversalTest {
    val e1 = e(t("cid"), cu("gpas"), t("pal"))
    val e2 = e(t("pal"), cu("fmapidosa"), t("sadl"))
    val e3 = e(t("cid"), t("sadl"), cu("join"), t("smr"))
    val e4 = e(t("smr"), cu("extract"), t("sadl"))

    override val target: Type = t("smr")
    override val available: Set[Type] = Set(t("cid"))
    override val graph: Graph[Vertex, WDiHyperEdge] = Graph(e1, e2, e3, e4)
    override val maxDepth: Int = Integer.MAX_VALUE
    override val expectedResults: List[List[WDiHyperEdge[Vertex]]] = List(
      List(e1, e2, e3)
    )
  }

  protected val noStackOverflowError: TraversalTest = new TraversalTest {
    val edges = List.range(0, 2500).map(i => {
      e(t(s"d${i - 1}"), cu(s"f$i"), t(s"d$i"))
    })
    override val target: Type = edges.last.last.asInstanceOf[Type]
    override val available: Set[Type] = Set(edges.head.head.asInstanceOf[Type])
    override val graph: Graph[Vertex, WDiHyperEdge] = Graph(edges: _*)
    override val maxDepth: Int = Integer.MAX_VALUE
    override val expectedResults: List[List[WDiHyperEdge[Vertex]]] = List(
      edges
    )
  }

  protected val dontConflateAlternativesAtChoice: TraversalTest = new TraversalTest {
    val e1a = e(t("x"), cu("f1"), t("y"))
    val e1b = e(cu("f2"), t("y"))
    val e1c = e(t("x"), cu("f3"), t("y"))
    val e2 = e(t("y"), cu("g"), t("z1"))
    val e3 = e(t("x"), t("z1"), cu("h"), t("z2"))

    override val target: Type = t("z2")
    override val available: Set[Type] = Set(t("x"))
    override val graph: Graph[Vertex, WDiHyperEdge] = Graph(e1a, e1b, e1c, e2, e3)
    override val maxDepth: Int = Integer.MAX_VALUE
    override val expectedResults: List[List[WDiHyperEdge[Vertex]]] = List(
      List(e1a, e2, e3),
      List(e1b, e2, e3),
      List(e1c, e2, e3)
    )
  }

  protected val completeHelloWorldModel: TraversalTest = CompleteHelloWorldModel()
}
