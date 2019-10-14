import java.util.concurrent.Executors.newFixedThreadPool

import auth.twitter.TwitterOAuthConfig
import connectors.TwitterOAuthConnector
import play.api.ApplicationLoader.Context
import play.api._
import play.api.db.{DBComponents, HikariCPComponents}
import play.api.routing.Router
import play.filters.HttpFiltersComponents
import repository._
import router.Routes
import services._

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
    extends BuiltInComponentsFromContext(context) with HttpFiltersComponents with DBComponents with AssetsComponents
    with HikariCPComponents {

  lazy val twitterConfig = TwitterOAuthConfig(configuration)

  lazy val pingController =
    new infra.PingController(controllerComponents)
  lazy val twitterOAuth =
    new TwitterOAuthConnector(twitterConfig)

  lazy val doobieTransactor =
    new DoobieTransactor(
      db = dbApi.database("fuelmeter"),
      unboundedExecutionContext = controllerComponents.executionContext,
      boundedExecutionContext = fromExecutorService(newFixedThreadPool(4))
    )

  lazy val userProfileService: UserProfileService =
    new UserProfileService(twitterOAuth.verifyCredential, userProfileRepository, vehicleRepository, doobieTransactor)

  lazy val twitterOAuthConnector: TwitterOAuthConnector = new TwitterOAuthConnector(twitterConfig)
  lazy val readingsRepository: ReadingsRepository = new ReadingsRepository()
  lazy val tokenRepository: TokenRepository = new TokenRepository()
  lazy val userProfileRepository: UserProfileRepository = new UserProfileRepository()
  lazy val vehicleRepository: VehicleRepository = new VehicleRepository()

  lazy val twitterOAuthService: TwitterOAuthService =
    new TwitterOAuthService(twitterConfig, twitterOAuthConnector, tokenRepository, userProfileService, doobieTransactor)
  lazy val readingsService: ReadingsService =
    new ReadingsService(userProfileService, readingsRepository, vehicleRepository, doobieTransactor)
  lazy val vehicleService: VehicleService =
    new VehicleService(userProfileService, readingsRepository, vehicleRepository, doobieTransactor)

  lazy val readingsController =
    new ReadingsController(readingsService, vehicleRepository.vehicleSamples, doobieTransactor, controllerComponents)
  lazy val userProfileController =
    new UserProfileController(userProfileService, doobieTransactor, controllerComponents)
  lazy val vehicleController =
    new VehicleController(vehicleService, doobieTransactor, controllerComponents)
  lazy val oAuthController =
    new OAuthController(userProfileService, twitterOAuthService, doobieTransactor, controllerComponents)

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
