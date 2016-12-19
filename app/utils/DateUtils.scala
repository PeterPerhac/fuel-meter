package utils

import java.text.SimpleDateFormat
import java.util.Date

object DateUtils {

  type DateProvider = () => java.util.Date

  implicit def today: DateProvider = () => new java.util.Date()

  implicit class PatternDateFormatter(val d: Date) {
    def toFormat(formatString: String): String = new SimpleDateFormat(formatString).format(d)
  }

}
