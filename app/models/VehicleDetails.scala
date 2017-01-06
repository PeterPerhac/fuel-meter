package models

import play.api.libs.json.Json
import reactivemongo.bson.Macros

case class VehicleDetails(reg: String, make: String, model: String, year: Int, color: Option[String])

object VehicleDetails {
  implicit val bsonHandler = Macros.handler[VehicleDetails]
  implicit val format = Json.format[VehicleDetails]
}