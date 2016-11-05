package model

import play.api.libs.json.Json
import play.modules.reactivemongo.json.BSONFormats
import reactivemongo.bson.Macros

/**
  * Created by peterperhac on 05/11/2016.
  */
final case class Reading(registration: String, date: String, miles: Double, total: Int, litres: Double, cost: Double)

object Reading {
  implicit val format = Json.format[Reading]
  implicit val bsonHandler = Macros.handler[Reading]
}