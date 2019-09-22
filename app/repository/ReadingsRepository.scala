package repository

import doobie.free.connection.ConnectionIO
import doobie.implicits._
import javax.inject.Inject
import models.{Reading, VehicleRecordSummary}

class ReadingsRepository @Inject()() {

  def findAll(reg: String): ConnectionIO[List[Reading]] =
    sql"""select reg, refuel_date, miles, mileage, liters, cost from reading where reg=$reg""".query[Reading].to[List]

  def removeByRegistration(reg: String): ConnectionIO[Int] =
    sql"""delete from reading where reg=$reg""".update.run

  def save(reading: Reading): ConnectionIO[Int] =
    sql"""insert into reading(refuel_date, reg, miles, mileage, liters, cost) values (${reading.date}::date, ${reading.reg}, ${reading.miles}, ${reading.mileage}, ${reading.liters}, ${reading.cost})""".update.run

  def uniqueRegistrations(limit: Int = 10): ConnectionIO[List[VehicleRecordSummary]] =
    sql"""select reg, COUNT(*) as "count", SUM(liters) as "liters", SUM(cost) as "cost" FROM reading group by reg limit $limit;"""
      .query[VehicleRecordSummary]
      .to[List]
}
