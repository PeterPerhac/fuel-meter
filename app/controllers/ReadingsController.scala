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

  implicit def mongoResultToJson(mongoResult: Future[List[JsObject]]): Future[Result] = mongoResult.map(readings => Ok(Json.toJson(readings)))

  def listAll() = Action.async { _ => repo.find() }

  def list(registration: String) = Action.async { _ => repo.find(Doc("reg" -> registration)) }

  def add = Action.async(BodyParsers.parse.json) { implicit req =>
    val r = req.body.as[Reading]
    repo.save(bson.write(r)).map(_ => Redirect(routes.ReadingsController.list(r.reg)))
  }

  def delete(id: String) = {
    Action.async(_ => repo.remove(Doc("_id" -> Doc("$oid" -> id))).map(_ => NoContent))
  }

}
