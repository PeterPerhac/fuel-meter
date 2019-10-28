package utils

import java.time.temporal.ChronoUnit.MONTHS
import java.time.{LocalDate, YearMonth}

import cats.data.NonEmptyList
import cats.implicits._
import models.{MonthValue, MonthlyStats, Reading}

import scala.language.implicitConversions
import scala.math.BigDecimal.RoundingMode.HALF_UP

trait MonthlyStatsCalculator extends FunctionBuilder {

  def calculate(rs: List[Reading]): MonthlyStats =
    NonEmptyList.fromList(rs).fold(MonthlyStats.empty) { rs =>
      val readings = rs.sortBy(_.date.toEpochDay)
      implicit def epoch(ld: LocalDate): Long = ld.toEpochDay

      def sumForMonth(g: Reading => Double): YearMonth => MonthValue = {
        val f = combinedAdditiveFunction(readings.map(r => LongDataPoint(r.date, g(r))))
        ym => MonthValue(ym, BigDecimal(f(ym.atEndOfMonth()) - f(ym.atDay(1))).setScale(2, HALF_UP))
      }

      val dates: NonEmptyList[LocalDate] = readings.map(_.date)
      val firstYearMonth = YearMonth.from(dates.head)
      def allMonthsBetween(m1: YearMonth, m2: YearMonth): List[YearMonth] =
        List.tabulate(MONTHS.between(m1, m2).intValue())(m => m1.plusMonths(m + 1L))

      val months: List[YearMonth] = firstYearMonth :: allMonthsBetween(firstYearMonth, YearMonth.from(dates.last))
      MonthlyStats(
        moneyBurned = months.map(sumForMonth(_.cost)),
        fuelBurned = months.map(sumForMonth(_.liters)),
        milesDriven = months.map(sumForMonth(_.miles))
      )
    }

}

object MonthlyStatsCalculator extends MonthlyStatsCalculator
