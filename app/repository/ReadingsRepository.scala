package repository

import doobie.enum.SqlState
import doobie.free.connection.ConnectionIO
import doobie.implicits._
import models.{Reading, VehicleRecordSummary}

object ReadingsRepository {

  def findAll(reg: String): ConnectionIO[List[Reading]] =
    sql"""SELECT 
         |reg, 
         |refuel_date, 
         |miles, 
         |mileage, 
         |liters, 
         |cost 
         |FROM reading 
         |WHERE reg=$reg 
         |ORDER BY refuel_date DESC""".stripMargin.query[Reading].to[List]

  def removeByRegistration(reg: String): ConnectionIO[Int] =
    sql"""DELETE FROM reading WHERE reg=$reg""".update.run

  def save(reading: Reading): ConnectionIO[Either[String, Int]] =
    sql"""INSERT INTO reading(
         |refuel_date,
         |reg,
         |miles,
         |mileage,
         |liters,
         |cost
         |) VALUES (
         |${reading.date}::DATE,
         |${reading.reg},
         |${reading.miles},
         |${reading.mileage},
         |${reading.liters},
         |${reading.cost})""".stripMargin.update.run
      .attemptSomeSqlState {
        case SqlState("23505") => "error.constraintViolation.doubleMileage"
      }

  def uniqueRegistrations(limit: Int = 10): ConnectionIO[List[VehicleRecordSummary]] =
    sql"""SELECT 
         |v.reg,
         |v.color,
         |v.make,
         |v.model,
         |COUNT(*) AS "count", 
         |SUM(r.liters) AS "liters",
         |SUM(r.cost) AS "cost"
         |FROM reading r JOIN vehicle v ON r.reg = v.reg AND v.is_public
         |GROUP BY v.reg, v.color, v.make, v.model
         |ORDER BY "count" DESC
         |LIMIT $limit;""".stripMargin.query[VehicleRecordSummary].to[List]
}
