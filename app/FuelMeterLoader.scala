import java.util.concurrent.Executors.newFixedThreadPool

import auth.twitter.TwitterOAuthConfig
import connectors.TwitterOAuthConnector
import play.api.ApplicationLoader.Context
import play.api._
import play.api.db.{DBComponents, HikariCPComponents}
import play.api.routing.Router
import play.filters.HttpFiltersComponents
import repository.DoobieTransactor
import router.Routes
import services.{ReadingsService, UserProfileService, VehicleService}

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.fromExecutorService

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
  lazy val twitterOauth = new TwitterOAuthConnector(twitterOAuthConfig)

  lazy val boundedEc: ExecutionContext = fromExecutorService(newFixedThreadPool(4))
  lazy val doobieTransactor = new DoobieTransactor(dbApi.database("fuelmeter"), controllerComponents.executionContext, boundedEc)

  lazy val userProfileService: UserProfileService =
    new UserProfileService(twitterOauth.verifyCredential, doobieTransactor)

  lazy val goodies: infra.Goodies = infra.Goodies(doobieTransactor, configuration, controllerComponents)
  lazy val readingsService: ReadingsService = new ReadingsService(doobieTransactor)
  lazy val vehicleService: VehicleService = new VehicleService(doobieTransactor)

  lazy val readingsController = new ReadingsController(readingsService)(goodies)
  lazy val userProfileController = new UserProfileController(userProfileService)(goodies)
  lazy val vehicleController = new VehicleController(vehicleService)(goodies)
  lazy val oAuthController = new OAuthController(userProfileService, twitterOAuthConfig, twitterOauth)(goodies)

  lazy val router: Router = new Routes(
    httpErrorHandler,
    readingsController,
    pingController,
    oAuthController,
    vehicleController,
    userProfileController,
    assets
  ).withPrefix(httpConfiguration.context)

}
