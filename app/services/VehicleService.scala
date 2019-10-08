package services

import cats.data.{EitherT, Kleisli => K}
import cats.effect.IO
import controllers.infra.UnauthorisedException
import doobie.free.connection.{AsyncConnectionIO => AIO}
import models._
import repository.DoobieTransactor
import repository.ReadingsRepository.deleteAllReadings
import repository.VehicleRepository._
import utils.TransactionSyntax

class VehicleService(
    override val doobieTransactor: DoobieTransactor
) extends TransactionSyntax {

  def removeVehicle(reg: String)(implicit user: User): IO[String] = transact {
    registrationsOwnedByUser(user).flatMap {
      case l if l.contains(reg) => (K(deleteAllReadings) andThen K(deleteVehicleOwnership) andThen K(deleteVehicle)).apply(reg)
      case _                    => AIO.raiseError[String](UnauthorisedException)
    }
  }

  def saveVehicle(newVehicle: Vehicle)(implicit user: User): EitherT[IO, Int, Vehicle] =
    findByReg(newVehicle.reg)
      .toLeft(newVehicle)
      .leftMap(_ => 3001)
      .semiflatMap((K(insertVehicle) andThen K(insertOwnership)).run)
      .mapK(runTransaction)

}
