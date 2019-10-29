package services

import cats.data.EitherT.right
import cats.data.{EitherT, OptionT}
import cats.implicits._
import doobie.free.connection.ConnectionIO
import models.{Reading, ReadingsVM, User, UserId}
import repository._

class ReadingsService(
      userProfileService: UserProfileService,
      readingsRepository: ReadingsRepository,
      vehicleRepository: VehicleRepository,
      override val doobieTransactor: DoobieTransactor
) extends FuelMeterService {

  import readingsRepository._
  import vehicleRepository._

  def readings(reg: String): ConnectionIO[List[Reading]] = findAll(reg)

  private def validateNewReading(
        newReading: Reading,
        latestReading: Option[Reading],
        ownedVehicles: List[String]
  ): Either[Int, Reading] =
    for {
      _ <- Either.cond(ownedVehicles.exists(_.equalsIgnoreCase(newReading.reg)), newReading, 2000)
      _ <- Either.cond(!latestReading.exists(_.mileage >= newReading.mileage), newReading, 2001)
    } yield newReading

  def saveReading(newReading: Reading)(implicit userId: UserId): EitherT[ConnectionIO, Int, Int] =
    for {
      user <- userProfileService.getUser.toRight(1000)
      registrationsOwned = user.vehiclesOwned.map(_.reg)
      lastReading  <- right(lastReading(newReading.reg))
      validReading <- EitherT.fromEither[ConnectionIO](validateNewReading(newReading, lastReading, registrationsOwned))
      n            <- save(validReading)
    } yield n

  def readingsViewModel(reg: String, optUser: Option[User]): OptionT[ConnectionIO, ReadingsVM] =
    vehicleSummary(reg).semiflatMap { summary =>
      (readings(reg), vehicleSamples(10)).mapN(ReadingsVM.apply(summary, _, _, optUser))
    }

}
