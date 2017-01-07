package controllers

import javax.inject.Inject

import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.ws.WSClient
import play.api.mvc.Controller


abstract class FuelMeterController(val commonDependencies: CommonDependencies) extends Controller with I18nSupport with WSSupport {

  protected lazy val ws = commonDependencies.ws
  protected lazy val conf = commonDependencies.conf
  implicit lazy val messagesApi = commonDependencies.messagesApi

}

final class CommonDependencies @Inject()(val ws: WSClient, val conf: play.api.Configuration, val messagesApi: MessagesApi)
