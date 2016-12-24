package utils

import play.api.data.validation.{Constraint, Invalid, Valid, ValidationError}

object ValidationUtils {

  def inRange[T](minValue: T, maxValue: T)(implicit ordering: scala.math.Ordering[T]): Constraint[T] =
    Constraint[T] { (t: T) =>
      assert(ordering.compare(minValue, maxValue) < 0, "min bound must be less than max bound")
      (ordering.compare(t, minValue).signum, ordering.compare(t, maxValue).signum) match {
        case (1, -1) | (0, _) | (_, 0) => Valid
        case (_, 1) => Invalid(ValidationError("error.range.above", maxValue))
        case (-1, _) => Invalid(ValidationError("error.range.below", minValue))
      }
    }

  def optionallyMatchingPattern(regex: String): Constraint[String] =
    Constraint[String] { s: String =>
      Option(s) match {
        case None | Some("") => Valid
        case _ if s.matches(regex) => Valid
        case _ => Invalid(ValidationError("error.string.pattern", s))
      }
    }

}
