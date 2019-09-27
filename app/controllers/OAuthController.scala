package controllers

import connectors.TwitterOAuthConnector
import controllers.infra.Goodies
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

class OAuthController(goodies: Goodies)(
    twitterOAuthConnector: TwitterOAuthConnector
) extends FuelMeterController(goodies) {

  // private def readings(reg: String): IO[List[Reading]] =
  //   repository.findAll(reg).transact(tx)

  val signIn: Action[AnyContent] = runAsync { implicit request =>
    twitterOAuthConnector.requestToken().map(token => Ok(token.asJson))
  }

  val callback: Action[AnyContent] = runAsync { implicit request =>
    twitterOAuthConnector.accessToken().map(token => Ok(token.asJson))
  }

}
