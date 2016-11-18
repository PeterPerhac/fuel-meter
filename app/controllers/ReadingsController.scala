package controllers

import javax.inject.Inject

import model.Reading
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.bson.{BSONObjectID, BSONDocument => DOC}
import reactivemongo.core.actors.Exceptions.PrimaryUnavailableException

import scala.concurrent.Future
import scala.language.implicitConversions

class ReadingsController @Inject()(val reactiveMongoApi: ReactiveMongoApi)
  extends Controller with MongoController with ReactiveMongoComponents {

  def repo = new repository.RefuelMongoRepository(reactiveMongoApi)

  implicit def mongoResultToJson(mongoResult: Future[List[JsObject]]): Future[Result] = {
    mongoResult
      .map(readings => Ok(Json.toJson(readings)))
      .recover { case PrimaryUnavailableException => InternalServerError("Please install MongoDB") }
  }

  def listAll() = Action.async { implicit req => repo.find() }

  def list(registration: String) = Action.async { implicit req => repo.find(DOC("registration" -> registration)) }

  def add = Action.async(BodyParsers.parse.json) { implicit req =>
    val reading = req.body.as[Reading]
    val reg = reading.registration
    repo.save(Reading.bsonHandler.write(reading)).map(res => Redirect(routes.ReadingsController.list(reg)))
  }

  //FIXME this only updates registration number of a vehicle associated with a reading, DOES NOT update reading
  def update(id: String) = Action.async(BodyParsers.parse.json) { implicit req =>
    val reading = req.body.as[Reading]
    repo.update(DOC("id" -> BSONObjectID(id)), DOC("$set" -> DOC("registration" -> reading.registration)))
      .map(u => Ok(Json.obj("success" -> u.ok)))
  }

  def delete(id: String) = Action.async { implicit req => repo.remove(DOC("id" -> id)).map(_ => Redirect("/app/index.html")) }

}