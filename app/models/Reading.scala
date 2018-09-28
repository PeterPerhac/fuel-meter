package models

import play.api.libs.json.Json
import reactivemongo.bson.Macros

import scala.math.BigDecimal.RoundingMode

final case class Reading(reg: String, date: String, mi: Double, total: Int, litres: Double, cost: Double) {

  val avgC =
    BigDecimal(litres / (mi * 0.0160934)).setScale(2, RoundingMode.HALF_UP)
  val mpg =
    BigDecimal(mi / (litres / 4.54609188)).setScale(2, RoundingMode.HALF_UP)
  val costOfLitre =
    BigDecimal(cost / litres).setScale(2, RoundingMode.HALF_EVEN)

}

object Reading {
  implicit val bsonHandler = Macros.handler[Reading]
  implicit val format = Json.format[Reading]
}
