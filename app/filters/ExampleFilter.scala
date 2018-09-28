package filters

import javax.inject._

import akka.stream.Materializer
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

/**
  * This filter is added to the application's list of filters by the [[Filters]] class.
  *
  * @param mat  This object is needed to handle streaming of requests and responses.
  * @param exec This class is needed to execute code asynchronously. It is used below by the `map` method.
  */
@Singleton
class ExampleFilter @Inject()(implicit override val mat: Materializer,
                              exec: ExecutionContext)
    extends Filter {

  override def apply(nextFilter: RequestHeader => Future[Result])(
      requestHeader: RequestHeader): Future[Result] =
    // Run the next filter in the chain. This will call other filters and eventually call the action.
    nextFilter(requestHeader).map { result =>
      result.withHeaders("X-ExampleFilter" -> "foo")
    }

}
