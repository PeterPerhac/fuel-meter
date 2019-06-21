package utils

import play.api.libs.json.JsObject
import play.api.libs.json.Json.obj

object SortingUtils {

  sealed trait SortOrder

  final case object Asc extends SortOrder

  final case object Desc extends SortOrder

  def by(field: String, so: SortOrder = Asc): JsObject = so match {
    case Asc  => obj(field -> 1)
    case Desc => obj(field -> -1)
  }

}
