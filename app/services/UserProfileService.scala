package services

import cats.Applicative
import cats.data.OptionT
import cats.data.OptionT.liftF
import cats.effect.IO
import cats.implicits._
import doobie._
import models.UserProfile.twitterReads
import models.{User, UserId, UserProfile}
import play.api.libs.json.{JsPath, JsValue, JsonValidationError}
import repository.{DoobieTransactor, UserProfileRepository, VehicleRepository}
import scalaj.http.Token

import scala.collection.Seq

class UserProfileService(
      verifyCredentials: Token => IO[JsValue],
      userProfileRepository: UserProfileRepository,
      vehicleRepository: VehicleRepository,
      override val doobieTransactor: DoobieTransactor
) extends FuelMeterService {

  import userProfileRepository._
  import vehicleRepository._

  private val errorHandler: Seq[(JsPath, Seq[JsonValidationError])] => ConnectionIO[UserProfile] = es =>
    ACIO.raiseError(
      new IllegalArgumentException(s"Failed to parse the returned user profile! ${es.mkString("[", ",", "]")}")
    )

  def createOrRetrieveUser(accessToken: Token): ConnectionIO[UserProfile] = {
    val createProfile: JsValue => ConnectionIO[UserProfile] = profileJson =>
      profileJson
        .validate[UserProfile](twitterReads(accessToken))
        .fold(errorHandler, ACIO.pure)
        .flatTap(createUserProfile)

    userProfileByAccessToken(accessToken).getOrElseF(ACIO.liftIO(verifyCredentials(accessToken)) >>= createProfile)
  }

  def getUser(implicit userId: UserId): OptionT[ConnectionIO, User] = {
    type F[A] = OptionT[ConnectionIO, A]
    Applicative[F].map2(findUserProfile(userId.id), liftF(vehiclesOwnedByUser))(User.apply)
  }

}
