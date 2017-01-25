package controllers

import javax.inject.Inject

import models._
import models.forms.ReadingForm.form
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc._
import repository.FuelMeterRepository
import utils.DateUtils

import scala.concurrent.Future
import scala.concurrent.duration._

class ReadingsController @Inject()(repo: FuelMeterRepository, ds: CommonDependencies) extends FuelMeterController(ds) {

  private val VReg = "vreg"

  lazy val lookupUrl = conf.underlying.getString("vehicle-lookup.service.url")

  val readingForm = form(DateUtils.today)

  def vRegCookie(implicit r: String) = Cookie(name = VReg, value = r.filter(_.isLetterOrDigit).mkString, maxAge = Some(Int.MaxValue))

  def readings(implicit r: String): Future[Vector[Reading]] = repo.findAll(r)

  def uniqueRegistrations: Future[Seq[VehicleRecordSummary]] = repo.uniqueRegistrations()

  def vehicleDetails(implicit reg: String) = {
    ws.url(s"$lookupUrl/$reg").withRequestTimeout(1.seconds).get() map toClassOf[Vehicle] recover {
      case t => Logger.warn(s"Failed to retrieve vehicle details. ${t.getMessage}")
        None
    }
  }

  def list(implicit r: String) = Action.async {
    readings map {
      case Seq() => NotFound
      case readings => Ok(Json.toJson(VehicleData(r, readings map ReadingData.apply)))
    }
  }

  def add: Action[JsValue] = Action.async(parse.json) { implicit req =>
    val r = req.body.as[Reading]
    repo.save(r).map(_ => Redirect(routes.ReadingsController.list(r.reg)))
  }

  def delete(id: String) = Action.async(_ => repo.remove(id).map(_ => NoContent))

  def listHtml(implicit r: String) = Action.async {
    for {
      rs <- readings
      urs <- uniqueRegistrations
      veh <- vehicleDetails
    } yield Ok(views.html.readings(r, rs, urs, veh))
      .withCookies(vRegCookie)
  }

  def captureForm(r: String) = Action(Ok(views.html.captureForm(r, readingForm)))

  //wrap in BasicSecured { ... } to prompt uses for admin/foofoo credentials
  def saveReading(r: String) =
    Action.async { implicit request =>
      readingForm.bindFromRequest() fold(
        invalidForm => Future.successful(BadRequest(views.html.captureForm(r, invalidForm))),
        form => repo.save(form).map(_ => Redirect(routes.ReadingsController.listHtml(r)))
      )
    }

  def index(): Action[AnyContent] = Action.async { implicit request =>
    val homePageOrElse = request.cookies.get(VReg).fold(uniqueRegistrations.map(fs => Ok(views.html.defaultHomePage(fs)))) _
    homePageOrElse(cookie => Future.successful(Redirect(routes.ReadingsController.listHtml(cookie.value))))
  }

}
