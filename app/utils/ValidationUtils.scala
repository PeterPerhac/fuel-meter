package utils

import play.api.data.format.Formatter
import play.api.data.validation.{Constraint, Invalid, Valid, ValidationError}
import play.api.data.{FieldMapping, FormError, Forms}

object ValidationUtils {

  implicit val mandatoryBooleanFormatter: Formatter[Boolean] = new Formatter[Boolean] {

    def bind(key: String, data: Map[String, String]): Either[Seq[FormError], Boolean] =
      Right(data.getOrElse(key, "")).right.flatMap {
        case "true"  => Right(true)
        case "false" => Right(false)
        case _       => Left(Seq(FormError(key, s"$key.error.boolean", Nil)))
      }

    def unbind(key: String, value: Boolean): Map[String, String] = Map(key -> value.toString)
  }

  val mandatoryBoolean: FieldMapping[Boolean] = Forms.of[Boolean]
  val notBlank: String => Boolean = _.trim.nonEmpty

  def unconstrained[T]: Constraint[T] = Constraint[T]((_: T) => Valid)

  def inRange[T](minValue: T, maxValue: T, errorCode: String = "")(
        implicit ordering: scala.math.Ordering[T]
  ): Constraint[T] =
    Constraint[T] { t: T =>
      assert(ordering.compare(minValue, maxValue) < 0, "min bound must be less than max bound")
      (ordering.compare(t, minValue).signum, ordering.compare(t, maxValue).signum) match {
        case (1, -1) | (0, _) | (_, 0) => Valid
        case (_, n) if n > 0 =>
          Invalid(ValidationError(s"error$errorCode.range.above", maxValue))
        case (-1, n) if n < 0 =>
          Invalid(ValidationError(s"error$errorCode.range.below", minValue))
      }
    }

}
