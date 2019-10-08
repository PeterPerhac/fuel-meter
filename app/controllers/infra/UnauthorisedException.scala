package controllers.infra

import scala.util.control.NoStackTrace

case object UnauthorisedException extends RuntimeException with NoStackTrace {
  override def toString: String = s"You are not authorised to perform the requested operation"
}
