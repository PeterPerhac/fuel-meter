package utils

import org.joda.time.format.DateTimeFormatter
import play.api.data.FormError
import play.api.data.format.Formatter
import play.api.data.validation.{Constraint, Invalid, Valid, ValidationError}

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

  /**
    * copy-pasted from Format.scala as, unfortunately, this def is private and I could not just import it
    */
  private def parsing[T](parse: String => T, errMsg: String, errArgs: Seq[Any])(key: String, data: Map[String, String]): Either[Seq[FormError], T] = {
    import play.api.data.format.Formats.stringFormat
    stringFormat.bind(key, data).right.flatMap { s => scala.util.control.Exception.allCatch[T].either(parse(s)).left.map(e => Seq(FormError(key, errMsg, errArgs))) }
  }

  /**
    * A special binder that will bind a missing or empty string and/or a String representation of a date matching the provided pattern
    */
  def dateString(pattern: String, allowBlank: Boolean = false) = new Formatter[String] {

    val formatter: DateTimeFormatter = org.joda.time.format.DateTimeFormat.forPattern(pattern)

    override val format = Some(("format.date", Seq(pattern)))

    def bind(key: String, data: Map[String, String]) = parsing(s => Option(s) match {
      case Some("") | None if allowBlank => ""
      case Some(text) => formatter.print(org.joda.time.LocalDate.parse(text, formatter))
      case _ => throw new IllegalArgumentException("blank or missing date value")
    }, "error.date", Nil)(key, data)

    def unbind(key: String, value: String) = Map(key -> value)
  }

}
