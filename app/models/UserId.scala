package models

class UserId(val id: String) extends AnyVal
object UserId {
  def apply(uid: String) = new UserId(uid)
}
