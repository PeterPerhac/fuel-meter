package utils


import java.time.DayOfWeek.MONDAY
import java.time.temporal.TemporalAdjusters.previousOrSame
import java.time.temporal.{TemporalField, WeekFields}
import java.time.{LocalDate, YearMonth}

import cats.implicits._
import cats.kernel.{Order => CatsOrder}

trait LocalDateUtils {

  implicit val localDateOrder: CatsOrder[LocalDate] = CatsOrder.by(_.toEpochDay)

  case class YearWeek(date: LocalDate, weekNumber: Int) {
    val year: Int = date.getYear
  }
  object YearWeek {
    val weekOfYear: TemporalField = WeekFields.ISO.weekOfWeekBasedYear
    def apply(d: LocalDate): YearWeek = YearWeek(d, d.get(weekOfYear))
  }

  private def unfold[A, B](start: B)(f: B => Option[(A, B)]): Stream[A] = f(start) match {
    case Some((elem, next)) => elem #:: unfold(next)(f)
    case None               => Stream.empty
  }

  def monthsInRange(d1: LocalDate, d2: LocalDate): Stream[YearMonth] = {
    val nextMonth: PartialFunction[LocalDate, (YearMonth, LocalDate)] = {
      case d if !d.isAfter(d2) => (YearMonth.from(d), d.plusMonths(1).withDayOfMonth(1))
    }
    unfold(d1)(nextMonth.lift)
  }

  def weeksInRange(d1: LocalDate, d2: LocalDate): Stream[YearWeek] = {
    val nextWeek: PartialFunction[LocalDate, (YearWeek, LocalDate)] = {
      case d if !d.isAfter(d2) => (YearWeek(d), d.plusWeeks(1) `with` previousOrSame(MONDAY))
    }
    unfold(d1)(nextWeek.lift)
  }

}

object LocalDateUtils extends LocalDateUtils
