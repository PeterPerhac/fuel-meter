package controllers.infra

import play.api.libs.json.{Json, OFormat}

case class SimpleUser(userId: String, vehicles: List[String] = List.empty)
object SimpleUser {
  implicit val format: OFormat[SimpleUser] = Json.format
}
