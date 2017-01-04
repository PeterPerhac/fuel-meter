package controllers.acme

import javax.inject.Inject

import controllers.FuelMeterController
import models.forms.AcmeForm.form
import play.api.i18n.MessagesApi
import play.api.mvc.Action

class AcmeController @Inject()(val messagesApi: MessagesApi) extends FuelMeterController {

  def addProductForm() = Action {
    Ok(views.html.acme.addProductForm(form))
  }

  def saveProduct() = Action { implicit request =>
    form.bindFromRequest().fold(
      invalidForm => BadRequest(views.html.acme.addProductForm(invalidForm)),
      _ => Ok("Happy!")
    )
  }
}
