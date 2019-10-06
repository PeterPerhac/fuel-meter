package auth

import models.User
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class OptionallyAuthenticatedActionBuilder(
    defaultParser: BodyParser[AnyContent],
)(implicit val executionContext: ExecutionContext)
    extends ActionBuilder[OptionallyAuthenticatedRequest[*, User], AnyContent] {

  lazy val parser: BodyParser[AnyContent] = defaultParser

  override def invokeBlock[A](request: Request[A], block: (OptionallyAuthenticatedRequest[A, User]) => Future[Result]): Future[Result] =
    block(new OptionallyAuthenticatedRequest(request.session.data.get("user_id").map(User.apply), request))

}
