# Search Challenge

## Defining an implementation
You can implement your own implementation in either of these classes:
* [Scala](src/main/scala/com/ing/sea/pdeng/graph/search/EfficientSearchStrategy.scala)
* [Java](src/main/java/com/ing/sea/pdeng/graph/search/EfficientSearchStrategy.java)

## Evaluating your implementation
After doing so, you can run all tests against your implementation by enabling the testsuite in the following class:
* [Small test suite](src/test/scala/com/ing/sea/pdeng/graph/search/TypeGraphSearchSpec.scala)
* [A larger graph](src/test/scala/com/ing/sea/pdeng/graph/search/TypeGraphSearchFilesSpec.scala)

When there are failures in any of the tests, all results will be written to disk so you can visually analyse the results. 
Note that because of graphviz limitations, all graphs will be serialized as normal graphs instead of hypergraphs, so you need to manually keep track of this. 
