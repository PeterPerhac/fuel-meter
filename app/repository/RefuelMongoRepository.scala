package repository

import play.api.libs.json.JsObject
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api.ReadPreference
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{BSONObjectID, BSONDocument => Doc}

import scala.concurrent.{ExecutionContext, Future}

class RefuelMongoRepository(reactiveMongoApi: ReactiveMongoApi) {

  // really don't like this but does the job. I wish this could be done more "implicitly"
  import SortingHelpers._

  // BSON-JSON implicit writers
  import play.modules.reactivemongo.json._

  protected def collection = reactiveMongoApi.db.collection[JSONCollection]("readings")

  def find(query: Doc = Doc())(implicit ec: ExecutionContext): Future[List[JsObject]] =
    collection.find(query).sort(descending("date")).cursor[JsObject](ReadPreference.Primary).collect[List]()

  def update(selector: Doc, update: Doc)(implicit ec: ExecutionContext): Future[WriteResult] = collection.update(selector, update)

  def remove(document: Doc)(implicit ec: ExecutionContext): Future[WriteResult] = collection.remove(document)

  def save(document: Doc)(implicit ec: ExecutionContext): Future[WriteResult] =
    collection.update(Doc("_id" -> document.get("_id").getOrElse(BSONObjectID.generate)), document, upsert = true)

}
