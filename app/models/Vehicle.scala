package models

import play.api.libs.json.{Json, OFormat}

case class Vehicle(reg: String, make: String, model: String, year: Int, color: Option[String])

object Vehicle {
  implicit val format: OFormat[Vehicle] = Json.format[Vehicle]
}
