package repository

import models.{Reading, VehicleRecordSummary}
import play.api.Logger
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.commands.WriteResult
import reactivemongo.api.{Cursor, ReadPreference}
import reactivemongo.bson.{BSONObjectID, BSONDocument => Doc}
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.Future

class RefuelMongoRepository(reactiveMongoApi: ReactiveMongoApi) {

  import play.modules.reactivemongo.json._
  import utils.SortingUtils._

  import scala.concurrent.ExecutionContext.Implicits.global

  private val collection: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection[JSONCollection]("readings"))

  private def oidSelector(oid: String) = Doc("_id" -> Doc("$oid" -> oid))

  def findAll(r: String): Future[Vector[Reading]] = {
    collection.flatMap { coll =>
      val foo = coll.find(Doc("reg" -> r))
        .sort(by("date", Desc))
        .cursor[Reading](ReadPreference.Primary)

      foo.collect[Vector](1000, Cursor.DoneOnError[Vector[Reading]]())
    }
  }

  def update(oid: String, reading: Reading): Future[WriteResult] = collection.flatMap(_.update(oidSelector(oid), reading))

  def remove(oid: String): Future[WriteResult] = collection.flatMap(_.remove(oidSelector(oid)))

  def removeByRegistration(r: String): Future[WriteResult] = collection.flatMap(_.remove(Doc("reg" -> r)))

  def save(reading: Reading): Future[WriteResult] = collection.flatMap(_.update(Doc("_id" -> BSONObjectID.generate), reading, upsert = true))

  def uniqueRegistrations(limit: Int = 10): Future[List[VehicleRecordSummary]] = {
    collection.flatMap(coll => {
      import coll.BatchCommands.AggregationFramework._
      coll.aggregate(
        GroupField("reg")("count" -> SumValue(1), "litres" -> SumField("litres")),
        List(Sort(Descending("count"), Ascending("_id")), Limit(limit))
      ).map(_.firstBatch.flatMap(_.asOpt[VehicleRecordSummary]))
    })
  }

}