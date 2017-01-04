package repository

import javax.inject.Inject

import models.{Reading, VehicleRecordSummary}
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.commands.WriteResult
import reactivemongo.api.{Cursor, ReadPreference}
import reactivemongo.bson.{BSONObjectID, BSONDocument => Doc}
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.Future

class RefuelMongoRepository @Inject()(reactiveMongoApi: ReactiveMongoApi) extends FuelMeterRepository {

  import play.modules.reactivemongo.json._
  import utils.SortingUtils._

  import scala.concurrent.ExecutionContext.Implicits.global

  private implicit lazy val collection = reactiveMongoApi.database.map(_.collection[JSONCollection]("readings"))

  private def oidSelector(oid: String) = Doc("_id" -> Doc("$oid" -> oid))

  private def generateId() = Doc("_id" -> BSONObjectID.generate)

  private def execute[T](op: JSONCollection => Future[T]): Future[T] = implicitly[Future[JSONCollection]] flatMap op

  def findAll(r: String): Future[Vector[Reading]] = {
    execute {
      _.find(Doc("reg" -> r)).sort(by("date", Desc)).cursor[Reading](ReadPreference.Primary)
        .collect[Vector](1000, Cursor.DoneOnError[Vector[Reading]]())
    }
  }

  def update(oid: String, reading: Reading): Future[WriteResult] = execute(_.update(oidSelector(oid), reading))

  def remove(oid: String): Future[WriteResult] = execute(_.remove(oidSelector(oid)))

  def removeByRegistration(r: String): Future[WriteResult] = execute(_.remove(Doc("reg" -> r)))

  def save(reading: Reading): Future[WriteResult] = execute(_.update(generateId(), reading, upsert = true))

  def uniqueRegistrations(limit: Int = 10): Future[List[VehicleRecordSummary]] = {
    execute(coll => {
      import coll.BatchCommands.AggregationFramework._
      coll.aggregate(
        GroupField("reg")("count" -> SumValue(1), "litres" -> SumField("litres")),
        List(Sort(Descending("count"), Ascending("_id")), Limit(limit))
      ).map(_.firstBatch.flatMap(_.asOpt[VehicleRecordSummary]))
    })
  }

}