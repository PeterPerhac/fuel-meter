package models.forms

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import models.Reading
import models.Reading.dateFormatWithSlashes
import play.api.data.Forms._
import play.api.data.format.Formats.doubleFormat
import play.api.data.{Form, Mapping}
import utils.ValidationUtils._

import scala.util.Try

object ReadingForm {

  private def dateStringMapping(datePattern: String, date: LocalDate): Mapping[String] = {
    val dateFormatter = DateTimeFormatter.ofPattern(datePattern)
    optional(localDate(datePattern)).transform[String](
      old => old.getOrElse(date).format(dateFormatter),
      s => Try(LocalDate.parse(s, dateFormatter)).toOption
    )
  }

  val readingForm: Form[Reading] = Form(
    mapping(
      "reg"     -> nonEmptyText(minLength = 2, maxLength = 8),
      "date"    -> dateStringMapping("yyyy-MM-dd", LocalDate.now).transform(LocalDate.parse, dateFormatWithSlashes.format),
      "miles"   -> of[Double].verifying(inRange(0.0, 1000.00)),
      "mileage" -> number(min = 0, max = 500000),
      "liters"  -> of[Double].verifying(inRange(0.0, 100.00)),
      "cost"    -> of[Double].verifying(inRange(0.0, 500.00))
    )(Reading.apply)(Reading.unapply)
  )

}
