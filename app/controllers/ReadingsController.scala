package controllers

import cats.effect.IO
import cats.implicits._
import controllers.infra.Goodies
import doobie.implicits._
import models._
import models.forms.ReadingForm.form
import play.api.data.Form
import play.api.mvc._
import repository.ReadingsRepository
import utils.DateUtils

class ReadingsController(goodies: Goodies) extends FuelMeterController(goodies) {

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
          .withCookies(VRegCookie.toCookie(reg)))
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
      .get(VRegCookie.name)
      .fold(uniqueRegistrations.map(fs => Ok(views.html.defaultHomePage(fs))))(cookie =>
        Redirect(routes.ReadingsController.listHtml(cookie.value)).pure[IO])
  }

}
