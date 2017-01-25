package utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.{DateTimeFormatter, ResolverStyle}
import java.util.Date

import models.DateComponents

import scala.util.Try

object DateUtils {

  //uuuu for year as we're using STRICT ResolverStyle
  val formatter = DateTimeFormatter.ofPattern("uuuu-M-d").withResolverStyle(ResolverStyle.STRICT)

  def validateParts(parts: DateComponents)(implicit dateProvider: DateProvider): Boolean = {
    Try {
      val localDate = LocalDate.parse(parts.toString, formatter)

    }.isSuccess
  }

  type DateProvider = () => java.util.Date

  implicit def today: DateProvider = () => new java.util.Date()

  implicit class PatternDateFormatter(val d: Date) {
    def toFormat(formatString: String): String = new SimpleDateFormat(formatString).format(d)
  }

}
