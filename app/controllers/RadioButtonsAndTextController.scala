package controllers

import javax.inject.Inject

import models.forms.FormWithRadioButtons
import play.api.mvc._
import play.twirl.api.Html

import scala.concurrent.Future

class RadioButtonsAndTextController @Inject()(ds: CommonDependencies) extends FuelMeterController(ds) {

  val form = FormWithRadioButtons.form

  def captureForm = Action(Ok(views.html.captureRadio("foo", form)))

  def save = Action.async { implicit request =>
    form.bindFromRequest() fold(
      invalidForm =>

        Future.successful(BadRequest(views.html.captureRadio("radio-buttons", invalidForm))),
      form => Future.successful(Ok(views.html.main(Some("Thanks!"))(Html("""<h1>Well done</h1>"""))))
    )
  }

}
