package models

import play.api.libs.json.{Json, OFormat}

import scala.math.BigDecimal.RoundingMode

final case class Reading(reg: String, date: String, mi: Double, total: Int, litres: Double, cost: Double) {

  val avgC: BigDecimal =
    BigDecimal(litres / (mi * 0.0160934)).setScale(2, RoundingMode.HALF_UP)
  val mpg: BigDecimal =
    BigDecimal(mi / (litres / 4.54609188)).setScale(2, RoundingMode.HALF_UP)
  val costOfLitre: BigDecimal =
    BigDecimal(cost / litres).setScale(2, RoundingMode.HALF_EVEN)
  val ppm: BigDecimal =
    BigDecimal((cost * 100) / mi).setScale(1, RoundingMode.HALF_EVEN)

}

object Reading {
  implicit val format: OFormat[Reading] = Json.format[Reading]
}
