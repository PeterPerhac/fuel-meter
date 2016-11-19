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

  def repo = new repository.RefuelMongoRepository(reactiveMongoApi)

  implicit def mongoResultToJson(mongoResult: Future[List[JsObject]]): Future[Result] = mongoResult.map(readings => Ok(Json.toJson(readings)))

  def listAll() = Action.async { implicit req => repo.find() }

  def list(registration: String) = Action.async { implicit req => repo.find(Doc("reg" -> registration)) }

  def add = Action.async(BodyParsers.parse.json) { implicit req =>
    val r = req.body.as[Reading]
    repo.save(Reading.bsonHandler.write(r)).map(res => Redirect(routes.ReadingsController.list(r.reg)))
  }

  //FIXME this only updates registration number of a vehicle associated with a reading, DOES NOT update reading
  def update(id: String) = Action.async(BodyParsers.parse.json) { implicit req =>
    val reading = req.body.as[Reading]
    repo.update(Doc("id" -> BSONObjectID(id)), Doc("$set" -> Doc("reg" -> reading.reg))).map(u => Ok(Json.obj("success" -> u.ok)))
  }

  def delete(id: String) = Action.async { implicit req => repo.remove(Doc("id" -> id)).map(_ => Redirect("/app/index.html")) }

}
