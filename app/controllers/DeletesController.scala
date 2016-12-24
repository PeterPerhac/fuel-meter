package controllers

import javax.inject.Inject

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}

class DeletesController @Inject()(val reactiveMongoApi: ReactiveMongoApi)
  extends Controller with MongoController with ReactiveMongoComponents {

  private val VReg = "vreg"

  lazy val repo = new repository.RefuelMongoRepository(reactiveMongoApi)

  def deleteVehicle(implicit r: String) = Action.async {
    _ => repo.removeByRegistration(r) map (_ => Redirect(routes.ReadingsController.index()).discardingCookies(DiscardingCookie("vreg")))
  }

}
