package controllers

import javax.inject.Inject

import model.Reading
import play.api.data.Forms._
import play.api.data._
import play.api.data.format.Formats._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.bson.{BSONDocument => Doc}

import scala.concurrent.Future
import scala.language.implicitConversions
import play.api.Play.current
import play.api.i18n.Messages.Implicits._

class ReadingsController @Inject()(val reactiveMongoApi: ReactiveMongoApi)
  extends Controller with MongoController with ReactiveMongoComponents {

  val readingForm: Form[Reading] = Form(
    mapping(
      "reg" -> nonEmptyText,
      "date" -> text,
      "mi" -> of(doubleFormat),
      "total" -> number(min=0, max=500000),
      "litres" -> of(doubleFormat),
      "cost" -> of(doubleFormat)
    )(Reading.apply)(Reading.unapply)
  )

  private val bson = Reading.bsonHandler

  def repo = new repository.RefuelMongoRepository(reactiveMongoApi)

  def readings(implicit reg: Registration): Future[List[JsObject]] = repo.find(Doc("reg" -> reg))

  def list(implicit r: Registration) = Action.async {
    readings map { o => Ok(Json.toJson(o)) }
  }

  def listHtml(implicit r: Registration) = Action.async {
    readings map { o => Ok(views.html.readings(r, o map (_.validate[Reading].get))) }
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

  def saveReading() = Action.async(parse.form(readingForm)) { implicit request =>
    val r = request.body
    repo.save(bson.write(r)).map(_ => Redirect(routes.ReadingsController.listHtml(r.reg)))
  }
}
