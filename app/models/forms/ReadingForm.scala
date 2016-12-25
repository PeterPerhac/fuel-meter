package models.forms

import models.Reading
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats.doubleFormat
import utils.DateUtils
import utils.ValidationUtils._

/**
  * Created by peterperhac on 18/12/2016.
  */
object ReadingForm {

  val form: Form[Reading] = Form(
    mapping(
      "reg" -> nonEmptyText(minLength = 2, maxLength = 8),
      "date" -> dateStringMapping("yyyy/MM/dd", DateUtils.today),
      "mi" -> of[Double].verifying(inRange(0.0, 1000.00)),
      "total" -> number(min = 0, max = 500000),
      "litres" -> of[Double].verifying(inRange(0.0, 100.00)),
      "cost" -> of[Double].verifying(inRange(0.0, 500.00))
    )(Reading.apply)(Reading.unapply)
  )

}
