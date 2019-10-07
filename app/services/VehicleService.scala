package services

import cats.data.{EitherT, Kleisli}
import cats.effect.IO
import doobie.free.connection.{AsyncConnectionIO => AIO}
import models._
import repository.DoobieTransactor
import repository.VehicleRepository._
import utils.TransactionSyntax

class VehicleService(
    override val doobieTransactor: DoobieTransactor
) extends TransactionSyntax {

  def saveVehicle(newVehicle: Vehicle)(implicit user: User): EitherT[IO, Int, Vehicle] =
    findByReg(newVehicle.reg)
      .toLeft(newVehicle)
      .leftMap(_ => 3001)
      .semiflatMap(Kleisli(insertVehicle).andThen(Kleisli(insertOwnership)).run)
      .mapK(runTransaction)

}
