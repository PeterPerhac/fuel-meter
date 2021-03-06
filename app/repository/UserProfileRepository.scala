package repository

import cats.data.OptionT
import doobie.free.connection.ConnectionIO
import doobie.implicits._
import doobie.util.fragment.Fragment
import models.UserProfile
import scalaj.http.Token

class UserProfileRepository {

  private val userProfileQueryFragment: Fragment =
    sql"""SELECT
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

  def userProfileByAccessToken(accessToken: Token): OptionT[ConnectionIO, UserProfile] =
    OptionT((userProfileQueryFragment ++ fr"""WHERE access_token=${accessToken.key}""").query[UserProfile].option)

  def findUserProfile(userId: String): OptionT[ConnectionIO, UserProfile] =
    OptionT((userProfileQueryFragment ++ fr"""WHERE id=$userId""").query[UserProfile].option)

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
