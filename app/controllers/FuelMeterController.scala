package controllers

import cats.data.OptionT
import doobie.free.connection.ConnectionIO
import models.{User, UserId}
import play.api.i18n.I18nSupport
import play.api.mvc._
import repository.DoobieTransactor
import services.UserProfileService
import uk.gov.hmrc.uritemplate.syntax.UriTemplateSyntax
import utils.{JsonSyntax, TransactionSyntax}

import scala.concurrent.Future.successful
import scala.concurrent.{ExecutionContext, Future}

abstract class FuelMeterController(userProfileService: UserProfileService)
    extends BaseController with I18nSupport with JsonSyntax with UriTemplateSyntax with TransactionSyntax {

  override def doobieTransactor: DoobieTransactor

  implicit val executionContext: ExecutionContext = controllerComponents.executionContext

  def transactToFuture[T]: ConnectionIO[T] => Future[T] =
    transact andThen (_.unsafeToFuture())

  class UserIdRequest[A](val userId: Option[UserId], request: Request[A]) extends WrappedRequest[A](request)

  class UserAction extends ActionBuilder[UserIdRequest, AnyContent] with ActionTransformer[Request, UserIdRequest] {
    override def parser: BodyParser[AnyContent] = parse.anyContent

    override protected def executionContext: ExecutionContext = controllerComponents.executionContext

    def transform[A](request: Request[A]): Future[UserIdRequest[A]] =
      successful(new UserIdRequest(request.session.get("user_id").map(UserId.apply), request))
  }

  val userAction = new UserAction()
  val optionalUserActionRefiner: ActionRefiner[UserIdRequest, OptionalUserRequest] =
    new ActionRefiner[UserIdRequest, OptionalUserRequest] {
      override def executionContext: ExecutionContext = controllerComponents.executionContext

      override protected def refine[A](input: UserIdRequest[A]): Future[Either[Result, OptionalUserRequest[A]]] =
        transactToFuture(
          OptionT.fromOption[ConnectionIO](input.userId).flatMap(implicit uid => userProfileService.getUser).value
        ).map[Either[Result, OptionalUserRequest[A]]](optUser => Right(new OptionalUserRequest(optUser, input)))(
          executionContext
        )
    }
  val userActionRefiner: ActionRefiner[OptionalUserRequest, UserRequest] =
    new ActionRefiner[OptionalUserRequest, UserRequest] {
      override def executionContext: ExecutionContext = controllerComponents.executionContext

      override protected def refine[A](request: OptionalUserRequest[A]): Future[Either[Result, UserRequest[A]]] =
        successful {
          request.user.fold[Either[Result, UserRequest[A]]](Left(Redirect(routes.ReadingsController.index())))(
            u => Right(new UserRequest(u, request))
          )
        }
    }

  class OptionalUserRequest[A](val user: Option[User], request: UserIdRequest[A]) extends WrappedRequest[A](request)
  class UserRequest[A](val user: User, request: OptionalUserRequest[A]) extends WrappedRequest[A](request)

  implicit def userExtractor(implicit authenticatedRequest: UserRequest[_]): User =
    authenticatedRequest.user

  implicit def userIdExtractor(implicit authenticatedRequest: UserRequest[_]): UserId =
    authenticatedRequest.user.userId

  protected val runCIO: AsyncActionBuilder.type = AsyncActionBuilder

  object AsyncActionBuilder {

    def apply(body: Request[AnyContent] => ConnectionIO[Result]): Action[AnyContent] =
      Action.async(body andThen transactToFuture)

    def authenticated(body: UserRequest[AnyContent] => ConnectionIO[Result]): Action[AnyContent] =
      userAction.andThen(optionalUserActionRefiner).andThen(userActionRefiner).async(body andThen transactToFuture)

    def optionallyAuthenticated(body: OptionalUserRequest[AnyContent] => ConnectionIO[Result]): Action[AnyContent] =
      userAction.andThen(optionalUserActionRefiner).async(body andThen transactToFuture)

  }
}
