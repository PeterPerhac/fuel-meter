package controllers

import cats.Applicative
import cats.instances.future._
import javax.inject.Inject
import models._
import models.forms.ReadingForm.form
import play.api.data.Form
import play.api.libs.json._
import play.api.libs.ws.WSClient
import play.api.mvc._
import play.api.{Configuration, Logger}
import repository.RefuelMongoRepository
import utils.DateUtils

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

class ReadingsController @Inject()(
    ws: WSClient,
    repository: RefuelMongoRepository,
    configuration: Configuration,
    controllerComponents: ControllerComponents
) extends FuelMeterController(configuration, controllerComponents) {

  import ReadingsController._

  lazy val lookupUrl: String =
    config.get[String]("vehicle-lookup.service.url")

  val readingForm: Form[Reading] =
    form(DateUtils.today)

  def readings(reg: String): Future[List[Reading]] =
    repository.findAll(reg)

  def uniqueRegistrations: Future[List[VehicleRecordSummary]] =
    repository.uniqueRegistrations()

  def list(reg: String): Action[AnyContent] =
    Action.async {
      readings(reg)
        .recover { case _ => Nil }
        .map(rs => Ok(Json.toJson(VehicleData(reg = reg, readings = rs.map(ReadingData.apply)))))
    }

  def listHtml(reg: String): Action[AnyContent] =
    Action.async {

      def vehicleDetails(reg: String): Future[Option[Vehicle]] =
        ws.url(s"$lookupUrl/$reg")
          .withRequestTimeout(1.seconds)
          .get()
          .map(toClassOf[Vehicle])
          .recover {
            case t =>
              Logger.warn(s"Failed to retrieve vehicle details. ${t.getMessage}")
              None
          }

      Applicative[Future].map3(readings(reg), uniqueRegistrations, vehicleDetails(reg)) { (rs, urs, veh) =>
        Ok(views.html.readings(reg, rs, urs, veh))
          .withCookies(Cookie(name = VRegCookieName, value = reg.filter(_.isLetterOrDigit).mkString, maxAge = Some(Int.MaxValue)))
      }
    }

  def captureForm(reg: String): Action[AnyContent] =
    Action { implicit request =>
      Ok(views.html.captureForm(reg, readingForm))
    }

  //wrap in BasicSecured { ... } to prompt uses for admin/foofoo credentials
  def saveReading(r: String): Action[AnyContent] =
    Action.async { implicit request =>
      readingForm
        .bindFromRequest()
        .fold(
          invalidForm => Future.successful(BadRequest(views.html.captureForm(r, invalidForm))),
          form =>
            repository
              .save(form)
              .map(_ => Redirect(routes.ReadingsController.listHtml(r)))
        )
    }

  def index(): Action[AnyContent] =
    Action.async { implicit request =>
      request.cookies
        .get(VRegCookieName)
        .fold(uniqueRegistrations.map(fs => Ok(views.html.defaultHomePage(fs))))(cookie =>
          Future.successful(Redirect(routes.ReadingsController.listHtml(cookie.value))))
    }

}

object ReadingsController {
  val VRegCookieName = "vreg"
}
