package models

import java.time.YearMonth
import java.time.format.DateTimeFormatter

import play.api.libs.json._

case class MonthValue(yearMonth: YearMonth, value: BigDecimal)
object MonthValue {

  val monthDisplayStringFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("MMM yy")

  implicit val writes: OWrites[MonthValue] = OWrites { mv: MonthValue =>
    JsObject(
      Seq(
        "year"  -> JsNumber(mv.yearMonth.getYear),
        "month" -> JsNumber(mv.yearMonth.getMonthValue),
        "label" -> JsString(monthDisplayStringFormatter.format(mv.yearMonth.atDay(1))),
        "value" -> JsNumber(mv.value)
      )
    )
  }

  implicit val ordering: Ordering[MonthValue] = Ordering.by(o => o.yearMonth)
}

case class MonthlyStats(moneyBurned: Seq[MonthValue], fuelBurned: Seq[MonthValue], milesDriven: Seq[MonthValue])
object MonthlyStats {
  val empty: MonthlyStats = MonthlyStats(Seq.empty, Seq.empty, Seq.empty)
  implicit val writes: OWrites[MonthlyStats] = Json.writes
}

case class VehicleData(
      reg: String,
      avgC: BigDecimal,
      mpg: BigDecimal,
      costOfLitre: BigDecimal,
      readings: Seq[ReadingData],
      monthlyStats: MonthlyStats
)

case class ReadingData(
      date: String,
      mi: Double,
      total: Int,
      liters: Double,
      cost: Double,
      avgC: BigDecimal,
      mpg: BigDecimal,
      costOfLitre: BigDecimal
)

object ReadingData {

  implicit val writes: OWrites[ReadingData] = Json.writes

  def apply(r: Reading): ReadingData =
    ReadingData(r.date.toString, r.miles, r.mileage, r.liters, r.cost, r.avgC, r.mpg, r.costOfLitre)

}

object VehicleData {

  implicit val writes: OWrites[VehicleData] = Json.writes

  def apply(reg: String, readings: Seq[ReadingData], monthlyStats: MonthlyStats): VehicleData = {
    import utils.GenericUtils._
    VehicleData(
      reg,
      (readings.map(_.avgC)).avg,
      (readings.map(_.mpg)).avg,
      (readings.map(_.costOfLitre)).avg,
      readings,
      monthlyStats
    )
  }

}
