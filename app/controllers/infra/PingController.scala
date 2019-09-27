package controllers.infra

import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

class PingController(
    override val controllerComponents: ControllerComponents
) extends BaseController {
  val ping: Action[AnyContent] = Action(Ok)
}
