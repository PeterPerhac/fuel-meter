package models

import scala.math.BigDecimal.RoundingMode

case class VehicleRecordSummary(
    reg: String,
    color: Option[String],
    make: String,
    model: String,
    count: Int,
    liters: Double,
    cost: Double
) {
  val ls: BigDecimal = BigDecimal(liters).setScale(2, RoundingMode.HALF_UP)
  val pounds: BigDecimal = BigDecimal(cost).setScale(2, RoundingMode.HALF_UP)
  val vehicleDescription: String = (List(make, model) ++ color.map(c => s"($c)").toList).mkString(" ")
}
