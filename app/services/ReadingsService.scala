package services

import cats.data.EitherT
import cats.data.EitherT.right
import cats.effect.IO
import doobie.free.connection.{AsyncConnectionIO => AIO}
import models.{Reading, User}
import repository.DoobieTransactor
import repository.ReadingsRepository._
import repository.VehicleRepository.registrationsOwnedByUser
import utils.TransactionSyntax

class ReadingsService(
    override val doobieTransactor: DoobieTransactor
) extends TransactionSyntax {

  private def validateNewReading(newReading: Reading,
                                 latestReading: Option[Reading],
                                 ownedVehicles: List[String]): Either[Int, Reading] =
    for {
      _ <- Either.cond(ownedVehicles.exists(_.equalsIgnoreCase(newReading.reg)), newReading, 2000)
      _ <- Either.cond(!latestReading.exists(_.mileage >= newReading.mileage), newReading, 2001)
    } yield newReading

  def saveReading(newReading: Reading)(implicit user: User): EitherT[IO, Int, Int] =
    (for {
      ownedVehicles <- right(registrationsOwnedByUser(user))
      lastReading   <- right(lastReading(newReading.reg))
      validReading  <- EitherT(AIO.pure(validateNewReading(newReading, lastReading, ownedVehicles)))
      n             <- save(validReading)
    } yield n).mapK(runTransaction)

}
