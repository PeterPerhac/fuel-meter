package controllers


import play.api.Play.current
import play.api.db._
import play.api.mvc._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Ahoj šéfe"))
  }

  def db = Action {
    var out = "FINE"
    try {
    } finally {
    }
    Ok(out)
  }
}
