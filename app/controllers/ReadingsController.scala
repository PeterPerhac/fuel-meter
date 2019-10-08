package controllers

import cats.effect.IO
import cats.implicits._
import controllers.infra.Goodies
import doobie.free.connection.{ConnectionIO, AsyncConnectionIO => AIO}
import models._
import models.forms.ReadingForm.readingForm
import play.api.mvc._
import repository.ReadingsRepository._
import services.ReadingsService

class ReadingsController(readingService: ReadingsService)(goodies: Goodies) extends FuelMeterController(goodies) {

  private def readings(reg: String): ConnectionIO[List[Reading]] = findAll(reg).recover { case _ => Nil }
  private def top10: ConnectionIO[List[VehicleRecordSummary]] = vehicleSamples(10)

  def list(reg: String): Action[AnyContent] = runIO { implicit request =>
    transact(readings(reg)).map(rs => Ok(VehicleData(reg = reg, readings = rs.map(ReadingData.apply)).asJson))
  }

  def listHtml(reg: String): Action[AnyContent] = runIO.optionallyAuthenticated { implicit request =>
    def userOwnsVehicle(reg: String, optUser: Option[User]): ConnectionIO[Boolean] =
      optUser.fold(AIO.pure(false))(implicit user => vehiclesOwnedByUser.map(_.exists(_.reg.equalsIgnoreCase(reg))))

    vehicleSummary(reg)
      .semiflatMap { summary =>
        (readings(reg), top10, userOwnsVehicle(reg, request.user)).mapN((readings, summaries, isOwner) =>
          Ok(views.html.readings(summary, readings, summaries, request.user, isOwner)))
      }
      .mapK(runTransaction)
      .getOrElse[Result](NotFound(views.html.error404(request.user, request)))
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
    transact(top10).map(vehicles => Ok(views.html.defaultHomePage(vehicles, request.user)))
  }

}
