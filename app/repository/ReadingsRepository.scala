package repository

import cats.data.{EitherT, OptionT}
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

  def deleteAllReadings(reg: String): ConnectionIO[String] =
    sql"""DELETE FROM reading WHERE reg=$reg""".update.run.map(_ => reg)

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

  def vehicleSummariesQuery(regFilter: Option[String], ownerFilter: Option[User]): Fragment = {
    val owner: Option[Fragment] = ownerFilter.map(user => fr"vo.owner=${user.id}")
    val reg: Option[Fragment] = regFilter.map(r => fr"vo.reg=$r")
    fr"""
        |SELECT
        |    v.reg,
        |    v.color,
        |    v.make,
        |    v.model,
        |    COUNT(r.reg) AS "count",
        |    CASE WHEN SUM(r.liters) IS NULL THEN 0 ELSE SUM(r.liters) END AS "liters",
        |    CASE WHEN SUM(r.cost) IS NULL THEN 0 ELSE SUM(r.cost) END AS "cost"
        |FROM
        |    vehicle v
        |    LEFT JOIN reading r ON r.reg = v.reg
        |    JOIN vehicle_owner vo ON v.reg = vo.reg""".stripMargin ++
      whereAndOpt(owner) ++
      whereAndOpt(reg) ++
      fr" GROUP BY v.reg, v.color, v.make, v.model "
  }

  def vehicleSamples(count: Int): ConnectionIO[List[VehicleRecordSummary]] =
    (vehicleSummariesQuery(None, None) ++ fr"""ORDER BY "count" DESC, reg LIMIT $count;""").query[VehicleRecordSummary].to[List]

  def vehiclesOwnedByUser(implicit user: User): ConnectionIO[List[VehicleRecordSummary]] =
    (vehicleSummariesQuery(None, Some(user)) ++ fr"""ORDER BY reg;""").query[VehicleRecordSummary].to[List]

  def vehicleSummary(reg: String): OptionT[ConnectionIO, VehicleRecordSummary] =
    OptionT((vehicleSummariesQuery(Some(reg), None)).query[VehicleRecordSummary].option)

}
