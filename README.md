# Search Challenge

## Description
The challenge is implementing a search algorithm for a hyper graph, and doing so in an 'optimal' way. 
We have no hard guidelines in place, but the faster your approach, the better it will be.
As for memory usage: assume it will be run on a standard personal laptop, so be thoughtful about this as well. 

The characteristics of the graph are the following:
* The graph is a hypergraph
* Each edge represents something that is similar to a function call: 
  * There are [0..n] arguments
  * There is one name
  * There is one result
  
### An example graph
* The graph H consists of the following edges: (1) `a, b, f, c`, (2) `g, d` and (3) `c, d, h, e`. 
* Here, `a`, `b`, `c`, `d` and `e` are `Type`s. 
* Accordingly, `f`, `g` and `h` are `Callable Unit`s.

### Searching
The search algorithm has three inputs:
* A graph
* A *single* `goal` (which is always a `Type`).
* Zero or more `given`s (which are always `Type`s)

Any search algorithm must (eventually) yield all traces that produce the `goal`, based on the set of `given` `Type`s. 
During traversal, in order to decide whether a single edge can be contained in a solution for the search, one must evaluate the _usefulness_ of an edge.
If we have our example graph, and are looking at edge (3), we can only assume it to be `useful` iff both `Type`s `c` and `d` can be produced. 
In order for those types to be produced, we again look at the same question. 
So, if we look at edge (2), we know we can produce `d` because `Callable Unit` `g` does not have any arguments. 
Looking at edge (1), we see that in order to produce `c`, we need to have an `a` and `b` available. 
Given there are no other edges in the example graph, there is only a valid search result available iff `a` and `b` are in the initial `given` set.

As you can see, the challenge of this search algorithm lies in the combinatoric aspects of evaluating the usefulness of an edge, because everything that precedes it must be taken into account. 
So, we are looking for the most effective way for you to solve this problem!

You can use any approach you want. 
Pruning the graph, caching (intermediate) results, everything is permitted as long as an implementation satisfies the test suite. 
Of course, when things more tests might be added to explain any inconsistencies. 

## Defining an implementation
You can implement your own implementation in either of these classes:
* [Scala](src/main/scala/com/ing/sea/pdeng/graph/search/EfficientSearchStrategy.scala)
* [Java](src/main/java/com/ing/sea/pdeng/graph/search/EfficientSearchStrategy.java)

## Evaluating your implementation
After doing so, you can run all tests against your implementation by enabling the testsuite in the following class:
* [Small test suite](src/test/scala/com/ing/sea/pdeng/graph/search/TypeGraphSearchSpec.scala)
* [A larger graph](src/test/scala/com/ing/sea/pdeng/graph/search/TypeGraphSearchFilesSpec.scala)

## Examples
You can look at the small testcases for a number of examples: [SearchTestCases](src/test/scala/com/ing/sea/pdeng/graph/search/testcases/SearchTestCases.scala).
This can be useful to figure out what is expected behaviour in specific situations. 

When there are failures in any of the tests, all results will be written to disk so you can visually analyse the results. 
Note that because of graphviz limitations, all graphs will be serialized as normal graphs instead of hypergraphs, so you need to manually keep track of this. 
