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

import scala.concurrent.ExecutionContext

abstract class FuelMeterController(goodies: Goodies)
    extends AbstractController(goodies.controllerComponents)
    with I18nSupport
    with JsonSyntax
    with TransactionSyntax
    with UriTemplateSyntax {

  implicit val executionContext: ExecutionContext = controllerComponents.executionContext

  override def doobieTransactor: DoobieTransactor = goodies.doobieTransactor

  protected val config: Configuration = goodies.configuration

  protected val runAsync: AsyncActionBuilder.type = AsyncActionBuilder

  object AsyncActionBuilder {

    def apply(body: (Request[AnyContent] => IO[Result])): Action[AnyContent] =
      Action.async(r => body(r).unsafeToFuture())

    def authenticated(body: (AuthenticatedRequest[AnyContent, String]) => IO[Result]): Action[AnyContent] =
      new AuthenticatedBuilder[String](
        userinfo = _.session.data.get("user_id"),
        defaultParser = parse.anyContent,
        onUnauthorized = _ => Redirect(routes.ReadingsController.index())
      ).async(body andThen (_.unsafeToFuture()))

  }

}
