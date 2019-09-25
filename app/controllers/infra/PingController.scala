package controllers.infra

import controllers.FuelMeterController
import javax.inject.{Inject, Singleton}
import play.api.Configuration
import play.api.mvc.{Action, AnyContent, ControllerComponents}

@Singleton
class PingController @Inject()(
    configuration: Configuration,
    controllerComponents: ControllerComponents
) extends FuelMeterController(configuration, controllerComponents) {

  val ping: Action[AnyContent] = Action(Ok)

}
