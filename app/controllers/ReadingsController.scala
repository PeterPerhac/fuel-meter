package controllers

import cats.implicits._
import doobie._
import models._
import models.forms.ReadingForm.readingForm
import play.api.mvc._
import repository.DoobieTransactor
import services.{ReadingsService, UserProfileService}
import utils.MonthlyStatsCalculator

class ReadingsController(
      readingService: ReadingsService,
      userProfileService: UserProfileService,
      topVehicles: Int => ConnectionIO[List[VehicleRecordSummary]],
      override val doobieTransactor: DoobieTransactor,
      override val controllerComponents: ControllerComponents
) extends FuelMeterController(userProfileService) {

  def list(reg: String): Action[AnyContent] = runCIO { implicit request =>
    readingService
      .readings(reg)
      .map(
        rs =>
          Ok(
            VehicleData(
              reg = reg,
              readings = rs.map(ReadingData.apply),
              monthlyStats = MonthlyStatsCalculator.calculate(rs)
            ).asJson
          )
      )
  }

  def listHtml(reg: String): Action[AnyContent] = runCIO.optionallyAuthenticated { implicit request =>
    readingService
      .readingsViewModel(reg, request.user)
      .fold(NotFound(views.html.error404(request.user, request)))(vm => Ok(views.html.readings(vm)))
  }

  def captureForm(reg: String): Action[AnyContent] = runCIO.authenticated { implicit request =>
    Ok(views.html.readingCaptureForm(reg, request.user, readingForm)).pure[ConnectionIO]
  }

  def saveReading(reg: String): Action[AnyContent] = runCIO.authenticated { implicit request =>
    val boundReadingForm = readingForm.bindFromRequest()
    boundReadingForm.fold(
      invalidForm => BadRequest(views.html.readingCaptureForm(reg, request.user, invalidForm)).pure[ConnectionIO],
      reading =>
        readingService
          .saveReading(reading)
          .fold(
            errorCode =>
              BadRequest(
                views.html
                  .readingCaptureForm(reg, request.user, boundReadingForm.withGlobalError(s"error.code.$errorCode"))
              ),
            _ => Redirect(routes.ReadingsController.listHtml(reg))
          )
    )
  }

  def index(): Action[AnyContent] = runCIO.optionallyAuthenticated { implicit request =>
    topVehicles(10).map(vehicles => Ok(views.html.defaultHomePage(vehicles, request.user)))
  }

}
