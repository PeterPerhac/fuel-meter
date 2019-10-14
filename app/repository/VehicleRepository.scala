package repository

import cats.data.OptionT
import doobie.free.connection.ConnectionIO
import doobie.implicits._
import doobie.util.fragment.Fragment
import doobie.util.fragments.whereAndOpt
import models.{UserId, Vehicle, VehicleRecordSummary}

class VehicleRepository {

  def findByReg(reg: String): OptionT[ConnectionIO, Vehicle] =
    OptionT(sql"""SELECT reg, make, model, year, color FROM vehicle WHERE reg=$reg""".query[Vehicle].option)

  def insertOwnership(vehicle: Vehicle)(implicit userId: UserId): ConnectionIO[Vehicle] =
    sql"""INSERT INTO vehicle_owner(reg, owner) VALUES (${vehicle.reg}, ${userId.id})""".update.run.map(_ => vehicle)

  def insertVehicle(vehicle: Vehicle): ConnectionIO[Vehicle] =
    sql"""INSERT INTO vehicle(reg, make, model, year, color)
         |VALUES (${vehicle.reg}, ${vehicle.make}, ${vehicle.model}, ${vehicle.year}, ${vehicle.color})""".stripMargin.update.run
      .map(_ => vehicle)

  def deleteVehicleOwnership(reg: String): ConnectionIO[String] =
    sql"DELETE FROM vehicle_owner WHERE reg=$reg".update.run.map(_ => reg)

  def deleteVehicle(reg: String): ConnectionIO[String] =
    sql"DELETE FROM vehicle WHERE reg=$reg".update.run.map(_ => reg)

  private def vehicleSummariesQuery(regFilter: Option[String], ownerFilter: Option[UserId]): Fragment = {
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
    (vehicleSummariesQuery(None, None) ++ fr"""ORDER BY "count" DESC, reg LIMIT $count;""")
      .query[VehicleRecordSummary]
      .to[List]

  def vehiclesOwnedByUser(implicit userId: UserId): ConnectionIO[List[VehicleRecordSummary]] =
    (vehicleSummariesQuery(None, Some(userId)) ++ fr"""ORDER BY reg;""").query[VehicleRecordSummary].to[List]

  def vehicleSummary(reg: String): OptionT[ConnectionIO, VehicleRecordSummary] =
    OptionT(vehicleSummariesQuery(Some(reg), None).query[VehicleRecordSummary].option)

}
