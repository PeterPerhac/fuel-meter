package controllers

import cats.effect.IO
import cats.implicits._
import controllers.infra.Goodies
import models.Vehicle
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import repository.ReadingsRepository._
import services.VehicleService

class VehicleController(vehicleService: VehicleService)(goodies: Goodies) extends FuelMeterController(goodies) {

  val form: Form[Vehicle] = Form(
    mapping(
      "reg"   -> nonEmptyText(minLength = 2, maxLength = 8),
      "make"  -> nonEmptyText(minLength = 2, maxLength = 20),
      "model" -> nonEmptyText(minLength = 2, maxLength = 20),
      "year"  -> number(min = 1900, max = 2100),
      "color" -> optional(nonEmptyText(minLength = 2, maxLength = 20))
    )(Vehicle.apply)(Vehicle.unapply)
  )

  def newVehicle(reg: String): Action[AnyContent] = runIO.authenticated { implicit request =>
    Ok(views.html.vehicleCaptureForm(reg, form)).pure[IO]
  }

  def saveVehicle(reg: String): Action[AnyContent] = runIO.authenticated { implicit request =>
    val boundForm = form.bindFromRequest()
    boundForm.fold(
      invalidForm => BadRequest(views.html.vehicleCaptureForm(reg, invalidForm)).pure[IO],
      vehicle =>
        vehicleService
          .saveVehicle(vehicle)
          .fold(
            errorCode =>
              BadRequest(views.html.vehicleCaptureForm(vehicle.reg, boundForm.withGlobalError(s"error.code.$errorCode"))),
            _ => Redirect(routes.ReadingsController.saveReading(vehicle.reg))
        )
    )
  }

  //TODO remove this stuff eventually
  def deleteVehicle(reg: String): Action[AnyContent] = runIO.authenticated { implicit request =>
    transact(removeByRegistration(reg)).map(_ => Redirect(routes.ReadingsController.index()))
  }

}
