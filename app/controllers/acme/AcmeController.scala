package controllers.acme

import models.forms.AcmeForm.form
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.mvc.{Action, Controller}

class AcmeController extends Controller {

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
