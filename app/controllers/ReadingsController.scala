package controllers

import javax.inject.Inject

import models.forms.ReadingForm.form
import models.{Reading, Registration}
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}

import scala.concurrent.Future

class ReadingsController @Inject()(val reactiveMongoApi: ReactiveMongoApi)
  extends Controller with MongoController with ReactiveMongoComponents {

  import utils.DateUtils._

  private def defaultDateString(implicit dp: DateProvider): String = dp().toFormat("yyyy/MM/dd")

  private val VReg = "vreg"

  def vRegCookie(implicit r: Registration) = Cookie(VReg, r, maxAge = Some(Int.MaxValue))

  def repo = new repository.RefuelMongoRepository(reactiveMongoApi)

  def readings(implicit r: Registration): Future[List[JsObject]] = repo.findAll(r)

  def uniqueRegistrations: Future[Seq[String]] = repo.uniqueRegistrations() map (_ flatMap (jso => jso.value.get("_id").map(_.as[String])))

  def list(implicit r: Registration) = Action.async(readings map (o => Ok(Json.toJson(o))))

  def add: Action[JsValue] = Action.async(parse.json) { implicit req =>
    val r = req.body.as[Reading]
    repo.save(r).map(_ => Redirect(routes.ReadingsController.list(r.reg)))
  }

  def delete(id: String) = Action.async(_ => repo.remove(id).map(_ => NoContent))

  def listHtml(implicit r: Registration) = Action.async {
    for {
      rs <- readings
      urs <- uniqueRegistrations
    } yield Ok(views.html.readings(r, rs flatMap (_.validate[Reading].asOpt), urs)).withCookies(vRegCookie)
  }

  def captureForm(r: Registration) = Action(Ok(views.html.captureForm(r, form)))

  def saveReading(r: Registration) = Action.async { implicit request =>
    def fixed(form: Reading) = if (form.date.isEmpty) form.copy(date = defaultDateString) else form

    form.bindFromRequest() fold(
      invalidForm => Future(BadRequest(views.html.captureForm(r, invalidForm))),
      form => repo.save(fixed(form)).map(_ => Redirect(routes.ReadingsController.listHtml(r)))
    )
  }

  def index(): Action[AnyContent] = Action.async { implicit request =>
    val homePageOrElse = request.cookies.get(VReg).fold(uniqueRegistrations map (r => Ok(views.html.defaultHomePage(r)))) _
    homePageOrElse(cookie => Future(Redirect(routes.ReadingsController.listHtml(cookie.value))))
  }
}
