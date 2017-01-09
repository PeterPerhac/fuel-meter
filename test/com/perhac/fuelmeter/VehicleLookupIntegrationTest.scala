package com.perhac.fuelmeter

import models.Vehicle
import org.scalatestplus.play._
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json._
import play.api.mvc._
import play.api.routing.sird._
import play.api.test.Helpers._
import play.api.test._
import play.core.server.Server

class VehicleLookupIntegrationTest extends FuelMeterTest with OneAppPerTest with RequestMethodExtractors {

  val testReg: String = "TE5TR3G"


  "Vehicle page" should {

    "still render fine even if vehicle lookup service is unreachable" in {
      inside(route(app, FakeRequest(controllers.routes.ReadingsController.listHtml("NA08MYW")))) {
        case Some(page) =>
          status(page) mustBe OK
          contentAsString(page) mustNot include("Red Star Forever")
      }
    }


    "render vehicle make and model when vehicle lookup service is reachable" in {

      Server.withRouter() {
        case GET(p"/v1/vehicles/$reg") => Action {
          val veh = Vehicle(reg, "Star", "Forever", 1968, Some("Red"))
          Results.Ok(Json.toJson(veh))
        }
      } { port =>
        val serviceUrl = "vehicle-lookup.service.url"
        inside(route(new GuiceApplicationBuilder().configure(Map(serviceUrl -> s"http://localhost:$port/v1/vehicles")).build(),
          FakeRequest(controllers.routes.ReadingsController.listHtml("NA08MYW")))) {
          case Some(page) =>
            status(page) mustBe OK
            contentAsString(page) must include("Red Star Forever")
        }
      }
    }


  }


}
