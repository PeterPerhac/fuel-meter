package controllers

import play.api.libs.json.Reads
import play.api.libs.ws.WSResponse
import play.api.mvc.Controller

trait WSSupport {
  this: Controller =>

  private[controllers] def toClassOf[T: Reads](res: WSResponse): Option[T] = res.status match {
    case n if n < 300 => res.json.asOpt[T]
    case _ => None
  }

}
