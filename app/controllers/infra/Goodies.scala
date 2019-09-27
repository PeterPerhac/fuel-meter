package controllers.infra

import play.api.Configuration
import play.api.mvc.ControllerComponents
import repository.DoobieTransactor

case class Goodies(
    doobieTransactor: DoobieTransactor,
    configuration: Configuration,
    controllerComponents: ControllerComponents
)
