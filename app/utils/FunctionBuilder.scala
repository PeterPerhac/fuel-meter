package utils

import cats.data.NonEmptyList

import scala.Function.{const, tupled}
import scala.collection.mutable

case class LongDataPoint(x: Long, y: Double)
case class Segment(left: LongDataPoint, right: LongDataPoint) {
  val slope: Double = (right.y - left.y) / (right.x - left.x)
  val f: Long => Double = n => slope * n + (left.y - (slope * left.x))
}

trait FunctionBuilder {

  def combinedFunction(data: List[LongDataPoint]): Long => Double =
    NonEmptyList.fromList(data.sortBy(_.x)).fold[Long => Double](const(0.0d)) { dps =>
      val functionTree = new mutable.TreeMap[Long, Long => Double]()
      val dummy = dps.last.copy(x = Long.MaxValue)
      dps match {
        case NonEmptyList(head, Nil) => functionTree += Long.MinValue -> const(head.y)
        case NonEmptyList(_, tail) =>
          val addToTree: Segment => functionTree.type = s => functionTree += s.left.x -> s.f
          dps.init.zipAll(tail, dummy, dummy).foreach(tupled(Segment.apply _) andThen addToTree)
      }
      (x: Long) => functionTree.to(x).lastOption.fold[Long => Double](const(dps.head.y))(_._2)(x)
    }

  def combinedAdditiveFunction(data: NonEmptyList[LongDataPoint]): Long => Double =
    combinedFunction {
      val dataAsList = data.toList
      val (mappedPoints, _, _) = data.tail.foldLeft((List.empty[LongDataPoint], 0, 0.0d)) {
        case ((ps, i, ySum), p) =>
          val ys = ySum + dataAsList(i).y
          (ps :+ LongDataPoint(p.x, ys), i + 1, ys)
      }
      LongDataPoint(Long.MinValue, 0.0d) :: LongDataPoint(data.head.x, 0.0d) :: mappedPoints
    }

}

object FunctionBuilder extends FunctionBuilder
