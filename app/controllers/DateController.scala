package controllers

import javax.inject.Inject

import models.forms.DateForm
import play.api.mvc._
import play.twirl.api.Html
import utils.DateUtils

import scala.concurrent.Future

class DateController @Inject()(ds: CommonDependencies) extends FuelMeterController(ds) {

  val dateForm = DateForm.form(DateUtils.today)

  def captureForm = Action(Ok(views.html.captureDate("foo", dateForm)))

  def saveDate = Action.async { implicit request =>
    dateForm.bindFromRequest() fold(
      invalidForm => Future.successful(BadRequest(views.html.captureDate("foo", invalidForm))),
      form => Future.successful(Ok(views.html.main(Some("Thanks!"))(Html("""<h1>Well done</h1>"""))))
    )
  }

}
