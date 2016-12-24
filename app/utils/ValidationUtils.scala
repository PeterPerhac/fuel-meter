package utils


import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat.forPattern
import play.api.data.Forms._
import play.api.data.Mapping
import play.api.data.validation.{Constraint, Invalid, Valid, ValidationError}
import utils.DateUtils._

/**
  * Created by peterperhac on 16/12/2016.
  */
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


  def dateString(datePattern: String, defaultDateProvider: DateProvider): Mapping[String] = {
    optional(jodaLocalDate(datePattern)) transform(
      old => old map (_.toString(datePattern)) getOrElse defaultDateProvider().toFormat(datePattern),
      s => Some(LocalDate.parse(s, forPattern(datePattern)))
    )
  }

}
