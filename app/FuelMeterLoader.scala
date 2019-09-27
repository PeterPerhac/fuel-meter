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

  lazy val pingController = new infra.PingController(controllerComponents)
  lazy val twitterOAuthConnector = new TwitterOAuthConnector(wsClient, configuration)

  lazy val doobieTransactor = new DoobieTransactor(dbApi.database("fuelmeter"))

  lazy val goodies: infra.Goodies = infra.Goodies(doobieTransactor, configuration, controllerComponents)
  lazy val readingsController = new ReadingsController(goodies)
  lazy val deletesController = new DeletesController(goodies)
  lazy val oAuthController = new OAuthController(goodies)(twitterOAuthConnector)

  lazy val router: Router = new Routes(
    httpErrorHandler,
    readingsController,
    pingController,
    oAuthController,
    deletesController,
    assets
  ).withPrefix(httpConfiguration.context)

}
