package controllers

import javax.inject.Inject

import model.Reading
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{BSONDocument, BSONObjectID, BSONValue}
import reactivemongo.core.actors.Exceptions.PrimaryUnavailableException

import scala.concurrent.Future
import scala.language.implicitConversions

class ReadingsController @Inject()(val reactiveMongoApi: ReactiveMongoApi)
  extends Controller with MongoController with ReactiveMongoComponents {

  type DOC = BSONDocument

  def refuelRepo = new repository.RefuelMongoRepository(reactiveMongoApi)

  implicit def mongoResultToJson(mongoResult: Future[List[JsObject]]): Future[Result] = {
    mongoResult
      .map(readings => Ok(Json.toJson(readings)))
      .recover { case PrimaryUnavailableException => InternalServerError("Please install MongoDB") }
  }

  def listAll() = Action.async { implicit request =>
    refuelRepo.find()
  }

  def list(registration: String) = Action.async { implicit request =>
    refuelRepo.find(BSONDocument("registration" -> registration))
  }

  //FIXME this only updates registration number of a vehicle associated with a reading, DOES NOT update reading
  def update(id: String) = Action.async(BodyParsers.parse.json) { implicit request =>
    val reading = request.body.as[Reading]
    refuelRepo.update(BSONDocument("id" -> BSONObjectID(id)), BSONDocument("$set" -> BSONDocument("registration" -> reading.registration)))
      .map(u => Ok(Json.obj("success" -> u.ok)))
  }

  def delete(id: String) = Action.async {
    refuelRepo.remove(BSONDocument("id" -> BSONObjectID(id))).map(_ => Redirect("/app/index.html"))
  }

  private def RedirectAfterPost(result: WriteResult, call: Call): Result =
    if (result.ok) Redirect(call) else InternalServerError(result.toString)

  def add = Action.async(BodyParsers.parse.json) { implicit request =>
    val reading = request.body.as[Reading]
    val reg = reading.registration
    //why below not working???
    //    refuelRepo.save(Reading.bsonHandler.write(reading)).map(u => Redirect(routes.ReadingsController.list(reg)))
    refuelRepo.save(
      BSONDocument(
        "registration" -> reg,
        "date" -> reading.date,
        "total" -> reading.total,
        "miles" -> reading.miles,
        "litres" -> reading.litres,
        "cost" -> reading.cost
      )).map(u => Redirect(routes.ReadingsController.list(reg)))
  }

}