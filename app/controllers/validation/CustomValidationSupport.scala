package controllers.validation

import play.api.data.validation.{Constraint, Invalid, Valid, ValidationError}

/**
  * Created by peterperhac on 16/12/2016.
  */
object CustomValidationSupport {

  def inRange[T](minValue: T, maxValue: T)(implicit ordering: scala.math.Ordering[T]): Constraint[T] =
    Constraint[T] { (t: T) =>
      assert(ordering.compare(minValue, maxValue) < 0, "min bound must be less than max bound")
      (ordering.compare(t, minValue).signum, ordering.compare(t, maxValue).signum) match {
        case (1, -1) | (0, _) | (_, 0) => Valid
        case (_, 1) => Invalid(ValidationError("error.range.above", maxValue))
        case (-1, _) => Invalid(ValidationError("error.range.below", minValue))
      }
    }

  def optionallyMatchingPattern(pattern: String): Constraint[String] =
    Constraint[String] { s:String =>
      val p = pattern.r.pattern
      Option(s) match {
        case None | Some("") => Valid
        case Some(str) if p.matcher(str).matches() => Valid
        case _ => Invalid(ValidationError("error.string.pattern", s))
      }
    }

}
