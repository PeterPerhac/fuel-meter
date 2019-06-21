package controllers

import javax.inject.Inject
import models.DateModel
import models.forms.DateForm
import play.api.Configuration
import play.api.data.Form
import play.api.mvc._
import play.twirl.api.Html

class DateController @Inject()(configuration: Configuration, controllerComponents: ControllerComponents)
    extends FuelMeterController(configuration, controllerComponents) {

  val dateForm: Form[DateModel] = DateForm.form()

  val show: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.captureDate("foo", dateForm))
  }

  val saveDate: Action[AnyContent] = Action { implicit request =>
    dateForm.bindFromRequest() fold (
      invalidForm => BadRequest(views.html.captureDate("foo", invalidForm)),
      validForm => Ok(views.html.main(Some("Thanks!"))(Html(s"""<h1>Well done</h1><p>You submitted ${validForm.toString}</p>""")))
    )
  }

}
