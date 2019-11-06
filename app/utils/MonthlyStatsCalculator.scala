package utils

import java.time.{LocalDate, YearMonth}

import cats.data.NonEmptyList
import cats.implicits._
import models.{MonthValue, MonthlyStats, Reading}

import scala.language.implicitConversions
import scala.math.BigDecimal.RoundingMode.HALF_UP

trait MonthlyStatsCalculator extends FunctionBuilder with LocalDateUtils {

  def calculate(rs: List[Reading]): MonthlyStats =
    NonEmptyList.fromList(rs).fold(MonthlyStats.empty) { rs =>
      val readings = rs.sortBy(_.date.toEpochDay)
      implicit def epoch(ld: LocalDate): Long = ld.toEpochDay
      val today: LocalDate = LocalDate.now()

      def sumForMonth(g: Reading => Double): YearMonth => MonthValue = {
        val f = combinedAdditiveFunction(readings.map(r => LongDataPoint(r.date, g(r))))
        ym => MonthValue(ym, BigDecimal(f(ym.atEndOfMonth().min(today)) - f(ym.atDay(1))).setScale(2, HALF_UP))
      }

      val months: List[YearMonth] = monthsInRange(readings.head.date, readings.last.date).toList

      MonthlyStats(
        moneyBurned = months.map(sumForMonth(_.cost)),
        fuelBurned = months.map(sumForMonth(_.liters)),
        milesDriven = months.map(sumForMonth(_.miles))
      )
    }

}

object MonthlyStatsCalculator extends MonthlyStatsCalculator
