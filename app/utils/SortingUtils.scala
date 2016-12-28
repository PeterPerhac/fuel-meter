package utils

import play.api.libs.json.{JsNumber, JsObject}

object SortingUtils {

  sealed trait SortOrder

  final case object Asc extends SortOrder

  final case object Desc extends SortOrder

  private def toJs(pair: (String, Int)) = JsObject(Map(pair._1 -> JsNumber(pair._2)))

  def by(field: String, so: SortOrder = Asc) = so match {
    case Asc => toJs(field -> 1)
    case Desc => toJs(field -> -1)
  }

}
