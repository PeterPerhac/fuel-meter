package controllers

import org.apache.commons.codec.binary.Base64.decodeBase64
import play.api.Configuration
import play.api.i18n.I18nSupport
import play.api.libs.json.Reads
import play.api.libs.ws.WSResponse
import play.api.mvc.{AbstractController, Action, ControllerComponents}

import scala.PartialFunction.condOpt
import scala.concurrent.Future.successful

abstract class FuelMeterController(
    protected final val config: Configuration,
    controllerComponents: ControllerComponents
) extends AbstractController(controllerComponents)
    with I18nSupport {

  def BasicSecured[A](action: Action[A]): Action[A] =
    Action.async(action.parser) { implicit request =>
      val submittedCredentials: Option[List[String]] = for {
        authHeader <- request.headers.get("Authorization")
        parts      <- authHeader.split(' ').drop(1).headOption
      } yield new String(decodeBase64(parts.getBytes)).split(":").toList

      submittedCredentials
        .collect {
          case u :: p :: Nil if u == "fuel" && p == "meter" => action(request)
        }
        .getOrElse {
          successful(Unauthorized.withHeaders("WWW-Authenticate" -> """Basic realm="Secured Area""""))
        }
    }

  protected def toClassOf[T: Reads](res: WSResponse): Option[T] = condOpt(res.status) {
    case n if n < 300 => res.json.as[T]
  }

}
