package com.ing.sea.pdeng.graph.csv

import java.io.File

import com.ing.sea.pdeng.graph.Vertex.e
import com.ing.sea.pdeng.graph.{CallableUnit, Type, Vertex}
import scalax.collection.Graph
import scalax.collection.edge.WDiHyperEdge

import scala.util.Try

object CSVReader {
  def apply(csvOfGraph: File): Try[Graph[Vertex, WDiHyperEdge]] = Try {
    val csv = io.Source.fromFile(csvOfGraph.getAbsolutePath)
    val g = fromCSV(csv.getLines())
    csv.close
    g
  }

  def fromCSV(csvLines: Iterator[String]): Graph[Vertex, WDiHyperEdge] = {
    val edges = csvLines.drop(1) //Drop the CSV header
      .map(_.split(";").map(_.trim).toList)
      .map(elems => {
        val ret :: f = elems
        val cu :: args = f
        args
          .reverse // And reverse because the order in the csv is like that
          .map(Type.apply) ++ List(CallableUnit(cu), Type(ret))
      })
      .map(edge => e(edge: _*))
    Graph(edges.toSeq: _*)
  }
}
