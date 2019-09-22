package models

import java.time.LocalDate
import java.time.format.{DateTimeFormatter, ResolverStyle}

import scala.util.Try

case class DateComponents(day: Int, month: Int, year: Int)

object DateComponents {
  //uuuu for year as we're using STRICT ResolverStyle
  val formatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("uuuu-M-d").withResolverStyle(ResolverStyle.STRICT)

  def toLocalDate(parts: DateComponents): Try[LocalDate] = Try {
    LocalDate.parse(s"${parts.year}-${parts.month}-${parts.day}", formatter)
  }

}

case class DateModel(date: DateComponents) {
  override def toString: String =
    DateComponents.toLocalDate(date).fold(_ => "", _.format(DateComponents.formatter))
}
