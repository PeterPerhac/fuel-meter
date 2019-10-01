package repository

import doobie.free.connection.ConnectionIO
import doobie.implicits._
import doobie.util.fragment.Fragment
import models.UserProfile
import scalaj.http.Token

object UserProfileRepository {

  private val userProfileQueryFragment: Fragment =
    fr"""SELECT
        |id,
        |name,
        |display_name,
        |location,
        |description,
        |profile_image_url,
        |profile_banner_url,
        |followers_count,
        |following_count,
        |created_at,
        |access_token
        |FROM user_profile """.stripMargin

  def userProfileByAccessToken(accessToken: Token): ConnectionIO[Option[UserProfile]] =
    (userProfileQueryFragment ++ fr"""WHERE access_token=${accessToken.key}""").query[UserProfile].option

  def getUserProfile(userId: String): ConnectionIO[UserProfile] =
    (userProfileQueryFragment ++ fr"""WHERE id=$userId""").query[UserProfile].unique

  def createUserProfile(profile: UserProfile): ConnectionIO[Int] =
    sql"""INSERT INTO user_profile(
         |id,
         |name,
         |display_name,
         |location,
         |description,
         |profile_image_url,
         |profile_banner_url,
         |followers_count,
         |following_count,
         |created_at,
         |access_token
         |) VALUES (
         |${profile.id},
         |${profile.name},
         |${profile.displayName},
         |${profile.location},
         |${profile.description},
         |${profile.profileImageUrl},
         |${profile.profileBannerUrl},
         |${profile.followersCount},
         |${profile.followingCount},
         |${profile.createdAt},
         |${profile.accessToken}
         |)""".stripMargin.update.run

}
