package services

import cats.data.EitherT.{leftT, liftF}
import cats.data.{EitherT, Kleisli => K}
import doobie.free.connection.ConnectionIO
import models._
import repository._

class VehicleService(
      userProfileService: UserProfileService,
      readingsRepository: ReadingsRepository,
      vehicleRepository: VehicleRepository,
      override val doobieTransactor: DoobieTransactor
) extends FuelMeterService {

  import readingsRepository._
  import vehicleRepository._

  def removeVehicle(implicit userId: UserId): String => EitherT[ConnectionIO, Int, String] =
    reg =>
      userProfileService.getUser.toRight(1000).flatMap { user =>
        if (user.owns(reg)) {
          liftF((K(deleteAllReadings) andThen K(deleteVehicleOwnership) andThen K(deleteVehicle)).apply(reg))
        } else {
          leftT[ConnectionIO, String](1001)
        }
      }

  def saveVehicle(newVehicle: Vehicle)(implicit userId: UserId): EitherT[ConnectionIO, Int, Vehicle] =
    findByReg(newVehicle.reg)
      .toLeft(newVehicle)
      .leftMap(_ => 3001)
      .semiflatMap((K(insertVehicle) andThen K(insertOwnership)).run)

}
