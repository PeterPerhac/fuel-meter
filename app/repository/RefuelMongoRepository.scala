package repository

import play.api.libs.json.JsObject
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api.ReadPreference
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{BSONObjectID, BSONDocument => Doc}

import scala.concurrent.Future

class RefuelMongoRepository(reactiveMongoApi: ReactiveMongoApi) {

  import SortingHelpers._
  import play.modules.reactivemongo.json._

  import scala.concurrent.ExecutionContext.Implicits.global

  protected def collection: JSONCollection = reactiveMongoApi.db.collection[JSONCollection]("readings")

  def find(query: Doc = Doc()): Future[List[JsObject]] =
    collection.find(query).sort(descending("date")).cursor[JsObject](ReadPreference.Primary).collect[List]()

  def update(selector: Doc, update: Doc): Future[WriteResult] = collection.update(selector, update)

  def remove(document: Doc): Future[WriteResult] = collection.remove(document)

  def save(document: Doc): Future[WriteResult] =
    collection.update(Doc("_id" -> document.get("_id").getOrElse(BSONObjectID.generate)), document, upsert = true)

  def topRegistrations: Future[List[JsObject]] = collection.find(Doc(), Doc("reg" -> 1, "_id" -> 0)).cursor[JsObject]().collect[List]()


}
