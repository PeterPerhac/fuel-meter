package controllers

import cats.effect.IO
import cats.implicits._
import controllers.infra.Goodies
import models._
import models.forms.ReadingForm.readingForm
import play.api.mvc._
import repository.ReadingsRepository._

class ReadingsController(goodies: Goodies) extends FuelMeterController(goodies) {

  private def readings(reg: String): IO[List[Reading]] = transact(findAll(reg)).recover { case _ => Nil }

  private def uniques: IO[List[VehicleRecordSummary]] = transact(uniqueRegistrations())

  def list(reg: String): Action[AnyContent] = runAsync { implicit request =>
    readings(reg).map(rs => Ok(VehicleData(reg = reg, readings = rs.map(ReadingData.apply)).asJson))
  }

  def listHtml(reg: String): Action[AnyContent] = runAsync { implicit request =>
    (readings(reg), uniques).mapN((readings, summaries) => Ok(views.html.readings(reg, readings, summaries)))
  }

  def captureForm(reg: String): Action[AnyContent] =
    Action(implicit request => Ok(views.html.captureForm(reg, readingForm)))

  def saveReading(r: String): Action[AnyContent] = runAsync { implicit request =>
    val boundReadingForm = readingForm.bindFromRequest()
    boundReadingForm.fold(
      invalidForm => BadRequest(views.html.captureForm(r, invalidForm)).pure[IO],
      reading =>
        transact(save(reading)).map {
          case Right(_)         => Redirect(routes.ReadingsController.listHtml(r))
          case Left(messageKey) => BadRequest(views.html.captureForm(r, boundReadingForm.withGlobalError(messageKey)))
      }
    )
  }

  def index(): Action[AnyContent] = runAsync { implicit request =>
    uniques.map(vehicles => Ok(views.html.defaultHomePage(vehicles)))
  }

}
