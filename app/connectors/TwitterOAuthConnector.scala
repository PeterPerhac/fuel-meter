package connectors

import cats.effect.IO
import cats.implicits._
import javax.inject.{Inject, Singleton}
import models.oauth.{AccessToken, RequestToken}
import play.api.Configuration
import play.api.libs.ws.WSClient

@Singleton
class TwitterOAuthConnector @Inject()(wsClient: WSClient, configuration: Configuration) {

  lazy val accessTokenUrl: String = configuration.get[String]("services.twitter.oauth.accessToken.url")
  lazy val requestTokenUrl: String = configuration.get[String]("services.twitter.oauth.requestToken.url")

  def requestToken(): IO[RequestToken] =
    IO(println(requestTokenUrl)) *> RequestToken(token = "request token",
                                                 tokenSecret = "request token secret",
                                                 callbackConfirmed = true).pure[IO]

  def accessToken(): IO[AccessToken] =
    IO(println(accessTokenUrl)) *> AccessToken(token = "access token",
                                               tokenSecret = "access token secret",
                                               userId = "user ID",
                                               screenName = "screen name").pure[IO]

}
