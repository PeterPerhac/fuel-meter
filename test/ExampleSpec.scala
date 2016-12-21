import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.selenium.HtmlUnit

class ExampleSpec extends FlatSpec with Matchers with HtmlUnit {

  val host = "http://localhost:9000/"

  "Homepage" should "have the correct title" in {
    go to host
    pageTitle should be ("Fuel Meter")
  }

}