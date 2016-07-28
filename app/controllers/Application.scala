package controllers


import org.mongodb.scala.MongoClient
import play.api.mvc._


import java.util.concurrent.TimeUnit

import scala.concurrent.Await
import scala.concurrent.duration.Duration

import org.mongodb.scala._


object Helpers {

  implicit class DocumentObservable[C](val observable: Observable[Document]) extends ImplicitObservable[Document] {
    override val converter: (Document) => String = (doc) => doc.toJson
  }

  implicit class GenericObservable[C](val observable: Observable[C]) extends ImplicitObservable[C] {
    override val converter: (C) => String = (doc) => doc.toString
  }

  trait ImplicitObservable[C] {
    val observable: Observable[C]
    val converter: (C) => String

    def results(): Seq[C] = Await.result(observable.toFuture(), Duration(10, TimeUnit.SECONDS))

    def headResult() = Await.result(observable.head(), Duration(10, TimeUnit.SECONDS))

    def printResults(initial: String = ""): Unit = {
      if (initial.length > 0) print(initial)
      results().foreach(res => println(converter(res)))
    }

    def convertedString(initial: String = ""): String = s"${initial}${converter(headResult())}";

    def printHeadResult(initial: String = ""): Unit = println(convertedString(initial))
  }

}

object Application extends Controller {

  val mongo: MongoClient = MongoClient(sys.env("PROD_MONGODB"))

  def index = Action {
        Ok
  }

  def db = Action {
    var out = ""
    import Helpers._
    val cars = mongo getDatabase "fuelmeter" getCollection "cars"
    val stuff = cars.find()
      out += stuff.first().convertedString()
    Ok(out)
  }
}
