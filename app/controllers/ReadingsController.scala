package controllers

import cats.effect.IO
import cats.implicits._
import doobie.implicits._
import javax.inject.Inject
import models._
import models.forms.ReadingForm.form
import play.api.Configuration
import play.api.data.Form
import play.api.libs.json._
import play.api.mvc._
import repository.{DoobieTransactor, ReadingsRepository}
import utils.DateUtils

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ReadingsController @Inject()(
    repository: ReadingsRepository,
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

  def readings(reg: String): IO[List[Reading]] =
    repository.findAll(reg).transact(tx)

  def uniqueRegistrations: IO[List[VehicleRecordSummary]] =
    repository.uniqueRegistrations().transact(tx)

  def list(reg: String): Action[AnyContent] =
    Action.async {
      readings(reg)
        .unsafeToFuture()
        .recover { case _ => Nil }
        .map(rs => Ok(Json.toJson(VehicleData(reg = reg, readings = rs.map(ReadingData.apply)))))
    }

  def listHtml(reg: String): Action[AnyContent] =
    Action.async {
      (readings(reg), uniqueRegistrations)
        .mapN(
          (readings, summaries) =>
            Ok(views.html.readings(reg, readings, summaries))
              .withCookies(
                Cookie(
                  name = VRegCookieName,
                  value = reg.filter(_.isLetterOrDigit).mkString,
                  maxAge = Some(Int.MaxValue)
                )))
        .unsafeToFuture()
    }

  def captureForm(reg: String): Action[AnyContent] =
    Action { implicit request =>
      Ok(views.html.captureForm(reg, readingForm))
    }

  def saveReading(r: String): Action[AnyContent] =
    Action.async { implicit request =>
      readingForm
        .bindFromRequest()
        .fold(
          invalidForm => IO.pure(BadRequest(views.html.captureForm(r, invalidForm))),
          form => repository.save(form).transact(tx).map(_ => Redirect(routes.ReadingsController.listHtml(r)))
        )
        .unsafeToFuture()
    }

  def index(): Action[AnyContent] =
    Action.async { implicit request =>
      request.cookies
        .get(VRegCookieName)
        .fold(uniqueRegistrations.map(fs => Ok(views.html.defaultHomePage(fs))).unsafeToFuture())(cookie =>
          Future.successful(Redirect(routes.ReadingsController.listHtml(cookie.value))))
    }

}

object ReadingsController {
  val VRegCookieName = "vreg"
}
