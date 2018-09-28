package controllers

import javax.inject.Inject

import models.forms.FormWithRadioButtons
import play.api.mvc._
import play.twirl.api.Html

class ConditionalValidationController @Inject()(ds: CommonDependencies)
    extends FuelMeterController(ds) {

  val form = FormWithRadioButtons.form

  def show = Action(Ok(views.html.captureRadio("foo", form)))

  def save = Action { implicit request =>
    form.bindFromRequest() fold (
      invalidForm =>
        BadRequest(views.html.captureRadio("radio-buttons", invalidForm)),
      validForm =>
        Ok(
          views.html.main(Some("Thanks!"))(
            Html(s"""<h1>Well done</h1><p>${validForm.toString}</p>""")))
    )
  }

}
