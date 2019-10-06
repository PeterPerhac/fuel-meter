package controllers

import controllers.infra.Goodies
import play.api.mvc._
import services.UserProfileService

class UserProfileController(userProfileService: UserProfileService)(goodies: Goodies) extends FuelMeterController(goodies) {

  val viewProfile: Action[AnyContent] = runIO.authenticated { implicit request =>
    userProfileService.getVehicleOwner.fold(Redirect(routes.OAuthController.signOut())){vehicleOwner =>
      Ok(views.html.userProfile(vehicleOwner))
    }
  }

}
