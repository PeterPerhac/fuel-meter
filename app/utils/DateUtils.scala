package utils

import java.text.SimpleDateFormat
import java.time.{Instant, LocalDate, ZoneId}
import java.util.Date

object DateUtils {

  type DateProvider = () => java.util.Date

  def daysFromToday(days: Int)(implicit dp: DateProvider): LocalDate =
    dp().toLocalDate.plusDays(days)

  implicit def today: DateProvider = () => new java.util.Date()

  implicit class DateOps(val d: Date) {

    def toFormat(formatString: String): String =
      new SimpleDateFormat(formatString).format(d)

    def toLocalDate =
      Instant.ofEpochMilli(d.getTime).atZone(ZoneId.systemDefault()).toLocalDate
  }

  implicit object LocalDateOrdering extends Ordering[LocalDate] {
    override def compare(x: LocalDate, y: LocalDate) = x.compareTo(y)
  }

}
