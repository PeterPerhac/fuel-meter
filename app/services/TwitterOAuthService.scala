package services

import auth.twitter.TwitterOAuthConfig
import cats.implicits._
import connectors.TwitterOAuthConnector
import doobie.free.connection.ConnectionIO
import models.UserProfile
import repository.{DoobieTransactor, TokenRepository}
import scalaj.http.Token

class TwitterOAuthService(
      twitterOAuthConfig: TwitterOAuthConfig,
      twitterOAuthConnector: TwitterOAuthConnector,
      tokenRepository: TokenRepository,
      userProfileService: UserProfileService,
      override val doobieTransactor: DoobieTransactor
) extends FuelMeterService {

  import tokenRepository._
  import userProfileService._
  import ACIO._

  def processCallback(token: String, verifier: String): ConnectionIO[UserProfile] = {
    val exchangeTokens = (rt: Token) => liftIO(twitterOAuthConnector.accessToken(rt, verifier)).flatTap(saveToken)
    getToken(token) >>= exchangeTokens >>= createOrRetrieveUser
  }

  val initiateSignIn: ConnectionIO[String] =
    liftIO(twitterOAuthConnector.requestToken(twitterOAuthConfig.callbackUrl))
      .flatTap(saveToken)
      .map(token => twitterOAuthConfig.authPageUrlTemplate.templated("oauth_token" -> token.key))

}
