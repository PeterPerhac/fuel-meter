package models

import play.api.libs.json.Json

import scala.math.BigDecimal.RoundingMode

// as the arriving JSON will have property _id for registration, we need an _id field for the Reads magic to work
// for a friendlier name, we define a val "reg"
// also, due to funky mongo rounding during aggregation, we define a val "ls" for a version of the number rounded to two decimal places

case class VehicleRecordSummary(_id: String, count: Int, litres: Double, cost: Double) {
  val reg = _id
  val ls = BigDecimal(litres).setScale(2, RoundingMode.HALF_UP)
  val pounds = BigDecimal(cost).setScale(2, RoundingMode.HALF_UP)
}

object VehicleRecordSummary {

  implicit val reads = Json.reads[VehicleRecordSummary]

}
