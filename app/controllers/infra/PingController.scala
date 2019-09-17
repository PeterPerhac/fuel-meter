package controllers.infra

import controllers.FuelMeterController
import javax.inject.Inject
import play.api.Configuration
import play.api.mvc.{Action, AnyContent, ControllerComponents}

class PingController @Inject()(
    configuration: Configuration,
    controllerComponents: ControllerComponents
) extends FuelMeterController(configuration, controllerComponents) {

  val ping: Action[AnyContent] = Action(Ok)

}
