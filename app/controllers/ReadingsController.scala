package controllers

import javax.inject.Inject

import model.Reading
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.bson.{BSONObjectID, BSONDocument => Doc}

import scala.concurrent.Future
import scala.language.implicitConversions

class ReadingsController @Inject()(val reactiveMongoApi: ReactiveMongoApi)
  extends Controller with MongoController with ReactiveMongoComponents {

  private val bson = Reading.bsonHandler

  def repo = new repository.RefuelMongoRepository(reactiveMongoApi)

  def list(registration: String) = Action.async { _ =>
    repo.find(Doc("reg" -> registration)) map { r => Ok(Json.toJson(r)) }
  }

  def listHtml(registration: String): Action[AnyContent] = Action.async { _ =>
    repo.find(Doc("reg" -> registration)).map(readings =>
      Ok(views.html.readings(registration, readings.map(_.validate[Reading].get))))
  }

  def add: Action[JsValue] = Action.async(BodyParsers.parse.json) { implicit req =>
    val r = req.body.as[Reading]
    repo.save(bson.write(r)).map(_ => Redirect(routes.ReadingsController.list(r.reg)))
  }

  def delete(id: String): Action[AnyContent] = {
    Action.async(_ => repo.remove(Doc("_id" -> Doc("$oid" -> id))).map(_ => NoContent))
  }

  def captureForm(registration: String) = Action {
    Ok(views.html.captureForm(registration))
  }

}
