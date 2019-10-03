package controllers

import cats.data.OptionT
import controllers.infra.Goodies
import play.api.mvc._
import repository.UserProfileRepository.findUserProfile

class UserProfileController(goodies: Goodies) extends FuelMeterController(goodies) {

  val viewProfile: Action[AnyContent] = runAsync.authenticated { implicit request =>
    OptionT(transact(findUserProfile(request.user))).fold(Redirect(routes.OAuthController.signOut())) { profile =>
      Ok(views.html.userProfile(profile))
    }
  }

}
