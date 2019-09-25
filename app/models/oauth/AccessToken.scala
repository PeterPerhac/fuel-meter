package models.oauth

import play.api.libs.functional.syntax._
import play.api.libs.json.{Reads, Writes, __}

case class AccessToken(token: String, tokenSecret: String, userId: String, screenName: String)
object AccessToken {

  implicit val jsonReads: Reads[AccessToken] =
    (
      (__ \ "oauth_token").read[String] and
        (__ \ "oauth_token_secret").read[String] and
        (__ \ "user_id").read[String] and
        (__ \ "screen_name").read[String]
    )(AccessToken.apply _)

  //TODO not needed, just for temporary testing
  implicit val jsonWrites: Writes[AccessToken] =
    Writes.of[String].contramap(_.toString)

}
