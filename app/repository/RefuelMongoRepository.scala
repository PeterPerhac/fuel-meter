package repository

import models.{Reading, Registration}
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

  private def documentIdSelector(oid: String) = Doc("_id" -> Doc("$oid" -> oid))

  def findAll(r: Registration): Future[List[JsObject]] = collection.find(Doc("reg" -> r)).sort(descending("date")).cursor[JsObject](ReadPreference.Primary).collect[List]()

  def update(oid: String, reading: Reading): Future[WriteResult] = collection.update(documentIdSelector(oid), Reading.bsonHandler.write(reading))

  def remove(oid: String): Future[WriteResult] = collection.remove(documentIdSelector(oid))

  def save(reading: Reading): Future[WriteResult] = collection.update(Doc("_id" -> BSONObjectID.generate), Reading.bsonHandler.write(reading), upsert = true)

  /**
    * Runs an aggregation command to figure out which registrations have logged the most readings
    *
    * @param limit top N registrations to retrieve from this aggregation pipeline
    * @return vehicle registrations ordered by (primary: count of readings logged, DESC) (secondary: registration, ASC)
    */
  def uniqueRegistrations(limit: Int = 10): Future[List[JsObject]] = {
    val agg = collection.BatchCommands.AggregationFramework
    collection.aggregate(
      agg.GroupField("reg")("count" -> agg.SumValue(1)),
      List(agg.Sort(agg.Descending("count"), agg.Ascending("_id")), agg.Limit(limit))
    ).map(_.documents)
  }

}
