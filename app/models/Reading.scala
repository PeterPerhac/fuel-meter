package models

import org.joda.time.LocalDate
import play.api.libs.json.Json
import reactivemongo.bson.Macros

/**
  * Created by peterperhac on 05/11/2016.
  */
final case class Reading(reg: String, date: String, mi: Double, total: Int, litres: Double, cost: Double)

object Reading {
  implicit val bsonHandler = Macros.handler[Reading]
  implicit val format = Json.format[Reading]
}