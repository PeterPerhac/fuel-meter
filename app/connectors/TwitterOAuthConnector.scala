package connectors

import auth.twitter.TwitterOAuthConfig
import cats.effect.IO
import play.api.libs.json.{JsValue, Json}
import scalaj.http.{Http, Token}
class TwitterOAuthConnector(twitterApi: TwitterOAuthConfig) {

  import twitterApi._

  def requestToken(callbackUrl: String): IO[Token] = IO {
    Http(requestTokenUrl).postForm(Seq("oauth_callback" -> callbackUrl)).oauth(consumerToken).asToken.body
  }

  def accessToken(requestToken: Token, verifier: String): IO[Token] = IO {
    Http(accessTokenUrl).postForm.oauth(consumerToken, requestToken, verifier).asToken.body
  }

  val verifyCredential: Token => IO[JsValue] = accessToken =>
    IO {
      Json.parse(
        Http("https://api.twitter.com/1.1/account/verify_credentials.json")
          .params(
            Seq(
              "include_entities" -> "false",
              "skip_status"      -> "true",
              "include_email"    -> "false"
            ))
          .oauth(consumerToken, accessToken)
          .asString
          .body
      )
  }
}
