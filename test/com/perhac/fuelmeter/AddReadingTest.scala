package com.perhac.fuelmeter

import org.scalatestplus.play.OneAppPerSuite

class AddReadingTest extends FuelMeterTest with OneAppPerSuite {

  "The OneAppPerSuite trait" must {

    "start the FakeApplication" in {
      app mustBe Some(app)
    }

  }

}
