package controllers

import auth.twitter.TwitterOAuthConfig
import cats.effect.IO
import cats.implicits._
import connectors.TwitterOAuthConnector
import controllers.infra.Goodies
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

  val doGetToken: String => IO[Token] = transact compose getToken
  val doSaveToken: Token => IO[Int] = transact compose saveToken

  val signIn: Action[AnyContent] = runAsync { implicit request =>
    twitterOAuthConnector
      .requestToken(callbackUrl)
      .flatTap(doSaveToken)
      .map(token => Redirect(authPageUrlTemplate.templated("oauth_token" -> token.key)))
  }

  val signOut: Action[AnyContent] = Action { implicit request =>
    Redirect(routes.ReadingsController.index()).withNewSession
  }

  def callback(oauth_token: Option[String], oauth_verifier: Option[String]): Action[AnyContent] = runAsync { implicit request =>
    (oauth_token, oauth_verifier)
      .mapN { (token, verifier) =>
        val exchangeTokens = (rt: Token) => twitterOAuthConnector.accessToken(rt, verifier).flatTap(doSaveToken)
        val redirect = (up: UserProfile) => Redirect(routes.UserProfileController.viewProfile()).withSession("user_id" -> up.id)

        (doGetToken(token) >>= exchangeTokens >>= createOrRetrieveUser).map(redirect)
      }
      .getOrElse(Redirect(routes.OAuthController.oauthFailure()).pure[IO])
  }

  val oauthFailure: Action[AnyContent] = Action(implicit request => Ok(views.html.failedOAuth(request)))

}
