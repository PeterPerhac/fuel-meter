package utils

import java.time.YearMonth

import cats.data.NonEmptyList
import cats.data.NonEmptyList.fromListUnsafe

import scala.Function.tupled

case class LongDataPoint(x: Long, y: Double)
case class Segment(left: LongDataPoint, right: LongDataPoint) {
  val slope: Double = (right.y - left.y) / (right.x - left.x)
  val f: Long => Double = n => slope * n + (left.y - (slope * left.x))
}
case class LongDataWindow(start: Long, end: Long)
object LongDataWindow {
  val yearMonthToWindow: YearMonth => LongDataWindow = ym =>
    LongDataWindow(
      start = ym.atDay(1).toEpochDay,
      end = ym.atEndOfMonth().toEpochDay
    )
}

trait FunctionBuilder {

  val segmentToPartialFunction: Segment => PartialFunction[Long, Double] = s => {
    case n: Long if n > s.left.x && n <= s.right.x => s.f(n)
  }
  def partialFunctionFromSegment(s: Segment, cond: Segment => Long => Boolean): PartialFunction[Long, Double] = {
    case n if cond(s)(n) => s.f(n)
  }

  def combinedFunction(data: List[LongDataPoint]): Long => Double =
    NonEmptyList.fromList(data).fold[Long => Double](Function.const(0.0d)) { dps =>
      val dummy = dps.last.copy(x = Long.MaxValue)
      val segments: NonEmptyList[Segment] = dps match {
        case NonEmptyList(head, Nil) => NonEmptyList.of(Segment(LongDataPoint(Long.MinValue, head.y), head))
        case NonEmptyList(_, tail)   => fromListUnsafe(dps.init.zipAll(tail, dummy, dummy).map(tupled(Segment.apply)))
      }
      val firstF: PartialFunction[Long, Double] = partialFunctionFromSegment(segments.head, s => _ <= s.right.x)
      val middleFs: List[PartialFunction[Long, Double]] = segments.size match {
        case n if n > 2 => segments.tail.init.map(segmentToPartialFunction)
        case _          => List.empty
      }
      val lastF: PartialFunction[Long, Double] = partialFunctionFromSegment(segments.last, s => _ > s.left.x)
      NonEmptyList(firstF, middleFs :+ lastF).reduceLeft(_ orElse _)
    }

  def combinedAdditiveFunction(data: NonEmptyList[LongDataPoint]): Long => Double =
    combinedFunction({
      val dataAsList = data.toList
      val (mappedPoints, _, _) = data.tail.foldLeft((List.empty[LongDataPoint], 0, 0.0d)) {
        case ((ps, i, ySum), p) =>
          val ys = ySum + dataAsList(i).y
          (ps :+ LongDataPoint(p.x, ys), i + 1, ys)
      }
      LongDataPoint(Long.MinValue, 0.0d) :: LongDataPoint(data.head.x, 0.0d) :: mappedPoints
    })

}

object FunctionBuilder extends FunctionBuilder
