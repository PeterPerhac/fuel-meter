package models.forms

import models.{DateComponents, DateModel}
import play.api.data.Form
import play.api.data.Forms._
import utils.DateUtils
import utils.DateUtils.DateProvider

object DateForm {

  def form(implicit dp: DateProvider): Form[DateModel] = Form(
    mapping(
      "date" -> mapping(
        "day" -> number(min = 1, max = 31),
        "month" -> number(min = 1, max = 12),
        "year" -> number(min = 2000, max = 2200)
      )(DateComponents.apply)(DateComponents.unapply).verifying("date.invalid", DateUtils.validateParts _)
    )(DateModel.apply)(DateModel.unapply)
  )

}
