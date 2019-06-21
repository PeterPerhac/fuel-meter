package utils

import java.time.LocalDate

object DateUtils {

  type DateProvider = () => java.time.LocalDate

  def daysFromToday(days: Int)(implicit dp: DateProvider): LocalDate =
    dp().plusDays(days)

  implicit def today: DateProvider = () => LocalDate.now()

  implicit object LocalDateOrdering extends Ordering[LocalDate] {
    override def compare(x: LocalDate, y: LocalDate): Int = x.compareTo(y)
  }

}
