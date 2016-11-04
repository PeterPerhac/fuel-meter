package controllers

import javax.inject.Inject

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.{JsObject, Json}
import play.api.mvc._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import reactivemongo.core.actors.Exceptions.PrimaryUnavailableException

import scala.concurrent.Future
import scala.language.implicitConversions

class ReadingsController @Inject()(val reactiveMongoApi: ReactiveMongoApi)
  extends Controller with MongoController with ReactiveMongoComponents {

  import controllers.ReadingFields._

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
    refuelRepo.find(BSONDocument(Registration -> registration))
  }

  //FIXME this only updates registration number of a vehicle associated with a reading, DOES NOT update reading
  def update(id: String) = Action.async(BodyParsers.parse.json) { implicit request =>
    val value = (request.body \ Registration).as[String]
    refuelRepo.update(BSONDocument(Id -> BSONObjectID(id)), BSONDocument("$set" -> BSONDocument(Registration -> value)))
      .map(le => Ok(Json.obj("success" -> le.ok)))
  }

  def delete(id: String) = Action.async {
    refuelRepo.remove(BSONDocument(Id -> BSONObjectID(id))).map(_ => Redirect("/app/index.html"))
  }

  private def RedirectAfterPost(result: WriteResult, call: Call): Result =
    if (result.inError) InternalServerError(result.toString)
    else Redirect(call)

  //TODO this is horrid, must be a less painful way of receiving and storing a JSON object
  def add = Action.async(BodyParsers.parse.json) { implicit request =>
    val reg = (request.body \ Registration).as[String]
    val date = (request.body \ Date).as[String]
    val total = (request.body \ Total).as[Int]
    val miles = (request.body \ Miles).as[Double]
    val litres = (request.body \ Litres).as[Double]
    val cost = (request.body \ Cost).as[Double]
    refuelRepo.save(BSONDocument(
      Registration -> reg,
      //posted date is ignored but default should only be provided if not posted
      Date -> System.currentTimeMillis(),
      Total -> total,
      Miles -> miles,
      Litres -> litres,
      Cost -> cost
    )).map(le => Redirect(routes.ReadingsController.list(reg)))
  }
}

object ReadingFields {
  val Id = "_id"
  val Registration = "reg"
  val Miles = "mi"
  val Total = "total"
  val Litres = "litres"
  val Cost = "cost"
  val Date = "date"
}

