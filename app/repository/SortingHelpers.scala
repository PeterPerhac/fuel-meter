package repository

import play.api.libs.json.{JsNumber, JsObject}

/**
  * Created by peterperhac on 13/12/2016.
  */
object SortingHelpers {

  private def toJs(pair: (String, Int)) = JsObject(Map(pair._1 -> JsNumber(pair._2)))

  def ascending(field: String): JsObject = toJs(field -> 1)

  def descending(field: String): JsObject = toJs(field -> -1)

}
