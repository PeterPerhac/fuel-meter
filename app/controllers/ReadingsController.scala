package controllers

import cats.effect.IO
import cats.implicits._
import doobie.implicits._
import models._
import models.forms.ReadingForm.form
import play.api.Configuration
import play.api.data.Form
import play.api.mvc._
import repository.{DoobieTransactor, ReadingsRepository}
import utils.DateUtils

import scala.concurrent.ExecutionContext.Implicits.global

class ReadingsController(
    transactor: DoobieTransactor,
    configuration: Configuration,
    controllerComponents: ControllerComponents
) extends FuelMeterController(configuration, controllerComponents) {

  import ReadingsController._
  import transactor._

  lazy val lookupUrl: String =
    config.get[String]("vehicle-lookup.service.url")

  val readingForm: Form[Reading] =
    form(DateUtils.today)

  private def readings(reg: String): IO[List[Reading]] =
    ReadingsRepository.findAll(reg).transact(tx)

  private def uniqueRegistrations: IO[List[VehicleRecordSummary]] =
    ReadingsRepository.uniqueRegistrations().transact(tx)

  def list(reg: String): Action[AnyContent] = runAsync { implicit request =>
    readings(reg)
      .recover { case _ => Nil }
      .map(rs => Ok(VehicleData(reg = reg, readings = rs.map(ReadingData.apply)).asJson))
  }

  def listHtml(reg: String): Action[AnyContent] = runAsync { implicit request =>
    (readings(reg), uniqueRegistrations).mapN(
      (readings, summaries) =>
        Ok(views.html.readings(reg, readings, summaries))
          .withCookies(
            Cookie(
              name = VRegCookieName,
              value = reg.filter(_.isLetterOrDigit).mkString,
              maxAge = Some(Int.MaxValue)
            )))
  }

  def captureForm(reg: String): Action[AnyContent] =
    Action(implicit request => Ok(views.html.captureForm(reg, readingForm)))

  def saveReading(r: String): Action[AnyContent] = runAsync { implicit request =>
    readingForm
      .bindFromRequest()
      .fold(
        invalidForm => BadRequest(views.html.captureForm(r, invalidForm)).pure[IO],
        form => ReadingsRepository.save(form).transact(tx).map(_ => Redirect(routes.ReadingsController.listHtml(r)))
      )
  }

  def index(): Action[AnyContent] = runAsync { implicit request =>
    request.cookies
      .get(VRegCookieName)
      .fold(uniqueRegistrations.map(fs => Ok(views.html.defaultHomePage(fs))))(cookie =>
        Redirect(routes.ReadingsController.listHtml(cookie.value)).pure[IO])
  }

}

object ReadingsController {
  val VRegCookieName = "vreg"
}
