package models.oauth

import play.api.libs.functional.syntax._
import play.api.libs.json.{Reads, Writes, __}

case class RequestToken(token: String, tokenSecret: String, callbackConfirmed: Boolean)
object RequestToken {

  implicit val jsonReads: Reads[RequestToken] =
    (
      (__ \ "oauth_token").read[String] and
        (__ \ "oauth_token_secret").read[String] and
        (__ \ "oauth_callback_confirmed").read[Boolean]
    )(RequestToken.apply _)

  //TODO not needed, just for temporary testing
  implicit val jsonWrites: Writes[RequestToken] =
    Writes.of[String].contramap(_.toString)

}
