package controllers

import org.mongodb.scala.MongoClient

/**
  * Created by Peter on 30/06/2016.
  */


object MongoTest {

  val mongo = MongoClient("""mongodb://fuelmeter:fu3lm3t3r@ds011495.mlab.com:11495/fuelmeter""")

  def main(args: Array[String]) {

  }

}
