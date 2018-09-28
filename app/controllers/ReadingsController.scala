package controllers

import cats.Applicative
import cats.instances.future._
import javax.inject.Inject
import models._
import models.forms.ReadingForm.form
import play.api.Logger
import play.api.data.Form
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc._
import repository.RefuelMongoRepository
import utils.DateUtils

import scala.concurrent.Future
import scala.concurrent.duration._

class ReadingsController @Inject()(repo: RefuelMongoRepository, ds: CommonDependencies) extends FuelMeterController(ds) {

  import ReadingsController._

  lazy val lookupUrl: String =
    conf.underlying.getString("vehicle-lookup.service.url")

  val readingForm: Form[Reading] =
    form(DateUtils.today)

  def readings(reg: String): Future[List[Reading]] =
    repo.findAll(reg)

  def uniqueRegistrations: Future[Seq[VehicleRecordSummary]] =
    repo.uniqueRegistrations()

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

  def captureForm(reg: String) =
    Action(Ok(views.html.captureForm(reg, readingForm)))

  //wrap in BasicSecured { ... } to prompt uses for admin/foofoo credentials
  def saveReading(r: String): Action[AnyContent] =
    Action.async { implicit request =>
      readingForm
        .bindFromRequest()
        .fold(
          invalidForm => Future.successful(BadRequest(views.html.captureForm(r, invalidForm))),
          form =>
            repo
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
