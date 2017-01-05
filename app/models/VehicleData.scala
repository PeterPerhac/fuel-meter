package models

import java.time.format.DateTimeFormatter

import play.api.libs.json.Json

import scala.collection.GenTraversable
import scala.math.BigDecimal.RoundingMode

case class VehicleData(reg: String, avgC: BigDecimal, mpg: BigDecimal, costOfLitre: BigDecimal, readings: Seq[ReadingData])

case class ReadingData(
                        date: String,
                        mi: Double,
                        total: Int,
                        litres: Double,
                        cost: Double,
                        avgC: BigDecimal,
                        mpg: BigDecimal,
                        costOfLitre: BigDecimal
                      )

object ReadingData {

  implicit val format = Json.writes[ReadingData]

  val dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd")

  def apply(r: Reading): ReadingData = ReadingData(r.date, r.mi, r.total, r.litres, r.cost, r.avgC, r.mpg, r.costOfLitre)
}

object VehicleData {

  implicit val format = Json.writes[VehicleData]

  def apply(reg: String, readings: Seq[ReadingData]): VehicleData = {
    import utils.GenericUtils.Averages
    VehicleData(
      reg,
      (readings map (_.avgC)).avg,
      (readings map (_.mpg)).avg,
      (readings map (_.costOfLitre)).avg,
      readings
    )
  }

}
