package repository

import javax.inject.Inject
import models.{Reading, VehicleRecordSummary}
import play.api.libs.json.JsObject
import play.api.libs.json.Json.obj
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.commands.WriteResult
import reactivemongo.api.{Cursor, ReadPreference}
import reactivemongo.play.json.collection.JSONCollection
import utils.SortingUtils._

import scala.concurrent.Future

class RefuelMongoRepository @Inject()(reactiveMongoApi: ReactiveMongoApi) {

  import reactivemongo.play.json.ImplicitBSONHandlers.JsObjectDocumentWriter

  import scala.concurrent.ExecutionContext.Implicits.global

  private lazy val collection: Future[JSONCollection] =
    reactiveMongoApi.database.map(_.collection[JSONCollection]("readings"))

  private def oidSelector(oid: String): JsObject =
    obj("_id" -> obj("$oid" -> oid))

  private def execute[T](op: JSONCollection => Future[T]): Future[T] =
    collection.flatMap(op)

  def findAll(reg: String): Future[List[Reading]] =
    execute(
      _.find(obj("reg" -> reg), None)
        .sort(by("date", Desc))
        .cursor[Reading](ReadPreference.Primary)
        .collect[List](1000, Cursor.DoneOnError[List[Reading]]()))

  def update(oid: String, reading: Reading): Future[WriteResult] =
    execute(_.update(false).one(oidSelector(oid), reading))

  def remove(oid: String): Future[WriteResult] =
    execute(_.delete(ordered = false).one(oidSelector(oid)))

  def removeByRegistration(reg: String): Future[WriteResult] =
    execute(_.delete(ordered = false).one(obj("reg" -> reg)))

  def save(reading: Reading): Future[WriteResult] =
    execute(_.insert(ordered = false).one(reading))

  def uniqueRegistrations(limit: Int = 10): Future[List[VehicleRecordSummary]] =
    execute(_.aggregateWith[VehicleRecordSummary]() { fw =>
      import fw._
      GroupField("reg")("count" -> SumAll, "litres" -> SumField("litres"), "cost" -> SumField("cost")) -> List(
        Sort(Descending("count"), Ascending("_id")),
        Limit(limit))
    }.collect[List](1000, Cursor.DoneOnError[List[VehicleRecordSummary]]()))

}
