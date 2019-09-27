package controllers

import controllers.infra.Goodies
import doobie.implicits._
import models.VRegCookie
import play.api.mvc._
import repository.ReadingsRepository

import scala.concurrent.ExecutionContext.Implicits.global

class DeletesController(goodies: Goodies) extends FuelMeterController(goodies) {

  def deleteVehicle(reg: String): Action[AnyContent] = runAsync { implicit request =>
    ReadingsRepository
      .removeByRegistration(reg)
      .map(_ => Redirect(routes.ReadingsController.index()).discardingCookies(DiscardingCookie(VRegCookie.name)))
      .transact(tx)
  }

}
