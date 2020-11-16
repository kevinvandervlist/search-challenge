package com.ing.sea.pdeng.graph.dot

import java.io.{File, PrintWriter}

object ImageWriter {
  def dumpToFile(target: File, dot: String): Unit = {
    val writer: PrintWriter =
      new PrintWriter(new File(target.getParentFile, s"${target.getName}.dot"))

    try {
      writer.write(dot)
    } finally {
      writer.close()
    }
  }

  /** Automatically suffix each dot with a number */
  def dumpToFile(target: File, dots: Iterable[String]): Unit = for((d,i) <- dots.view.zipWithIndex) {
    dumpToFile(new File(target.getParentFile, f"${target.getName}-$i%03d"), d)
  }
}
