package controllers

import controllers.infra.Goodies
import play.api.mvc._
import repository.UserProfileRepository.getUserProfile

class UserProfileController(goodies: Goodies) extends FuelMeterController(goodies) {

  val viewProfile: Action[AnyContent] = runAsync.authenticated { implicit request =>
    transact(getUserProfile(request.user)).map(profile => Ok(views.html.userProfile(profile)))
  }

}
