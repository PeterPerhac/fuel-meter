package com.perhac.fuelmeter

import org.scalatestplus.play.OneAppPerSuite
import play.api.{Application, Play}
import play.api.inject.guice.GuiceApplicationBuilder

class AddReadingTest extends FuelMeterTest with OneAppPerSuite {

  implicit override lazy val app: Application = new GuiceApplicationBuilder().configure(Map("ehcacheplugin" -> "disabled")).build()

  "The OneAppPerSuite trait" must {
    "provide a FakeApplication" in {
      app.configuration.getString("ehcacheplugin") mustBe Some("disabled")
    }
    "start the FakeApplication" in {
      Play.maybeApplication mustBe Some(app)
    }
  }

}
