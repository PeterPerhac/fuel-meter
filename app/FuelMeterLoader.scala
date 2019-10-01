import auth.twitter.TwitterOAuthConfig
import connectors.TwitterOAuthConnector
import play.api.ApplicationLoader.Context
import play.api._
import play.api.db.{DBComponents, HikariCPComponents}
import play.api.routing.Router
import play.filters.HttpFiltersComponents
import repository.DoobieTransactor
import router.Routes
import services.UserProfileService

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
    with HikariCPComponents {

  lazy val twitterOAuthConfig = TwitterOAuthConfig(configuration)

  lazy val pingController = new infra.PingController(controllerComponents)
  lazy val twitterOAuthConnector = new TwitterOAuthConnector(twitterOAuthConfig)

  lazy val doobieTransactor = new DoobieTransactor(dbApi.database("fuelmeter"))

  lazy val userProfileService: UserProfileService = new UserProfileService(twitterOAuthConnector, doobieTransactor)

  lazy val goodies: infra.Goodies = infra.Goodies(doobieTransactor, configuration, controllerComponents)
  lazy val readingsController = new ReadingsController(goodies)
  lazy val deletesController = new DeletesController(goodies)
  lazy val userProfileController = new UserProfileController(goodies)

  lazy val oAuthController = new OAuthController(goodies)(userProfileService, twitterOAuthConfig, twitterOAuthConnector)

  lazy val router: Router = new Routes(
    httpErrorHandler,
    readingsController,
    pingController,
    oAuthController,
    deletesController,
    userProfileController,
    assets
  ).withPrefix(httpConfiguration.context)

}
