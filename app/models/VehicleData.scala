package models

import play.api.libs.json.{Json, OWrites}

case class VehicleData(
    reg: String,
    avgC: BigDecimal,
    mpg: BigDecimal,
    costOfLitre: BigDecimal,
    readings: Seq[ReadingData]
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

  implicit val format: OWrites[ReadingData] = Json.writes

  def apply(r: Reading): ReadingData =
    ReadingData(r.date.toString, r.miles, r.mileage, r.liters, r.cost, r.avgC, r.mpg, r.costOfLitre)

}

object VehicleData {

  implicit val format: OWrites[VehicleData] = Json.writes

  def apply(reg: String, readings: Seq[ReadingData]): VehicleData = {
    import utils.GenericUtils._
    VehicleData(
      reg,
      (readings.map(_.avgC)).avg,
      (readings.map(_.mpg)).avg,
      (readings.map(_.costOfLitre)).avg,
      readings
    )
  }

}
