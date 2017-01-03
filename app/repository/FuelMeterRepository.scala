package repository

import com.google.inject.ImplementedBy
import models.{Reading, VehicleRecordSummary}
import reactivemongo.api.commands.WriteResult

import scala.concurrent.Future

@ImplementedBy(classOf[RefuelMongoRepository])
trait FuelMeterRepository {

  def findAll(r: String): Future[Vector[Reading]]

  def update(oid: String, reading: Reading): Future[WriteResult]

  def remove(oid: String): Future[WriteResult]

  def removeByRegistration(r: String): Future[WriteResult]

  def save(reading: Reading): Future[WriteResult]

  def uniqueRegistrations(limit: Int = 10): Future[List[VehicleRecordSummary]]

}
