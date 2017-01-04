package models

import java.sql.Timestamp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

import play.api.libs.json.Json

import scala.math.BigDecimal.RoundingMode

case class VehicleData(reg: String, readings: Seq[ReadingData])

case class ReadingData(timestamp: Long, mi: Double, total: Int, litres: Double, cost: Double, avgC: BigDecimal, costOfLitre: BigDecimal)

object ReadingData {

  implicit val format = Json.writes[ReadingData]

  val dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd")

  def apply(r: Reading): ReadingData = ReadingData(
    Timestamp.valueOf(LocalDate.parse(r.date, dateFormat).atStartOfDay()).getTime,
    r.mi, r.total, r.litres, r.cost,
    BigDecimal(r.litres / (r.mi * 0.0160934)).setScale(2, RoundingMode.HALF_UP),
    BigDecimal(r.cost / r.litres).setScale(3, RoundingMode.HALF_EVEN)
  )
}

object VehicleData {
  implicit val format = Json.writes[VehicleData]
}
