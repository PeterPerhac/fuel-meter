package services

import cats.data.OptionT
import cats.effect.IO
import cats.implicits._
import connectors.TwitterOAuthConnector
import doobie.implicits._
import models.UserProfile
import models.UserProfile.twitterReads
import play.api.libs.json.{JsPath, JsValue, JsonValidationError}
import repository.DoobieTransactor
import repository.UserProfileRepository._
import scalaj.http.Token

import scala.collection.Seq

class UserProfileService(twitterOAuthConnector: TwitterOAuthConnector, doobieTransactor: DoobieTransactor) {

  private val errorHandler: Seq[(JsPath, Seq[JsonValidationError])] => IO[UserProfile] = es =>
    IO.raiseError(new IllegalArgumentException(s"Failed to parse the returned user profile! ${es.mkString("[", ",", "]")}"))

  def createOrRetrieveUser(accessToken: Token): IO[UserProfile] = {
    val createProfile: JsValue => IO[UserProfile] = profileJson =>
      profileJson
        .validate[UserProfile](twitterReads(accessToken))
        .fold(errorHandler, IO.pure)
        .flatTap(profile => createUserProfile(profile).transact(doobieTransactor.tx))

    OptionT(userProfileByAccessToken(accessToken).transact(doobieTransactor.tx))
      .getOrElseF(twitterOAuthConnector.verifyCredentials(accessToken) >>= createProfile)

  }

}
