package repository

import cats.data.EitherT
import doobie.enum.SqlState
import doobie.free.connection.ConnectionIO
import doobie.implicits._
import doobie.util.fragment._
import models.Reading

class ReadingsRepository {

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

}
