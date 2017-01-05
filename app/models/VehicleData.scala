package models

import java.time.format.DateTimeFormatter

import play.api.libs.json.Json

import scala.collection.GenTraversable
import scala.math.BigDecimal.RoundingMode

case class VehicleData(reg: String, avgC: BigDecimal, mpg: BigDecimal, costOfLitre: BigDecimal, readings: Seq[ReadingData])

case class ReadingData(date: String, mi: Double, total: Int, litres: Double, cost: Double, avgC: BigDecimal, mpg: BigDecimal, costOfLitre: BigDecimal)

object ReadingData {

  implicit val format = Json.writes[ReadingData]

  val dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd")

  def apply(r: Reading): ReadingData = ReadingData(
    r.date,
    r.mi, r.total, r.litres, r.cost,
    BigDecimal(r.litres / (r.mi * 0.0160934)).setScale(2, RoundingMode.HALF_UP),
    BigDecimal(r.mi / (r.litres / 4.54609188)).setScale(2, RoundingMode.HALF_UP),
    BigDecimal(r.cost / r.litres).setScale(3, RoundingMode.HALF_EVEN)
  )
}

object VehicleData {
  implicit val format = Json.writes[VehicleData]

  private def avg[T, C: Numeric](m: GenTraversable[T], componentExtractor: T => C): BigDecimal =
    BigDecimal(implicitly[Numeric[C]].toDouble(m map componentExtractor sum) / m.size).setScale(2, RoundingMode.HALF_EVEN)


  def apply(reg: String, readings: Seq[ReadingData]): VehicleData = new VehicleData(
    reg,
    avg[ReadingData, BigDecimal](readings, _.avgC),
    avg[ReadingData, BigDecimal](readings, _.mpg),
    avg[ReadingData, BigDecimal](readings, _.costOfLitre),
    readings
  )
}
