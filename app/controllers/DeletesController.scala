package controllers

import controllers.ReadingsController.VRegCookieName
import javax.inject.Inject
import play.api.Configuration
import play.api.mvc._
import repository.RefuelMongoRepository

import scala.concurrent.ExecutionContext.Implicits.global

class DeletesController @Inject()(
    repo: RefuelMongoRepository,
    configuration: Configuration,
    controllerComponents: ControllerComponents
) extends FuelMeterController(configuration, controllerComponents) {

  def deleteVehicle(reg: String): Action[AnyContent] = Action.async { implicit request =>
    repo.removeByRegistration(reg).map { _ =>
      Redirect(routes.ReadingsController.index())
        .discardingCookies(DiscardingCookie(VRegCookieName))
    }
  }

}
