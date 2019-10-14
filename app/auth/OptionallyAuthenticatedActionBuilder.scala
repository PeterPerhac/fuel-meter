package auth

import models.UserId
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class OptionallyAuthenticatedActionBuilder(
      defaultParser: BodyParser[AnyContent]
)(implicit val executionContext: ExecutionContext)
    extends ActionBuilder[OptionallyAuthenticatedRequest[*, UserId], AnyContent] {

  lazy val parser: BodyParser[AnyContent] = defaultParser

  override def invokeBlock[A](
        request: Request[A],
        block: (OptionallyAuthenticatedRequest[A, UserId]) => Future[Result]
  ): Future[Result] =
    block(new OptionallyAuthenticatedRequest(request.session.data.get("user_id").map(UserId.apply), request))

}
