package com.ing.sea.pdeng.graph.csv

import java.io.{File, PrintWriter}

import com.ing.sea.pdeng.graph.Vertex
import scalax.collection.Graph
import scalax.collection.edge.WDiHyperEdge

import scala.util.Try

object CSVWriter {
  def apply(g: Graph[Vertex, WDiHyperEdge], target: File): Try[Unit] = Try {
    val testcaseWriter: PrintWriter = new PrintWriter(target)
    testcaseWriter.write(toCSV(g))
    testcaseWriter.close()
  }

  def toCSV(g: Graph[Vertex, WDiHyperEdge]): String = {
    val allEdges = g.edges
      .map(_.toOuter)
      .map(_.map(_.name))
      .map(_.toVector.reverse) // Reverse because that's how we organize the CSV

    val columnCount: Int = allEdges.foldLeft(0) {
      case (max, edge) =>
        if (max >= edge.size) {
          max
        } else {
          edge.size
        }
    }

    def row(elements: Iterable[String]): String =
      (1 to columnCount).zipAll(elements, "", "").map(_._2).mkString(";")

    val header = row(
      List("output type", "callable unit") ++ (1 to (columnCount - 2)).map(n => s"arg $n")
    )

    allEdges.foldLeft(new StringBuilder(header + "\n")) {
      case (sb, e) => sb.append(row(e)).append("\n")
    }.toString()
  }
}
