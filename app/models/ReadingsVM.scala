package models

case class ReadingsVM(
      thisVehicle: VehicleRecordSummary,
      readings: Seq[Reading],
      otherVehicles: Seq[VehicleRecordSummary],
      optUser: Option[UserId],
      isOwner: Boolean
)
