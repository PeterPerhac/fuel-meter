package controllers.infra

import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

class SignOutController(
    override val controllerComponents: ControllerComponents
) extends BaseController {
  val signOut: Action[AnyContent] = Action(Ok.withNewSession)
}
