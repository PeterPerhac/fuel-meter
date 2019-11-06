package controllers

import cats.implicits._
import doobie.free.connection.ConnectionIO
import models.Vehicle
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import repository.DoobieTransactor
import services.{UserProfileService, VehicleService}

class VehicleController(
      vehicleService: VehicleService,
      userProfileService: UserProfileService,
      override val doobieTransactor: DoobieTransactor,
      override val controllerComponents: ControllerComponents
) extends FuelMeterController(userProfileService) {

  val form: Form[Vehicle] = Form(
    mapping(
      "reg"   -> nonEmptyText(minLength = 2, maxLength = 8),
      "make"  -> nonEmptyText(minLength = 2, maxLength = 20),
      "model" -> nonEmptyText(minLength = 2, maxLength = 20),
      "year"  -> number(min = 1900, max = 2100),
      "color" -> optional(nonEmptyText(minLength = 2, maxLength = 20))
    )(Vehicle.apply)(Vehicle.unapply)
  )

  def newVehicle(reg: String): Action[AnyContent] = runCIO.authenticated { implicit request =>
    Ok(views.html.vehicleCaptureForm(reg, request.user, form)).pure[ConnectionIO]
  }

  def saveVehicle(reg: String): Action[AnyContent] = runCIO.authenticated { implicit request =>
    val boundForm = form.bindFromRequest()
    boundForm.fold(
      invalidForm => BadRequest(views.html.vehicleCaptureForm(reg, request.user, invalidForm)).pure[ConnectionIO],
      vehicle =>
        vehicleService
          .saveVehicle(vehicle)
          .fold(
            errorCode =>
              BadRequest(
                views.html
                  .vehicleCaptureForm(vehicle.reg, request.user, boundForm.withGlobalError(s"error.code.$errorCode"))
              ),
            _ => Redirect(routes.ReadingsController.saveReading(vehicle.reg))
          )
    )
  }

  def deleteVehicle(reg: String): Action[AnyContent] = runCIO.authenticated { implicit request =>
    vehicleService.removeVehicle
      .apply(reg)
      .fold(
        _ => Forbidden(views.html.error403(Some(request.user), request)),
        _ => Redirect(routes.ReadingsController.index())
      )
  }

}
