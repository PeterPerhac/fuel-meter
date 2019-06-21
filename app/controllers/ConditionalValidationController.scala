package controllers

import javax.inject.Inject
import models.forms.{FormWithRadioButtons, FormWithRadioButtonsModel}
import play.api.Configuration
import play.api.data.Form
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import play.twirl.api.Html

class ConditionalValidationController @Inject()(
    configuration: Configuration,
    controllerComponents: ControllerComponents
) extends FuelMeterController(configuration, controllerComponents) {

  val form: Form[FormWithRadioButtonsModel] = FormWithRadioButtons.form

  val show: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.captureRadio("foo", form))
  }

  val save: Action[AnyContent] = Action { implicit request =>
    form
      .bindFromRequest()
      .fold(
        invalidForm => BadRequest(views.html.captureRadio("radio-buttons", invalidForm)),
        validForm => Ok(views.html.main(Some("Thanks!"))(Html(s"""<h1>Well done</h1><p>${validForm.toString}</p>""")))
      )
  }

}
