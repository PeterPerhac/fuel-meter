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

  import SortingUtils._
  import play.modules.reactivemongo.json._
  import scala.concurrent.ExecutionContext.Implicits.global

  private val collection: JSONCollection = reactiveMongoApi.db.collection[JSONCollection]("readings")

  private def oidSelector(oid: String) = Doc("_id" -> Doc("$oid" -> oid))

  def findAll(r: Registration): Future[List[JsObject]] = collection.find(Doc("reg" -> r)).sort(by("date")).cursor[JsObject](ReadPreference.Primary).collect[List]()

  def update(oid: String, reading: Reading): Future[WriteResult] = collection.update(oidSelector(oid), reading)

  def remove(oid: String): Future[WriteResult] = collection.remove(oidSelector(oid))

  def save(reading: Reading): Future[WriteResult] = collection.update(Doc("_id" -> BSONObjectID.generate), reading, upsert = true)

  /**
    * Runs an aggregation command to figure out which registrations have logged the most readings
    *
    * @param limit top N registrations to retrieve from this aggregation pipeline
    * @return vehicle registrations ordered by (primary: count of readings logged, DESC) (secondary: registration, ASC)
    */
  def uniqueRegistrations(limit: Int = 10): Future[List[JsObject]] = {
    import collection.BatchCommands.AggregationFramework._
    collection.aggregate(
      GroupField("reg")("count" -> SumValue(1)),
      List(Sort(Descending("count"), Ascending("_id")), Limit(limit))
    ).map(_.documents)
  }

}