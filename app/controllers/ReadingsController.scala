package controllers

import cats.effect.IO
import cats.implicits._
import controllers.infra.Goodies
import models._
import models.forms.ReadingForm.readingForm
import play.api.mvc._
import repository.ReadingsRepository._
import services.ReadingsService

class ReadingsController(readingService: ReadingsService)(goodies: Goodies) extends FuelMeterController(goodies) {

  private def readings(reg: String): IO[List[Reading]] = transact(findAll(reg)).recover { case _ => Nil }
  private def top10: IO[List[VehicleRecordSummary]] = transact(vehicleSamples(10))

  private def userOwnsVehicle(reg: String, optUser: Option[User]): IO[Boolean] =
    optUser.fold(false.pure[IO]) { implicit user =>
      transact(vehiclesOwnedByUser).map(_.exists(_.reg.equalsIgnoreCase(reg)))
    }

  def list(reg: String): Action[AnyContent] = runIO { implicit request =>
    readings(reg).map(rs => Ok(VehicleData(reg = reg, readings = rs.map(ReadingData.apply)).asJson))
  }

  def listHtml(reg: String): Action[AnyContent] = runIO.optionallyAuthenticated { implicit request =>
    (readings(reg), top10, userOwnsVehicle(reg, request.user)).mapN((readings, summaries, isOwner) =>
      Ok(views.html.readings(reg, readings, summaries, request.user, isOwner)))
  }

  def captureForm(reg: String): Action[AnyContent] =
    runIO.authenticated(implicit request => Ok(views.html.captureForm(reg, readingForm)).pure[IO])

  def saveReading(reg: String): Action[AnyContent] = runIO.authenticated { implicit request =>
    val boundReadingForm = readingForm.bindFromRequest()
    boundReadingForm.fold(
      invalidForm => BadRequest(views.html.captureForm(reg, invalidForm)).pure[IO],
      reading => {
        val errHandler: Int => Result =
          errorCode => BadRequest(views.html.captureForm(reg, boundReadingForm.withGlobalError(s"error.code.$errorCode")))
        readingService.saveReading(reading).fold(errHandler, _ => Redirect(routes.ReadingsController.listHtml(reg)))
      }
    )
  }

  def index(): Action[AnyContent] = runIO.optionallyAuthenticated { implicit request =>
    top10.map(vehicles => Ok(views.html.defaultHomePage(vehicles, request.user)))
  }

}
