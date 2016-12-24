package controllers

import javax.inject.Inject

import models.forms.ReadingForm.form
import models.{Reading, VehicleRecordSummary}
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}

import scala.concurrent.Future

class ReadingsController @Inject()(val reactiveMongoApi: ReactiveMongoApi)
  extends Controller with MongoController with ReactiveMongoComponents {

  private val VReg = "vreg"

  def vRegCookie(implicit r: String) = Cookie(VReg, r, maxAge = Some(Int.MaxValue))

  def repo = new repository.RefuelMongoRepository(reactiveMongoApi)

  def readings(implicit r: String): Future[List[Reading]] = repo.findAll(r)

  def uniqueRegistrations: Future[Seq[VehicleRecordSummary]] = repo.uniqueRegistrations()

  def list(implicit r: String) = Action.async(readings map (o => Ok(Json.toJson(o))))

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
