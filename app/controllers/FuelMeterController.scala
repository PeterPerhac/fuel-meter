package controllers

import cats.effect.IO
import controllers.infra.Goodies
import doobie.util.transactor.Transactor.Aux
import javax.sql.DataSource
import play.api.Configuration
import play.api.i18n.I18nSupport
import play.api.mvc._
import utils.JsonSyntax

abstract class FuelMeterController(goodies: Goodies)
    extends AbstractController(goodies.controllerComponents)
    with I18nSupport
    with JsonSyntax {

  protected val config: Configuration = goodies.configuration
  protected val tx: Aux[IO, DataSource] = goodies.doobieTransactor.tx

  protected val runAsync: (Request[AnyContent] => IO[Result]) => Action[AnyContent] =
    f => Action.async(r => f(r).unsafeToFuture())

}
