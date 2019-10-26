package utils

import cats.data.NonEmptyList
import cats.data.NonEmptyList.fromListUnsafe
import cats.implicits._

import scala.Function.tupled
import scala.util.Random

case class LongDataPoint(x: Long, y: Double)
object LongDataPoint {
  implicit val o: Ordering[LongDataPoint] = Ordering.by(_.x)
}
case class Segment(left: LongDataPoint, right: LongDataPoint) {
  val slope: Double = (right.y - left.y) / (right.x - left.x)
  val f: Long => Double = n => slope * n + (left.y - (slope * left.x))
}
case class LongDataWindow(start: Long, end: Long)

case class AppliesFromFunction[A, B](f: A => B, appliesFrom: A) extends (A => B) {

  override def apply(a: A): B = f(a)

  override def toString: String = s"apFrom: $appliesFrom"
}

trait FunctionBuilder {

  def combinedFunction(data: List[LongDataPoint]): Long => Double =
    NonEmptyList.fromList(data.sorted).fold[Long => Double](Function.const(0.0d)) { dps =>
      val dummy = dps.last.copy(x = Long.MaxValue)
      val segments: NonEmptyList[Segment] = dps match {
        case NonEmptyList(head, Nil) => NonEmptyList.of(Segment(LongDataPoint(Long.MinValue, head.y), head))
        case NonEmptyList(_, tail)   => fromListUnsafe(dps.init.zipAll(tail, dummy, dummy).map(tupled(Segment.apply)))
      }

      val functions: NonEmptyList[AppliesFromFunction[Long, Double]] =
        segments.map(s => AppliesFromFunction(s.f, s.left.x))

      (x: Long) => {
        functions.reverse.find(_.appliesFrom < x).getOrElse(functions.head.f)(x)
      }

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
