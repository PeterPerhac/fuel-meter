package repository

import doobie.free.connection.ConnectionIO
import doobie.implicits._
import models.{Reading, User}

object VehicleRepository {

  def findAll(reg: String): ConnectionIO[List[Reading]] =
    sql"""select reg, refuel_date, miles, mileage, liters, cost from reading where reg=$reg order by refuel_date desc"""
      .query[Reading]
      .to[List]

  def registrationsOwnedByUser(user: User): ConnectionIO[List[String]] =
    sql"""SELECT reg FROM vehicle_owner where owner=${user.id}""".query[String].to[List]

}
