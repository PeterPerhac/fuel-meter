package models.forms

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import models.Reading
import play.api.data.Forms._
import play.api.data.format.Formats.doubleFormat
import play.api.data.{Form, Mapping}
import utils.DateUtils._
import utils.ValidationUtils._

import scala.util.Try

object ReadingForm {

  def dateStringMapping(datePattern: String, defaultDateProvider: DateProvider): Mapping[String] = {
    val dateFormatter = DateTimeFormatter.ofPattern(datePattern)
    optional(localDate(datePattern)).transform[String](
      old => old.getOrElse(defaultDateProvider()).format(dateFormatter),
      s => Try(LocalDate.parse(s, dateFormatter)).toOption
    )
  }

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

  def apply(defaultDateProvider: DateProvider): Form[Reading] =
    form(defaultDateProvider)

}
