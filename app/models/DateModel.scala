package models


case class DateComponents(day: Int, month: Int, year: Int) {
  override def toString = s"$year-$month-$day"
}

case class DateModel(date: DateComponents)
