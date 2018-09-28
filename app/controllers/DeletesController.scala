package controllers

import controllers.ReadingsController.VRegCookieName
import javax.inject.Inject
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc._
import repository.RefuelMongoRepository

class DeletesController @Inject()(repo: RefuelMongoRepository) extends Controller {

  def deleteVehicle(reg: String): Action[AnyContent] =
    Action.async { _ =>
      repo.removeByRegistration(reg).map { _ =>
        Redirect(routes.ReadingsController.index())
          .discardingCookies(DiscardingCookie(VRegCookieName))
      }
    }

}
