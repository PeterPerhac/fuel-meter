package com.perhac.fuelmeter

import models.Vehicle
import org.scalatest.matchers.Matcher
import play.api.http
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json._
import play.api.mvc._
import play.api.routing.sird._
import play.api.test.Helpers._
import play.api.test._
import play.core.server.Server

class VehicleLookupIntegrationTest extends FuelMeterTest with RequestMethodExtractors {

  val testReg: String = "TE5TR3G"
  val testVehicle = Vehicle(testReg, "Star", "Forever", 1968, Some("Red"))
  val expectedRenderedText = "Red Star Forever"

  private def appConnectingTo(vehicleLookupPort: http.Port) =
    new GuiceApplicationBuilder()
      .configure(Map("vehicle-lookup.service.url" -> s"http://localhost:$vehicleLookupPort"))
      .build()

  private val testUrl = controllers.routes.ReadingsController.listHtml(testReg)

  def callAppWithStubbedVehicleService(vehicleLookupStub: PartialFunction[RequestHeader, Handler])(
      contentMatcher: Matcher[String]): Unit =
    Server.withRouter()(vehicleLookupStub) { port =>
      inside(route(appConnectingTo(port), FakeRequest(testUrl))) {
        case Some(page) =>
          status(page) mustBe OK
          contentAsString(page) must contentMatcher
      }
    }

  "Vehicle page" should {

    "still render fine even if vehicle lookup service is unreachable" in {
      // setup a vanilla application that will attempt to connect to non-existent vehicle-lookup service
      inside(route(new GuiceApplicationBuilder().build(), FakeRequest(testUrl))) {
        case Some(page) =>
          status(page) mustBe OK
          contentAsString(page) must not contain expectedRenderedText
      }
    }

    "render vehicle make and model when vehicle lookup service is reachable" in {
      callAppWithStubbedVehicleService {
        case GET(p"/$reg") => Action(Results.Ok(Json.toJson(testVehicle)))
      }(include(expectedRenderedText))
    }

    "still render fine even if vehicle lookup service is returning invalid JSON" in {
      callAppWithStubbedVehicleService {
        case GET(p"/$reg") =>
          Action(Results.Ok("""{"foo": "bar", "moo": 7, "reg": "something"}"""))
      }(not include expectedRenderedText)
    }

    "still render fine even if vehicle lookup service is returning 5xx server error" in {
      callAppWithStubbedVehicleService {
        case GET(p"/$reg") =>
          Action(Results.InternalServerError("""Something went south!"""))
      }(not include expectedRenderedText)
    }
  }

}
