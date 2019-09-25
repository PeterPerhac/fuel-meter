package controllers

import controllers.ReadingsController.VRegCookieName
import doobie.implicits._
import javax.inject.{Inject, Singleton}
import play.api.Configuration
import play.api.mvc._
import repository.{DoobieTransactor, ReadingsRepository}

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class DeletesController @Inject()(
    repo: ReadingsRepository,
    transactor: DoobieTransactor,
    configuration: Configuration,
    controllerComponents: ControllerComponents
) extends FuelMeterController(configuration, controllerComponents) {

  def deleteVehicle(reg: String): Action[AnyContent] = runAsync { implicit request =>
    repo
      .removeByRegistration(reg)
      .map(_ => Redirect(routes.ReadingsController.index()).discardingCookies(DiscardingCookie(VRegCookieName)))
      .transact(transactor.tx)
  }

}
