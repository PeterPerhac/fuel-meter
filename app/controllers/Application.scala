package controllers


import javax.inject.Inject

import org.mongodb.scala.MongoClient
import play.api.mvc._
import org.mongodb.scala.model.Filters._

class Application @Inject()(configuration: play.api.Configuration) extends Controller {

  import helpers.MongoHelpers._

  val mongo: MongoClient = MongoClient(configuration.underlying.getString("mongo.connectionURI"))
  val carsCollection = mongo getDatabase "fuelmeter" getCollection "cars"

  def index = Action {
    Ok(views.html.index())
  }

  def car(reg: String) = Action {
    try{
      Ok(carsCollection.find(equal("reg", reg)).first().headResultString)
    } catch {
      case ise: IllegalStateException => NotFound(s"vehicle with registration $reg not found")
    }
  }
}
