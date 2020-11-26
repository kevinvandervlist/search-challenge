package com.ing.techrd.minoa

import scala.annotation.tailrec
import scala.collection.immutable.ArraySeq

// AUTOGENERATED CODE
object CartesianProduct {
  /** A slower, but more convenient method */
  def productC[T](in: Iterable[Iterable[T]]): IndexedSeq[IndexedSeq[T]] =
    product(in.map(_.toVector).toVector)

  // Slow, but always works regardless of size of `in`
  private def product_generic[T](in: IndexedSeq[IndexedSeq[T]]): IndexedSeq[IndexedSeq[T]] = {
    @tailrec
    def loop[T](acc: List[List[T]], rest: List[List[T]]): List[List[T]] = rest match {
      case Nil => acc
      case head :: tail =>
        val newAcc: List[List[T]] = for {
          i <- head
          a <- acc
        } yield i :: a
        loop(newAcc, tail)
    }

    loop(List(List.empty), in.toList.map(_.toList)).map(_.toVector).toVector
  }

  def product[T](in: IndexedSeq[IndexedSeq[T]]): IndexedSeq[IndexedSeq[T]] = in.size match {
    case 0 => Vector.empty
    case 1 => product_1(in)
    case 2 => product_2(in)
    case 3 => product_3(in)
    case 4 => product_4(in)
    case 5 => product_5(in)
    case 6 => product_6(in)
    case 7 => product_7(in)
    case 8 => product_8(in)
    case 9 => product_9(in)
    case 10 => product_10(in)
    case 11 => product_11(in)
    case 12 => product_12(in)
    case 13 => product_13(in)
    case 14 => product_14(in)
    case 15 => product_15(in)
    case 16 => product_16(in)
    case _ => product_generic(in)
  }

  private def product_1[T](in: IndexedSeq[IndexedSeq[T]]): IndexedSeq[IndexedSeq[T]] = {
    val len_0 = in(0).length

    val lb = new Array[IndexedSeq[T]](len_0)
    var idx = 0

    var index_0 = 0
    index_0 = 0
    while (index_0 < len_0) {
      lb(idx) = Vector(
        in(0)(index_0)
      )
      idx += 1

      index_0 += 1
    }

    ArraySeq.unsafeWrapArray(lb)
  }

  private def product_2[T](in: IndexedSeq[IndexedSeq[T]]): IndexedSeq[IndexedSeq[T]] = {
    val len_0 = in(0).length
    val len_1 = in(1).length

    val lb = new Array[IndexedSeq[T]](len_0 * len_1)
    var idx = 0

    var index_0 = 0
    var index_1 = 0
    index_0 = 0
    while (index_0 < len_0) {
      index_1 = 0
      while (index_1 < len_1) {
        lb(idx) = Vector(
          in(0)(index_0),
          in(1)(index_1)
        )
        idx += 1

        index_1 += 1
      }

      index_0 += 1
    }

    ArraySeq.unsafeWrapArray(lb)
  }

  private def product_3[T](in: IndexedSeq[IndexedSeq[T]]): IndexedSeq[IndexedSeq[T]] = {
    val len_0 = in(0).length
    val len_1 = in(1).length
    val len_2 = in(2).length

    val lb = new Array[IndexedSeq[T]](len_0 * len_1 * len_2)
    var idx = 0

    var index_0 = 0
    var index_1 = 0
    var index_2 = 0
    index_0 = 0
    while (index_0 < len_0) {
      index_1 = 0
      while (index_1 < len_1) {
        index_2 = 0
        while (index_2 < len_2) {
          lb(idx) = Vector(
            in(0)(index_0),
            in(1)(index_1),
            in(2)(index_2)
          )
          idx += 1

          index_2 += 1
        }

        index_1 += 1
      }

      index_0 += 1
    }

    ArraySeq.unsafeWrapArray(lb)
  }

  private def product_4[T](in: IndexedSeq[IndexedSeq[T]]): IndexedSeq[IndexedSeq[T]] = {
    val len_0 = in(0).length
    val len_1 = in(1).length
    val len_2 = in(2).length
    val len_3 = in(3).length

    val lb = new Array[IndexedSeq[T]](len_0 * len_1 * len_2 * len_3)
    var idx = 0

    var index_0 = 0
    var index_1 = 0
    var index_2 = 0
    var index_3 = 0
    index_0 = 0
    while (index_0 < len_0) {
      index_1 = 0
      while (index_1 < len_1) {
        index_2 = 0
        while (index_2 < len_2) {
          index_3 = 0
          while (index_3 < len_3) {
            lb(idx) = Vector(
              in(0)(index_0),
              in(1)(index_1),
              in(2)(index_2),
              in(3)(index_3)
            )
            idx += 1

            index_3 += 1
          }

          index_2 += 1
        }

        index_1 += 1
      }

      index_0 += 1
    }

    ArraySeq.unsafeWrapArray(lb)
  }

  private def product_5[T](in: IndexedSeq[IndexedSeq[T]]): IndexedSeq[IndexedSeq[T]] = {
    val len_0 = in(0).length
    val len_1 = in(1).length
    val len_2 = in(2).length
    val len_3 = in(3).length
    val len_4 = in(4).length

    val lb = new Array[IndexedSeq[T]](len_0 * len_1 * len_2 * len_3 * len_4)
    var idx = 0

    var index_0 = 0
    var index_1 = 0
    var index_2 = 0
    var index_3 = 0
    var index_4 = 0
    index_0 = 0
    while (index_0 < len_0) {
      index_1 = 0
      while (index_1 < len_1) {
        index_2 = 0
        while (index_2 < len_2) {
          index_3 = 0
          while (index_3 < len_3) {
            index_4 = 0
            while (index_4 < len_4) {
              lb(idx) = Vector(
                in(0)(index_0),
                in(1)(index_1),
                in(2)(index_2),
                in(3)(index_3),
                in(4)(index_4)
              )
              idx += 1

              index_4 += 1
            }

            index_3 += 1
          }

          index_2 += 1
        }

        index_1 += 1
      }

      index_0 += 1
    }

    ArraySeq.unsafeWrapArray(lb)
  }

  private def product_6[T](in: IndexedSeq[IndexedSeq[T]]): IndexedSeq[IndexedSeq[T]] = {
    val len_0 = in(0).length
    val len_1 = in(1).length
    val len_2 = in(2).length
    val len_3 = in(3).length
    val len_4 = in(4).length
    val len_5 = in(5).length

    val lb = new Array[IndexedSeq[T]](len_0 * len_1 * len_2 * len_3 * len_4 * len_5)
    var idx = 0

    var index_0 = 0
    var index_1 = 0
    var index_2 = 0
    var index_3 = 0
    var index_4 = 0
    var index_5 = 0
    index_0 = 0
    while (index_0 < len_0) {
      index_1 = 0
      while (index_1 < len_1) {
        index_2 = 0
        while (index_2 < len_2) {
          index_3 = 0
          while (index_3 < len_3) {
            index_4 = 0
            while (index_4 < len_4) {
              index_5 = 0
              while (index_5 < len_5) {
                lb(idx) = Vector(
                  in(0)(index_0),
                  in(1)(index_1),
                  in(2)(index_2),
                  in(3)(index_3),
                  in(4)(index_4),
                  in(5)(index_5)
                )
                idx += 1

                index_5 += 1
              }

              index_4 += 1
            }

            index_3 += 1
          }

          index_2 += 1
        }

        index_1 += 1
      }

      index_0 += 1
    }

    ArraySeq.unsafeWrapArray(lb)
  }

  private def product_7[T](in: IndexedSeq[IndexedSeq[T]]): IndexedSeq[IndexedSeq[T]] = {
    val len_0 = in(0).length
    val len_1 = in(1).length
    val len_2 = in(2).length
    val len_3 = in(3).length
    val len_4 = in(4).length
    val len_5 = in(5).length
    val len_6 = in(6).length

    val lb = new Array[IndexedSeq[T]](len_0 * len_1 * len_2 * len_3 * len_4 * len_5 * len_6)
    var idx = 0

    var index_0 = 0
    var index_1 = 0
    var index_2 = 0
    var index_3 = 0
    var index_4 = 0
    var index_5 = 0
    var index_6 = 0
    index_0 = 0
    while (index_0 < len_0) {
      index_1 = 0
      while (index_1 < len_1) {
        index_2 = 0
        while (index_2 < len_2) {
          index_3 = 0
          while (index_3 < len_3) {
            index_4 = 0
            while (index_4 < len_4) {
              index_5 = 0
              while (index_5 < len_5) {
                index_6 = 0
                while (index_6 < len_6) {
                  lb(idx) = Vector(
                    in(0)(index_0),
                    in(1)(index_1),
                    in(2)(index_2),
                    in(3)(index_3),
                    in(4)(index_4),
                    in(5)(index_5),
                    in(6)(index_6)
                  )
                  idx += 1

                  index_6 += 1
                }

                index_5 += 1
              }

              index_4 += 1
            }

            index_3 += 1
          }

          index_2 += 1
        }

        index_1 += 1
      }

      index_0 += 1
    }

    ArraySeq.unsafeWrapArray(lb)
  }

  private def product_8[T](in: IndexedSeq[IndexedSeq[T]]): IndexedSeq[IndexedSeq[T]] = {
    val len_0 = in(0).length
    val len_1 = in(1).length
    val len_2 = in(2).length
    val len_3 = in(3).length
    val len_4 = in(4).length
    val len_5 = in(5).length
    val len_6 = in(6).length
    val len_7 = in(7).length

    val lb = new Array[IndexedSeq[T]](len_0 * len_1 * len_2 * len_3 * len_4 * len_5 * len_6 * len_7)
    var idx = 0

    var index_0 = 0
    var index_1 = 0
    var index_2 = 0
    var index_3 = 0
    var index_4 = 0
    var index_5 = 0
    var index_6 = 0
    var index_7 = 0
    index_0 = 0
    while (index_0 < len_0) {
      index_1 = 0
      while (index_1 < len_1) {
        index_2 = 0
        while (index_2 < len_2) {
          index_3 = 0
          while (index_3 < len_3) {
            index_4 = 0
            while (index_4 < len_4) {
              index_5 = 0
              while (index_5 < len_5) {
                index_6 = 0
                while (index_6 < len_6) {
                  index_7 = 0
                  while (index_7 < len_7) {
                    lb(idx) = Vector(
                      in(0)(index_0),
                      in(1)(index_1),
                      in(2)(index_2),
                      in(3)(index_3),
                      in(4)(index_4),
                      in(5)(index_5),
                      in(6)(index_6),
                      in(7)(index_7)
                    )
                    idx += 1

                    index_7 += 1
                  }

                  index_6 += 1
                }

                index_5 += 1
              }

              index_4 += 1
            }

            index_3 += 1
          }

          index_2 += 1
        }

        index_1 += 1
      }

      index_0 += 1
    }

    ArraySeq.unsafeWrapArray(lb)
  }

  private def product_9[T](in: IndexedSeq[IndexedSeq[T]]): IndexedSeq[IndexedSeq[T]] = {
    val len_0 = in(0).length
    val len_1 = in(1).length
    val len_2 = in(2).length
    val len_3 = in(3).length
    val len_4 = in(4).length
    val len_5 = in(5).length
    val len_6 = in(6).length
    val len_7 = in(7).length
    val len_8 = in(8).length

    val lb = new Array[IndexedSeq[T]](len_0 * len_1 * len_2 * len_3 * len_4 * len_5 * len_6 * len_7 * len_8)
    var idx = 0

    var index_0 = 0
    var index_1 = 0
    var index_2 = 0
    var index_3 = 0
    var index_4 = 0
    var index_5 = 0
    var index_6 = 0
    var index_7 = 0
    var index_8 = 0
    index_0 = 0
    while (index_0 < len_0) {
      index_1 = 0
      while (index_1 < len_1) {
        index_2 = 0
        while (index_2 < len_2) {
          index_3 = 0
          while (index_3 < len_3) {
            index_4 = 0
            while (index_4 < len_4) {
              index_5 = 0
              while (index_5 < len_5) {
                index_6 = 0
                while (index_6 < len_6) {
                  index_7 = 0
                  while (index_7 < len_7) {
                    index_8 = 0
                    while (index_8 < len_8) {
                      lb(idx) = Vector(
                        in(0)(index_0),
                        in(1)(index_1),
                        in(2)(index_2),
                        in(3)(index_3),
                        in(4)(index_4),
                        in(5)(index_5),
                        in(6)(index_6),
                        in(7)(index_7),
                        in(8)(index_8)
                      )
                      idx += 1

                      index_8 += 1
                    }

                    index_7 += 1
                  }

                  index_6 += 1
                }

                index_5 += 1
              }

              index_4 += 1
            }

            index_3 += 1
          }

          index_2 += 1
        }

        index_1 += 1
      }

      index_0 += 1
    }

    ArraySeq.unsafeWrapArray(lb)
  }

  private def product_10[T](in: IndexedSeq[IndexedSeq[T]]): IndexedSeq[IndexedSeq[T]] = {
    val len_0 = in(0).length
    val len_1 = in(1).length
    val len_2 = in(2).length
    val len_3 = in(3).length
    val len_4 = in(4).length
    val len_5 = in(5).length
    val len_6 = in(6).length
    val len_7 = in(7).length
    val len_8 = in(8).length
    val len_9 = in(9).length

    val lb = new Array[IndexedSeq[T]](len_0 * len_1 * len_2 * len_3 * len_4 * len_5 * len_6 * len_7 * len_8 * len_9)
    var idx = 0

    var index_0 = 0
    var index_1 = 0
    var index_2 = 0
    var index_3 = 0
    var index_4 = 0
    var index_5 = 0
    var index_6 = 0
    var index_7 = 0
    var index_8 = 0
    var index_9 = 0
    index_0 = 0
    while (index_0 < len_0) {
      index_1 = 0
      while (index_1 < len_1) {
        index_2 = 0
        while (index_2 < len_2) {
          index_3 = 0
          while (index_3 < len_3) {
            index_4 = 0
            while (index_4 < len_4) {
              index_5 = 0
              while (index_5 < len_5) {
                index_6 = 0
                while (index_6 < len_6) {
                  index_7 = 0
                  while (index_7 < len_7) {
                    index_8 = 0
                    while (index_8 < len_8) {
                      index_9 = 0
                      while (index_9 < len_9) {
                        lb(idx) = Vector(
                          in(0)(index_0),
                          in(1)(index_1),
                          in(2)(index_2),
                          in(3)(index_3),
                          in(4)(index_4),
                          in(5)(index_5),
                          in(6)(index_6),
                          in(7)(index_7),
                          in(8)(index_8),
                          in(9)(index_9)
                        )
                        idx += 1

                        index_9 += 1
                      }

                      index_8 += 1
                    }

                    index_7 += 1
                  }

                  index_6 += 1
                }

                index_5 += 1
              }

              index_4 += 1
            }

            index_3 += 1
          }

          index_2 += 1
        }

        index_1 += 1
      }

      index_0 += 1
    }

    ArraySeq.unsafeWrapArray(lb)
  }

  private def product_11[T](in: IndexedSeq[IndexedSeq[T]]): IndexedSeq[IndexedSeq[T]] = {
    val len_0 = in(0).length
    val len_1 = in(1).length
    val len_2 = in(2).length
    val len_3 = in(3).length
    val len_4 = in(4).length
    val len_5 = in(5).length
    val len_6 = in(6).length
    val len_7 = in(7).length
    val len_8 = in(8).length
    val len_9 = in(9).length
    val len_10 = in(10).length

    val lb = new Array[IndexedSeq[T]](len_0 * len_1 * len_2 * len_3 * len_4 * len_5 * len_6 * len_7 * len_8 * len_9 * len_10)
    var idx = 0

    var index_0 = 0
    var index_1 = 0
    var index_2 = 0
    var index_3 = 0
    var index_4 = 0
    var index_5 = 0
    var index_6 = 0
    var index_7 = 0
    var index_8 = 0
    var index_9 = 0
    var index_10 = 0
    index_0 = 0
    while (index_0 < len_0) {
      index_1 = 0
      while (index_1 < len_1) {
        index_2 = 0
        while (index_2 < len_2) {
          index_3 = 0
          while (index_3 < len_3) {
            index_4 = 0
            while (index_4 < len_4) {
              index_5 = 0
              while (index_5 < len_5) {
                index_6 = 0
                while (index_6 < len_6) {
                  index_7 = 0
                  while (index_7 < len_7) {
                    index_8 = 0
                    while (index_8 < len_8) {
                      index_9 = 0
                      while (index_9 < len_9) {
                        index_10 = 0
                        while (index_10 < len_10) {
                          lb(idx) = Vector(
                            in(0)(index_0),
                            in(1)(index_1),
                            in(2)(index_2),
                            in(3)(index_3),
                            in(4)(index_4),
                            in(5)(index_5),
                            in(6)(index_6),
                            in(7)(index_7),
                            in(8)(index_8),
                            in(9)(index_9),
                            in(10)(index_10)
                          )
                          idx += 1

                          index_10 += 1
                        }

                        index_9 += 1
                      }

                      index_8 += 1
                    }

                    index_7 += 1
                  }

                  index_6 += 1
                }

                index_5 += 1
              }

              index_4 += 1
            }

            index_3 += 1
          }

          index_2 += 1
        }

        index_1 += 1
      }

      index_0 += 1
    }

    ArraySeq.unsafeWrapArray(lb)
  }

  private def product_12[T](in: IndexedSeq[IndexedSeq[T]]): IndexedSeq[IndexedSeq[T]] = {
    val len_0 = in(0).length
    val len_1 = in(1).length
    val len_2 = in(2).length
    val len_3 = in(3).length
    val len_4 = in(4).length
    val len_5 = in(5).length
    val len_6 = in(6).length
    val len_7 = in(7).length
    val len_8 = in(8).length
    val len_9 = in(9).length
    val len_10 = in(10).length
    val len_11 = in(11).length

    val lb = new Array[IndexedSeq[T]](len_0 * len_1 * len_2 * len_3 * len_4 * len_5 * len_6 * len_7 * len_8 * len_9 * len_10 * len_11)
    var idx = 0

    var index_0 = 0
    var index_1 = 0
    var index_2 = 0
    var index_3 = 0
    var index_4 = 0
    var index_5 = 0
    var index_6 = 0
    var index_7 = 0
    var index_8 = 0
    var index_9 = 0
    var index_10 = 0
    var index_11 = 0
    index_0 = 0
    while (index_0 < len_0) {
      index_1 = 0
      while (index_1 < len_1) {
        index_2 = 0
        while (index_2 < len_2) {
          index_3 = 0
          while (index_3 < len_3) {
            index_4 = 0
            while (index_4 < len_4) {
              index_5 = 0
              while (index_5 < len_5) {
                index_6 = 0
                while (index_6 < len_6) {
                  index_7 = 0
                  while (index_7 < len_7) {
                    index_8 = 0
                    while (index_8 < len_8) {
                      index_9 = 0
                      while (index_9 < len_9) {
                        index_10 = 0
                        while (index_10 < len_10) {
                          index_11 = 0
                          while (index_11 < len_11) {
                            lb(idx) = Vector(
                              in(0)(index_0),
                              in(1)(index_1),
                              in(2)(index_2),
                              in(3)(index_3),
                              in(4)(index_4),
                              in(5)(index_5),
                              in(6)(index_6),
                              in(7)(index_7),
                              in(8)(index_8),
                              in(9)(index_9),
                              in(10)(index_10),
                              in(11)(index_11)
                            )
                            idx += 1

                            index_11 += 1
                          }

                          index_10 += 1
                        }

                        index_9 += 1
                      }

                      index_8 += 1
                    }

                    index_7 += 1
                  }

                  index_6 += 1
                }

                index_5 += 1
              }

              index_4 += 1
            }

            index_3 += 1
          }

          index_2 += 1
        }

        index_1 += 1
      }

      index_0 += 1
    }

    ArraySeq.unsafeWrapArray(lb)
  }

  private def product_13[T](in: IndexedSeq[IndexedSeq[T]]): IndexedSeq[IndexedSeq[T]] = {
    val len_0 = in(0).length
    val len_1 = in(1).length
    val len_2 = in(2).length
    val len_3 = in(3).length
    val len_4 = in(4).length
    val len_5 = in(5).length
    val len_6 = in(6).length
    val len_7 = in(7).length
    val len_8 = in(8).length
    val len_9 = in(9).length
    val len_10 = in(10).length
    val len_11 = in(11).length
    val len_12 = in(12).length

    val lb = new Array[IndexedSeq[T]](len_0 * len_1 * len_2 * len_3 * len_4 * len_5 * len_6 * len_7 * len_8 * len_9 * len_10 * len_11 * len_12)
    var idx = 0

    var index_0 = 0
    var index_1 = 0
    var index_2 = 0
    var index_3 = 0
    var index_4 = 0
    var index_5 = 0
    var index_6 = 0
    var index_7 = 0
    var index_8 = 0
    var index_9 = 0
    var index_10 = 0
    var index_11 = 0
    var index_12 = 0
    index_0 = 0
    while (index_0 < len_0) {
      index_1 = 0
      while (index_1 < len_1) {
        index_2 = 0
        while (index_2 < len_2) {
          index_3 = 0
          while (index_3 < len_3) {
            index_4 = 0
            while (index_4 < len_4) {
              index_5 = 0
              while (index_5 < len_5) {
                index_6 = 0
                while (index_6 < len_6) {
                  index_7 = 0
                  while (index_7 < len_7) {
                    index_8 = 0
                    while (index_8 < len_8) {
                      index_9 = 0
                      while (index_9 < len_9) {
                        index_10 = 0
                        while (index_10 < len_10) {
                          index_11 = 0
                          while (index_11 < len_11) {
                            index_12 = 0
                            while (index_12 < len_12) {
                              lb(idx) = Vector(
                                in(0)(index_0),
                                in(1)(index_1),
                                in(2)(index_2),
                                in(3)(index_3),
                                in(4)(index_4),
                                in(5)(index_5),
                                in(6)(index_6),
                                in(7)(index_7),
                                in(8)(index_8),
                                in(9)(index_9),
                                in(10)(index_10),
                                in(11)(index_11),
                                in(12)(index_12)
                              )
                              idx += 1

                              index_12 += 1
                            }

                            index_11 += 1
                          }

                          index_10 += 1
                        }

                        index_9 += 1
                      }

                      index_8 += 1
                    }

                    index_7 += 1
                  }

                  index_6 += 1
                }

                index_5 += 1
              }

              index_4 += 1
            }

            index_3 += 1
          }

          index_2 += 1
        }

        index_1 += 1
      }

      index_0 += 1
    }

    ArraySeq.unsafeWrapArray(lb)
  }

  private def product_14[T](in: IndexedSeq[IndexedSeq[T]]): IndexedSeq[IndexedSeq[T]] = {
    val len_0 = in(0).length
    val len_1 = in(1).length
    val len_2 = in(2).length
    val len_3 = in(3).length
    val len_4 = in(4).length
    val len_5 = in(5).length
    val len_6 = in(6).length
    val len_7 = in(7).length
    val len_8 = in(8).length
    val len_9 = in(9).length
    val len_10 = in(10).length
    val len_11 = in(11).length
    val len_12 = in(12).length
    val len_13 = in(13).length

    val lb = new Array[IndexedSeq[T]](len_0 * len_1 * len_2 * len_3 * len_4 * len_5 * len_6 * len_7 * len_8 * len_9 * len_10 * len_11 * len_12 * len_13)
    var idx = 0

    var index_0 = 0
    var index_1 = 0
    var index_2 = 0
    var index_3 = 0
    var index_4 = 0
    var index_5 = 0
    var index_6 = 0
    var index_7 = 0
    var index_8 = 0
    var index_9 = 0
    var index_10 = 0
    var index_11 = 0
    var index_12 = 0
    var index_13 = 0
    index_0 = 0
    while (index_0 < len_0) {
      index_1 = 0
      while (index_1 < len_1) {
        index_2 = 0
        while (index_2 < len_2) {
          index_3 = 0
          while (index_3 < len_3) {
            index_4 = 0
            while (index_4 < len_4) {
              index_5 = 0
              while (index_5 < len_5) {
                index_6 = 0
                while (index_6 < len_6) {
                  index_7 = 0
                  while (index_7 < len_7) {
                    index_8 = 0
                    while (index_8 < len_8) {
                      index_9 = 0
                      while (index_9 < len_9) {
                        index_10 = 0
                        while (index_10 < len_10) {
                          index_11 = 0
                          while (index_11 < len_11) {
                            index_12 = 0
                            while (index_12 < len_12) {
                              index_13 = 0
                              while (index_13 < len_13) {
                                lb(idx) = Vector(
                                  in(0)(index_0),
                                  in(1)(index_1),
                                  in(2)(index_2),
                                  in(3)(index_3),
                                  in(4)(index_4),
                                  in(5)(index_5),
                                  in(6)(index_6),
                                  in(7)(index_7),
                                  in(8)(index_8),
                                  in(9)(index_9),
                                  in(10)(index_10),
                                  in(11)(index_11),
                                  in(12)(index_12),
                                  in(13)(index_13)
                                )
                                idx += 1

                                index_13 += 1
                              }

                              index_12 += 1
                            }

                            index_11 += 1
                          }

                          index_10 += 1
                        }

                        index_9 += 1
                      }

                      index_8 += 1
                    }

                    index_7 += 1
                  }

                  index_6 += 1
                }

                index_5 += 1
              }

              index_4 += 1
            }

            index_3 += 1
          }

          index_2 += 1
        }

        index_1 += 1
      }

      index_0 += 1
    }

    ArraySeq.unsafeWrapArray(lb)
  }

  private def product_15[T](in: IndexedSeq[IndexedSeq[T]]): IndexedSeq[IndexedSeq[T]] = {
    val len_0 = in(0).length
    val len_1 = in(1).length
    val len_2 = in(2).length
    val len_3 = in(3).length
    val len_4 = in(4).length
    val len_5 = in(5).length
    val len_6 = in(6).length
    val len_7 = in(7).length
    val len_8 = in(8).length
    val len_9 = in(9).length
    val len_10 = in(10).length
    val len_11 = in(11).length
    val len_12 = in(12).length
    val len_13 = in(13).length
    val len_14 = in(14).length

    val lb = new Array[IndexedSeq[T]](len_0 * len_1 * len_2 * len_3 * len_4 * len_5 * len_6 * len_7 * len_8 * len_9 * len_10 * len_11 * len_12 * len_13 * len_14)
    var idx = 0

    var index_0 = 0
    var index_1 = 0
    var index_2 = 0
    var index_3 = 0
    var index_4 = 0
    var index_5 = 0
    var index_6 = 0
    var index_7 = 0
    var index_8 = 0
    var index_9 = 0
    var index_10 = 0
    var index_11 = 0
    var index_12 = 0
    var index_13 = 0
    var index_14 = 0
    index_0 = 0
    while (index_0 < len_0) {
      index_1 = 0
      while (index_1 < len_1) {
        index_2 = 0
        while (index_2 < len_2) {
          index_3 = 0
          while (index_3 < len_3) {
            index_4 = 0
            while (index_4 < len_4) {
              index_5 = 0
              while (index_5 < len_5) {
                index_6 = 0
                while (index_6 < len_6) {
                  index_7 = 0
                  while (index_7 < len_7) {
                    index_8 = 0
                    while (index_8 < len_8) {
                      index_9 = 0
                      while (index_9 < len_9) {
                        index_10 = 0
                        while (index_10 < len_10) {
                          index_11 = 0
                          while (index_11 < len_11) {
                            index_12 = 0
                            while (index_12 < len_12) {
                              index_13 = 0
                              while (index_13 < len_13) {
                                index_14 = 0
                                while (index_14 < len_14) {
                                  lb(idx) = Vector(
                                    in(0)(index_0),
                                    in(1)(index_1),
                                    in(2)(index_2),
                                    in(3)(index_3),
                                    in(4)(index_4),
                                    in(5)(index_5),
                                    in(6)(index_6),
                                    in(7)(index_7),
                                    in(8)(index_8),
                                    in(9)(index_9),
                                    in(10)(index_10),
                                    in(11)(index_11),
                                    in(12)(index_12),
                                    in(13)(index_13),
                                    in(14)(index_14)
                                  )
                                  idx += 1

                                  index_14 += 1
                                }

                                index_13 += 1
                              }

                              index_12 += 1
                            }

                            index_11 += 1
                          }

                          index_10 += 1
                        }

                        index_9 += 1
                      }

                      index_8 += 1
                    }

                    index_7 += 1
                  }

                  index_6 += 1
                }

                index_5 += 1
              }

              index_4 += 1
            }

            index_3 += 1
          }

          index_2 += 1
        }

        index_1 += 1
      }

      index_0 += 1
    }

    ArraySeq.unsafeWrapArray(lb)
  }

  private def product_16[T](in: IndexedSeq[IndexedSeq[T]]): IndexedSeq[IndexedSeq[T]] = {
    val len_0 = in(0).length
    val len_1 = in(1).length
    val len_2 = in(2).length
    val len_3 = in(3).length
    val len_4 = in(4).length
    val len_5 = in(5).length
    val len_6 = in(6).length
    val len_7 = in(7).length
    val len_8 = in(8).length
    val len_9 = in(9).length
    val len_10 = in(10).length
    val len_11 = in(11).length
    val len_12 = in(12).length
    val len_13 = in(13).length
    val len_14 = in(14).length
    val len_15 = in(15).length

    val lb = new Array[IndexedSeq[T]](len_0 * len_1 * len_2 * len_3 * len_4 * len_5 * len_6 * len_7 * len_8 * len_9 * len_10 * len_11 * len_12 * len_13 * len_14 * len_15)
    var idx = 0

    var index_0 = 0
    var index_1 = 0
    var index_2 = 0
    var index_3 = 0
    var index_4 = 0
    var index_5 = 0
    var index_6 = 0
    var index_7 = 0
    var index_8 = 0
    var index_9 = 0
    var index_10 = 0
    var index_11 = 0
    var index_12 = 0
    var index_13 = 0
    var index_14 = 0
    var index_15 = 0
    index_0 = 0
    while (index_0 < len_0) {
      index_1 = 0
      while (index_1 < len_1) {
        index_2 = 0
        while (index_2 < len_2) {
          index_3 = 0
          while (index_3 < len_3) {
            index_4 = 0
            while (index_4 < len_4) {
              index_5 = 0
              while (index_5 < len_5) {
                index_6 = 0
                while (index_6 < len_6) {
                  index_7 = 0
                  while (index_7 < len_7) {
                    index_8 = 0
                    while (index_8 < len_8) {
                      index_9 = 0
                      while (index_9 < len_9) {
                        index_10 = 0
                        while (index_10 < len_10) {
                          index_11 = 0
                          while (index_11 < len_11) {
                            index_12 = 0
                            while (index_12 < len_12) {
                              index_13 = 0
                              while (index_13 < len_13) {
                                index_14 = 0
                                while (index_14 < len_14) {
                                  index_15 = 0
                                  while (index_15 < len_15) {
                                    lb(idx) = Vector(
                                      in(0)(index_0),
                                      in(1)(index_1),
                                      in(2)(index_2),
                                      in(3)(index_3),
                                      in(4)(index_4),
                                      in(5)(index_5),
                                      in(6)(index_6),
                                      in(7)(index_7),
                                      in(8)(index_8),
                                      in(9)(index_9),
                                      in(10)(index_10),
                                      in(11)(index_11),
                                      in(12)(index_12),
                                      in(13)(index_13),
                                      in(14)(index_14),
                                      in(15)(index_15)
                                    )
                                    idx += 1

                                    index_15 += 1
                                  }

                                  index_14 += 1
                                }

                                index_13 += 1
                              }

                              index_12 += 1
                            }

                            index_11 += 1
                          }

                          index_10 += 1
                        }

                        index_9 += 1
                      }

                      index_8 += 1
                    }

                    index_7 += 1
                  }

                  index_6 += 1
                }

                index_5 += 1
              }

              index_4 += 1
            }

            index_3 += 1
          }

          index_2 += 1
        }

        index_1 += 1
      }

      index_0 += 1
    }

    ArraySeq.unsafeWrapArray(lb)
  }

}
