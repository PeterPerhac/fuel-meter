package controllers

import auth.twitter.TwitterOAuthConfig
import cats.effect.IO
import cats.implicits._
import connectors.TwitterOAuthConnector
import controllers.infra.Goodies
import doobie.free.connection.{AsyncConnectionIO => AIO}
import models.UserProfile
import play.api.mvc._
import repository.TokenRepository._
import scalaj.http.Token
import services.UserProfileService

class OAuthController(
    userProfileService: UserProfileService,
    twitterOAuthConfig: TwitterOAuthConfig,
    twitterOAuthConnector: TwitterOAuthConnector
)(goodies: Goodies)
    extends FuelMeterController(goodies) {

  import twitterOAuthConfig._
  import userProfileService._

  val signIn: Action[AnyContent] = runIO { implicit request =>
    twitterOAuthConnector
      .requestToken(callbackUrl)
      .flatTap(transact compose saveToken)
      .map(token => Redirect(authPageUrlTemplate.templated("oauth_token" -> token.key)))
  }

  val signOut: Action[AnyContent] = Action { implicit request =>
    Redirect(routes.ReadingsController.index()).withNewSession
  }

  def callback(oauth_token: Option[String], oauth_verifier: Option[String]): Action[AnyContent] = runIO { implicit request =>
    (oauth_token, oauth_verifier)
      .mapN { (token, verifier) =>
        val exchangeTokens = (rt: Token) => AIO.liftIO(twitterOAuthConnector.accessToken(rt, verifier)).flatTap(saveToken)
        val redirect = (up: UserProfile) => Redirect(routes.UserProfileController.viewProfile()).withSession("user_id" -> up.id)

        (transact(getToken(token) >>= exchangeTokens) >>= createOrRetrieveUser).map(redirect)
      }
      .getOrElse(Redirect(routes.OAuthController.oauthFailure()).pure[IO])
  }

  val oauthFailure: Action[AnyContent] = Action(implicit request => Ok(views.html.failedOAuth(request)))

}
