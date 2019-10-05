package services

import cats.effect.IO
import cats.implicits._
import doobie._
import doobie.implicits._
import models.UserProfile
import models.UserProfile.twitterReads
import play.api.libs.json.{JsPath, JsValue, JsonValidationError}
import repository.DoobieTransactor
import repository.UserProfileRepository._
import scalaj.http.Token
import utils.TransactionSyntax

import scala.collection.Seq

class UserProfileService(
    verifyCredentials: Token => IO[JsValue],
    override val doobieTransactor: DoobieTransactor
) extends TransactionSyntax {

  val AIO = AsyncConnectionIO

  private val errorHandler: Seq[(JsPath, Seq[JsonValidationError])] => ConnectionIO[UserProfile] = es =>
    AIO.raiseError(new IllegalArgumentException(s"Failed to parse the returned user profile! ${es.mkString("[", ",", "]")}"))

  def createOrRetrieveUser(accessToken: Token): IO[UserProfile] = {
    val createProfile: JsValue => ConnectionIO[UserProfile] = profileJson =>
      profileJson.validate[UserProfile](twitterReads(accessToken)).fold(errorHandler, AIO.pure).flatTap(createUserProfile)

    transact(userProfileByAccessToken(accessToken).getOrElseF(AIO.liftIO(verifyCredentials(accessToken)) >>= createProfile))
  }

}
