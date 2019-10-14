package auth

import play.api.mvc.{Request, WrappedRequest}

class OptionallyAuthenticatedRequest[+A, U](val user: Option[U], request: Request[A])
    extends WrappedRequest[A](request) {

  protected override def newWrapper[B](newRequest: Request[B]): OptionallyAuthenticatedRequest[B, U] =
    new OptionallyAuthenticatedRequest[B, U](user, newRequest)

}
