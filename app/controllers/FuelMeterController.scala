package controllers

import play.api.Configuration
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, ControllerComponents}

abstract class FuelMeterController(
    protected final val config: Configuration,
    controllerComponents: ControllerComponents
) extends AbstractController(controllerComponents) with I18nSupport
