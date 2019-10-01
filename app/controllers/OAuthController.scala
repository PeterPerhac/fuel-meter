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

class OAuthController(goodies: Goodies)(
    userProfileService: UserProfileService,
    twitterOAuthConfig: TwitterOAuthConfig,
    twitterOAuthConnector: TwitterOAuthConnector
) extends FuelMeterController(goodies) {

  import twitterOAuthConfig._
  import userProfileService._

  val doGetToken: String => IO[Token] = transact o getToken
  val doSaveToken: Token => IO[Int] = transact o saveToken

  val signIn: Action[AnyContent] = runAsync { implicit request =>
    twitterOAuthConnector
      .requestToken(callbackUrl)
      .flatTap(doSaveToken)
      .map(token => Redirect(authPageUrlTemplate.templated("oauth_token" -> token.key)))
  }

  def callback(oauth_token: Option[String], oauth_verifier: Option[String]): Action[AnyContent] = runAsync { implicit request =>
    (oauth_token, oauth_verifier)
      .mapN { (token, verifier) =>
        val exchangeTokens = (rt: Token) => twitterOAuthConnector.accessToken(rt, verifier).flatTap(doSaveToken)
        val redirect = (up: UserProfile) => Redirect(routes.UserProfileController.viewProfile()).withSession("user_id" -> up.id)

        (doGetToken(token) >>= exchangeTokens >>= createOrRetrieveUser).map(redirect)
      }
      .getOrElse(IO.pure(Redirect(routes.OAuthController.oauthFailure())))
  }

  val oauthFailure: Action[AnyContent] = Action(Ok(views.html.failedOAuth()))

}
