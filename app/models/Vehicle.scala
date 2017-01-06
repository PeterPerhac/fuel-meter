package models

import play.api.libs.json.Json
import reactivemongo.bson.Macros

case class Vehicle(reg: String, make: String, model: String, year: Int, color: Option[String])

object Vehicle {
  implicit val bsonHandler = Macros.handler[Vehicle]
  implicit val format = Json.format[Vehicle]
}