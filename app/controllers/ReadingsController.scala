package controllers

import javax.inject.Inject

import models._
import models.forms.ReadingForm.form
import play.api.i18n.MessagesApi
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.libs.ws.{WSClient, WSResponse}
import play.api.mvc._
import repository.FuelMeterRepository

import scala.concurrent.Future

class ReadingsController @Inject()(val messagesApi: MessagesApi, val ws: WSClient, repo: FuelMeterRepository) extends FuelMeterController {

  private val VReg = "vreg"

  def vRegCookie(implicit r: String) = Cookie(VReg, r.filter(_.isLetterOrDigit).mkString, maxAge = Some(Int.MaxValue))

  def readings(implicit r: String): Future[Vector[Reading]] = repo.findAll(r)

  def uniqueRegistrations: Future[Seq[VehicleRecordSummary]] = repo.uniqueRegistrations()

  def vehicleDetails(implicit reg: String): Future[Option[VehicleDetails]] = {

    def noneOnErrors[T: Reads](wsr: WSResponse): Option[T] = wsr.status match {
      case n if 200 to 299 contains n =>
        wsr.json.validate[T] match {
          case JsSuccess(t, path) => Some(t)
          case JsError(errors) => None
        }
      case _ => None
    }

    ws.url(s"http://localhost:9001/v1/vehicles/$reg").get() map noneOnErrors[VehicleDetails]
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

  def captureForm(r: String) = Action(Ok(views.html.captureForm(r, form)))

  def saveReading(r: String) = Action.async { implicit request =>
    form.bindFromRequest() fold(
      invalidForm => Future(BadRequest(views.html.captureForm(r, invalidForm))),
      form => repo.save(form).map(_ => Redirect(routes.ReadingsController.listHtml(r)))
    )
  }

  def index(): Action[AnyContent] = Action.async { implicit request =>
    val homePageOrElse = request.cookies.get(VReg).fold(uniqueRegistrations.map(fs => Ok(views.html.defaultHomePage(fs)))) _
    homePageOrElse(cookie => Future(Redirect(routes.ReadingsController.listHtml(cookie.value))))
  }
}
