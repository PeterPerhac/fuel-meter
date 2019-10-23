package utils

import java.time.YearMonth
import java.time.temporal.ChronoUnit.MONTHS

import cats.data.NonEmptyList
import cats.implicits._
import models.{MonthValue, MonthlyStats, Reading}
import utils.LongDataWindow.yearMonthToWindow

import scala.math.BigDecimal.RoundingMode

trait MonthlyStatsCalculator extends FunctionBuilder {

  def calculateBurn(f: Long => Double)(window: LongDataWindow): Double = f(window.end) - f(window.start)

  def calculate(rs: List[Reading]): MonthlyStats = {

    val emptyStats = MonthlyStats(moneyBurned = Seq.empty, fuelBurned = Seq.empty)

    NonEmptyList.fromList(rs).fold(emptyStats) { rs =>
      val readings = rs.sortBy(_.date.toEpochDay)
      val dates = readings.map(_.date)

      val firstYearMonth = YearMonth.from(dates.head)
      def allMonthsBetween(m1: YearMonth, m2: YearMonth): List[YearMonth] =
        List.tabulate(MONTHS.between(m1, m2).intValue())(m => m1.plusMonths(m + 1L))

      val months: NonEmptyList[YearMonth] =
        NonEmptyList(firstYearMonth, allMonthsBetween(firstYearMonth, YearMonth.from(dates.last)))

      def dataPoints(valueOf: Reading => Double): NonEmptyList[LongDataPoint] =
        readings.map(r => LongDataPoint(r.date.toEpochDay, valueOf(r)))

      val fCost = combinedAdditiveFunction(dataPoints(_.cost))
      val fFuel = combinedAdditiveFunction(dataPoints(_.liters))

      val moneyBurned: LongDataWindow => Double = calculateBurn(fCost)
      val fuelBurned: LongDataWindow => Double = calculateBurn(fFuel)

      val calc: (YearMonth => Double) => YearMonth => MonthValue =
        f => ym => MonthValue(ym, BigDecimal(f(ym)).setScale(2, RoundingMode.HALF_UP))

      MonthlyStats(
        months.toList.map(calc(yearMonthToWindow andThen moneyBurned)),
        months.toList.map(calc(yearMonthToWindow andThen fuelBurned))
      )
    }
  }
}

object MonthlyStatsCalculator extends MonthlyStatsCalculator
