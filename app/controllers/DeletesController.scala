package controllers

import controllers.infra.Goodies
import play.api.mvc._
import repository.ReadingsRepository.removeByRegistration

class DeletesController(goodies: Goodies) extends FuelMeterController(goodies) {

  def deleteVehicle(reg: String): Action[AnyContent] = runAsync.authenticated { implicit request =>
    transact(removeByRegistration(reg)).map(_ => Redirect(routes.ReadingsController.index()))
  }

}
