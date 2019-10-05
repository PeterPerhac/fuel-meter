package models

class User(val id: String) extends AnyVal
object User {
  def apply(uid: String) = new User(uid)
}
