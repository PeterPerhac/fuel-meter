package controllers

import controllers.infra.Goodies
import play.api.mvc._
import repository.UserProfileRepository.findUserProfile

class UserProfileController(goodies: Goodies) extends FuelMeterController(goodies) {

  val viewProfile: Action[AnyContent] = runAsync.authenticated { implicit request =>
    findUserProfile(request.user.id).mapK(runTransaction).fold(Redirect(routes.OAuthController.signOut())) { profile =>
      Ok(views.html.userProfile(profile))
    }
  }

}
