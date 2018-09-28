package models.forms

import models.Reading
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import play.api.data.Forms._
import play.api.data.format.Formats.doubleFormat
import play.api.data.{Form, Mapping}
import utils.DateUtils._
import utils.ValidationUtils._

object ReadingForm {

  def dateStringMapping(datePattern: String,
                        defaultDateProvider: DateProvider): Mapping[String] =
    optional(jodaLocalDate(datePattern)) transform (
      old =>
        old map (_.toString(datePattern)) getOrElse defaultDateProvider()
          .toFormat(datePattern),
      s => Some(LocalDate.parse(s, DateTimeFormat.forPattern(datePattern)))
    )

  def form(defaultDateProvider: DateProvider): Form[Reading] = Form(
    mapping(
      "reg"    -> nonEmptyText(minLength = 2, maxLength = 8),
      "date"   -> dateStringMapping("yyyy/MM/dd", defaultDateProvider),
      "mi"     -> of[Double].verifying(inRange(0.0, 1000.00)),
      "total"  -> number(min = 0, max = 500000),
      "litres" -> of[Double].verifying(inRange(0.0, 100.00)),
      "cost"   -> of[Double].verifying(inRange(0.0, 500.00))
    )(Reading.apply)(Reading.unapply)
  )

  def apply(defaultDateProvider: DateProvider) = form(defaultDateProvider)

}
