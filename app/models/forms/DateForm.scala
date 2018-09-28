package models.forms

import models.{DateComponents, DateModel}
import play.api.data.Form
import play.api.data.Forms._
import utils.DateUtils.{DateProvider, LocalDateOrdering, daysFromToday}
import utils.ValidationUtils._

object DateForm {

  def form(implicit dp: DateProvider): Form[DateModel] = Form(
    mapping(
      "date" -> mapping(
        "day"   -> number(min = 1, max = 31),
        "month" -> number(min = 1, max = 12),
        "year"  -> number(min = 2000, max = 2200)
      )(DateComponents.apply)(DateComponents.unapply).verifying {
        validDate(inRange(daysFromToday(2), daysFromToday(60), errorCode = ".date"))
      }
    )(DateModel.apply)(DateModel.unapply)
  )

}
