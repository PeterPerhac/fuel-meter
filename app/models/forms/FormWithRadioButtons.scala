package models.forms

import play.api.data.Form
import play.api.data.Forms._
import uk.gov.voa.play.form.ConditionalMappings._
import utils.ValidationUtils._

object FormWithRadioButtons {

  def form: Form[FormWithRadioButtonsModel] = Form(
    mapping(
      "yesNo" -> mandatoryBoolean,
      "textField" -> mandatoryIf(isTrue("yesNo"),
                                 text.verifying("custom.required.message",
                                                notBlank))
    )(FormWithRadioButtonsModel.apply)(FormWithRadioButtonsModel.unapply)
  )

}

case class FormWithRadioButtonsModel(yesNo: Boolean, text: Option[String]) {
  override def toString =
    s"""You ticked ${if (yesNo) "yes" else "no"} and answered: "${text
      .getOrElse("")}""""
}
