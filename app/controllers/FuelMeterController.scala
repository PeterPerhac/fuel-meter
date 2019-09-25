package controllers

import cats.effect.IO
import play.api.Configuration
import play.api.i18n.I18nSupport
import play.api.mvc._
import utils.JsonSyntax

abstract class FuelMeterController(
    protected final val config: Configuration,
    controllerComponents: ControllerComponents
) extends AbstractController(controllerComponents)
    with I18nSupport
    with JsonSyntax {

  protected val runAsync: (Request[AnyContent] => IO[Result]) => Action[AnyContent] =
    f => Action.async(r => f(r).unsafeToFuture())

}
