package controllers.infra

import play.api.mvc._

class PingController(override val controllerComponents: ControllerComponents) extends BaseController {
  val ping: Action[AnyContent] = Action(Ok)
}
