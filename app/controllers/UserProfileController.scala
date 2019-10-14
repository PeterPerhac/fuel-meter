package controllers

import play.api.mvc._
import repository.DoobieTransactor
import services.UserProfileService

class UserProfileController(userProfileService: UserProfileService, override val doobieTransactor: DoobieTransactor, override val controllerComponents: ControllerComponents)
    extends FuelMeterController {

  val viewProfile: Action[AnyContent] = runCIO.authenticated { implicit request =>
    userProfileService.getUser.fold(Redirect(routes.OAuthController.signOut())) { vehicleOwner =>
      Ok(views.html.userProfile(vehicleOwner))
    }
  }

}
