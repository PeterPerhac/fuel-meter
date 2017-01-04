package controllers

import javax.inject.Inject

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc._
import repository.FuelMeterRepository

class DeletesController @Inject()(repo: FuelMeterRepository) extends Controller {

  private val VReg = "vreg"

  def deleteVehicle(implicit r: String) = Action.async {
    _ => repo.removeByRegistration(r) map (_ => Redirect(routes.ReadingsController.index()).discardingCookies(DiscardingCookie(VReg)))
  }

}
