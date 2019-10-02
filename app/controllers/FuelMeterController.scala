package controllers

import cats.effect.IO
import controllers.infra.Goodies
import play.api.Configuration
import play.api.i18n.I18nSupport
import play.api.mvc.Security.{AuthenticatedBuilder, AuthenticatedRequest}
import play.api.mvc._
import repository.DoobieTransactor
import uk.gov.hmrc.uritemplate.syntax.UriTemplateSyntax
import utils.{JsonSyntax, TransactionSyntax}

abstract class FuelMeterController(goodies: Goodies)
    extends AbstractController(goodies.controllerComponents)
    with I18nSupport
    with JsonSyntax
    with TransactionSyntax
    with UriTemplateSyntax {

  override def doobieTransactor: DoobieTransactor = goodies.doobieTransactor

  protected val config: Configuration = goodies.configuration

  protected val runAsync: AsyncActionBuilder.type = AsyncActionBuilder

  object AsyncActionBuilder {

    def apply(body: (Request[AnyContent] => IO[Result])): Action[AnyContent] =
      Action.async(r => body(r).unsafeToFuture())

    def authenticated(body: (AuthenticatedRequest[AnyContent, String]) => IO[Result]): Action[AnyContent] =
      new AuthenticatedBuilder[String](
        userinfo = r => r.session.data.get("user_id"),
        defaultParser = controllerComponents.parsers.anyContent,
        onUnauthorized = r => Redirect(routes.ReadingsController.index())
      )(controllerComponents.executionContext).async(r => body(r).unsafeToFuture())

  }

}
