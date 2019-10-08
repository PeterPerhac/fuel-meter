package repository

import cats.data.OptionT
import doobie.free.connection.ConnectionIO
import doobie.implicits._
import models.{User, Vehicle}

object VehicleRepository {

  def findByReg(reg: String): OptionT[ConnectionIO, Vehicle] =
    OptionT(sql"""SELECT reg, make, model, year, color FROM vehicle WHERE reg=$reg""".query[Vehicle].option)

  def insertOwnership(vehicle: Vehicle)(implicit user: User): ConnectionIO[Vehicle] =
    sql"""INSERT INTO vehicle_owner(reg, owner) VALUES (${vehicle.reg}, ${user.id})""".update.run.map(_ => vehicle)

  def insertVehicle(vehicle: Vehicle): ConnectionIO[Vehicle] =
    sql"""INSERT INTO vehicle(reg, make, model, year, color)
         |VALUES (${vehicle.reg}, ${vehicle.make}, ${vehicle.model}, ${vehicle.year}, ${vehicle.color})""".stripMargin.update.run
      .map(_ => vehicle)

  def registrationsOwnedByUser(user: User): ConnectionIO[List[String]] =
    sql"""SELECT reg FROM vehicle_owner where owner=${user.id}""".query[String].to[List]

  def deleteVehicleOwnership(reg: String): ConnectionIO[String] =
    sql"DELETE FROM vehicle_owner WHERE reg=$reg".update.run.map(_ => reg)

  def deleteVehicle(reg: String): ConnectionIO[String] =
    sql"DELETE FROM vehicle WHERE reg=$reg".update.run.map(_ => reg)

}
