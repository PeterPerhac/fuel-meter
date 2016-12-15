package controllers

import javax.inject.Inject

import model.Reading
import play.api.Play.current
import play.api.data.Forms._
import play.api.data._
import play.api.data.format.Formats._
import play.api.i18n.Messages.Implicits._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.bson.{BSONDocument => Doc}

import scala.collection.GenTraversable
import scala.concurrent.Future
import scala.language.implicitConversions

class ReadingsController @Inject()(val reactiveMongoApi: ReactiveMongoApi)
  extends Controller with MongoController with ReactiveMongoComponents {

  val readingForm: Form[Reading] = Form(
    mapping(
      "reg" -> nonEmptyText(minLength = 4, maxLength = 8),
      "date" -> text,
      "mi" -> of(doubleFormat).verifying("Not in 0..1000 range", d => d > 0.0 && d < 1000.0),
      "total" -> number(min = 0, max = 500000),
      "litres" -> of(doubleFormat).verifying("Not in 0..100 range", d => d > 0.0 && d < 100.0),
      "cost" -> of(doubleFormat).verifying("Not in 0..1000 range", d => d > 0.0 && d < 1000.0)
    )(Reading.apply)(Reading.unapply)
  )

  private val bson = Reading.bsonHandler

  def repo = new repository.RefuelMongoRepository(reactiveMongoApi)

  def readings(implicit reg: Registration): Future[List[JsObject]] = repo.find(Doc("reg" -> reg))

  def list(implicit r: Registration) = Action.async {
    readings map { o => Ok(Json.toJson(o)) }
  }

  def listHtml(implicit r: Registration) = Action.async {
    readings map { os => Ok(views.html.readings(r, os flatMap (_.validate[Reading].asOpt))) }
  }

  def add: Action[JsValue] = Action.async(BodyParsers.parse.json) { implicit req =>
    val r = req.body.as[Reading]
    repo.save(bson.write(r)).map(_ => Redirect(routes.ReadingsController.list(r.reg)))
  }

  def delete(id: String): Action[AnyContent] = {
    Action.async(_ => repo.remove(Doc("_id" -> Doc("$oid" -> id))).map(_ => NoContent))
  }

  def captureForm(r: Registration) = Action {
    Ok(views.html.captureForm(r, readingForm))
  }

  def saveReading() = Action.async { implicit request =>
    readingForm.bindFromRequest() fold(
      withErrors => Future {
        BadRequest(views.html.captureForm(withErrors.data("reg"), withErrors))
      },
      form => repo.save(bson.write(form)).map(_ => Redirect(routes.ReadingsController.listHtml(form.reg)))
    )
  }

  val uniqueRegistrations: JsObject => GenTraversable[String] = jso => jso.fields.find(_._1 == "reg").map(_._2.as[String])

  def index() = Action.async { implicit request =>
    request.cookies.get("vreg").fold {
      repo.topRegistrations map (objects => Ok(views.html.defaultHomePage((objects flatMap uniqueRegistrations).distinct)))
    }(regCookie =>
      Future(Redirect(routes.ReadingsController.listHtml(regCookie.value)))
    )
  }
}
