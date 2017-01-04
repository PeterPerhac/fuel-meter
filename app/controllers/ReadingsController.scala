package controllers

import javax.inject.Inject

import models.forms.ReadingForm.form
import models.{Reading, ReadingData, VehicleData, VehicleRecordSummary}
import play.api.i18n.MessagesApi
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc._
import repository.FuelMeterRepository

import scala.concurrent.Future

class ReadingsController @Inject()(val messagesApi: MessagesApi, repo: FuelMeterRepository) extends FuelMeterController {

  private val VReg = "vreg"

  def vRegCookie(implicit r: String) = Cookie(VReg, r, maxAge = Some(Int.MaxValue))

  def readings(implicit r: String): Future[Vector[Reading]] = repo.findAll(r)

  def uniqueRegistrations: Future[Seq[VehicleRecordSummary]] = repo.uniqueRegistrations()

  def list(implicit r: String) = Action.async(readings map (o => Ok(Json.toJson(VehicleData(r, o map ReadingData.apply)))))

  def add: Action[JsValue] = Action.async(parse.json) { implicit req =>
    val r = req.body.as[Reading]
    repo.save(r).map(_ => Redirect(routes.ReadingsController.list(r.reg)))
  }

  def delete(id: String) = Action.async(_ => repo.remove(id).map(_ => NoContent))

  def listHtml(implicit r: String) = Action.async {
    for {
      rs <- readings
      urs <- uniqueRegistrations
    } yield Ok(views.html.readings(r, rs, urs)).withCookies(vRegCookie)
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
