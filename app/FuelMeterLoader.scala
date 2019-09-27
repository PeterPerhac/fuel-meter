import connectors.TwitterOAuthConnector
import play.api.ApplicationLoader.Context
import play.api._
import play.api.db.{DBComponents, HikariCPComponents}
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.routing.Router
import play.filters.HttpFiltersComponents
import repository.DoobieTransactor
import router.Routes

class FuelMeterLoader extends ApplicationLoader {
  def load(context: Context): Application = {
    LoggerConfigurator(context.environment.classLoader).foreach {
      _.configure(context.environment, context.initialConfiguration, Map.empty)
    }
    new MyComponents(context).application
  }
}
import _root_.controllers._

class MyComponents(context: Context)
    extends BuiltInComponentsFromContext(context)
    with HttpFiltersComponents
    with DBComponents
    with AssetsComponents
    with AhcWSComponents
    with HikariCPComponents {

  lazy val doobieTransactor = new DoobieTransactor(dbApi.database("fuelmeter"))

  lazy val twitterOAuthConnector = new TwitterOAuthConnector(wsClient, configuration)

  lazy val pingController = new infra.PingController(configuration, controllerComponents)
  lazy val readingsController = new ReadingsController(doobieTransactor, configuration, controllerComponents)
  lazy val deletesController = new DeletesController(doobieTransactor, configuration, controllerComponents)

  lazy val oAuthController = new OAuthController(twitterOAuthConnector, doobieTransactor, configuration, controllerComponents)

  lazy val router: Router = new Routes(
    httpErrorHandler,
    readingsController,
    pingController,
    oAuthController,
    deletesController,
    assets
  ).withPrefix(httpConfiguration.context)

}
