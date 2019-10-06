package repository

import cats.data.EitherT
import doobie.Fragments._
import doobie.enum.SqlState
import doobie.free.connection.ConnectionIO
import doobie.implicits._
import doobie.util.fragment._
import models.{Reading, User, VehicleRecordSummary}
object ReadingsRepository {

  private def readingsForRegOrdered(reg: String): Fragment =
    sql"""SELECT
         |reg,
         |refuel_date,
         |miles,
         |mileage,
         |liters,
         |cost
         |FROM reading
         |WHERE reg=$reg
         |ORDER BY refuel_date DESC""".stripMargin

  def lastReading(reg: String): ConnectionIO[Option[Reading]] =
    (readingsForRegOrdered(reg) ++ fr""" LIMIT 1""").query[Reading].option

  def findAll(reg: String): ConnectionIO[List[Reading]] =
    readingsForRegOrdered(reg).query[Reading].to[List]

  def removeByRegistration(reg: String): ConnectionIO[Int] =
    sql"""DELETE FROM reading WHERE reg=$reg""".update.run

  def save(reading: Reading): EitherT[ConnectionIO, Int, Int] =
    EitherT {
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
          case SqlState("23505") => 2002
        }
    }

  private def vehicleSummariesQuery(ownerFilter: Option[User]): Fragment = {
    val owner: Option[Fragment] = ownerFilter.map(user => fr"vo.owner=${user.id}")
    fr"""SELECT
         |v.reg,
         |v.color,
         |v.make,
         |v.model,
         |COUNT(*) AS "count",
         |SUM(r.liters) AS "liters",
         |SUM(r.cost) AS "cost"
         |FROM reading r
         |JOIN vehicle_owner vo ON r.reg = vo.reg
         |JOIN vehicle v ON v.reg = vo.reg""".stripMargin ++
      whereAndOpt(owner) ++
      fr" GROUP BY v.reg, v.color, v.make, v.model "
  }

  def vehicleSamples(count: Int): ConnectionIO[List[VehicleRecordSummary]] =
    (vehicleSummariesQuery(None) ++ fr"""ORDER BY "count" DESC LIMIT $count;""").query[VehicleRecordSummary].to[List]

  def vehiclesOwnedByUser(implicit user: User): ConnectionIO[List[VehicleRecordSummary]] =
    (vehicleSummariesQuery(Some(user)) ++ fr"""ORDER BY reg;""").query[VehicleRecordSummary].to[List]

}
