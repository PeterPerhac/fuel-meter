package com.perhac.fuelmeter

import org.scalatestplus.play._
import play.api.test.Helpers._
import play.api.test._

class RoutingTest extends FuelMeterTest with OneAppPerSuite {

  "Routes" should {

    "send 404 on a bad request" in {
      route(app, FakeRequest(GET, "/theres-nothing-mapped-to-this-url"))
        .map(status) mustBe Some(NOT_FOUND)
    }

    "send 200 on a good request" in {
      route(app, FakeRequest(controllers.routes.ReadingsController.index())) match {
        case Some(homepage) =>
          status(homepage) mustBe OK
          contentAsString(homepage) must include("Fuel Meter")
        case None => fail
      }
    }

  }

  "render the index page" in {
    route(app, FakeRequest(GET, "/")) match {
      case Some(homepage) =>
        status(homepage) mustBe OK
        contentType(homepage) mustBe Some("text/html")
        contentAsString(homepage) must include("Here's how it works...")
      case None => fail("request to /acme/capture returned nothing")
    }

  }

}
