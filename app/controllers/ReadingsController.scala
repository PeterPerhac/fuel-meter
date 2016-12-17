package controllers

import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

import models.{Reading, Registration}
import play.api.Play.current
import play.api.data.Forms._
import play.api.data._
import play.api.data.format.Formats._
import play.api.i18n.Messages.Implicits._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.bson.{BSONDocument => Doc}

import scala.concurrent.Future
import scala.language.implicitConversions

class ReadingsController @Inject()(val reactiveMongoApi: ReactiveMongoApi)
  extends Controller with MongoController with ReactiveMongoComponents {

  import controllers.validation.CustomValidationSupport._

  val readingForm: Form[Reading] = Form(
    mapping(
      "reg" -> nonEmptyText(minLength = 4, maxLength = 8),
      "date" -> text.verifying(optionallyMatchingPattern("""^20\d\d\/[01]\d\/[0123]\d$""")),
      "mi" -> of(doubleFormat).verifying(inRange(0.0, 1000.00)),
      "total" -> number(min = 0, max = 500000),
      "litres" -> of(doubleFormat).verifying(inRange(0.0, 100.00)),
      "cost" -> of(doubleFormat).verifying(inRange(0.0, 500.00))
    )(Reading.apply)(Reading.unapply)
  )

  trait DateProvider {
    def get: Date
  }

  implicit class DateFormattingUtils(val d: Date) {
    def toFormat(formatString: String): Registration = new SimpleDateFormat(formatString).format(d)
  }

  implicit val dateProvider: DateProvider = new DateProvider {
    def get = new Date()
  }

  private def todaysDate(implicit dateProvider: DateProvider): String = dateProvider.get.toFormat("yyyy/MM/dd")

  private val bson = Reading.bsonHandler

  private val VReg = "vreg"

  def vRegCookie(r: Registration) = Cookie(VReg, r, maxAge = Some(Int.MaxValue))

  def repo = new repository.RefuelMongoRepository(reactiveMongoApi)

  def readings(implicit reg: Registration): Future[List[JsObject]] = repo.find(Doc("reg" -> reg))

  def uniqueRegistrations: Future[Seq[String]] = repo.uniqueRegistrations map (_ flatMap (_.value("reg").as[JsArray].value.map(_.as[String]).sorted))

  def list(implicit r: Registration): Action[AnyContent] = Action.async {
    readings map (o => Ok(Json.toJson(o)))
  }

  def listHtml(implicit r: Registration): Action[AnyContent] = Action.async {
    for {
      rs <- readings
      urs <- uniqueRegistrations
    } yield Ok(views.html.readings(r, rs flatMap (_.validate[Reading].asOpt), urs)).withCookies(vRegCookie(r))
  }

  def add: Action[JsValue] = Action.async(BodyParsers.parse.json) {
    implicit req =>
      val r = req.body.as[Reading]
      repo.save(bson.write(r)).map(_ => Redirect(routes.ReadingsController.list(r.reg)))
  }

  def delete(id: String): Action[AnyContent] = {
    Action.async(_ => repo.remove(Doc("_id" -> Doc("$oid" -> id))).map(_ => NoContent))
  }

  def captureForm(r: Registration) = Action {
    Ok(views.html.captureForm(r, readingForm))
  }

  def fixed(form: Reading) = form match {
    case Reading(_, d, _, _, _, _) if d.isEmpty =>
      form.copy(date = todaysDate)
    case _ => form
  }

  def saveReading(reg: Registration): Action[AnyContent] = Action.async { implicit request =>
    readingForm.bindFromRequest() fold(
      invalidForm => Future(BadRequest(views.html.captureForm(reg, invalidForm))),
      form => repo.save(bson.write(fixed(form))).map(_ => Redirect(routes.ReadingsController.listHtml(reg)))
    )
  }

  def index(): Action[AnyContent] = Action.async { implicit request =>
    val homePageOrElse = request.cookies.get(VReg).fold(uniqueRegistrations map (fus => Ok(views.html.defaultHomePage(fus)))) _
    homePageOrElse(cookie => Future(Redirect(routes.ReadingsController.listHtml(cookie.value))))
  }
}
