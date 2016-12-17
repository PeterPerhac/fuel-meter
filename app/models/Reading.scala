package models

import play.api.libs.json.Json
import play.modules.reactivemongo.json.BSONFormats
import reactivemongo.bson.Macros

/**
  * Created by peterperhac on 05/11/2016.
  */
final case class Reading(reg: String, date: String, mi: Double, total: Int, litres: Double, cost: Double)

object Reading {
  val bsonHandler = Macros.handler[Reading]
  implicit val format = Json.format[Reading]
}