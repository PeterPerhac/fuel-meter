package controllers

import auth.{OptionallyAuthenticatedActionBuilder, OptionallyAuthenticatedRequest}
import doobie.free.connection.ConnectionIO
import models.UserId
import play.api.i18n.I18nSupport
import play.api.mvc.Security.{AuthenticatedBuilder, AuthenticatedRequest}
import play.api.mvc._
import repository.DoobieTransactor
import uk.gov.hmrc.uritemplate.syntax.UriTemplateSyntax
import utils.{JsonSyntax, TransactionSyntax}

import scala.concurrent.{ExecutionContext, Future}

abstract class FuelMeterController
    extends BaseController with I18nSupport with JsonSyntax with UriTemplateSyntax with TransactionSyntax {

  override def doobieTransactor: DoobieTransactor

  implicit val executionContext: ExecutionContext = controllerComponents.executionContext

  implicit def userExtractor(implicit authenticatedRequest: AuthenticatedRequest[_, UserId]): UserId =
    authenticatedRequest.user

  protected val runCIO: AsyncActionBuilder.type = AsyncActionBuilder

  object AsyncActionBuilder {

    def transactToFuture[T]: ConnectionIO[T] => Future[T] =
      transact andThen (_.unsafeToFuture())

    def apply(body: Request[AnyContent] => ConnectionIO[Result]): Action[AnyContent] =
      Action.async(body andThen transactToFuture)

    def authenticated(body: AuthenticatedRequest[AnyContent, UserId] => ConnectionIO[Result]): Action[AnyContent] =
      new AuthenticatedBuilder[UserId](
        userinfo = _.session.data.get("user_id").map(UserId.apply),
        defaultParser = parse.anyContent,
        onUnauthorized = _ => Redirect(routes.ReadingsController.index())
      ).async(body andThen transactToFuture)

    def optionallyAuthenticated(
          body: OptionallyAuthenticatedRequest[AnyContent, UserId] => ConnectionIO[Result]
    ): Action[AnyContent] =
      new OptionallyAuthenticatedActionBuilder(parse.anyContent).async(body andThen transactToFuture)
  }

}
