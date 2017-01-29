package controllers

import javax.inject.Inject

import models.forms.DateForm
import play.api.mvc._
import play.twirl.api.Html
import utils.DateUtils

class DateController @Inject()(ds: CommonDependencies) extends FuelMeterController(ds) {

  val dateForm = DateForm.form(DateUtils.today)

  def show = Action(Ok(views.html.captureDate("foo", dateForm)))

  def saveDate = Action { implicit request =>
    dateForm.bindFromRequest() fold(
      invalidForm => BadRequest(views.html.captureDate("foo", invalidForm)),
      validForm => Ok(views.html.main(Some("Thanks!"))(Html(s"""<h1>Well done</h1><p>You submitted ${validForm.toString}</p>""")))
    )
  }

}
