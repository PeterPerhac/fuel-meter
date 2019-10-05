package models

import java.time.format.DateTimeFormatter
import java.time.{Instant, OffsetDateTime, ZoneOffset}

import play.api.libs.functional.syntax._
import play.api.libs.json._
import scalaj.http.Token

case class UserProfile(
    id: String,
    name: String,
    displayName: String,
    location: Option[String],
    description: Option[String],
    profileImageUrl: Option[String],
    profileBannerUrl: Option[String],
    followersCount: Int,
    followingCount: Int,
    createdAt: Instant,
    accessToken: String
) {
  val createdAtFormatted: String =
    DateTimeFormatter.ofPattern("d MMM yyyy 'at' h:mm a").format(OffsetDateTime.ofInstant(createdAt, ZoneOffset.UTC))
}

object UserProfile {

  def twitterReads(accessToken: Token): Reads[UserProfile] =
    ((__ \ "id_str").read[String] and
      (__ \ "name").read[String] and
      (__ \ "screen_name").read[String] and
      (__ \ "location").readNullable[String] and
      (__ \ "description").readNullable[String] and
      (__ \ "profile_image_url").readNullable[String] and
      (__ \ "profile_banner_url").readNullable[String] and
      (__ \ "followers_count").read[Int] and
      (__ \ "friends_count").read[Int] and
      Reads.pure(Instant.now()) and
      Reads.pure(accessToken.key))(UserProfile.apply _)

}
