package models

case class User(userId: UserId, userProfile: UserProfile, vehiclesOwned: List[VehicleRecordSummary]) {
  def owns(reg: String): Boolean = vehiclesOwned.exists(_.reg.equalsIgnoreCase(reg))
}

object User {

  def apply(userProfile: UserProfile, vehiclesOwned: List[VehicleRecordSummary]): User =
    User(new UserId(userProfile.id), userProfile, vehiclesOwned)

}
