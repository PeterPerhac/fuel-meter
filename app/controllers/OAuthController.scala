package controllers

import cats.implicits._
import doobie.free.connection.ConnectionIO
import play.api.mvc._
import repository.DoobieTransactor
import services.{TwitterOAuthService, UserProfileService}

class OAuthController(
      userProfileService: UserProfileService,
      twitterOAuthService: TwitterOAuthService,
      override val doobieTransactor: DoobieTransactor,
      override val controllerComponents: ControllerComponents
) extends FuelMeterController(userProfileService) {

  val signIn: Action[AnyContent] = runCIO { implicit request =>
    twitterOAuthService.initiateSignIn.map(Redirect(_))
  }

  val signOut: Action[AnyContent] = Action(Redirect(routes.ReadingsController.index()).withNewSession)

  def callback(oauth_token: Option[String], oauth_verifier: Option[String]): Action[AnyContent] = runCIO {
    implicit request =>
      (oauth_token, oauth_verifier)
        .mapN(twitterOAuthService.processCallback)
        .fold[ConnectionIO[Result]](Redirect(routes.OAuthController.oauthFailure()).pure[ConnectionIO])(
          _.map(up => Redirect(routes.UserProfileController.viewProfile()).withSession("user_id" -> up.id))
        )
  }

  val oauthFailure: Action[AnyContent] = Action(implicit request => Ok(views.html.failedOAuth(request)))

}
