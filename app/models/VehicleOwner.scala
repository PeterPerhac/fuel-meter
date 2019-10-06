package models

case class VehicleOwner(user: User, userProfile: UserProfile, vehiclesOwned: List[VehicleRecordSummary])

object VehicleOwner {
  
  private def apply(user: User, userProfile: UserProfile, vehiclesOwned: List[VehicleRecordSummary]): VehicleOwner =
    new VehicleOwner(user, userProfile, vehiclesOwned)

  def apply(userProfile: UserProfile, vehiclesOwned: List[VehicleRecordSummary]): VehicleOwner =
    VehicleOwner(new User(userProfile.id), userProfile, vehiclesOwned)

}
