package services

import cats.data.OptionT
import cats.data.OptionT.liftF
import cats.effect.IO
import cats.implicits._
import doobie._
import doobie.free.connection.{AsyncConnectionIO => AIO}
import models.UserProfile.twitterReads
import models.{User, UserProfile, VehicleOwner}
import play.api.libs.json.{JsPath, JsValue, JsonValidationError}
import repository.DoobieTransactor
import repository.ReadingsRepository.vehiclesOwnedByUser
import repository.UserProfileRepository._
import scalaj.http.Token
import utils.TransactionSyntax

import scala.collection.Seq

class UserProfileService(
    verifyCredentials: Token => IO[JsValue],
    override val doobieTransactor: DoobieTransactor
) extends TransactionSyntax {

  private val errorHandler: Seq[(JsPath, Seq[JsonValidationError])] => ConnectionIO[UserProfile] = es =>
    AIO.raiseError(new IllegalArgumentException(s"Failed to parse the returned user profile! ${es.mkString("[", ",", "]")}"))

  def createOrRetrieveUser(accessToken: Token): IO[UserProfile] = {
    val createProfile: JsValue => ConnectionIO[UserProfile] = profileJson =>
      profileJson.validate[UserProfile](twitterReads(accessToken)).fold(errorHandler, AIO.pure).flatTap(createUserProfile)

    transact(userProfileByAccessToken(accessToken).getOrElseF(AIO.liftIO(verifyCredentials(accessToken)) >>= createProfile))
  }

  def getVehicleOwner(implicit user: User): OptionT[IO, VehicleOwner] =
    (for {
      profile  <- findUserProfile(user.id)
      vehicles <- liftF(vehiclesOwnedByUser)
    } yield VehicleOwner(profile, vehicles)).mapK(runTransaction)

}
