package models

case class VehicleOwner(userProfile: UserProfile, vehiclesOwned: List[VehicleRecordSummary])
