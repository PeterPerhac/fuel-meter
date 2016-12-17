package controllers.validation

import play.api.data.validation.{Constraint, Invalid, Valid, ValidationError}

/**
  * Created by peterperhac on 16/12/2016.
  */
object CustomValidationSupport {

  def doubleInRange[Double](minValue: Double, maxValue: Double)(implicit ordering: scala.math.Ordering[Double]): Constraint[Double] =
    Constraint[Double] { (d: Double) =>
      assert(ordering.compare(minValue, maxValue) == -1, "min bound must be less than max bound")
      (ordering.compare(d, minValue).signum, ordering.compare(d, maxValue).signum) match {
        case (1, -1) | (0, _) | (_, 0) => Valid
        case (_, 1) => Invalid(ValidationError("error.double.range.max", maxValue))
        case (-1, _) => Invalid(ValidationError("error.double.range.min", minValue))
      }
    }

}
