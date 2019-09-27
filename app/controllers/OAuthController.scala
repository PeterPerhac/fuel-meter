package controllers

import connectors.TwitterOAuthConnector
import play.api.Configuration
import play.api.mvc._
import repository.DoobieTransactor

import scala.concurrent.ExecutionContext.Implicits.global

class OAuthController(
    twitterOAuthConnector: TwitterOAuthConnector,
    transactor: DoobieTransactor,
    configuration: Configuration,
    controllerComponents: ControllerComponents
) extends FuelMeterController(configuration, controllerComponents) {

  // private def readings(reg: String): IO[List[Reading]] =
  //   repository.findAll(reg).transact(tx)

  val signIn: Action[AnyContent] = runAsync { implicit request =>
    twitterOAuthConnector.requestToken().map(token => Ok(token.asJson))
  }

  val callback: Action[AnyContent] = runAsync { implicit request =>
    twitterOAuthConnector.accessToken().map(token => Ok(token.asJson))
  }

}
